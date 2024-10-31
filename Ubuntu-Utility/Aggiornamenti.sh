#!/bin/bash

set -e

# Function to check and install snapd if not installed
install_snapd() {
    if ! command -v snapd &> /dev/null; then
        echo "Installazione di snapd..."
        sudo apt install -y snapd
    else
        echo "snapd è già installato."
    fi
}

# Uninstall unused Flatpak packages
echo "Disinstallazione dei pacchetti Flatpak inutilizzati..."
flatpak uninstall --unused -y

echo "Ricaricamento dei demoni di sistema..."
sudo systemctl daemon-reload

echo "Installazione dei driver proprietari..."
sudo ubuntu-drivers autoinstall

echo "Configurazione dei pacchetti APT..."
sudo dpkg --configure -a

echo "Aggiornamento dell'elenco e dei pacchetti APT..."
sudo apt update && sudo apt upgrade -y && sudo apt dist-upgrade -y

echo "Sblocco della sospensione energetica del wifi..."
sudo rfkill unblock wifi
sudo rfkill unblock all

echo "Pulizia dei pacchetti inutili e orfani..."
sudo apt autoremove --purge -y

echo "Pulizia della cache dei pacchetti APT..."
sudo apt clean

install_snapd

echo "Aggiornamento dei pacchetti Snap..."
sudo snap refresh

echo "Abilitazione del firewall..."
sudo ufw enable

echo "Riavvio di Ubuntu Software..."
sudo systemctl restart snapd

echo "Aggiornamenti completati e Ubuntu Software riavviato!"

