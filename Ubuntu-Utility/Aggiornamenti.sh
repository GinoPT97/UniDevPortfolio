#!/bin/bash

set -e

# Verifica connessione di rete
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Verificando connessione di rete..."
ping -c 1 google.com &> /dev/null || { echo "[ERRORE] $(date '+%Y-%m-%d %H:%M:%S') - Nessuna connessione di rete. Script interrotto."; exit 1; }

# Installazione di snapd se non è già installato
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Verificando se snapd è installato..."
command -v snapd &> /dev/null || { echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Snapd non trovato. Installazione in corso..."; sudo apt install -y snapd; }

# Disinstallazione dei pacchetti Flatpak non utilizzati
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Disinstallazione dei pacchetti Flatpak non utilizzati..."
flatpak uninstall --unused -y

# Aggiornamento dei pacchetti APT
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Configurando pacchetti APT..."
sudo dpkg --configure -a
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornando pacchetti APT..."
sudo apt-get update -y
sudo apt-get upgrade -y
sudo apt-get dist-upgrade -y
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento dei pacchetti APT completato."

# Aggiornamento dei pacchetti Snap
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornando pacchetti Snap..."
sudo snap refresh
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento dei pacchetti Snap completato."

# Abilitazione della rete wifi e sblocco
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Sbloccando il wifi..."
sudo rfkill unblock wifi || echo "[ERRORE] $(date '+%Y-%m-%d %H:%M:%S') - Impossibile sbloccare il wifi."
sudo rfkill unblock all || echo "[ERRORE] $(date '+%Y-%m-%d %H:%M:%S') - Impossibile sbloccare tutte le interfacce."

# Pulizia dei pacchetti inutilizzati e cache APT
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Rimuovendo pacchetti inutilizzati..."
sudo apt-get autoremove --purge -y
sudo apt-get clean

# Abilitazione del firewall
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Abilitando il firewall..."
sudo ufw status | grep -q "active" || sudo ufw enable

# Riavvio di snapd
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Riavviando snapd..."
sudo systemctl restart snapd || echo "[ERRORE] $(date '+%Y-%m-%d %H:%M:%S') - Impossibile riavviare snapd."

# Aggiornamento dei driver hardware
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento dei driver hardware..."
sudo ubuntu-drivers autoinstall || echo "[ERRORE] $(date '+%Y-%m-%d %H:%M:%S') - Errore nell'aggiornamento dei driver."

echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamenti completati e Ubuntu Software riavviato!"

