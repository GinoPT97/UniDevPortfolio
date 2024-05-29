#!/bin/bash

# Riparazione dei pacchetti APT
sudo dpkg --configure -a

# Aggiornamento dei pacchetti APT
sudo apt update && sudo apt upgrade -y

# Aggiornamento dei pacchetti e delle dipendenze
sudo apt-get dist-upgrade -y

# Per avanzamenti di sistema
sudo do-release-upgrade 

# Blocca la sospensione energetica del wifi
sudo rfkill unblock wifi
sudo rfkill unblock all

# Pulizia dei pacchetti inutili e orfani APT
sudo apt autoremove --purge -y

# Pulizia della cache dei pacchetti APT
sudo apt clean

# Aggiornamento dei pacchetti Snap
sudo snap refresh

# Aggiunta del firewall
sudo ufw enable

# Ricarica il demone di systemd
sudo systemctl daemon-reload

echo "Aggiornamenti completati e Ubuntu Software riavviato!"
