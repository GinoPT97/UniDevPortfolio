#!/bin/bash

log_info() {
    echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_info "Inizio aggiornamenti..."

log_info "Ricaricamento dei demoni di sistema..."
sudo systemctl daemon-reload

log_info "Configurazione dei pacchetti APT..."
while sudo fuser /var/lib/dpkg/lock-frontend >/dev/null 2>&1; do
    log_info "In attesa del rilascio del blocco dpkg..."
    sleep 5
done
sudo dpkg --configure -a

log_info "Aggiornamento dell'elenco dei pacchetti..."
sudo apt-get update

log_info "Aggiornamento dei pacchetti APT..."
sudo apt-get upgrade -y

log_info "Aggiornamento dei pacchetti e delle dipendenze..."
sudo apt-get dist-upgrade -y

log_info "Aggiornamento di Conda..."
if command -v conda &> /dev/null; then
    conda update --all -y
else
    log_info "Conda non è installato."
fi

log_info "Aggiornamento di Node.js..."
if command -v node &> /dev/null; then
    sudo npm install -g n
    sudo n stable
else
    log_info "Node.js non è installato."
fi

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

log_info "Avanzamento di versione del sistema..."
sudo do-release-upgrade -f DistUpgradeViewNonInteractive

log_info "Sblocco della sospensione energetica del wifi..."
sudo rfkill unblock wifi
sudo rfkill unblock all

log_info "Pulizia dei pacchetti inutili e orfani..."
sudo apt-get autoremove --purge -y

log_info "Pulizia della cache dei pacchetti APT..."
sudo apt-get clean -y

sudo apt autoremove --purge


log_info "Aggiornamento dei pacchetti con aptitude..."
if command -v aptitude &> /dev/null; then
    sudo aptitude upgrade -y
else
    log_info "Aptitude non è installato. Salto questo passaggio."
fi

log_info "Installazione di snapd se non presente..."
if ! dpkg -l | grep -q snapd; then
    sudo apt-get install snapd -y
else
    log_info "Snapd è già installato."
fi

log_info "Aggiornamento dei pacchetti Snap..."
sudo snap refresh

log_info "Abilitazione del firewall..."
sudo ufw status | grep -q 'Status: active' || sudo ufw enable

log_info "Riavvio di Ubuntu Software..."
sudo systemctl restart snapd

log_info "Aggiornamenti completati e Ubuntu Software riavviato!"
