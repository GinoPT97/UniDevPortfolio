#!/bin/bash

# Funzione per loggare messaggi
log() {
    local type="$1"
    shift
    echo "[$type] $(date '+%Y-%m-%d %H:%M:%S') - $*"
}

# Funzione per aggiornare pacchetti APT
update_apt_packages() {
    log "INFO" "Aggiornamento dell'elenco dei pacchetti..."
    sudo apt-get update -y
    log "INFO" "Elenco pacchetti aggiornato."

    log "INFO" "Aggiornamento dei pacchetti APT..."
    sudo apt-get upgrade -y
}

# Funzione per pulire i pacchetti APT
clean_apt_packages() {
    log "INFO" "Pulizia dei pacchetti inutili e della cache..."
    sudo apt-get autoremove --purge -y
    sudo apt-get clean -y
}

# Funzione per installare Node.js e npm
install_node_and_npm() {
    log "INFO" "Installazione di Node.js e npm..."
    sudo apt install -y nodejs npm

    log "INFO" "Installazione dei pacchetti npm..."
    npm install
}

# Funzione per aggiornare Node.js
update_node() {
    if command -v node &> /dev/null; then
        log "INFO" "Aggiornamento di Node.js alla versione stabile..."
        sudo npm install -g n && sudo n stable

        log "INFO" "Verifica dei pacchetti npm obsoleti..."
        npm outdated

        log "INFO" "Aggiornamento dei pacchetti npm..."
        npm update

        log "INFO" "Reinstallazione dei pacchetti npm..."
        npm install

        log "INFO" "Correzione delle vulnerabilità npm..."
        npm audit fix --force
    else
        log "INFO" "Node.js non è installato. Salto questo passaggio."
    fi
}

# Funzione per aggiornare ambienti virtuali Python
update_python_envs() {
    log "INFO" "Ricerca di ambienti virtuali Python..."
    find ~/ -type d \( -name "env*" -or -name "venv*" \) -exec bash -c '
        for venv; do
            if [ -d "$venv/bin" ]; then
                log "INFO" "Aggiornamento dell'ambiente virtuale: $venv..."
                source "$venv/bin/activate"
                pip install --upgrade pip
                pip list --outdated | awk "{print \$1}" | tail -n +3 | xargs -n 1 pip install --upgrade
                deactivate
            fi
        done
    ' bash {} +
}

# Funzione per aggiornare Conda
update_conda() {
    if command -v conda &> /dev/null; then
        log "INFO" "Aggiornamento di Conda..."
        conda update --all -y
    else
        log "INFO" "Conda non è installato. Salto questo passaggio."
    fi
}

# Funzione per sbloccare il Wi-Fi
unblock_wifi() {
    log "INFO" "Sblocco della sospensione energetica del Wi-Fi..."
    sudo rfkill unblock wifi
}

# Funzione per installare Snapd
install_snapd() {
    log "INFO" "Controllo di Snapd..."
    if ! dpkg -l | grep -q snapd; then
        log "INFO" "Installazione di Snapd..."
        sudo apt-get install snapd -y
    else
        log "INFO" "Snapd è già installato."
    fi

    log "INFO" "Aggiornamento dei pacchetti Snap..."
    sudo snap refresh
}

# Funzione per gestire eventuali blocchi di dpkg
reload_systemd_and_dpkg() {
    log "INFO" "Ricaricamento dei demoni di sistema..."
    sudo systemctl daemon-reload

    log "INFO" "Gestione del blocco dpkg..."
    while sudo fuser /var/lib/dpkg/lock-frontend >/dev/null 2>&1; do
        log "INFO" "Blocco dpkg rilevato. Attesa di 5 secondi..."
        sleep 5
    done
    sudo dpkg --configure -a
}

# Funzione per abilitare il firewall
enable_firewall() {
    log "INFO" "Verifica dello stato del firewall..."
    if ! sudo ufw status | grep -q 'Status: active'; then
        log "INFO" "Abilitazione del firewall..."
        sudo ufw enable
    else
        log "INFO" "Il firewall è già attivo."
    fi
}

# Inizio dello script
log "INFO" "Inizio aggiornamenti..."

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

log "INFO" "Aggiornamenti completati!"
