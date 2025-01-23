#!/bin/bash

# Richiede permessi di superutente
if [[ $EUID -ne 0 ]]; then
    echo "[ERROR] Questo script deve essere eseguito come superutente."
    exit 1
fi

# Funzione per loggare messaggi
log() {
    local level="$1"
    local message="$2"
    echo "[$level] $(date '+%Y-%m-%d %H:%M:%S') - $message"
}

# Esecuzione sicura di comandi
execute_command() {
    eval "$1"
    if [[ $? -ne 0 ]]; then
        log "ERROR" "$2 fallita"
    else
        log "INFO" "$2 completata con successo."
    fi
}

# Pulizia dei file temporanei e della cache
log "INFO" "Avvio pulizia sistema..."
execute_command "journalctl --vacuum-files=1" "Pulizia log di sistema"
execute_command "rm -rf /tmp/* /var/tmp/*" "Pulizia file temporanei"
execute_command "apt-get clean" "Pulizia cache APT"
execute_command "apt-get autoremove --purge -y" "Rimozione pacchetti obsoleti"
execute_command "fc-cache -f -v" "Aggiornamento cache font"
execute_command "journalctl --vacuum-time=7d" "Rimozione log vecchi di 7 giorni"

# Pulizia cache browser
log "INFO" "Pulizia cache browser..."
if command -v google-chrome &>/dev/null; then
    execute_command "google-chrome --no-sandbox --clear-cache" "Pulizia cache Google Chrome"
else
    log "INFO" "Google Chrome non installato."
fi
if [ -d "$HOME/.cache/mozilla/firefox/" ]; then
    execute_command "rm -rf $HOME/.cache/mozilla/firefox/*" "Pulizia cache Firefox"
else
    log "INFO" "Cache di Firefox non trovata."
fi

# Pulizia log Apache
if [ -d "/var/log/apache2/" ]; then
    execute_command "rm -f /var/log/apache2/*.log" "Pulizia log Apache"
else
    log "INFO" "Apache non installato."
fi

# Ottimizzazione file system e spazio libero
if command -v e4defrag &>/dev/null; then
    execute_command "e4defrag /" "Deframmentazione filesystem"
else
    log "INFO" "e4defrag non disponibile."
fi

if command -v fstrim &>/dev/null; then
    execute_command "fstrim -av" "Ottimizzazione spazio libero"
else
    log "INFO" "fstrim non disponibile."
fi

# Pulizia con BleachBit
if command -v bleachbit &>/dev/null; then
    execute_command "bleachbit --cleanup --quiet" "Pulizia con BleachBit"
else
    log "INFO" "BleachBit non installato."
fi

# Aggiornamenti e controllo sistema
log "INFO" "Aggiornamento pacchetti e controllo sistema..."
execute_command "apt-get update" "Aggiornamento lista pacchetti"
execute_command "apt-get upgrade -y" "Aggiornamento pacchetti"
df -h | log "INFO" "Stato disco verificato."
free -h | log "INFO" "Stato memoria verificato."

# Ottimizzazione memoria swap
execute_command "swapoff -a && swapon -a" "Ottimizzazione swap"

# Generazione file pacchettidriver.txt
log "INFO" "Generazione file pacchettidriver.txt..."
(echo "### Pacchetti APT installati ###" && dpkg --get-selections | sort && \
 echo -e "\n### Pacchetti Snap installati ###" && snap list | column -t && \
 echo -e "\n### Driver disponibili (APT e Snap) ###" && \
 ubuntu-drivers devices | awk '/recommended|manual/ {print}' | sort && \
 echo -e "\n### Moduli del Kernel (Driver caricati) ###" && \
 lsmod | sort && \
 echo -e "\n### Dettagli moduli NVIDIA (se presenti) ###" && \
 modinfo nvidia 2>/dev/null && \
 echo -e "\n### Dettagli moduli di rete ###" && \
 lsmod | grep -i net && \
 echo -e "\n### Informazioni hardware (inclusi driver) ###" && \
sudo lshw -short) > /home/kenobi/Documenti/pacchettidriver.txt 2>&1

log "INFO" "Ottimizzazioni completate con successo!"
