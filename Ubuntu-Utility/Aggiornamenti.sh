#!/bin/bash

set -e

# Funzione per loggare informazioni
log_info() {
    echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# Funzione per loggare gli errori
log_error() {
    echo "[ERROR] $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# Funzione per gestire errori
handle_error() {
    log_error "Si è verificato un errore nello script."
    exit 1
}

trap handle_error ERR

# Funzione per aggiornare pacchetti APT
update_apt_packages() {
    log_info "Aggiornamento dell'elenco dei pacchetti..."
    sudo apt-get update -y
    log_info "Elenco pacchetti aggiornato."

    log_info "Aggiornamento dei pacchetti APT..."
    sudo apt-get upgrade -y
}

# Funzione per pulire i pacchetti APT
clean_apt_packages() {
    log_info "Pulizia dei pacchetti inutili e della cache..."
    sudo apt-get autoremove --purge -y
    sudo apt-get clean -y
}

# Funzione per installare Node.js e npm
install_node_and_npm() {
    log_info "Aggiornamento dell'elenco dei pacchetti..."
    sudo apt update -y

    log_info "Installazione di Node.js e npm..."
    sudo apt install -y nodejs npm

    log_info "Installazione dei pacchetti npm..."
    npm install
}

# Funzione per aggiornare Node.js
update_node() {
    if command -v node &> /dev/null; then
        log_info "Aggiornamento di Node.js alla versione stabile..."
        sudo npm install -g n && sudo n stable
    else
        log_info "Node.js non è installato. Salto questo passaggio."
    fi
}

# Funzione per aggiornare ambienti virtuali Python
update_python_envs() {
    log_info "Ricerca di ambienti virtuali Python..."
    for venv in $(find ~/ -type d -name "env*" -or -name "venv*"); do
        if [ -d "$venv/bin" ]; then
            log_info "Aggiornamento dell'ambiente virtuale: $venv..."
            source "$venv/bin/activate"
            pip install --upgrade pip
            pip list --outdated | awk '{print $1}' | tail -n +3 | xargs -n 1 pip install --upgrade
            deactivate
        fi
    done
}

# Funzione per aggiornare Conda
update_conda() {
    if command -v conda &> /dev/null; then
        log_info "Aggiornamento di Conda..."
        conda update --all -y
    else
        log_info "Conda non è installato. Salto questo passaggio."
    fi
}

# Funzione per sbloccare il Wi-Fi
unblock_wifi() {
    log_info "Sblocco della sospensione energetica del Wi-Fi..."
    sudo rfkill unblock wifi
}

# Funzione per installare Snapd
install_snapd() {
    log_info "Controllo di Snapd..."
    if ! dpkg -l | grep -q snapd; then
        log_info "Installazione di Snapd..."
        sudo apt-get install snapd -y
    else
        log_info "Snapd è già installato."
    fi

    log_info "Aggiornamento dei pacchetti Snap..."
    sudo snap refresh
}

# Funzione per gestire eventuali blocchi di dpkg
reload_systemd_and_dpkg() {
    log_info "Ricaricamento dei demoni di sistema..."
    sudo systemctl daemon-reload

    log_info "Gestione del blocco dpkg..."
    while sudo fuser /var/lib/dpkg/lock-frontend >/dev/null 2>&1; do
        log_info "Blocco dpkg rilevato. Attesa di 5 secondi..."
        sleep 5
    done
    sudo dpkg --configure -a
}

# Funzione per abilitare il firewall
enable_firewall() {
    log_info "Verifica dello stato del firewall..."
    if ! sudo ufw status | grep -q 'Status: active'; then
        log_info "Abilitazione del firewall..."
        sudo ufw enable
    else
        log_info "Il firewall è già attivo."
    fi
}

# Inizio dello script
log_info "Inizio aggiornamenti..."

reload_systemd_and_dpkg
update_apt_packages
clean_apt_packages
install_node_and_npm
update_node
update_python_envs
update_conda
unblock_wifi
install_snapd
enable_firewall

log_info "Aggiornamenti completati!"
