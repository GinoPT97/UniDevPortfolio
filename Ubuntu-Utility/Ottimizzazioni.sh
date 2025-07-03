#!/bin/bash
set -e
set -o pipefail

# Configurazione script
SCRIPT_NAME="Sistema di Ottimizzazione Ubuntu"
VERSION="2.0"
LOGFILE="/var/log/ubuntu-cleaner.log"
DRY_RUN=false
VERBOSE=false
FORCE=false

# Colori per output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Funzione per mostrare l'help
show_help() {
    cat << EOF
$SCRIPT_NAME v$VERSION

UTILIZZO:
    $0 [OPZIONI]

OPZIONI:
    -h, --help          Mostra questo messaggio di aiuto
    -d, --dry-run       Esegue una simulazione senza apportare modifiche
    -v, --verbose       Output dettagliato
    -f, --force         Forza l'esecuzione senza conferme
    -l, --log-file FILE Specifica un file di log personalizzato

ESEMPI:
    $0                  Esegue la pulizia completa
    $0 --dry-run        Simula la pulizia senza modifiche
    $0 --verbose        Pulizia con output dettagliato

EOF
}

# Parsing degli argomenti della riga di comando
while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -d|--dry-run)
            DRY_RUN=true
            shift
            ;;
        -v|--verbose)
            VERBOSE=true
            shift
            ;;
        -f|--force)
            FORCE=true
            shift
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
    
    # Output colorato su terminale
    echo -e "${color}[$level]${NC} $timestamp - $message"
    
    # Log senza colori nel file
    echo "[$level] $timestamp - $message" >> "$LOGFILE"
}

# Funzione per eseguire comandi con controllo degli errori migliorato
run_cmd() {
    local cmd="$1"
    local desc="$2"
    local optional="${3:-false}"
    
    if [[ "$VERBOSE" == "true" ]]; then
        log "DEBUG" "Eseguendo: $cmd"
    fi
    
    if [[ "$DRY_RUN" == "true" ]]; then
        log "INFO" "[DRY-RUN] $desc: $cmd"
        return 0
    fi
    
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

# Funzione per conferma utente
confirm_action() {
    local message="$1"
    
    if [[ "$FORCE" == "true" ]] || [[ "$DRY_RUN" == "true" ]]; then
        return 0
    fi
    
    echo -e "${YELLOW}$message${NC}"
    read -p "Continuare? [s/N]: " -n 1 -r
    echo
    [[ $REPLY =~ ^[Ss]$ ]]
}

# Funzione per calcolare lo spazio recuperato
calculate_space() {
    local before_size=$(df / | awk 'NR==2 {print $3}')
    echo "$before_size"
}

# Funzione per la pulizia di base del sistema
basic_cleanup() {
    log "INFO" "=== PULIZIA DI BASE DEL SISTEMA ==="
    
    # Aggiornamento indici pacchetti
    run_cmd "apt-get update" "Aggiornamento indici pacchetti"
    
    # Configurazione pacchetti interrotti
    run_cmd "dpkg --configure -a" "Configurazione pacchetti non configurati"
    
    # Risoluzione dipendenze mancanti
    run_cmd "apt-get install -f -y" "Risoluzione dipendenze mancanti"
    
    # Pulizia cache APT
    run_cmd "apt-get clean" "Pulizia cache APT"
    run_cmd "apt-get autoclean -y" "Pulizia pacchetti APT obsoleti"
    
    # Rimozione pacchetti non necessari
    run_cmd "apt-get autoremove --purge -y" "Rimozione pacchetti obsoleti"
    
    # Aggiornamento cache font
    run_cmd "fc-cache -f -v" "Aggiornamento cache font" true
}

# Funzione per la pulizia dei log di sistema
cleanup_logs() {
    log "INFO" "=== PULIZIA LOG DI SISTEMA ==="
    
    # Pulizia journal
    run_cmd "journalctl --vacuum-time=7d" "Rimozione log journal vecchi di 7 giorni"
    run_cmd "journalctl --vacuum-size=100M" "Limitazione dimensione journal a 100MB"
    run_cmd "journalctl --vacuum-files=3" "Mantenimento massimo 3 file journal"
    
    # Rotazione e compressione log
    run_cmd "logrotate -f /etc/logrotate.conf" "Rotazione forzata dei log" true
    
    # Troncamento file di log grandi
    run_cmd "find /var/log -type f -name '*.log' -size +50M -exec truncate -s 10M {} \;" "Troncamento file di log di grandi dimensioni" true
    
    # Rimozione log vecchi
    run_cmd "find /var/log -type f -name '*.log.*' -mtime +30 -delete" "Rimozione log vecchi di 30 giorni" true
}

# Funzione per la pulizia dei file temporanei
cleanup_temp_files() {
    log "INFO" "=== PULIZIA FILE TEMPORANEI ==="
    
    # Pulizia directory temporanee di sistema
    run_cmd "find /tmp -type f -atime +7 -delete" "Rimozione file temporanei vecchi"
    run_cmd "find /var/tmp -type f -atime +7 -delete" "Rimozione file temporanei variabili vecchi"
    
    # Pulizia crash reports
    run_cmd "rm -rf /var/crash/*" "Rimozione crash reports" true
    
    # Pulizia file lock orfani
    run_cmd "find /var/lock -type f -delete" "Rimozione file lock orfani" true
    run_cmd "find /var/run -name '*.pid' -delete" "Rimozione file PID orfani" true
}

# Funzione per la pulizia delle cache utente
cleanup_user_cache() {
    log "INFO" "=== PULIZIA CACHE UTENTE ==="
    
    # Cache browser (eseguite come utenti specifici)
    local users=$(getent passwd | awk -F: '$3 >= 1000 && $3 < 65534 {print $1}')
    
    for user in $users; do
        local home_dir=$(getent passwd "$user" | cut -d: -f6)
        if [[ -d "$home_dir" ]]; then
            # Firefox
            run_cmd "sudo -u $user find $home_dir/.mozilla/firefox -name 'cache2' -type d -exec rm -rf {} + 2>/dev/null || true" "Pulizia cache Firefox per $user" true
            run_cmd "sudo -u $user find $home_dir/.cache/mozilla -type f -delete 2>/dev/null || true" "Pulizia cache Mozilla per $user" true
            
            # Chrome/Chromium
            run_cmd "sudo -u $user rm -rf $home_dir/.cache/google-chrome/* 2>/dev/null || true" "Pulizia cache Chrome per $user" true
            run_cmd "sudo -u $user rm -rf $home_dir/.cache/chromium/* 2>/dev/null || true" "Pulizia cache Chromium per $user" true
            
            # Thumbnails
            run_cmd "sudo -u $user rm -rf $home_dir/.thumbnails/* 2>/dev/null || true" "Pulizia miniature per $user" true
            run_cmd "sudo -u $user rm -rf $home_dir/.cache/thumbnails/* 2>/dev/null || true" "Pulizia cache thumbnails per $user" true
            
            # Pulizia cache generiche
            run_cmd "sudo -u $user find $home_dir/.cache -type f -atime +30 -delete 2>/dev/null || true" "Pulizia cache generiche vecchie per $user" true
        fi
    done
    
    # Pulizia cache di sistema
    run_cmd "rm -rf /var/cache/apt/archives/*.deb" "Rimozione archivi DEB scaricati" true
    run_cmd "rm -rf /var/cache/debconf/*" "Pulizia cache debconf" true
}

# Funzione per la pulizia avanzata
advanced_cleanup() {
    log "INFO" "=== PULIZIA AVANZATA ==="
    
    # Rimozione localizzazioni non utilizzate
    if command -v localepurge &>/dev/null; then
        run_cmd "localepurge" "Rimozione localizzazioni non utilizzate" true
    else
        log "WARN" "localepurge non installato, installarlo con: apt-get install localepurge"
    fi
    
    # Rimozione pacchetti orfani
    if command -v deborphan &>/dev/null; then
        local orphans=$(deborphan 2>/dev/null || true)
        if [[ -n "$orphans" ]]; then
            run_cmd "deborphan | xargs -r apt-get remove --purge -y" "Rimozione pacchetti orfani"
        else
            log "INFO" "Nessun pacchetto orfano trovato"
        fi
    else
        log "INFO" "deborphan non disponibile, installarlo con: apt-get install deborphan"
    fi
    
    # Rimozione vecchie versioni del kernel
    local current_kernel=$(uname -r | sed 's/-generic//')
    local old_kernels=$(dpkg -l 'linux-image-*' 'linux-headers-*' | awk '/^ii/{ print $2 }' | grep -v "$current_kernel" | grep -E '^linux-(image|headers)-[0-9]' || true)
    
    if [[ -n "$old_kernels" ]]; then
        if confirm_action "Trovate vecchie versioni del kernel. Rimuoverle?"; then
            run_cmd "echo '$old_kernels' | xargs -r apt-get remove --purge -y" "Rimozione vecchie versioni del kernel"
        fi
    else
        log "INFO" "Nessuna vecchia versione del kernel da rimuovere"
    fi
    
    # Pulizia cache Python
    run_cmd "find /root -name '__pycache__' -type d -exec rm -rf {} + 2>/dev/null || true" "Pulizia cache Python sistema" true
    run_cmd "rm -rf /root/.cache/pip/* 2>/dev/null || true" "Pulizia cache pip root" true
}

# Funzione per ottimizzazioni sistema
system_optimization() {
    log "INFO" "=== OTTIMIZZAZIONI SISTEMA ==="
    
    # Ottimizzazione SSD (TRIM)
    if command -v fstrim &>/dev/null; then
        run_cmd "fstrim -av" "Ottimizzazione spazio libero (TRIM)" true
    else
        log "INFO" "fstrim non disponibile"
    fi
    
    # Ottimizzazione swap
    if [[ -f /proc/swaps ]] && [[ $(wc -l < /proc/swaps) -gt 1 ]]; then
        if confirm_action "Ottimizzare la memoria swap?"; then
            run_cmd "swapoff -a && swapon -a" "Ottimizzazione swap" true
        fi
    fi
    
    # Aggiornamento database locate
    if command -v updatedb &>/dev/null; then
        run_cmd "updatedb" "Aggiornamento database locate" true
    fi
    
    # Ricostruzione cache ldconfig
    run_cmd "ldconfig" "Ricostruzione cache librerie condivise" true
    
    # Sincronizzazione filesystem
    run_cmd "sync" "Sincronizzazione filesystem"
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
    advanced_cleanup
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
if ! command -v bc &>/dev/null; then
    log "WARN" "bc non installato, i calcoli dello spazio potrebbero non funzionare"
fi

# Esecuzione del main
main "$@"