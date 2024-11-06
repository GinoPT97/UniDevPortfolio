#!/bin/bash

# Richiede permessi di superutente
sudo -v

# Pulizia dei log di sistema e temporanei
sudo journalctl --vacuum-files=1
sudo rm -rf /tmp/* /var/tmp/*

# Pulizia della cache di APT e dei font
sudo apt-get clean
sudo fc-cache -f -v

# Pulizia dei log di sistema più vecchi di 7 giorni
sudo journalctl --vacuum-time=7d

# Pulizia della cache del browser (Google Chrome)
google-chrome --clear-cache

# Pulizia dei log di Apache, se esistono
[ -d /var/log/apache2 ] && sudo rm -f /var/log/apache2/*.log

# Compattazione e deframmentazione dei file di sistema
sudo e4defrag /

# Controllo e riparazione della partizione radice, se non è montata
mounted=$(mount | grep " on / " | awk '{print $1}')
if [[ $mounted != "/dev/nvme0n1p4" ]]; then
    sudo fsck -y /
else
    echo "La partizione radice è montata, fsck non eseguibile. Riavviare in modalità di ripristino."
fi

# Ottimizzazione dello spazio libero e pulizia della cache di systemd
sudo fstrim -av
sudo systemctl reset-failed

# Rimozione dei pacchetti orfani e aggiornamenti di sicurezza
sudo apt-get autoremove --purge -y
sudo apt-get update && sudo apt-get upgrade -y

# Controllo dell'utilizzo del disco
df -h

echo "Ottimizzazioni completate!"
