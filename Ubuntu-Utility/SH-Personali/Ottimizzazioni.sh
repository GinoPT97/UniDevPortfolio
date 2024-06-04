#!/bin/bash


# Ottieni i permessi
sudo -v

# Pulisci i file temporanei
echo "Pulizia dei file temporanei..."
sudo rm -rf /tmp/*

# Svuota la cache dei font
echo "Svuotamento della cache dei font..."
sudo fc-cache -f -v

# Pulisci i file di registro di sistema compressi più vecchi di 7 giorni
echo "Pulizia dei file di registro di sistema compressi..."
sudo journalctl --vacuum-time=7d

# Limita le dimensioni dei file di registro di sistema a 1G
echo "Limitazione delle dimensioni dei file di registro di sistema..."
sudo journalctl --vacuum-size=1G

# Pulisci la cache del browser (nota: comando specifico per Google Chrome)
# Assicurati di sostituire con il comando corretto per il tuo browser, se necessario
echo "Pulizia della cache del browser..."
if command -v google-chrome >/dev/null 2>&1; then
    google-chrome --clear-cache
else
    echo "Google Chrome non è installato. Salto la pulizia della cache del browser."
fi

# Pulisci i file di registro di Apache solo se esistono
echo "Pulizia dei file di registro di Apache..."
if [ -d /var/log/apache2 ]; then
    sudo rm -f /var/log/apache2/*.log
else
    echo "La directory /var/log/apache2 non esiste. Salto la pulizia dei file di registro di Apache."
fi

# Compatta e deframmenta i file di sistema
echo "Compattazione e deframmentazione dei file di sistema..."
sudo e4defrag /

# Riparazione dei file danneggiati
echo "Riparazione dei file danneggiati..."
sudo fsck -y /

# Esegui fstrim sulla partizione radice
echo "Esecuzione di fstrim sulla partizione radice..."
sudo fstrim -av

echo "Ottimizzazioni completate!"
