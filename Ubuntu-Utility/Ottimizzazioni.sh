#!/bin/bash

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

# Funzione per eseguire un comando e verificare l'esito senza usare eval
run_cmd() {
    bash -c "$1"
    if [[ $? -ne 0 ]]; then
        log "ERROR" "$2 fallita"
    else
        log "INFO" "$2 completata con successo"
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
df -h | while read line; do log "INFO" "Stato disco: $line"; done
free -h | while read line; do log "INFO" "Stato memoria: $line"; done

# (Opzionale) Ottimizzazione della swap
run_cmd "swapoff -a && swapon -a" "Ottimizzazione swap"

# Deframmentazione del disco (opzionale, solo per file system ext4)
if command -v e4defrag &>/dev/null; then
    run_cmd "e4defrag /" "Deframmentazione del disco (root)"
else
    log "INFO" "e4defrag non disponibile, salto la deframmentazione."
fi

log "INFO" "Pulizia completata con successo!"

