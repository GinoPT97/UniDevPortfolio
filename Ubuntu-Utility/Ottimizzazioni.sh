#!/bin/bash

# Richiede permessi di superutente
sudo -v

# Funzione per eseguire un comando e gestire gli errori
execute_command() {
    $1
    if [[ $? -ne 0 ]]; then
        echo "[WARNING] $2 fallita"
    fi
}

# Pulizia dei log di sistema e temporanei
echo "[INFO] Pulizia dei log di sistema e temporanei..."
execute_command "sudo journalctl --vacuum-files=1" "Pulizia dei log di sistema"
execute_command "sudo rm -rf /tmp/* /var/tmp/*" "Pulizia dei file temporanei"

# Pulizia della cache di APT, dei pacchetti obsoleti e dei font
echo "[INFO] Pulizia della cache di APT, pacchetti obsoleti e dei font..."
execute_command "sudo apt-get clean" "Pulizia della cache di APT"
execute_command "sudo apt-get autoremove --purge -y" "Rimozione pacchetti obsoleti"
execute_command "sudo fc-cache -f -v" "Pulizia dei font"

# Pulizia dei log di sistema più vecchi di 7 giorni
echo "[INFO] Pulizia dei log di sistema più vecchi di 7 giorni..."
execute_command "sudo journalctl --vacuum-time=7d" "Pulizia dei log di sistema più vecchi di 7 giorni"

# Pulizia della cache del browser (Google Chrome e Firefox)
echo "[INFO] Pulizia della cache dei browser..."
execute_command "google-chrome --clear-cache" "Pulizia cache Google Chrome"
execute_command "rm -rf ~/.cache/mozilla/firefox/*" "Pulizia cache Firefox"

# Pulizia dei log di Apache, se esistono
echo "[INFO] Pulizia dei log di Apache..."
execute_command "sudo rm -f /var/log/apache2/*.log" "Pulizia log Apache"

# Compattazione e deframmentazione dei file di sistema
echo "[INFO] Compattazione e deframmentazione dei file di sistema..."
execute_command "sudo e4defrag /" "Deframmentazione"

# Controllo e riparazione della partizione radice, se non è montata
echo "[INFO] Controllo della partizione radice..."
mounted=$(mount | grep " on / " | awk '{print $1}')
if [ "$mounted" != "/dev/nvme0n1p4" ]; then
    echo "[INFO] La partizione radice non è montata. Avvio fsck..."
    execute_command "sudo fsck -y /" "fsck"
else
    echo "[INFO] La partizione radice è già montata."
fi

# Ottimizzazione dello spazio libero e pulizia della cache di systemd
echo "[INFO] Ottimizzazione dello spazio libero e pulizia della cache di systemd..."
execute_command "sudo fstrim -av" "fstrim"
execute_command "sudo systemctl reset-failed" "Reset systemd"

# Pulizia dei log di systemd
echo "[INFO] Pulizia dei log di systemd..."
execute_command "sudo journalctl --vacuum-size=50M" "Pulizia log systemd"

# Controllo e pulizia del sistema con BleachBit (se installato)
if command -v bleachbit &>/dev/null; then
    echo "[INFO] Pulizia con BleachBit..."
    execute_command "sudo bleachbit --cleanup --quiet" "Pulizia con BleachBit"
else
    echo "[INFO] BleachBit non trovato, pulizia manuale..."
fi

# Rimozione dei pacchetti orfani e aggiornamenti di sicurezza
echo "[INFO] Rimozione dei pacchetti orfani e aggiornamenti di sicurezza..."
execute_command "sudo apt-get update && sudo apt-get upgrade -y" "Aggiornamenti"

# Controllo dell'utilizzo del disco
echo "[INFO] Controllo dell'utilizzo del disco..."
execute_command "df -h" "Controllo dello spazio disco"

# Controllo della memoria
echo "[INFO] Controllo dell'utilizzo della memoria..."
execute_command "free -h" "Controllo memoria"

# Ottimizzazione della memoria swap
echo "[INFO] Ottimizzazione della memoria swap..."
execute_command "sudo swapoff -a" "Disabilitazione swap"
execute_command "sudo swapon -a" "Riabilitazione swap"

echo "[INFO] Ottimizzazioni completate!"
