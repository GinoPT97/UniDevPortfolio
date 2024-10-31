#!/bin/bash

# Richiede permessi di superutente
sudo -v

# Pulizia dei file di registro di sistema, mantenendo solo 1 file
echo "Pulizia dei file di registro di sistema..."
sudo journalctl --vacuum-files=1

# Pulizia dei file temporanei
echo "Pulizia dei file temporanei..."
sudo rm -rf /tmp/* /var/tmp/*

# Pulizia della cache dei pacchetti APT
echo "Pulizia della cache dei pacchetti APT..."
sudo apt-get clean

# Svuota la cache dei font
echo "Svuotamento della cache dei font..."
sudo fc-cache -f -v

# Pulizia dei file di registro di sistema più vecchi di 7 giorni
echo "Pulizia dei file di registro di sistema più vecchi di 7 giorni..."
sudo journalctl --vacuum-time=7d

# Pulizia della cache del browser (Google Chrome)
echo "Pulizia della cache del browser Google Chrome..."
google-chrome --clear-cache

# Pulizia dei file di log di Apache solo se esistono
echo "Pulizia dei file di log di Apache..."
if [ -d /var/log/apache2 ]; then
    sudo rm -f /var/log/apache2/*.log
fi

# Compatta e deframmenta i file di sistema
echo "Compattazione e deframmentazione dei file di sistema..."
sudo e4defrag /

# Controllo e riparazione della partizione radice
echo "Controllo della partizione radice..."
mounted=$(mount | grep " on / " | awk '{print $1}')
if [[ $mounted == "/dev/nvme0n1p4" ]]; then
    echo "La partizione radice è montata, fsck non può essere eseguito. Riavviare in modalità di ripristino per eseguire fsck."
else
    # Riparazione dei file danneggiati solo se non è montata
    echo "Riparazione dei file danneggiati..."
    sudo fsck -y /
fi

# Esegui fstrim per ottimizzare lo spazio libero
echo "Ottimizzazione dello spazio libero con fstrim..."
sudo fstrim -av

# Pulizia della cache di systemd
echo "Pulizia della cache di systemd..."
sudo systemctl reset-failed

# Rimozione dei pacchetti orfani
echo "Rimozione dei pacchetti orfani..."
sudo apt-get autoremove --purge -y

# Controllo degli aggiornamenti di sicurezza
echo "Controllo degli aggiornamenti di sicurezza..."
sudo apt-get update && sudo apt-get upgrade -y

# Controllo dell'utilizzo del disco
echo "Controllo dell'utilizzo del disco..."
df -h

echo "Ottimizzazioni completate!"
