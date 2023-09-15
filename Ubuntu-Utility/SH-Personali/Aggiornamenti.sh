#!/bin/bash

# Assicurati di avere tutti i permessi necessari
sudo true

# Aggiornamento dei pacchetti APT
sudo apt update
sudo apt upgrade -y

# Pulizia dei pacchetti inutili e orfani APT
sudo apt autoremove -y
sudo apt autoremove --purge -y
sudo apt-get autoremove --purge -y $(deborphan)

# Pulizia della cache dei pacchetti APT
sudo apt clean -y

# Aggiornamento dei pacchetti Snap
sudo snap refresh

#aggiunta del firewall
sudo ufw enable

# Riavvio di Ubuntu Software
sudo systemctl restart snapd

echo "Aggiornamenti completati e Ubuntu Software riavviato!"
