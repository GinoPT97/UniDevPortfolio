#!/bin/bash

# Ottieni i permessi
sudo journalctl --vacuum-files

# Pulisci i file temporanei
sudo rm -rf /tmp/*

# Svuota la cache DNS
sudo systemd-resolve --flush-caches

# Pulisci la cache dei font
sudo fc-cache -f -v

# Pulisci i file di registro di sistema compressi
sudo journalctl --vacuum-time=7d

# Pulisci la cache del browser
google-chrome --clear-cache

# Pulisci i file di registro di Apache
sudo rm /var/log/apache2/*.log

# Compatti e deframmenta i file di sistema
sudo e4defrag /

# Riparazione dei file danneggiati
sudo fsck -y /

# Esegui fstrim sulla partizione radice
sudo fstrim -av

echo "Ottimizzazioni completate!"
