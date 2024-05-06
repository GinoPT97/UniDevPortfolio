#!/bin/bash

# Ottieni i permessi
sudo journalctl --vacuum-files=1G

# Pulisci i file temporanei
sudo rm -rf /tmp/*

# Svuota la cache dei font
sudo fc-cache -f -v

# Pulisci i file di registro di sistema compressi
sudo journalctl --vacuum-time=7d

# Pulisci la cache del browser (nota: comando specifico per Google Chrome)
# Assicurati di sostituire con il comando corretto per il tuo browser, se necessario
google-chrome --clear-cache

# Pulisci i file di registro di Apache solo se esistono
sudo rm -f /var/log/apache2/*.log

# Compatta e deframmenta i file di sistema
sudo e4defrag /

# Riparazione dei file danneggiati
sudo fsck -y /

# Esegui fstrim sulla partizione radice
sudo fstrim -av

echo "Ottimizzazioni completate!"
