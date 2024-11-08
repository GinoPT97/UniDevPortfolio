#!/bin/bash

# Richiede permessi di superutente
sudo -v

# Pulizia dei log di sistema e temporanei
echo "[INFO] Pulizia dei log di sistema e temporanei..."
sudo journalctl --vacuum-files=1
sudo rm -rf /tmp/* /var/tmp/*

# Pulizia della cache di APT e dei font
echo "[INFO] Pulizia della cache di APT e dei font..."
sudo apt-get clean
sudo fc-cache -f -v

# Pulizia dei log di sistema più vecchi di 7 giorni
echo "[INFO] Pulizia dei log di sistema più vecchi di 7 giorni..."
sudo journalctl --vacuum-time=7d

# Pulizia della cache del browser (Google Chrome)
echo "[INFO] Pulizia della cache del browser Google Chrome..."
google-chrome --clear-cache

# Pulizia dei log di Apache, se esistono
echo "[INFO] Pulizia dei log di Apache..."
sudo rm -f /var/log/apache2/*.log

# Compattazione e deframmentazione dei file di sistema
echo "[INFO] Compattazione e deframmentazione dei file di sistema..."
sudo e4defrag /

# Controllo e riparazione della partizione radice, se non è montata
echo "[INFO] Controllo della partizione radice..."
mounted=$(mount | grep " on / " | awk '{print $1}')
[ "$mounted" != "/dev/nvme0n1p4" ] && sudo fsck -y /

# Ottimizzazione dello spazio libero e pulizia della cache di systemd
echo "[INFO] Ottimizzazione dello spazio libero e pulizia della cache di systemd..."
sudo fstrim -av
sudo systemctl reset-failed

# Rimozione dei pacchetti orfani e aggiornamenti di sicurezza
echo "[INFO] Rimozione dei pacchetti orfani e aggiornamenti di sicurezza..."
sudo apt-get autoremove --purge -y
sudo apt-get update && sudo apt-get upgrade -y

# Controllo dell'utilizzo del disco
echo "[INFO] Controllo dell'utilizzo del disco..."
df -h

echo "[INFO] Ottimizzazioni completate!"
