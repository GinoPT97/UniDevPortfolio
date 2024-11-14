#!/bin/bash

# Richiede permessi di superutente
sudo -v

# Pulizia dei log di sistema e temporanei
echo "[INFO] Pulizia dei log di sistema e temporanei..."
sudo journalctl --vacuum-files=1
if [[ $? -ne 0 ]]; then echo "[WARNING] Pulizia dei log di sistema fallita"; fi
sudo rm -rf /tmp/* /var/tmp/*
if [[ $? -ne 0 ]]; then echo "[WARNING] Pulizia dei file temporanei fallita"; fi

# Pulizia della cache di APT, dei pacchetti obsoleti e dei font
echo "[INFO] Pulizia della cache di APT, pacchetti obsoleti e dei font..."
sudo apt-get clean
if [[ $? -ne 0 ]]; then echo "[WARNING] Pulizia della cache di APT fallita"; fi
sudo apt-get autoremove --purge -y
if [[ $? -ne 0 ]]; then echo "[WARNING] Rimozione pacchetti obsoleti fallita"; fi
sudo fc-cache -f -v
if [[ $? -ne 0 ]]; then echo "[WARNING] Pulizia dei font fallita"; fi

# Pulizia dei log di sistema più vecchi di 7 giorni
echo "[INFO] Pulizia dei log di sistema più vecchi di 7 giorni..."
sudo journalctl --vacuum-time=7d
if [[ $? -ne 0 ]]; then echo "[WARNING] Pulizia dei log di sistema più vecchi di 7 giorni fallita"; fi

# Pulizia della cache del browser (Google Chrome e Firefox)
echo "[INFO] Pulizia della cache dei browser..."
google-chrome --clear-cache
if [[ $? -ne 0 ]]; then echo "[WARNING] Pulizia cache Google Chrome fallita"; fi
rm -rf ~/.cache/mozilla/firefox/*
if [[ $? -ne 0 ]]; then echo "[WARNING] Pulizia cache Firefox fallita"; fi

# Pulizia dei log di Apache, se esistono
echo "[INFO] Pulizia dei log di Apache..."
sudo rm -f /var/log/apache2/*.log
if [[ $? -ne 0 ]]; then echo "[WARNING] Pulizia log Apache fallita"; fi

# Compattazione e deframmentazione dei file di sistema
echo "[INFO] Compattazione e deframmentazione dei file di sistema..."
sudo e4defrag /
if [[ $? -ne 0 ]]; then echo "[WARNING] Deframmentazione fallita"; fi

# Controllo e riparazione della partizione radice, se non è montata
echo "[INFO] Controllo della partizione radice..."
mounted=$(mount | grep " on / " | awk '{print $1}')
if [ "$mounted" != "/dev/nvme0n1p4" ]; then
    echo "[INFO] La partizione radice non è montata. Avvio fsck..."
    sudo fsck -y /
    if [[ $? -ne 0 ]]; then echo "[WARNING] fsck fallito"; fi
else
    echo "[INFO] La partizione radice è già montata."
fi

# Ottimizzazione dello spazio libero e pulizia della cache di systemd
echo "[INFO] Ottimizzazione dello spazio libero e pulizia della cache di systemd..."
sudo fstrim -av
if [[ $? -ne 0 ]]; then echo "[WARNING] fstrim fallito"; fi
sudo systemctl reset-failed
if [[ $? -ne 0 ]]; then echo "[WARNING] Reset systemd fallito"; fi

# Pulizia dei log di systemd
echo "[INFO] Pulizia dei log di systemd..."
sudo journalctl --vacuum-size=50M
if [[ $? -ne 0 ]]; then echo "[WARNING] Pulizia log systemd fallita"; fi

# Controllo e pulizia del sistema con BleachBit (se installato)
if command -v bleachbit &>/dev/null; then
    echo "[INFO] Pulizia con BleachBit..."
    sudo bleachbit --cleanup --quiet
    if [[ $? -ne 0 ]]; then echo "[WARNING] Pulizia con BleachBit fallita"; fi
else
    echo "[INFO] BleachBit non trovato, pulizia manuale..."
fi

# Rimozione dei pacchetti orfani e aggiornamenti di sicurezza
echo "[INFO] Rimozione dei pacchetti orfani e aggiornamenti di sicurezza..."
sudo apt-get update && sudo apt-get upgrade -y
if [[ $? -ne 0 ]]; then echo "[WARNING] Aggiornamenti falliti"; fi

# Controllo dell'utilizzo del disco
echo "[INFO] Controllo dell'utilizzo del disco..."
df -h
if [[ $? -ne 0 ]]; then echo "[WARNING] Controllo dello spazio disco fallito"; fi

# Controllo della memoria
echo "[INFO] Controllo dell'utilizzo della memoria..."
free -h
if [[ $? -ne 0 ]]; then echo "[WARNING] Controllo memoria fallito"; fi

# Ottimizzazione della memoria swap
echo "[INFO] Ottimizzazione della memoria swap..."
sudo swapoff -a
if [[ $? -ne 0 ]]; then echo "[WARNING] Disabilitazione swap fallita"; fi
sudo swapon -a
if [[ $? -ne 0 ]]; then echo "[WARNING] Riabilitazione swap fallita"; fi

echo "[INFO] Ottimizzazioni completate!"

