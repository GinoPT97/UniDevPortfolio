#!/bin/bash

set -e

# Verifica connessione di rete
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Verificando connessione di rete..."
ping -c 1 google.com || { echo "[ERRORE] $(date '+%Y-%m-%d %H:%M:%S') - Nessuna connessione di rete. Script interrotto."; exit 1; }

# Disinstallazione dei pacchetti Flatpak non utilizzati
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Disinstallazione dei pacchetti Flatpak non utilizzati..."
flatpak uninstall --unused -y || echo "[ERRORE] $(date '+%Y-%m-%d %H:%M:%S') - Errore nella disinstallazione dei pacchetti Flatpak."

# Aggiornamento dei pacchetti APT
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Configurando e aggiornando pacchetti APT..."
sudo dpkg --configure -a
sudo apt-get update -y
sudo apt-get full-upgrade -y
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento dei pacchetti APT completato."

# Aggiornamento dei pacchetti Snap
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornando pacchetti Snap..."
sudo snap refresh
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Aggiornamento dei pacchetti Snap completato."

# Sblocco del wifi e delle interfacce di rete
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - Sbloccando il wifi e tutte le interfacce..."
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
