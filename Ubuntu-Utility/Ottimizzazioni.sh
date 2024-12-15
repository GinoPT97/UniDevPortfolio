#!/bin/bash

# Richiede permessi di superutente
if [[ $EUID -ne 0 ]]; then
    echo "[ERROR] Questo script deve essere eseguito come superutente."
    exit 1
fi

# Funzione per loggare informazioni
log_info() {
    echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# Funzione per loggare errori
log_error() {
    echo "[ERROR] $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# Funzione per eseguire un comando in modo sicuro
execute_command() {
    eval "$1"
    if [[ $? -ne 0 ]]; then
        log_error "$2 fallita"
    else
        log_info "$2 completata con successo."
    fi
}

# Pulizia dei log di sistema e temporanei
log_info "Pulizia dei log di sistema e file temporanei..."
execute_command "sudo journalctl --vacuum-files=1" "Pulizia dei log di sistema"
execute_command "sudo rm -rf /tmp/* /var/tmp/*" "Pulizia dei file temporanei"

# Pulizia della cache di APT, pacchetti obsoleti e font
log_info "Pulizia della cache di APT e pacchetti obsoleti..."
execute_command "sudo apt-get clean" "Pulizia della cache APT"
execute_command "sudo apt-get autoremove --purge -y" "Rimozione pacchetti obsoleti"
execute_command "sudo fc-cache -f -v" "Pulizia della cache dei font"

# Pulizia dei log di sistema più vecchi di 7 giorni
log_info "Pulizia dei log di sistema più vecchi di 7 giorni..."
execute_command "sudo journalctl --vacuum-time=7d" "Pulizia dei log di sistema vecchi"

# Pulizia della cache del browser
log_info "Pulizia della cache dei browser..."
if command -v google-chrome &>/dev/null; then
    execute_command "google-chrome --no-sandbox --clear-cache" "Pulizia cache Google Chrome"
else
    log_info "Google Chrome non installato. Salto la pulizia."
fi
if [ -d "~/.cache/mozilla/firefox/" ]; then
    execute_command "rm -rf ~/.cache/mozilla/firefox/*" "Pulizia cache Firefox"
else
    log_info "Firefox non trovato o cache non presente."
fi

# Pulizia dei log di Apache
if [ -d "/var/log/apache2/" ]; then
    log_info "Pulizia dei log di Apache..."
    execute_command "sudo rm -f /var/log/apache2/*.log" "Pulizia log Apache"
else
    log_info "Apache non trovato. Salto la pulizia dei log."
fi

# Compattazione e deframmentazione
log_info "Compattazione e deframmentazione del filesystem..."
if command -v e4defrag &>/dev/null; then
    execute_command "sudo e4defrag /" "Deframmentazione del filesystem"
else
    log_info "Strumento e4defrag non installato. Salto la deframmentazione."
fi

# Verifica dello stato della partizione radice
log_info "Controllo della partizione radice..."
mounted=$(findmnt -n -o SOURCE /)
if [[ "$mounted" == "" ]]; then
    log_error "Impossibile trovare la partizione radice. Controlla il sistema."
else
    log_info "La partizione radice è montata come $mounted. Nessuna azione necessaria."
fi

# Ottimizzazione dello spazio libero
log_info "Ottimizzazione dello spazio libero..."
if command -v fstrim &>/dev/null; then
    execute_command "sudo fstrim -av" "Ottimizzazione spazio libero"
else
    log_info "Strumento fstrim non installato. Salto l'ottimizzazione."
fi

# Pulizia dei log di systemd
log_info "Pulizia dei log di systemd..."
execute_command "sudo journalctl --vacuum-size=50M" "Pulizia log systemd"

# Pulizia con BleachBit, se installato
if command -v bleachbit &>/dev/null; then
    log_info "Pulizia con BleachBit..."
    execute_command "sudo bleachbit --cleanup --quiet" "Pulizia con BleachBit"
else
    log_info "BleachBit non trovato. Salto la pulizia."
fi

# Rimozione dei pacchetti orfani e aggiornamenti di sicurezza
log_info "Aggiornamenti e rimozione dei pacchetti orfani..."
execute_command "sudo apt-get update" "Aggiornamento lista pacchetti"
execute_command "sudo apt-get upgrade -y" "Aggiornamenti di sicurezza"

# Controllo dello spazio su disco
log_info "Controllo dello spazio su disco..."
df -h | tee >(log_info "Controllo spazio disco eseguito.")

# Controllo della memoria
log_info "Controllo della memoria..."
free -h | tee >(log_info "Controllo memoria eseguito.")

# Ottimizzazione della memoria swap
log_info "Ottimizzazione della memoria swap..."
execute_command "sudo swapoff -a && sudo swapon -a" "Ottimizzazione della swap"

log_info "Script completato con successo!"
