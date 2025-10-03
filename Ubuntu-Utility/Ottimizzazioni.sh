#!/bin/bash
set -Eeuo pipefail

# Script di Pulizia Ubuntu - Versione Sicura
SCRIPT_NAME="Ubuntu Safe Cleaner"
VERSION="3.0"
LOGFILE="/var/log/ubuntu-cleaner.log"
VERBOSE=false  # Meno output per esecuzione automatica
FORCE=true     # Consenso automatico per cron
DRY_RUN=false

# Controllo permessi superutente
if [[ $EUID -ne 0 ]]; then
    echo -e "\033[0;31m[ERROR]\033[0m Questo script deve essere eseguito come superutente."
    echo "Prova: sudo $0"
    exit 1
fi

# Colori per output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

show_help() {
    cat << EOF
$SCRIPT_NAME v$VERSION

USO: $0 [COMANDO] [OPZIONI]

COMANDI:
    ottimizzazioni      Esegue pulizia completa (default)
    help                Mostra questo messaggio

OPZIONI:
    -h, --help          Mostra questo messaggio
    -v, --verbose       Output dettagliato
    -f, --force         Nessuna conferma (usa con cautela)
    -d, --dry-run       Simula senza eliminare
    -l, --log-file FILE Log personalizzato

Esempi:
    $0 ottimizzazioni --dry-run     Simula pulizia
    $0 ottimizzazioni --force       Pulizia automatica
    $0 ottimizzazioni               Pulizia con conferme
    $0 --dry-run                    Simula (shortcut)
EOF
}

# Parsing argomenti
COMMAND=""
while [[ $# -gt 0 ]]; do
    case $1 in
        ottimizzazioni|help)
            COMMAND="$1"
            shift
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
        -v|--verbose)
            VERBOSE=true
            shift
            ;;
        -f|--force)
            FORCE=true
            shift
            ;;
        -d|--dry-run)
            DRY_RUN=true
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

# Se è stato usato "help", mostra aiuto ed esci
if [[ "$COMMAND" == "help" ]]; then
    show_help
    exit 0
fi

# "ottimizzazioni" o nessun comando = esegui pulizia
if [[ "$COMMAND" == "ottimizzazioni" || -z "$COMMAND" ]]; then
    COMMAND="ottimizzazioni"
fi

# Inizializza log
mkdir -p "$(dirname "$LOGFILE")"
touch "$LOGFILE"

# Funzione log con timestamp
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
    echo -e "${color}[$level]${NC} $message"
    echo "[$level] $timestamp - $message" >> "$LOGFILE"
}

# Conferma azione
confirm_action() {
    if [[ "$FORCE" == "true" ]]; then
        return 0
    fi
    local prompt="$1"
    read -p "$prompt [s/N]: " -n 1 -r
    echo
    [[ $REPLY =~ ^[SsYy]$ ]]
}

# Esegui comando con gestione errori
run_cmd() {
    local cmd="$1"
    local desc="$2"
    
    if [[ "$VERBOSE" == "true" ]]; then
        log "DEBUG" "Eseguendo: $cmd"
    fi
    
    if [[ "$DRY_RUN" == "true" ]]; then
        log "INFO" "[DRY-RUN] $desc"
        return 0
    fi
    
    if eval "$cmd" >> "$LOGFILE" 2>&1; then
        log "INFO" "✓ $desc"
        return 0
    else
        log "WARN" "✗ $desc fallito"
        return 1
    fi
}

# Cache utenti
declare -A USER_HOMES
cache_users() {
    log "INFO" "Caricamento lista utenti..."
    while IFS=: read -r user _ uid _ _ home _; do
        if [[ $uid -ge 1000 && $uid -lt 65534 && -d "$home" ]]; then
            USER_HOMES["$user"]="$home"
        fi
    done < <(getent passwd)
    log "INFO" "Trovati ${#USER_HOMES[@]} utenti"
}

# Calcola spazio disco
calculate_space() {
    df --output=used / | tail -1
}

# Pulizia BASE - Sicura e standard
basic_cleanup() {
    log "INFO" "=== PULIZIA BASE SISTEMA ==="
    
    if ping -c 2 -W 2 8.8.8.8 &>/dev/null; then
        run_cmd "apt-get update -qq" "Aggiornamento indici pacchetti"
    else
        log "WARN" "Nessuna connessione internet, salto update"
    fi
    
    run_cmd "apt-get clean" "Pulizia cache APT"
    run_cmd "apt-get autoclean -y" "Pulizia pacchetti obsoleti"
    run_cmd "apt-get autoremove --purge -y" "Rimozione pacchetti inutilizzati"
}

# Pulizia LOG di sistema
cleanup_logs() {
    log "INFO" "=== PULIZIA LOG SISTEMA ==="
    
    run_cmd "journalctl --vacuum-time=7d" "Pulizia journal (>7 giorni)"
    
    local old_logs=$(find /var/log -type f \( -name "*.gz" -o -name "*.1" -o -name "*.old" \) -mtime +30 2>/dev/null | wc -l)
    if [[ $old_logs -gt 0 ]]; then
        log "INFO" "Trovati $old_logs log compressi vecchi"
        if confirm_action "Eliminare $old_logs log compressi vecchi (>30 giorni)?"; then
            if [[ "$DRY_RUN" == "false" ]]; then
                find /var/log -type f \( -name "*.gz" -o -name "*.1" -o -name "*.old" \) -mtime +30 -delete
                log "INFO" "✓ Log vecchi eliminati"
            else
                log "INFO" "[DRY-RUN] Eliminazione log vecchi"
            fi
        fi
    fi
}

# Pulizia THUMBNAILS
cleanup_thumbnails() {
    log "INFO" "=== PULIZIA THUMBNAILS ==="
    
    for user in "${!USER_HOMES[@]}"; do
        local home="${USER_HOMES[$user]}"
        local thumb_dir="$home/.cache/thumbnails"
        
        if [[ -d "$thumb_dir" ]]; then
            local size=$(du -sh "$thumb_dir" 2>/dev/null | cut -f1)
            local count=$(find "$thumb_dir" -type f 2>/dev/null | wc -l)
            
            if [[ $count -gt 100 ]]; then
                log "INFO" "Utente $user: $count thumbnails ($size)"
                if confirm_action "Pulire thumbnails di $user?"; then
                    if [[ "$DRY_RUN" == "false" ]]; then
                        find "$thumb_dir" -type f -delete 2>/dev/null
                        log "INFO" "✓ Thumbnails di $user pulite"
                    else
                        log "INFO" "[DRY-RUN] Pulizia thumbnails $user"
                    fi
                fi
            fi
        fi
    done
}

# Pulizia CACHE BROWSER
cleanup_browser_cache() {
    log "INFO" "=== PULIZIA CACHE BROWSER ==="
    
    for user in "${!USER_HOMES[@]}"; do
        local home="${USER_HOMES[$user]}"
        
        # Firefox cache
        local ff_cache="$home/.cache/mozilla/firefox"
        if [[ -d "$ff_cache" ]]; then
            local size=$(du -sh "$ff_cache" 2>/dev/null | cut -f1)
            log "INFO" "Firefox cache di $user: $size"
            if confirm_action "Pulire cache Firefox di $user ($size)?"; then
                if [[ "$DRY_RUN" == "false" ]]; then
                    find "$ff_cache" -type f \( -name "*.cache" -o -path "*/cache2/*" \) -delete 2>/dev/null
                    log "INFO" "✓ Cache Firefox pulita"
                else
                    log "INFO" "[DRY-RUN] Pulizia cache Firefox"
                fi
            fi
        fi
        
        # Chrome/Chromium cache
        for browser_cache in "$home/.cache/google-chrome" "$home/.cache/chromium"; do
            if [[ -d "$browser_cache/Default/Cache" ]]; then
                local size=$(du -sh "$browser_cache" 2>/dev/null | cut -f1)
                log "INFO" "Cache browser di $user: $size"
                if confirm_action "Pulire cache browser di $user ($size)?"; then
                    if [[ "$DRY_RUN" == "false" ]]; then
                        find "$browser_cache/Default/Cache" -type f -delete 2>/dev/null
                        log "INFO" "✓ Cache browser pulita"
                    else
                        log "INFO" "[DRY-RUN] Pulizia cache browser"
                    fi
                fi
            fi
        done
    done
}

# Pulizia FILE TEMPORANEI
cleanup_temp_files() {
    log "INFO" "=== PULIZIA FILE TEMPORANEI ==="
    
    # /tmp - solo file vecchi >3 giorni
    local tmp_old=$(find /tmp -type f -mtime +3 2>/dev/null | wc -l)
    if [[ $tmp_old -gt 0 ]]; then
        log "INFO" "Trovati $tmp_old file temporanei vecchi in /tmp"
        if confirm_action "Eliminare $tmp_old file temporanei vecchi (>3 giorni)?"; then
            if [[ "$DRY_RUN" == "false" ]]; then
                find /tmp -type f -mtime +3 -delete 2>/dev/null
                log "INFO" "✓ File temporanei /tmp eliminati"
            else
                log "INFO" "[DRY-RUN] Eliminazione file temporanei /tmp"
            fi
        fi
    fi
    
    # /var/tmp - solo file vecchi >7 giorni
    local vartmp_old=$(find /var/tmp -type f -mtime +7 2>/dev/null | wc -l)
    if [[ $vartmp_old -gt 0 ]]; then
        log "INFO" "Trovati $vartmp_old file temporanei vecchi in /var/tmp"
        if confirm_action "Eliminare $vartmp_old file temporanei vecchi (>7 giorni)?"; then
            if [[ "$DRY_RUN" == "false" ]]; then
                find /var/tmp -type f -mtime +7 -delete 2>/dev/null
                log "INFO" "✓ File temporanei /var/tmp eliminati"
            else
                log "INFO" "[DRY-RUN] Eliminazione file temporanei /var/tmp"
            fi
        fi
    fi
}

# Pulizia PYTHON cache (solo in .cache, non progetti)
cleanup_python_cache() {
    log "INFO" "=== PULIZIA CACHE PYTHON ==="
    
    for user in "${!USER_HOMES[@]}"; do
        local home="${USER_HOMES[$user]}"
        local cache_dir="$home/.cache"
        
        if [[ -d "$cache_dir" ]]; then
            local pycache_count=$(find "$cache_dir" -type d -name "__pycache__" 2>/dev/null | wc -l)
            local pyc_count=$(find "$cache_dir" -type f -name "*.pyc" 2>/dev/null | wc -l)
            
            if [[ $pycache_count -gt 0 || $pyc_count -gt 0 ]]; then
                log "INFO" "Utente $user: $pycache_count __pycache__, $pyc_count file .pyc"
                if confirm_action "Pulire cache Python di $user?"; then
                    if [[ "$DRY_RUN" == "false" ]]; then
                        find "$cache_dir" -type d -name "__pycache__" -exec rm -rf {} + 2>/dev/null
                        find "$cache_dir" -type f -name "*.pyc" -delete 2>/dev/null
                        log "INFO" "✓ Cache Python pulita"
                    else
                        log "INFO" "[DRY-RUN] Pulizia cache Python"
                    fi
                fi
            fi
        fi
    done
}

# Pulizia BASH HISTORY - mantieni ultimi 100 comandi
cleanup_bash_history() {
    log "INFO" "=== OTTIMIZZAZIONE BASH HISTORY ==="
    
    for user in "${!USER_HOMES[@]}"; do
        local home="${USER_HOMES[$user]}"
        local hist_file="$home/.bash_history"
        
        if [[ -f "$hist_file" ]]; then
            local lines=$(wc -l < "$hist_file")
            
            if [[ $lines -gt 100 ]]; then
                log "INFO" "Utente $user: $lines righe in bash_history"
                if confirm_action "Mantenere solo ultimi 100 comandi per $user?"; then
                    if [[ "$DRY_RUN" == "false" ]]; then
                        # Backup
                        cp "$hist_file" "$hist_file.backup"
                        # Mantieni solo ultimi 100
                        tail -100 "$hist_file" > "$hist_file.tmp" && mv "$hist_file.tmp" "$hist_file"
                        chown --reference="$hist_file.backup" "$hist_file"
                        log "INFO" "✓ Bash history ottimizzata (backup: .bash_history.backup)"
                    else
                        log "INFO" "[DRY-RUN] Ottimizzazione bash history"
                    fi
                fi
            fi
        fi
    done
}

# Pulizia FILE .tmp e .bak vecchi nelle home
cleanup_tmp_bak_files() {
    log "INFO" "=== PULIZIA FILE .tmp/.bak OBSOLETI ==="
    
    for user in "${!USER_HOMES[@]}"; do
        local home="${USER_HOMES[$user]}"
        
        local tmp_count=$(find "$home" -maxdepth 3 -type f -name "*.tmp" -mtime +14 2>/dev/null | wc -l)
        local bak_count=$(find "$home" -maxdepth 3 -type f -name "*.bak" -mtime +30 2>/dev/null | wc -l)
        
        if [[ $tmp_count -gt 0 ]]; then
            log "INFO" "Utente $user: $tmp_count file .tmp vecchi (>14 giorni)"
            if confirm_action "Eliminare file .tmp vecchi di $user?"; then
                if [[ "$DRY_RUN" == "false" ]]; then
                    find "$home" -maxdepth 3 -type f -name "*.tmp" -mtime +14 -delete 2>/dev/null
                    log "INFO" "✓ File .tmp eliminati"
                else
                    log "INFO" "[DRY-RUN] Eliminazione file .tmp"
                fi
            fi
        fi
        
        if [[ $bak_count -gt 0 ]]; then
            log "INFO" "Utente $user: $bak_count file .bak vecchi (>30 giorni)"
            if confirm_action "Eliminare file .bak vecchi di $user?"; then
                if [[ "$DRY_RUN" == "false" ]]; then
                    find "$home" -maxdepth 3 -type f -name "*.bak" -mtime +30 -delete 2>/dev/null
                    log "INFO" "✓ File .bak eliminati"
                else
                    log "INFO" "[DRY-RUN] Eliminazione file .bak"
                fi
            fi
        fi
    done
}

# Ottimizzazioni finali
system_optimization() {
    log "INFO" "=== OTTIMIZZAZIONI FINALI ==="
    
    # Aggiorna database locate
    if command -v updatedb &>/dev/null; then
        run_cmd "updatedb" "Aggiornamento database locate"
    fi
    
    # Ricostruisci cache librerie
    run_cmd "ldconfig" "Ricostruzione cache librerie"
    
    # Sincronizza filesystem
    run_cmd "sync" "Sincronizzazione filesystem"
}

# Funzione principale
main() {
    local start_time=$(date +%s)
    local space_before=$(calculate_space)
    
    echo -e "${GREEN}╔════════════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║  $SCRIPT_NAME v$VERSION           ║${NC}"
    echo -e "${GREEN}╚════════════════════════════════════════════╝${NC}"
    echo
    
    if [[ "$DRY_RUN" == "true" ]]; then
        echo -e "${BLUE}▶ MODALITÀ DRY-RUN: Nessun file verrà eliminato${NC}"
    elif [[ "$FORCE" == "true" ]]; then
        echo -e "${GREEN}▶ MODALITÀ AUTOMATICA: Pulizia senza conferme${NC}"
    else
        echo -e "${YELLOW}▶ MODALITÀ INTERATTIVA: Conferme richieste${NC}"
    fi
    echo
    
    log "INFO" "Inizio pulizia sistema"
    
    # Calcolo spazio più preciso
    local space_before_mb=$((space_before / 1024))
    local space_before_gb=$((space_before_mb / 1024))
    log "INFO" "Spazio utilizzato: ${space_before_gb}.$(printf '%02d' $((space_before_mb % 1024 * 100 / 1024))) GB"
    
    # Cache utenti una volta sola
    cache_users
    
    # Esegui pulizie
    basic_cleanup
    cleanup_logs
    cleanup_thumbnails
    cleanup_browser_cache
    cleanup_temp_files
    cleanup_python_cache
    cleanup_bash_history
    cleanup_tmp_bak_files
    system_optimization
    
    # Calcola spazio recuperato
    local space_after=$(calculate_space)
    local space_freed_kb=$((space_before - space_after))
    local space_freed_mb=$((space_freed_kb / 1024))
    local space_freed_gb=$((space_freed_mb / 1024))
    
    # Formato migliore per visualizzazione
    local display_space=""
    if [[ $space_freed_gb -gt 0 ]]; then
        display_space="${space_freed_gb}.$(printf '%02d' $((space_freed_mb % 1024 * 100 / 1024))) GB"
    elif [[ $space_freed_mb -gt 0 ]]; then
        display_space="${space_freed_mb} MB"
    else
        display_space="${space_freed_kb} KB"
    fi
    
    local duration=$(( $(date +%s) - start_time ))
    
    echo
    log "INFO" "=== PULIZIA COMPLETATA ==="
    log "INFO" "Tempo esecuzione: ${duration} secondi"
    log "INFO" "Spazio recuperato: ${display_space}"
    
    if [[ "$DRY_RUN" == "false" ]]; then
        echo -e "${GREEN}✓ Pulizia completata con successo!${NC}"
        echo -e "  Spazio recuperato: ${display_space}"
        echo -e "  Log: $LOGFILE"
    else
        echo -e "${BLUE}ℹ Simulazione completata. Nessun file eliminato.${NC}"
        echo -e "  Spazio potenzialmente recuperabile: ${display_space}"
    fi
}

# Gestione segnali
trap 'log "WARN" "Script interrotto dall'\''utente"; exit 130' INT TERM

# Mostra istruzioni per cron se eseguito manualmente
if [[ -t 1 ]] && [[ "$DRY_RUN" == "false" ]]; then
    cat << 'CRONINFO'

CRONINFO
fi

# Esecuzione
main "$@"