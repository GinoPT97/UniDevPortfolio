#!/bin/bash

# Funzione per loggare informazioni
log_info() {
    echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# Funzione per aggiornare pacchetti APT
update_apt_packages() {
    log_info "Aggiornamento dell'elenco dei pacchetti..."
    sudo apt-get update -y

    log_info "Aggiornamento dei pacchetti APT..."
    sudo apt-get upgrade -y

    log_info "Aggiornamento dei pacchetti e delle dipendenze..."
    sudo apt-get dist-upgrade -y
}

# Funzione per pulire i pacchetti APT
clean_apt_packages() {
    log_info "Pulizia dei pacchetti inutili e orfani..."
    sudo apt-get autoremove --purge -y

    log_info "Pulizia della cache dei pacchetti APT..."
    sudo apt-get clean -y
}

# Funzione per aggiornare Node.js
update_node() {
    log_info "Aggiornamento di Node.js..."
    if command -v node &> /dev/null; then
        sudo npm install -g n
        sudo n stable
    else
        log_info "Node.js non è installato."
    fi
}

# Funzione per aggiornare ambienti virtuali Python
update_python_envs() {
    log_info "Aggiornamento degli ambienti virtuali Python..."
    for venv in $(find ~/ -type d -name "env*" -or -name "venv*"); do
        if [ -d "$venv/bin" ]; then
            log_info "Attivazione e aggiornamento dell'ambiente virtuale: $venv..."
            source "$venv/bin/activate"
            pip install --upgrade pip
            pip list --outdated | awk '{print $1}' | tail -n +3 | xargs -n 1 pip install --upgrade
            deactivate
        fi
    done
}

# Funzione per aggiornare Conda
update_conda() {
    log_info "Aggiornamento di Conda..."
    if command -v conda &> /dev/null; then
        conda update --all -y
    else
        log_info "Conda non è installato."
    fi
}

# Funzione per pulire i pacchetti obsoleti di Conda
clean_conda_packages() {
    log_info "Pulizia dei pacchetti obsoleti di Conda..."
    if command -v conda &> /dev/null; then
        conda clean --all -y
    else
        log_info "Conda non è installato."
    fi
}

# Inizio dello script
log_info "Inizio aggiornamenti..."

# Ricaricamento dei demoni di sistema
log_info "Ricaricamento dei demoni di sistema..."
sudo systemctl daemon-reload

# Configurazione dei pacchetti APT
log_info "Configurazione dei pacchetti APT..."
while sudo fuser /var/lib/dpkg/lock-frontend >/dev/null 2>&1; do
    log_info "In attesa del rilascio del blocco dpkg..."
    sleep 5
done
sudo dpkg --configure -a

# Aggiornamento APT
update_apt_packages

# Aggiornamento Conda
update_conda

# Pulizia pacchetti obsoleti Conda
clean_conda_packages

# Aggiornamento Node.js
update_node

# Aggiornamento ambienti virtuali Python
update_python_envs

# Avanzamento di versione del sistema
log_info "Avanzamento di versione del sistema..."
sudo do-release-upgrade -f DistUpgradeViewNonInteractive

# Sblocco della sospensione energetica del Wi-Fi
log_info "Sblocco della sospensione energetica del Wi-Fi..."
sudo rfkill unblock wifi
sudo rfkill unblock all

# Pulizia pacchetti APT
clean_apt_packages

# Aggiornamento dei pacchetti con aptitude (se disponibile)
log_info "Aggiornamento dei pacchetti con aptitude..."
if command -v aptitude &> /dev/null; then
    sudo aptitude upgrade -y
else
    log_info "Aptitude non è installato. Salto questo passaggio."
fi

# Installazione e aggiornamento Snap
log_info "Installazione di snapd se non presente..."
if ! dpkg -l | grep -q snapd; then
    sudo apt-get install snapd -y
else
    log_info "Snapd è già installato."
fi

log_info "Aggiornamento dei pacchetti Snap..."
sudo snap refresh

# Abilitazione del firewall
log_info "Abilitazione del firewall..."
if ! sudo ufw status | grep -q 'Status: active'; then
    sudo ufw enable
fi

# Riavvio di Ubuntu Software
log_info "Riavvio di Ubuntu Software..."
sudo systemctl restart snapd

log_info "Aggiornamenti completati!"
