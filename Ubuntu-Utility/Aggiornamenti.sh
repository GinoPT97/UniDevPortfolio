#!/bin/bash

# Inizio aggiornamenti
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Inizio aggiornamenti..."

# Ricaricamento dei demoni di sistema
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Ricaricamento dei demoni di sistema..."
sudo systemctl daemon-reload

# Configurazione dei pacchetti APT
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Configurazione dei pacchetti APT..."
sudo dpkg --configure -a

# Aggiornamento dell'elenco dei pacchetti
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento dell'elenco dei pacchetti..."
sudo apt-get update

# Aggiornamento dei pacchetti APT
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento dei pacchetti APT..."
sudo apt-get upgrade -y

# Aggiornamento delle dipendenze APT
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento dei pacchetti e delle dipendenze..."
sudo apt-get dist-upgrade -y

# Aggiornamento Conda
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento di Conda..."
if command -v conda &> /dev/null; then
    conda update --all -y
else
    echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Conda non è installato."
fi

# Aggiornamento di Node.js
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento di Node.js..."
if command -v node &> /dev/null; then
    sudo npm install -g n
    sudo n stable
else
    echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Node.js non è installato."
fi

# Aggiornamento degli ambienti virtuali Python
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento degli ambienti virtuali Python..."
for venv in $(find ~/ -type d -name "env*" -or -name "venv*"); do
    if [ -d "$venv/bin" ]; then
        echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Attivazione e aggiornamento dell'ambiente virtuale: $venv..."
        source "$venv/bin/activate"
        pip install --upgrade pip
        pip list --outdated | awk '{print $1}' | tail -n +3 | xargs -n 1 pip install --upgrade
        deactivate
    fi
done

# Avanzamento di versione del sistema
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Avanzamento di versione del sistema..."
sudo do-release-upgrade -f DistUpgradeViewNonInteractive

# Sblocco della sospensione energetica del wifi
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Sblocco della sospensione energetica del wifi..."
sudo rfkill unblock wifi
sudo rfkill unblock all

# Pulizia dei pacchetti inutili e orfani
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Pulizia dei pacchetti inutili e orfani..."
sudo apt-get autoremove --purge -y

# Pulizia della cache dei pacchetti APT
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Pulizia della cache dei pacchetti APT..."
sudo apt-get clean -y

# Aggiornamento dei pacchetti con aptitude (rimosso -q e migliorata la gestione)
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento dei pacchetti con aptitude..."
if command -v aptitude &> /dev/null; then
    sudo aptitude upgrade -y
else
    echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aptitude non è installato. Salto questo passaggio."
fi

# Installazione di snapd se non presente
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Installazione di snapd se non presente..."
if ! dpkg -l | grep -q snapd; then
    sudo apt-get install snapd -y
else
    echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Snapd è già installato."
fi

# Aggiornamento dei pacchetti Snap
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento dei pacchetti Snap..."
sudo snap refresh

# Abilitazione del firewall
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Abilitazione del firewall..."
sudo ufw status | grep -q 'Status: active' || sudo ufw enable

# Riavvio di Ubuntu Software
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Riavvio di Ubuntu Software..."
sudo systemctl restart snapd

echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamenti completati e Ubuntu Software riavviato!"