#!/bin/bash

# Funzione per loggare informazioni
log_info() {
    echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# Funzione per loggare gli errori
log_error() {
    echo "[ERROR] $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# Funzione per aggiornare pacchetti APT
update_apt_packages() {
    log_info "Aggiornamento dell'elenco dei pacchetti..."
    sudo apt-get update -y || { log_error "Errore nell'aggiornamento dei pacchetti APT"; exit 1; }

    log_info "Aggiornamento dei pacchetti APT..."
    sudo apt-get upgrade -y || { log_error "Errore nell'aggiornamento dei pacchetti APT"; exit 1; }

    log_info "Aggiornamento dei pacchetti e delle dipendenze..."
    sudo apt-get dist-upgrade -y || { log_error "Errore nell'aggiornamento dei pacchetti e delle dipendenze"; exit 1; }
}

# Funzione per pulire i pacchetti APT
clean_apt_packages() {
    log_info "Pulizia dei pacchetti inutili e orfani..."
    sudo apt-get autoremove --purge -y || { log_error "Errore nella pulizia dei pacchetti orfani"; exit 1; }

    log_info "Pulizia della cache dei pacchetti APT..."
    sudo apt-get clean -y || { log_error "Errore nella pulizia della cache dei pacchetti"; exit 1; }
}

# Funzione per aggiornare Node.js e gestire i pacchetti npm
update_node() {
    log_info "Aggiornamento di Node.js..."
    if command -v node &> /dev/null; then
        sudo npm install -g n || { log_error "Errore nell'installazione di 'n'"; exit 1; }
        sudo n stable || { log_error "Errore nell'aggiornamento di Node.js"; exit 1; }

        log_info "Aggiornamento dei pacchetti npm globali..."
        sudo npm update -g || { log_error "Errore nell'aggiornamento dei pacchetti npm globali"; exit 1; }
        sudo npm audit fix || { log_error "Errore nell'esecuzione di npm audit fix"; exit 1; }

        log_info "Controllo pacchetti npm obsoleti..."
        npm outdated -g || { log_error "Errore nel controllo dei pacchetti obsoleti npm"; exit 1; }

        log_info "Rimozione pacchetti npm obsoleti..."
        npm uninstall -g $(npm outdated -g --parseable --depth=0 | cut -d: -f2) || { log_error "Errore nella rimozione dei pacchetti obsoleti npm"; exit 1; }
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
            pip install --upgrade pip || { log_error "Errore nell'aggiornamento di pip per $venv"; exit 1; }
            pip list --outdated | awk '{print $1}' | tail -n +3 | xargs -n 1 pip install --upgrade || { log_error "Errore nell'aggiornamento dei pacchetti Python per $venv"; exit 1; }
            deactivate
        fi
    done
}

# Funzione per aggiornare Conda
update_conda() {
    log_info "Aggiornamento di Conda..."
    if command -v conda &> /dev/null; then
        conda update --all -y || { log_error "Errore nell'aggiornamento di Conda"; exit 1; }
    else
        log_info "Conda non è installato."
    fi
}

# Funzione per pulire i pacchetti obsoleti di Conda
clean_conda_packages() {
    log_info "Pulizia dei pacchetti obsoleti di Conda..."
    if command -v conda &> /dev/null; then
        conda clean --all -y || { log_error "Errore nella pulizia dei pacchetti Conda"; exit 1; }
    else
        log_info "Conda non è installato."
    fi
}

# Funzione per ricaricare i demoni di sistema e gestire il blocco dpkg
reload_systemd_and_dpkg() {
    log_info "Ricaricamento dei demoni di sistema..."
    sudo systemctl daemon-reload

    log_info "Ricaricamento dei pacchetti APT..."
    while sudo fuser /var/lib/dpkg/lock-frontend >/dev/null 2>&1; do
        log_info "In attesa del rilascio del blocco dpkg..."
        sleep 5
    done
    sudo dpkg --configure -a || { log_error "Errore nel configurare dpkg"; exit 1; }
}

# Funzione per installare Snap
install_snapd() {
    log_info "Verifica e installazione di Snapd..."
    if ! dpkg -l | grep -q snapd; then
        sudo apt-get install snapd -y || { log_error "Errore nell'installazione di snapd"; exit 1; }
    else
        log_info "Snapd è già installato."
    fi

    log_info "Aggiornamento dei pacchetti Snap..."
    sudo snap refresh || { log_error "Errore nell'aggiornamento dei pacchetti Snap"; exit 1; }
}

# Inizio dello script
log_info "Inizio aggiornamenti..."

# Ricarica i demoni di sistema e gestisce il blocco dpkg
reload_systemd_and_dpkg

# Aggiornamento APT
update_apt_packages

# Aggiornamento Conda
update_conda

# Pulizia pacchetti obsoleti Conda
clean_conda_packages

# Aggiornamento Node.js e npm
update_node

# Aggiornamento ambienti virtuali Python
update_python_envs

# Avanzamento di versione del sistema
log_info "Avanzamento di versione del sistema..."
sudo do-release-upgrade -f DistUpgradeViewNonInteractive || { log_error "Errore nell'avanzamento di versione del sistema"; exit 1; }

# Sblocco della sospensione energetica del Wi-Fi
log_info "Sblocco della sospensione energetica del Wi-Fi..."
sudo rfkill unblock all

# Pulizia pacchetti APT
clean_apt_packages

# Aggiornamento dei pacchetti con aptitude (se disponibile)
log_info "Aggiornamento dei pacchetti con aptitude..."
if command -v aptitude &> /dev/null; then
    sudo aptitude upgrade -y || { log_error "Errore nell'aggiornamento con aptitude"; exit 1; }
else
    log_info "Aptitude non è installato. Salto questo passaggio."
fi

# Installazione e aggiornamento Snap
install_snapd

# Abilitazione del firewall
log_info "Abilitazione del firewall..."
if ! sudo ufw status | grep -q 'Status: active'; then
    sudo ufw enable || { log_error "Errore nell'abilitazione del firewall"; exit 1; }
fi

# Riavvio di Ubuntu Software
log_info "Riavvio di Ubuntu Software..."
sudo systemctl restart snapd || { log_error "Errore nel riavvio di Ubuntu Software"; exit 1; }

# Pulizia della cache di Jest
log_info "Pulizia della cache di Jest..."
npx jest --clearCache || { log_error "Errore nella pulizia della cache di Jest"; exit 1; }

log_info "Aggiornamenti completati!"
