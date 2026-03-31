#!/bin/bash

set -e  # Ferma lo script in caso di errore

# Verifica permessi root
if [[ $EUID -ne 0 ]]; then
    echo "[ERROR] $(date '+%Y-%m-%d %H:%M:%S') - Devi eseguire questo script come root!" | tee -a /var/log/aggiornamenti.log
    exit 1
fi

# Funzione per registrare i messaggi
log() {
    local type="$1"
    shift
    echo "[$type] $(date '+%Y-%m-%d %H:%M:%S') - $*" | tee -a /var/log/aggiornamenti.log
}

# Funzione per aggiornare i pacchetti APT
update_apt_packages() {
    log "INFO" "Aggiornamento dell'elenco dei pacchetti..."
    apt-get update -y
    log "INFO" "Elenco pacchetti aggiornato."

    log "INFO" "Aggiornamento dei pacchetti APT..."
    apt-get upgrade -y
    log "INFO" "Pacchetti APT aggiornati."
}

# Funzione per pulire i pacchetti APT
clean_apt_packages() {
    log "INFO" "Pulizia dei pacchetti inutili e della cache..."
    apt-get autoremove --purge -y
    apt-get clean -y
    log "INFO" "Pacchetti inutili rimossi e cache pulita."
}

# Funzione per sbloccare tutte le interfacce di rete
unblock_network_interfaces() {
    log "INFO" "Sblocco della sospensione energetica di tutte le interfacce di rete..."
    rfkill unblock all
}

# Funzione per installare Snapd
install_snapd() {
    log "INFO" "Controllo di Snapd..."
    if ! dpkg -l | grep -q snapd; then
        log "INFO" "Installazione di Snapd..."
    apt-get install snapd -y
    else
        log "INFO" "Snapd è già installato."
    fi

    log "INFO" "Aggiornamento dei pacchetti Snap..."
    snap refresh
}

# Funzione per gestire i potenziali blocchi di dpkg
reload_systemd_and_dpkg() {
    log "INFO" "Ricaricamento dei demoni di sistema..."
    systemctl daemon-reload

    log "INFO" "Gestione del blocco dpkg..."
    while fuser /var/lib/dpkg/lock-frontend >/dev/null 2>&1; do
        log "INFO" "Blocco dpkg rilevato. Attesa di 5 secondi..."
        sleep 5
    done
    dpkg --configure -a
}

# Funzione per abilitare il firewall
enable_firewall() {
    log "INFO" "Verifica dello stato del firewall..."
    if ! ufw status | grep -q 'Status: active'; then
        log "INFO" "Abilitazione del firewall..."
        ufw enable
    else
        log "INFO" "Il firewall è già attivo."
    fi
}

# Funzione per eseguire un comando e registrare il risultato
execute_command() {
    local command="$1"
    local message="$2"
    log "INFO" "Esecuzione: $message..."
    if eval "$command"; then
        log "INFO" "$message completato con successo."
    else
        log "ERROR" "Errore durante: $message."
        exit 1
    fi
}

# Funzione per verificare se un comando è disponibile
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Funzione per aggiornare alla nuova versione di Ubuntu
upgrade_ubuntu() {
    log "INFO" "Aggiornamento alla nuova versione di Ubuntu..."
    if do-release-upgrade; then
        log "INFO" "Aggiornamento completato con successo."
    else
        log "INFO" "Nessuna nuova versione di Ubuntu trovata."
    fi
}

# Inizio dello script
log "INFO" "Inizio aggiornamenti..."

reload_systemd_and_dpkg
update_apt_packages
clean_apt_packages
unblock_network_interfaces
install_snapd
enable_firewall
PKGS_RC=$(dpkg -l | grep '^rc' | awk '{print $2}' | tr '\n' ' ')
if [ -n "$PKGS_RC" ]; then
    log "INFO" "Rimozione pacchetti rc: $PKGS_RC"
    dpkg --purge $PKGS_RC 2>/dev/null
    log "INFO" "Pacchetti rc rimossi."
else
    log "INFO" "Nessun pacchetto rc da rimuovere. Salto."
fi
sudo rm -rf ~/.cache/thumbnails/*
# apt modernize-sources -y  # Comando non standard, commentato

# Aggiungi comandi per aggiornare il sistema Ubuntu 24.10
log "INFO" "Aggiornamento del sistema Ubuntu 24.10..."
update_apt_packages
execute_command "apt-get full-upgrade -y" "Aggiornamento completo"
execute_command "apt-get autoremove -y" "Rimozione pacchetti non necessari"

# Rimuove tutti gli oggetti inutilizzati (container, immagini, network) tranne i volumi
if command_exists docker; then
    log "INFO" "Pulizia delle risorse Docker inutilizzate..."
    docker system prune --filter "until=3h" -f
    log "INFO" "Risorse Docker pulite."
else
    log "INFO" "Docker non è installato. Salto la pulizia delle risorse Docker."
fi

log "INFO" "Aggiornamenti completati!"
