#!/bin/bash

# Funzione per loggare messaggi
log() {
    local type="$1"
    shift
    echo "[$type] $(date '+%Y-%m-%d %H:%M:%S') - $*"
}

# Funzione per aggiornare pacchetti APT
update_apt_packages() {
    log "INFO" "Aggiornamento dell'elenco dei pacchetti..."
    if sudo apt-get update -y; then
        log "INFO" "Elenco pacchetti aggiornato."
    else
        log "ERROR" "Errore durante l'aggiornamento dell'elenco dei pacchetti."
        exit 1
    fi

    log "INFO" "Aggiornamento dei pacchetti APT..."
    if sudo apt-get upgrade -y; then
        log "INFO" "Pacchetti APT aggiornati."
    else
        log "ERROR" "Errore durante l'aggiornamento dei pacchetti APT."
        exit 1
    fi
}

# Funzione per pulire i pacchetti APT
clean_apt_packages() {
    log "INFO" "Pulizia dei pacchetti inutili e della cache..."
    sudo apt-get autoremove --purge -y && sudo apt-get clean -y
}

# Funzione per sbloccare il Wi-Fi
unblock_wifi() {
    log "INFO" "Sblocco della sospensione energetica del Wi-Fi..."
    sudo rfkill unblock wifi
}

# Funzione per installare Snapd
install_snapd() {
    log "INFO" "Controllo di Snapd..."
    if ! dpkg -l | grep -q snapd; then
        log "INFO" "Installazione di Snapd..."
        sudo apt-get install snapd -y
    else
        log "INFO" "Snapd è già installato."
    fi

    log "INFO" "Aggiornamento dei pacchetti Snap..."
    sudo snap refresh
}

# Funzione per gestire eventuali blocchi di dpkg
reload_systemd_and_dpkg() {
    log "INFO" "Ricaricamento dei demoni di sistema..."
    sudo systemctl daemon-reload

    log "INFO" "Gestione del blocco dpkg..."
    while sudo fuser /var/lib/dpkg/lock-frontend >/dev/null 2>&1; do
        log "INFO" "Blocco dpkg rilevato. Attesa di 5 secondi..."
        sleep 5
    done
    sudo dpkg --configure -a
}

# Funzione per abilitare il firewall
enable_firewall() {
    log "INFO" "Verifica dello stato del firewall..."
    if ! sudo ufw status | grep -q 'Status: active'; then
        log "INFO" "Abilitazione del firewall..."
        sudo ufw enable
    else
        log "INFO" "Il firewall è già attivo."
    fi
}

# Inizio dello script
log "INFO" "Inizio aggiornamenti..."

reload_systemd_and_dpkg
update_apt_packages
clean_apt_packages
unblock_wifi
install_snapd
enable_firewall

log "INFO" "Aggiornamenti completati!"
