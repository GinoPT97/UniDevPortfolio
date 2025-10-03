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

# Array per tracciare spazio per categoria
declare -A SPACE_SAVED
TOTAL_OPERATIONS=0
SUCCESSFUL_OPERATIONS=0

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

# Esegui comando con gestione errori e tracking spazio
run_cmd() {
    local cmd="$1"
    local desc="$2"
    local space_before_op=$(calculate_space)
    
    ((TOTAL_OPERATIONS++))
    
    if [[ "$VERBOSE" == "true" ]]; then
        log "DEBUG" "Eseguendo: $cmd"
    fi
    
    if [[ "$DRY_RUN" == "true" ]]; then
        log "INFO" "[DRY-RUN] $desc"
        ((SUCCESSFUL_OPERATIONS++))
        return 0
    fi
    
    if eval "$cmd" >> "$LOGFILE" 2>&1; then
        local space_after_op=$(calculate_space)
        local space_freed_op=$((space_before_op - space_after_op))
        local space_mb=$((space_freed_op / 1024))
        
        if [[ $space_mb -gt 0 ]]; then
            log "INFO" "✓ $desc - Recuperati: ${space_mb} MB"
        else
            log "INFO" "✓ $desc"
        fi
        ((SUCCESSFUL_OPERATIONS++))
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
    
    local space_before=$(du -sk /var/log 2>/dev/null | cut -f1)
    
    run_cmd "journalctl --vacuum-time=7d" "Pulizia journal (>7 giorni)"
    
    local old_logs=$(find /var/log -type f \( -name "*.gz" -o -name "*.1" -o -name "*.old" \) -mtime +30 2>/dev/null | wc -l)
    if [[ $old_logs -gt 0 ]]; then
        log "INFO" "Trovati $old_logs log compressi vecchi (>30 giorni)"
        if [[ "$DRY_RUN" == "false" ]]; then
            find /var/log -type f \( -name "*.gz" -o -name "*.1" -o -name "*.old" \) -mtime +30 -delete
            log "INFO" "✓ Log vecchi eliminati"
        else
            log "INFO" "[DRY-RUN] Eliminazione log vecchi"
        fi
    else
        log "INFO" "Nessun log compresso vecchio da eliminare"
    fi
    
    local space_after=$(du -sk /var/log 2>/dev/null | cut -f1)
    local freed=$(($space_before - $space_after))
    [[ $freed -gt 0 ]] && log "INFO" "Totale log sistema: $((freed / 1024)) MB recuperati"
}

# Pulizia THUMBNAILS
cleanup_thumbnails() {
    log "INFO" "=== PULIZIA THUMBNAILS ==="
    
    local total_freed=0
    
    for user in "${!USER_HOMES[@]}"; do
        local home="${USER_HOMES[$user]}"
        local thumb_dir="$home/.cache/thumbnails"
        
        if [[ -d "$thumb_dir" ]]; then
            local size_before=$(du -sk "$thumb_dir" 2>/dev/null | cut -f1)
            local count=$(find "$thumb_dir" -type f 2>/dev/null | wc -l)
            
            if [[ $count -gt 0 ]]; then
                log "INFO" "Utente $user: $count thumbnails ($(($size_before / 1024)) MB)"
                if [[ "$DRY_RUN" == "false" ]]; then
                    find "$thumb_dir" -type f -delete 2>/dev/null
                    local size_after=$(du -sk "$thumb_dir" 2>/dev/null | cut -f1)
                    local freed=$(($size_before - $size_after))
                    total_freed=$((total_freed + freed))
                    log "INFO" "✓ Thumbnails di $user pulite - Recuperati: $((freed / 1024)) MB"
                else
                    log "INFO" "[DRY-RUN] Pulizia thumbnails $user - Recuperabili: $(($size_before / 1024)) MB"
                fi
            fi
        fi
    done
    
    [[ $total_freed -gt 0 ]] && log "INFO" "Totale thumbnails: $((total_freed / 1024)) MB recuperati"
}

# Pulizia CACHE BROWSER
cleanup_browser_cache() {
    log "INFO" "=== PULIZIA CACHE BROWSER ==="
    
    local total_freed=0
    
    for user in "${!USER_HOMES[@]}"; do
        local home="${USER_HOMES[$user]}"
        
        # Firefox cache
        local ff_cache="$home/.cache/mozilla/firefox"
        if [[ -d "$ff_cache" ]]; then
            local size_before=$(du -sk "$ff_cache" 2>/dev/null | cut -f1)
            log "INFO" "Firefox cache di $user: $(($size_before / 1024)) MB"
            if [[ "$DRY_RUN" == "false" ]]; then
                find "$ff_cache" -type f \( -name "*.cache" -o -path "*/cache2/*" \) -delete 2>/dev/null
                local size_after=$(du -sk "$ff_cache" 2>/dev/null | cut -f1)
                local freed=$(($size_before - $size_after))
                total_freed=$((total_freed + freed))
                log "INFO" "✓ Cache Firefox pulita - Recuperati: $((freed / 1024)) MB"
            else
                log "INFO" "[DRY-RUN] Pulizia cache Firefox - Recuperabili: $(($size_before / 1024)) MB"
            fi
        fi
        
        # Chrome/Chromium cache
        for browser_cache in "$home/.cache/google-chrome" "$home/.cache/chromium"; do
            if [[ -d "$browser_cache/Default/Cache" ]]; then
                local size_before=$(du -sk "$browser_cache/Default/Cache" 2>/dev/null | cut -f1)
                log "INFO" "Cache browser di $user: $(($size_before / 1024)) MB"
                if [[ "$DRY_RUN" == "false" ]]; then
                    find "$browser_cache/Default/Cache" -type f -delete 2>/dev/null
                    local size_after=$(du -sk "$browser_cache/Default/Cache" 2>/dev/null | cut -f1)
                    local freed=$(($size_before - $size_after))
                    total_freed=$((total_freed + freed))
                    log "INFO" "✓ Cache browser pulita - Recuperati: $((freed / 1024)) MB"
                else
                    log "INFO" "[DRY-RUN] Pulizia cache browser - Recuperabili: $(($size_before / 1024)) MB"
                fi
            fi
        done
    done
    
    [[ $total_freed -gt 0 ]] && log "INFO" "Totale cache browser: $((total_freed / 1024)) MB recuperati"
}

# Pulizia FILE TEMPORANEI
cleanup_temp_files() {
    log "INFO" "=== PULIZIA FILE TEMPORANEI ==="
    
    # /tmp - solo file vecchi >3 giorni
    local tmp_old=$(find /tmp -type f -mtime +3 2>/dev/null | wc -l)
    if [[ $tmp_old -gt 0 ]]; then
        local size_before=$(du -sk /tmp 2>/dev/null | cut -f1)
        log "INFO" "Trovati $tmp_old file temporanei vecchi in /tmp ($(($size_before / 1024)) MB)"
        if [[ "$DRY_RUN" == "false" ]]; then
            find /tmp -type f -mtime +3 -delete 2>/dev/null
            local size_after=$(du -sk /tmp 2>/dev/null | cut -f1)
            local freed=$(($size_before - $size_after))
            log "INFO" "✓ File temporanei /tmp eliminati - Recuperati: $((freed / 1024)) MB"
        else
            log "INFO" "[DRY-RUN] Eliminazione file temporanei /tmp"
        fi
    else
        log "INFO" "Nessun file temporaneo vecchio in /tmp"
    fi
    
    # /var/tmp - solo file vecchi >7 giorni
    local vartmp_old=$(find /var/tmp -type f -mtime +7 2>/dev/null | wc -l)
    if [[ $vartmp_old -gt 0 ]]; then
        local size_before=$(du -sk /var/tmp 2>/dev/null | cut -f1)
        log "INFO" "Trovati $vartmp_old file temporanei vecchi in /var/tmp ($(($size_before / 1024)) MB)"
        if [[ "$DRY_RUN" == "false" ]]; then
            find /var/tmp -type f -mtime +7 -delete 2>/dev/null
            local size_after=$(du -sk /var/tmp 2>/dev/null | cut -f1)
            local freed=$(($size_before - $size_after))
            log "INFO" "✓ File temporanei /var/tmp eliminati - Recuperati: $((freed / 1024)) MB"
        else
            log "INFO" "[DRY-RUN] Eliminazione file temporanei /var/tmp"
        fi
    else
        log "INFO" "Nessun file temporaneo vecchio in /var/tmp"
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
                if [[ "$DRY_RUN" == "false" ]]; then
                    find "$cache_dir" -type d -name "__pycache__" -exec rm -rf {} + 2>/dev/null
                    find "$cache_dir" -type f -name "*.pyc" -delete 2>/dev/null
                    log "INFO" "✓ Cache Python pulita"
                else
                    log "INFO" "[DRY-RUN] Pulizia cache Python"
                fi
            fi
        fi
    done
}

# Pulizia SNAP vecchie versioni (mantiene solo le ultime 2)
cleanup_snap_old_versions() {
    log "INFO" "=== PULIZIA SNAP VERSIONI VECCHIE ==="
    
    if ! command -v snap &>/dev/null; then
        log "INFO" "Snap non installato, salto"
        return 0
    fi
    
    local removed=0
    snap list --all | awk '/disabled/{print $1, $3}' | while read snapname revision; do
        if [[ "$DRY_RUN" == "false" ]]; then
            snap remove "$snapname" --revision="$revision" 2>/dev/null && ((removed++))
            log "INFO" "✓ Rimossa versione vecchia: $snapname (revision $revision)"
        else
            log "INFO" "[DRY-RUN] Rimozione $snapname revision $revision"
        fi
    done
    
    [[ $removed -gt 0 ]] && log "INFO" "Rimosse $removed versioni vecchie di snap"
}

# Pulizia FLATPAK cache e dati inutilizzati
cleanup_flatpak_unused() {
    log "INFO" "=== PULIZIA FLATPAK DATI INUTILIZZATI ==="
    
    if ! command -v flatpak &>/dev/null; then
        log "INFO" "Flatpak non installato, salto"
        return 0
    fi
    
    if [[ "$DRY_RUN" == "false" ]]; then
        flatpak uninstall --unused -y 2>/dev/null && log "INFO" "✓ Rimossi runtime Flatpak inutilizzati"
        flatpak repair --user 2>/dev/null && log "INFO" "✓ Riparato Flatpak utente"
        flatpak repair --system 2>/dev/null && log "INFO" "✓ Riparato Flatpak sistema"
    else
        log "INFO" "[DRY-RUN] Pulizia Flatpak"
    fi
}

# Pulizia DOCKER (immagini, container, volumi non usati)
cleanup_docker() {
    log "INFO" "=== PULIZIA DOCKER ==="
    
    if ! command -v docker &>/dev/null; then
        log "INFO" "Docker non installato, salto"
        return 0
    fi
    
    if [[ "$DRY_RUN" == "false" ]]; then
        # Rimuovi container fermati
        docker container prune -f 2>/dev/null && log "INFO" "✓ Container Docker fermati rimossi"
        # Rimuovi immagini non usate
        docker image prune -a -f 2>/dev/null && log "INFO" "✓ Immagini Docker non usate rimosse"
        # Rimuovi volumi non usati
        docker volume prune -f 2>/dev/null && log "INFO" "✓ Volumi Docker non usati rimossi"
        # Rimuovi network non usati
        docker network prune -f 2>/dev/null && log "INFO" "✓ Network Docker non usati rimossi"
        # Pulizia completa build cache
        docker builder prune -a -f 2>/dev/null && log "INFO" "✓ Build cache Docker pulita"
    else
        log "INFO" "[DRY-RUN] Pulizia Docker"
    fi
}

# Pulizia CORE DUMPS
cleanup_core_dumps() {
    log "INFO" "=== PULIZIA CORE DUMPS ==="
    
    local core_count=0
    
    # Core dumps in /var/crash
    if [[ -d /var/crash ]]; then
        core_count=$(find /var/crash -type f -name "*.crash" 2>/dev/null | wc -l)
        if [[ $core_count -gt 0 ]]; then
            log "INFO" "Trovati $core_count crash dump"
            if [[ "$DRY_RUN" == "false" ]]; then
                find /var/crash -type f -name "*.crash" -delete 2>/dev/null
                log "INFO" "✓ Core dumps eliminati"
            else
                log "INFO" "[DRY-RUN] Eliminazione core dumps"
            fi
        fi
    fi
    
    # Core dumps systemd
    if command -v coredumpctl &>/dev/null; then
        if [[ "$DRY_RUN" == "false" ]]; then
            coredumpctl --no-pager list 2>/dev/null && coredumpctl --no-pager --vacuum-time=7d 2>/dev/null
            log "INFO" "✓ Core dumps systemd puliti"
        else
            log "INFO" "[DRY-RUN] Pulizia core dumps systemd"
        fi
    fi
}

# Pulizia FONT CACHE duplicata
cleanup_font_cache() {
    log "INFO" "=== PULIZIA FONT CACHE ==="
    
    for user in "${!USER_HOMES[@]}"; do
        local home="${USER_HOMES[$user]}"
        local font_cache="$home/.cache/fontconfig"
        
        if [[ -d "$font_cache" ]]; then
            local size=$(du -sh "$font_cache" 2>/dev/null | cut -f1)
            log "INFO" "Font cache di $user: $size"
            if [[ "$DRY_RUN" == "false" ]]; then
                rm -rf "$font_cache"/* 2>/dev/null
                log "INFO" "✓ Font cache pulita (si rigenera automaticamente)"
            else
                log "INFO" "[DRY-RUN] Pulizia font cache"
            fi
        fi
    done
    
    # Rigenera cache font sistema
    if command -v fc-cache &>/dev/null; then
        if [[ "$DRY_RUN" == "false" ]]; then
            fc-cache -f -v &>/dev/null && log "INFO" "✓ Cache font sistema rigenerata"
        fi
    fi
}

# Pulizia SYSTEMD journal size limit
cleanup_systemd_journal() {
    log "INFO" "=== OTTIMIZZAZIONE SYSTEMD JOURNAL ==="
    
    if [[ "$DRY_RUN" == "false" ]]; then
        # Limita a 100MB
        journalctl --vacuum-size=100M 2>/dev/null && log "INFO" "✓ Journal limitato a 100MB"
        # Rimuovi vecchi di 7 giorni
        journalctl --vacuum-time=7d 2>/dev/null && log "INFO" "✓ Journal vecchi di 7+ giorni rimossi"
    else
        log "INFO" "[DRY-RUN] Ottimizzazione journal"
    fi
}

# Pulizia ROTATED LOGS anche non compressi
cleanup_rotated_logs() {
    log "INFO" "=== PULIZIA LOG RUOTATI ==="
    
    local log_count=$(find /var/log -type f \( -name "*.log.[0-9]*" -o -name "*.log.old" \) -mtime +7 2>/dev/null | wc -l)
    
    if [[ $log_count -gt 0 ]]; then
        log "INFO" "Trovati $log_count log ruotati vecchi"
        if [[ "$DRY_RUN" == "false" ]]; then
            find /var/log -type f \( -name "*.log.[0-9]*" -o -name "*.log.old" \) -mtime +7 -delete 2>/dev/null
            log "INFO" "✓ Log ruotati vecchi eliminati"
        else
            log "INFO" "[DRY-RUN] Eliminazione log ruotati"
        fi
    fi
}

# Pulizia MAN-DB cache
cleanup_man_cache() {
    log "INFO" "=== PULIZIA MAN-DB CACHE ==="
    
    if [[ -d /var/cache/man ]]; then
        local size=$(du -sh /var/cache/man 2>/dev/null | cut -f1)
        log "INFO" "Cache man-db: $size"
        if [[ "$DRY_RUN" == "false" ]]; then
            rm -rf /var/cache/man/* 2>/dev/null
            mandb -q 2>/dev/null && log "INFO" "✓ Cache man-db rigenerata"
        else
            log "INFO" "[DRY-RUN] Pulizia cache man-db"
        fi
    fi
}

# Pulizia DPKG/APT liste vecchie
cleanup_apt_lists() {
    log "INFO" "=== PULIZIA APT LISTE PARZIALI ==="
    
    if [[ -d /var/lib/apt/lists ]]; then
        local partial_count=$(find /var/lib/apt/lists/partial -type f 2>/dev/null | wc -l)
        
        if [[ $partial_count -gt 0 ]]; then
            log "INFO" "Trovati $partial_count file parziali"
            if [[ "$DRY_RUN" == "false" ]]; then
                rm -rf /var/lib/apt/lists/partial/* 2>/dev/null
                log "INFO" "✓ Liste parziali APT pulite"
            else
                log "INFO" "[DRY-RUN] Pulizia liste APT"
            fi
        fi
    fi
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
    
    local total_freed=0
    
    for user in "${!USER_HOMES[@]}"; do
        local home="${USER_HOMES[$user]}"
        
        local tmp_count=$(find "$home" -maxdepth 3 -type f -name "*.tmp" -mtime +14 2>/dev/null | wc -l)
        local bak_count=$(find "$home" -maxdepth 3 -type f -name "*.bak" -mtime +30 2>/dev/null | wc -l)
        
        if [[ $tmp_count -gt 0 ]]; then
            log "INFO" "Utente $user: $tmp_count file .tmp vecchi (>14 giorni)"
            if [[ "$DRY_RUN" == "false" ]]; then
                local size_before=$(find "$home" -maxdepth 3 -type f -name "*.tmp" -mtime +14 -exec du -sk {} + 2>/dev/null | awk '{sum+=$1}END{print sum}')
                find "$home" -maxdepth 3 -type f -name "*.tmp" -mtime +14 -delete 2>/dev/null
                total_freed=$((total_freed + ${size_before:-0}))
                log "INFO" "✓ File .tmp eliminati - Recuperati: $((${size_before:-0} / 1024)) MB"
            else
                log "INFO" "[DRY-RUN] Eliminazione file .tmp"
            fi
        fi
        
        if [[ $bak_count -gt 0 ]]; then
            log "INFO" "Utente $user: $bak_count file .bak vecchi (>30 giorni)"
            if [[ "$DRY_RUN" == "false" ]]; then
                local size_before=$(find "$home" -maxdepth 3 -type f -name "*.bak" -mtime +30 -exec du -sk {} + 2>/dev/null | awk '{sum+=$1}END{print sum}')
                find "$home" -maxdepth 3 -type f -name "*.bak" -mtime +30 -delete 2>/dev/null
                total_freed=$((total_freed + ${size_before:-0}))
                log "INFO" "✓ File .bak eliminati - Recuperati: $((${size_before:-0} / 1024)) MB"
            else
                log "INFO" "[DRY-RUN] Eliminazione file .bak"
            fi
        fi
    done
    
    [[ $total_freed -gt 0 ]] && log "INFO" "Totale file .tmp/.bak: $((total_freed / 1024)) MB recuperati"
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
    cleanup_systemd_journal
    cleanup_rotated_logs
    cleanup_thumbnails
    cleanup_browser_cache
    cleanup_temp_files
    cleanup_python_cache
    cleanup_snap_old_versions
    cleanup_flatpak_unused
    cleanup_docker
    cleanup_core_dumps
    cleanup_font_cache
    cleanup_man_cache
    cleanup_apt_lists
    cleanup_bash_history
    cleanup_tmp_bak_files
    system_optimization
    
    # Calcola spazio recuperato
    local space_after=$(calculate_space)
    local space_freed_kb=$((space_before - space_after))
    
    # Formato migliore per visualizzazione
    local display_space=""
    local space_freed_mb=0
    local space_freed_gb=0
    
    if [[ $space_freed_kb -lt 0 ]]; then
        # A volte può essere negativo per scritture durante l'esecuzione
        display_space="< 1 MB"
    elif [[ $space_freed_kb -lt 1024 ]]; then
        display_space="${space_freed_kb} KB"
    else
        space_freed_mb=$((space_freed_kb / 1024))
        if [[ $space_freed_mb -lt 1024 ]]; then
            display_space="${space_freed_mb} MB"
        else
            space_freed_gb=$((space_freed_mb / 1024))
            local decimal=$((space_freed_mb % 1024 * 100 / 1024))
            display_space="${space_freed_gb}.$(printf '%02d' $decimal) GB"
        fi
    fi
    
    local duration=$(( $(date +%s) - start_time ))
    local minutes=$((duration / 60))
    local seconds=$((duration % 60))
    
    echo
    echo -e "${GREEN}╔═══════════════════════════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║            PULIZIA COMPLETATA CON SUCCESSO                ║${NC}"
    echo -e "${GREEN}╚═══════════════════════════════════════════════════════════╝${NC}"
    echo
    
    # Calcolo percentuale se possibile
    local percent_freed=0
    if [[ $space_before -gt 0 ]] && [[ $space_freed_kb -gt 0 ]]; then
        percent_freed=$((space_freed_kb * 100 / space_before))
    fi
    
    # Statistiche operazioni
    local success_rate=0
    if [[ $TOTAL_OPERATIONS -gt 0 ]]; then
        success_rate=$((SUCCESSFUL_OPERATIONS * 100 / TOTAL_OPERATIONS))
    fi
    
    log "INFO" "=== RIEPILOGO FINALE ==="
    log "INFO" "Tempo esecuzione: ${minutes}m ${seconds}s"
    log "INFO" "Spazio disco recuperato: ${display_space}"
    [[ $percent_freed -gt 0 ]] && log "INFO" "Percentuale liberata: ${percent_freed}%"
    log "INFO" "Operazioni completate: ${SUCCESSFUL_OPERATIONS}/${TOTAL_OPERATIONS} (${success_rate}%)"
    
    if [[ "$DRY_RUN" == "false" ]]; then
        echo -e "${GREEN}✓ Operazioni completate!${NC}"
        echo
        echo -e "  ${BLUE}╔═══════════════════════════════════════════════════════╗${NC}"
        echo -e "  ${BLUE}║${NC}  📊 ${GREEN}SPAZIO RECUPERATO:${NC} ${YELLOW}${display_space}${NC}"
        [[ $percent_freed -gt 0 ]] && echo -e "  ${BLUE}║${NC}  📈 ${GREEN}PERCENTUALE LIBERATA:${NC} ${YELLOW}${percent_freed}%${NC}"
        echo -e "  ${BLUE}║${NC}  ⏱️  ${GREEN}TEMPO IMPIEGATO:${NC} ${YELLOW}${minutes}m ${seconds}s${NC}"
        echo -e "  ${BLUE}║${NC}  ✅ ${GREEN}OPERAZIONI RIUSCITE:${NC} ${YELLOW}${SUCCESSFUL_OPERATIONS}/${TOTAL_OPERATIONS}${NC} (${success_rate}%)"
        echo -e "  ${BLUE}║${NC}  📝 ${GREEN}LOG COMPLETO:${NC} ${YELLOW}$LOGFILE${NC}"
        echo -e "  ${BLUE}╚═══════════════════════════════════════════════════════╝${NC}"
        echo
        
        # Suggerimento se poco spazio recuperato
        if [[ $space_freed_mb -lt 50 ]] && [[ "$DRY_RUN" == "false" ]]; then
            echo -e "  ${YELLOW}💡 Suggerimento:${NC} Sistema già pulito! Esegui di nuovo quando necessario."
        elif [[ $space_freed_gb -ge 1 ]]; then
            echo -e "  ${GREEN}🎉 Ottimo!${NC} Recuperati oltre 1 GB di spazio disco!"
        fi
        echo
    else
        echo -e "${BLUE}ℹ Simulazione completata. Nessun file eliminato.${NC}"
        echo
        echo -e "  ${BLUE}╔═══════════════════════════════════════════════════════╗${NC}"
        echo -e "  ${BLUE}║${NC}  📊 ${YELLOW}SPAZIO RECUPERABILE:${NC} ${GREEN}${display_space}${NC}"
        [[ $percent_freed -gt 0 ]] && echo -e "  ${BLUE}║${NC}  📈 ${YELLOW}PERCENTUALE LIBERABILE:${NC} ${GREEN}${percent_freed}%${NC}"
        echo -e "  ${BLUE}║${NC}  🔍 ${YELLOW}OPERAZIONI SIMULATE:${NC} ${GREEN}${TOTAL_OPERATIONS}${NC}"
        echo -e "  ${BLUE}╚═══════════════════════════════════════════════════════╝${NC}"
        echo
        echo -e "  ${GREEN}✓${NC} Esegui senza ${YELLOW}--dry-run${NC} per applicare le modifiche"
        echo
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