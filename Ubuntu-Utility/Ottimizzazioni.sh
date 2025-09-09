#!/bin/bash
set -Eeuo pipefail

# Variabile globale per tracciare l'uscita pulita

# Configurazione script
SCRIPT_NAME="Sistema di Ottimizzazione Ubuntu"
VERSION="2.1"
LOGFILE="/var/log/ubuntu-cleaner.log"
DRY_RUN=false  # Modalità dry-run rimossa
BACKUP_BEFORE_DELETE=false  # Imposta a true per abilitare backup automatico dei file eliminati
VERBOSE=true
FORCE=true

# Controllo permessi superutente all'avvio
if [[ $EUID -ne 0 ]]; then
    echo -e "\033[0;31m[ERROR]\033[0m Questo script deve essere eseguito come superutente."
    echo "Prova: sudo $0"
    exit 1
fi

SCRIPT_SUCCESS=false
# Cleanup handler
cleanup() {
    if [[ "$SCRIPT_SUCCESS" == "true" ]]; then
        # Uscita normale, nessun errore
        return
    fi
    log "ERROR" "Script interrotto o errore. Pulizia in corso."
}
trap cleanup SIGINT ERR EXIT

# Colori per output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

show_help() {
    cat << EOF
$SCRIPT_NAME v$VERSION

USO: $0 [OPZIONI]
    -h, --help          Mostra questo messaggio
    -v, --verbose       Output dettagliato
    -f, --force         Nessuna conferma
    -l, --log-file FILE Log personalizzato

Esempi:
    $0                  Pulizia completa
    $0 --verbose        Output dettagliato
EOF
}

# Parsing degli argomenti della riga di comando
while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -l|--log-file)
            LOGFILE="$2"
            shift 2
            ;;
        *)
            echo -e "${RED}[ERROR]${NC} Opzione sconosciuta: $1"
            show_help
            exit 1
            ;;
    esac
done

# Verifica permessi superutente
if [[ $EUID -ne 0 ]]; then
    echo -e "${RED}[ERROR]${NC} Questo script deve essere eseguito come superutente."
    echo "Prova: sudo $0"
    exit 1
fi

# Controllo dell'esistenza delle directory necessarie
mkdir -p "$(dirname "$LOGFILE")"
touch "$LOGFILE"

# Funzione per loggare i messaggi con colori e timestamp
log() {
    local level="$1"
    local message="$2"
    local timestamp=$(date '+%Y-%m-%d %H:%M:%S')
    local color=""
    case "$level" in
        "INFO")  color="$GREEN" ;;
        "WARN")  color="$YELLOW" ;;
        "ERROR") color="$RED" ;;
        "DEBUG") color="$BLUE" ;;
    esac
    echo -e "${color}[$level]${NC} $timestamp - $message"
    echo "[$level] $timestamp - $message" >> "$LOGFILE"
}

# Funzione per backup file prima di eliminare (opzionale)
backup_file() {
    local file="$1"
    if [[ -f "$file" ]]; then
        cp -- "$file" "${file}.bak_$(date +%s)"
        log "INFO" "Backup creato: ${file}.bak_$(date +%s)"
    fi
}

# Funzione per eliminazione sicura di file (non directory!) solo in percorsi consentiti
safe_delete() {
    local target="$1"
    local allowed_root=("/home" "/tmp" "/var/log" "/var/tmp")
    if [[ -z "$target" || ! -e "$target" ]]; then
        log "WARN" "Percorso non valido: '$target'"
        return 1
    fi
    if [[ -d "$target" ]]; then
        log "WARN" "È una cartella, non la rimuovo: '$target'"
        return 1
    fi
    local allowed=false
    for dir in "${allowed_root[@]}"; do
        if [[ "$target" == $dir* ]]; then
            allowed=true
            break
        fi
    done
    if [[ "$allowed" != true ]]; then
        log "ERROR" "Tentativo di eliminazione fuori da percorsi sicuri: $target"
        return 1
    fi
    if [[ "$BACKUP_BEFORE_DELETE" == true ]]; then
        backup_file "$target"
    fi
    if [[ "$FORCE" == "true" ]]; then
        run_cmd "rm -f -- \"$target\"" "Eliminazione sicura di $target"
    else
        if confirm_action "Eliminare il file $target? Percorso: $target"; then
            run_cmd "rm -f -- \"$target\"" "Eliminazione sicura di $target"
        fi
    fi
}

# Funzione per eseguire comandi con controllo degli errori migliorato
run_cmd() {
    local cmd="$1"
    local desc="$2"
    local optional="${3:-false}"
    
    if [[ "$VERBOSE" == "true" ]]; then
        log "DEBUG" "Eseguendo: $cmd"
    fi
    
    # Dry-run rimosso: esecuzione sempre eseguita
    
    local start_time=$(date +%s)
    
    if eval "$cmd" 2>&1 | while IFS= read -r line; do
        [[ "$VERBOSE" == "true" ]] && echo "  $line"
        echo "  $line" >> "$LOGFILE"
    done; then
        local end_time=$(date +%s)
        local duration=$((end_time - start_time))
        log "INFO" "$desc completata con successo (${duration}s)"
        return 0
    else
        local exit_code=$?
        if [[ "$optional" == "true" ]]; then
            log "WARN" "$desc fallita (opzionale) - Codice: $exit_code"
            return 0
        else
            log "ERROR" "$desc fallita - Codice: $exit_code"
            return $exit_code
        fi
    fi
}

confirm_action() {
    # Con FORCE attivo, conferma automatica
    return 0
}

# Calcola spazio usato su /
calculate_space() {
    df --output=used / | tail -1
}

# Funzione per la pulizia di base del sistema
basic_cleanup() {
    log "INFO" "=== PULIZIA DI BASE DEL SISTEMA ==="
    # Check connessione internet prima di update
    if ping -c 1 1.1.1.1 &>/dev/null; then
        run_cmd "apt-get update" "Aggiornamento indici pacchetti"
    else
        log "WARN" "Nessuna connessione internet: salto apt-get update"
    fi
    run_cmd "dpkg --configure -a" "Configurazione pacchetti non configurati"
    run_cmd "apt-get install -f -y" "Risoluzione dipendenze mancanti"
    run_cmd "apt-get clean" "Pulizia cache APT"
    run_cmd "apt-get autoclean -y" "Pulizia pacchetti APT obsoleti"
    run_cmd "apt-get autoremove -y" "Rimozione pacchetti obsoleti"
    run_cmd "fc-cache -f -v" "Aggiornamento cache font" true
}

# Funzione per la pulizia dei log di sistema
cleanup_logs() {
    log "INFO" "=== PULIZIA LOG DI SISTEMA ==="
    
    run_cmd "journalctl --vacuum-time=7d" "Rimozione log journal vecchi di 7 giorni"
    run_cmd "logrotate -f /etc/logrotate.conf" "Rotazione forzata dei log" true
}

# Funzione per la pulizia dei file temporanei e cache aggiuntive
cleanup_temp_files() {
    log "INFO" "=== PULIZIA FILE TEMPORANEI E CACHE AGGIUNTIVE ==="

    # Pulizia cartelle temporanee utente (SAFE, solo file, no directory)
    for user in $(getent passwd | awk -F: '$3 >= 1000 && $3 < 65534 && $1!~/^(nobody|systemd-)/ {print $1}'); do
        home_dir=$(getent passwd "$user" | cut -d: -f6)
        # Thumbnails
        thumb_dir="$home_dir/.cache/thumbnails"
        if [[ -d "$thumb_dir" ]]; then
            find "$thumb_dir" -type f -print0 | while IFS= read -r -d '' file; do
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
        # Firefox
        ff_dir="$home_dir/.cache/mozilla"
        if [[ -d "$ff_dir" ]]; then
            find "$ff_dir" -type f -print0 | while IFS= read -r -d '' file; do
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
        # Chromium
        chromium_dir="$home_dir/.cache/chromium"
        if [[ -d "$chromium_dir" ]]; then
            find "$chromium_dir" -type f -print0 | while IFS= read -r -d '' file; do
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
        # Fontconfig
        font_dir="$home_dir/.cache/fontconfig"
        if [[ -d "$font_dir" ]]; then
            find "$font_dir" -type f -print0 | while IFS= read -r -d '' file; do
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
        # npm
        npm_dir="$home_dir/.npm"
        if [[ -d "$npm_dir" ]]; then
            find "$npm_dir" -type f -print0 | while IFS= read -r -d '' file; do
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
        # yarn
        yarn_dir="$home_dir/.cache/yarn"
        if [[ -d "$yarn_dir" ]]; then
            find "$yarn_dir" -type f -print0 | while IFS= read -r -d '' file; do
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
        # Flatpak
        flatpak_dir="$home_dir/.var/app"
        if [[ -d "$flatpak_dir" ]]; then
            find "$flatpak_dir" -type f -print0 | while IFS= read -r -d '' file; do
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
        # Snap
        snap_cache="$home_dir/snap"
        if [[ -d "$snap_cache" ]]; then
            find "$snap_cache" -type f -print0 | while IFS= read -r -d '' file; do
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
        # Python __pycache__ e *.pyc
        find "$home_dir" -type d -name "__pycache__" -prune -exec rm -rf {} +
        find "$home_dir" -type f -name "*.pyc" -delete
    done

    # File temporanei di sistema
    find /tmp -type f -print0 | while IFS= read -r -d '' file; do
        safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
    done
    find /var/tmp -type f -print0 | while IFS= read -r -d '' file; do
        safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
    done

    # Log compressi vecchi (>30 giorni)
    find /var/log -type f \( -name "*.gz" -o -name "*.1" \) -mtime +30 -print0 | while IFS= read -r -d '' file; do
        safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
    done
}

# Funzione per la pulizia delle cache utente
cleanup_user_cache() {
    log "INFO" "=== PULIZIA CACHE UTENTE ==="
    local users=$(getent passwd | awk -F: '$3 >= 1000 && $3 < 65534 && $1!~/^(nobody|systemd-)/ {print $1}')
    for user in $users; do
        local home_dir=$(getent passwd "$user" | cut -d: -f6)
        if [[ -d "$home_dir/.cache/mozilla" ]]; then
            find "$home_dir/.cache/mozilla" -type f -print0 | while IFS= read -r -d '' file; do
                if [[ "$file" != /* ]]; then
                    log "WARN" "Percorso non valido: '$file'"
                    continue
                fi
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
        if [[ -d "$home_dir/.cache/google-chrome" ]]; then
            find "$home_dir/.cache/google-chrome" -type f -print0 | while IFS= read -r -d '' file; do
                if [[ "$file" != /* ]]; then
                    log "WARN" "Percorso non valido: '$file'"
                    continue
                fi
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
        if [[ -d "$home_dir/.cache/chromium" ]]; then
            find "$home_dir/.cache/chromium" -type f -print0 | while IFS= read -r -d '' file; do
                if [[ "$file" != /* ]]; then
                    log "WARN" "Percorso non valido: '$file'"
                    continue
                fi
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
        if [[ -d "$home_dir/.cache/thumbnails" ]]; then
            find "$home_dir/.cache/thumbnails" -type f -print0 | while IFS= read -r -d '' file; do
                if [[ "$file" != /* ]]; then
                    log "WARN" "Percorso non valido: '$file'"
                    continue
                fi
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
    done
}

# Funzione per ottimizzazioni sistema
system_optimization() {
    log "INFO" "=== OTTIMIZZAZIONI SISTEMA ==="
    
    # Ottimizzazione SSD (TRIM) DISABILITATA per sicurezza
    log "INFO" "TRIM SSD disabilitato per sicurezza. Abilitalo solo se sei sicuro."
    # Ottimizzazione swap DISABILITATA per sicurezza
    log "INFO" "Ottimizzazione swap disabilitata per sicurezza."
    
    # Aggiornamento database locate
    if command -v updatedb &>/dev/null; then
        run_cmd "updatedb" "Aggiornamento database locate" true
    fi
    run_cmd "ldconfig" "Ricostruzione cache librerie condivise" true
    run_cmd "sync" "Sincronizzazione filesystem"

    # Pulizia e backup della bash history per ogni utente reale
    local users=()
    while IFS=: read -r user _ uid _ _ home _; do
        if [[ $uid -ge 1000 && $uid -lt 65534 && -d "$home" ]]; then
            users+=("$home")
        fi
    done < <(getent passwd)
    for home_dir in "${users[@]}"; do
        local hist_file="$home_dir/.bash_history"
        if [[ -f "$hist_file" ]]; then
            log "INFO" "Backup della bash history per $home_dir..."
            cp "$hist_file" "$hist_file.bak"
            log "INFO" "Mantieni solo gli ultimi 100 comandi nella bash history di $home_dir."
            tail -100 "$hist_file" > "$hist_file.tmp" && mv "$hist_file.tmp" "$hist_file"
        else
            log "WARN" "Nessuna bash history trovata per $home_dir."
        fi
    done
    log "INFO" "Bash history ottimizzata e sincronizzata per tutti gli utenti."
}

# Funzione per la gestione dei servizi PostgreSQL
postgresql_maintenance() {
    log "INFO" "=== MANUTENZIONE POSTGRESQL ==="
    
    # Verifica se PostgreSQL è installato
    if command -v postgres &>/dev/null || [[ -d /var/lib/postgresql ]]; then
        # Creazione directory log se necessaria
        run_cmd "mkdir -p /var/log/postgresql/" "Creazione directory log PostgreSQL"
        run_cmd "chown -R postgres:postgres /var/log/postgresql/" "Impostazione permessi directory log PostgreSQL" true
        
        # Pulizia log PostgreSQL vecchi
        run_cmd "find /var/log/postgresql -name '*.log' -mtime +7 -delete" "Rimozione log PostgreSQL vecchi" true
    else
        log "INFO" "PostgreSQL non installato, salto manutenzione"
    fi
}

# Funzione principale
main() {
    local start_time=$(date +%s)
    local space_before=$(calculate_space)
    
    log "INFO" "=== AVVIO $SCRIPT_NAME v$VERSION ==="
    log "INFO" "Modalità: $([ "$DRY_RUN" == "true" ] && echo "DRY-RUN" || echo "NORMALE")"
    log "INFO" "Spazio utilizzato iniziale: $(echo "scale=2; $space_before/1024/1024" | bc -l 2>/dev/null || echo "N/A") GB"
    
    # Backup del log precedente
    [[ -f "$LOGFILE" ]] && cp "$LOGFILE" "${LOGFILE}.backup" 2>/dev/null || true
    
    # Esecuzione delle funzioni di pulizia
    basic_cleanup
    cleanup_logs
    cleanup_temp_files
    cleanup_user_cache
    postgresql_maintenance
    system_optimization
    
    # Calcolo dello spazio recuperato
    local space_after=$(calculate_space)
    local space_freed=$(echo "scale=2; ($space_before-$space_after)/1024/1024" | bc -l 2>/dev/null || echo "N/A")
    local duration=$(( $(date +%s) - start_time ))
    
    log "INFO" "=== PULIZIA COMPLETATA ==="
    log "INFO" "Tempo di esecuzione: ${duration} secondi"
    log "INFO" "Spazio recuperato: ${space_freed} GB"
    log "INFO" "Log salvato in: $LOGFILE"
    
    if [[ "$DRY_RUN" == "false" ]]; then
        echo -e "${GREEN}✓ Pulizia completata con successo!${NC}"
        echo -e "  Spazio recuperato: ${space_freed} GB"
        echo -e "  Log disponibile in: $LOGFILE"
    else
        echo -e "${BLUE}ℹ Simulazione completata. Nessuna modifica applicata.${NC}"
    fi
}

# Gestione dei segnali
trap 'log "ERROR" "Script interrotto dall'\''utente"; exit 130' INT TERM

# Controllo dipendenze (bc per calcoli)
check_dependencies() {
    local deps=(bc apt-get dpkg logrotate journalctl find tail df sync ldconfig)
    local missing=()
    for dep in "${deps[@]}"; do
        if ! command -v "$dep" &>/dev/null; then
            missing+=("$dep")
        fi
    done
    if [[ ${#missing[@]} -gt 0 ]]; then
        log "WARN" "Comandi mancanti: ${missing[*]}"
    fi
    # smartctl è opzionale
    if ! command -v smartctl &>/dev/null; then
        log "INFO" "smartctl non installato: analisi SMART dischi non disponibile"
    fi
}

# Esecuzione del main

# Pulizia file .tmp/.bak obsoleti nelle home
cleanup_tmp_bak_files() {
    log "INFO" "=== PULIZIA FILE .tmp/.bak OBSOLETI NELLE HOME ==="
    local users=$(getent passwd | awk -F: '$3 >= 1000 && $3 < 65534 && $1!~/^(nobody|systemd-)/ {print $1}')
    for user in $users; do
        local home_dir=$(getent passwd "$user" | cut -d: -f6)
        if [[ -d "$home_dir" ]]; then
            find "$home_dir" -type f \( -name '*.tmp' -o -name '*.bak' \) -mtime +14 -print0 | while IFS= read -r -d '' file; do
                if [[ "$file" != /* ]]; then
                    log "WARN" "Percorso non valido: '$file'"
                    continue
                fi
                safe_delete "$file" || log "WARN" "Impossibile eliminare: $file"
            done
        fi
    done
}

# Analisi SMART dischi
smart_disks_report() {
    log "INFO" "=== ANALISI SMART DEI DISCHI ==="
    if command -v smartctl &>/dev/null; then
        for disk in /dev/sd[a-z]; do
            if [[ -b "$disk" ]]; then
                log "INFO" "SMART info per $disk:"
                smartctl -H "$disk" | grep -E 'SMART overall-health|PASSED|FAILED' | while read -r line; do
                    log "INFO" "$disk: $line"
                done
            fi
        done
    else
        log "INFO" "smartctl non disponibile, salto analisi SMART."
    fi
}

# Ottimizzazione RAM (drop caches)
optimize_ram() {
    log "INFO" "=== OTTIMIZZAZIONE RAM (DROP CACHES) ==="
    # Dry-run rimosso: drop caches sempre eseguito se permessi
    if [[ $EUID -ne 0 ]]; then
        log "ERROR" "Permessi insufficienti per drop caches."
        return 1
    fi
    sync
    echo 3 > /proc/sys/vm/drop_caches && log "INFO" "RAM ottimizzata: caches droppate."
}

# Report dettagliato finale
report_dettagliato() {
    log "INFO" "=== REPORT DETTAGLIATO ==="
    echo -e "${BLUE}Spazio usato prima: $1 MB${NC}"
    echo -e "${BLUE}Spazio usato dopo: $2 MB${NC}"
    echo -e "${GREEN}Spazio recuperato: $3 GB${NC}"
    echo -e "${BLUE}Log: $LOGFILE${NC}"
}

main() {
    local start_time=$(date +%s)
    local space_before=$(calculate_space)

    log "INFO" "=== AVVIO $SCRIPT_NAME v$VERSION ==="
    log "INFO" "Modalità: NORMALE"
    log "INFO" "Spazio utilizzato iniziale: $(echo "scale=2; $space_before/1024/1024" | bc -l 2>/dev/null || echo "N/A") GB"

    [[ -f "$LOGFILE" ]] && cp "$LOGFILE" "${LOGFILE}.backup" 2>/dev/null || true

    check_dependencies
    basic_cleanup
    cleanup_logs
    cleanup_temp_files
    cleanup_user_cache
    cleanup_tmp_bak_files
    postgresql_maintenance
    system_optimization
    smart_disks_report
    optimize_ram

    local space_after=$(calculate_space)
    local space_freed=$(echo "scale=2; ($space_before-$space_after)/1024/1024" | bc -l 2>/dev/null || echo "N/A")
    local duration=$(( $(date +%s) - start_time ))

    log "INFO" "=== PULIZIA COMPLETATA ==="
    log "INFO" "Tempo di esecuzione: ${duration} secondi"
    log "INFO" "Spazio recuperato: ${space_freed} GB"
    log "INFO" "Log salvato in: $LOGFILE"

    report_dettagliato "$space_before" "$space_after" "$space_freed"

    echo -e "${GREEN}✓ Pulizia completata con successo!${NC}"
    echo -e "  Spazio recuperato: ${space_freed} GB"
    echo -e "  Log disponibile in: $LOGFILE"
}

main "$@"
SCRIPT_SUCCESS=true