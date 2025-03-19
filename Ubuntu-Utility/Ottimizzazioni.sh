#!/bin/bash
set -e
set -o pipefail

LOGFILE="/var/log/cleaner.log"

# Lo script va eseguito come superutente
if [[ $EUID -ne 0 ]]; then
    echo "[ERROR] Questo script deve essere eseguito come superutente."
    exit 1
fi

# Funzione per loggare i messaggi (livello e timestamp)
log() {
    local level="$1"
    local message="$2"
    echo "[$level] $(date '+%Y-%m-%d %H:%M:%S') - $message" | tee -a "$LOGFILE"
}

# Funzione per eseguire un comando e loggare l'esito
run_cmd() {
    local cmd="$1"
    local desc="$2"
    if eval "$cmd"; then
        log "INFO" "$desc completata con successo"
    else
        log "ERROR" "$desc fallita"
    fi
}

log "INFO" "Avvio pulizia sistema..."

# Pulizia di sistema di base
run_cmd "journalctl --vacuum-files=1" "Pulizia log di sistema"
run_cmd "rm -rf /tmp/* /var/tmp/*" "Pulizia file temporanei"
run_cmd "apt-get clean" "Pulizia cache APT"
run_cmd "apt-get autoremove --purge -y" "Rimozione pacchetti obsoleti"
run_cmd "fc-cache -f -v" "Aggiornamento cache font"
run_cmd "journalctl --vacuum-time=7d" "Rimozione log vecchi di 7 giorni"

# Configurazione pacchetti non configurati e risoluzione dipendenze mancanti
run_cmd "dpkg --configure -a" "Configurazione pacchetti non configurati"
run_cmd "apt-get install -f" "Risoluzione dipendenze mancanti"

# Rimozione delle localizzazioni non utilizzate
if command -v localepurge &>/dev/null; then
    run_cmd "localepurge" "Rimozione delle localizzazioni non utilizzate"
else
    log "INFO" "localepurge non disponibile, salto questa operazione."
fi

# Pulizia delle cache delle applicazioni
run_cmd "rm -rf ~/.cache/*" "Pulizia delle cache delle applicazioni"

# Rimozione dei file di configurazione residui
run_cmd "dpkg -l | awk '/^rc/ {print \$2}' | xargs -r dpkg --purge" "Rimozione dei file di configurazione residui"

# Pulizia delle miniature
run_cmd "rm -rf ~/.thumbnails/*" "Pulizia delle miniature"

# Pulizia dei pacchetti APT scaricati
run_cmd "apt-get autoclean" "Rimozione dei pacchetti APT scaricati"

# Verifica e impostazione permessi per directory log PostgreSQL
run_cmd "mkdir -p /var/log/postgresql/" "Creazione directory log PostgreSQL"
run_cmd "chown -R postgres:postgres /var/log/postgresql/" "Impostazione permessi directory log PostgreSQL"

# Esempi di ulteriori pulizie in stile BleachBit:
run_cmd "rm -rf ~/.mozilla/firefox/*.default*/cache2/*" "Pulizia cache Firefox"
run_cmd "rm -rf ~/.cache/chromium/*" "Pulizia cache Chromium"
run_cmd "find /var/log -type f -name '*.log' -exec truncate -s 0 {} \;" "Svuotamento file di log"

# Se disponibile, ottimizzazione dello spazio libero (per SSD)
if command -v fstrim &>/dev/null; then
    run_cmd "fstrim -av" "Ottimizzazione spazio libero"
else
    log "INFO" "fstrim non disponibile, salto questa operazione."
fi

# (Opzionale) Ottimizzazione della swap
run_cmd "swapoff -a && swapon -a" "Ottimizzazione swap"

log "INFO" "Pulizia completata con successo!"