#!/bin/bash

set -e

# Lo script va eseguito come superutente
if [[ $EUID -ne 0 ]]; then
    echo "[ERROR] Questo script deve essere eseguito come superutente."
    exit 1
fi

# Funzione per loggare i messaggi (livello e timestamp)
log() {
    local level="$1"
    local message="$2"
    echo "[$level] $(date '+%Y-%m-%d %H:%M:%S') - $message"
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

# Se disponibile, ottimizzazione dello spazio libero (per SSD)
if command -v fstrim &>/dev/null; then
    run_cmd "fstrim -av" "Ottimizzazione spazio libero"
else
    log "INFO" "fstrim non disponibile, salto questa operazione."
fi

# Visualizzazione stato disco e memoria (opzionale)
df -h | while read -r line; do log "INFO" "Stato disco: $line"; done
free -h | while read -r line; do log "INFO" "Stato memoria: $line"; done

# (Opzionale) Ottimizzazione della swap
run_cmd "swapoff -a && swapon -a" "Ottimizzazione swap"

log "INFO" "Pulizia completata con successo!"

