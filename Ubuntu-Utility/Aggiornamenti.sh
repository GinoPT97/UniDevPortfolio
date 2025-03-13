#!/bin/bash

# Funzione per registrare i messaggi
log() {
    local type="$1"
    shift
    echo "[$type] $(date '+%Y-%m-%d %H:%M:%S') - $*"
}

# Funzione per aggiornare i pacchetti APT
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

# Funzione per gestire i potenziali blocchi di dpkg
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

# Funzione per aggiornare alla nuova versione di Ubuntu
upgrade_ubuntu() {
    log "INFO" "Aggiornamento alla nuova versione di Ubuntu..."
    sudo do-release-upgrade
}

# Funzione per mantenere solo l'ultimo container creato per ogni immagine Docker
clean_old_docker_containers() {
    log "INFO" "Pulizia dei vecchi container Docker (mantenendo solo l'ultimo per immagine)..."

    # Per ogni immagine usata dai container:
    for image in $(docker ps -a --format '{{.Image}}' | sort | uniq); do
        log "INFO" "Processing image: $image"

        # Ottiene gli ID dei container per questa immagine, ordinati per data di creazione (i più vecchi per primi)
        mapfile -t containers < <(
            for id in $(docker ps -a --filter "ancestor=$image" --format '{{.ID}}'); do
                echo "$(docker inspect -f '{{.Created}}' "$id") $id"
            done | sort | awk '{print $2}'
        )

        count=${#containers[@]}
        log "INFO" "Trovati $count container per $image"

        # Se ci sono più di un container, elimina tutti tranne l'ultimo (più recente)
        if (( count > 1 )); then
            n_to_remove=$(( count - 1 ))
            log "INFO" "Elimino $n_to_remove container (i più vecchi) per $image"
            for ((i=0; i<n_to_remove; i++)); do
                log "INFO" "Rimuovo container: ${containers[i]}"
                docker rm "${containers[i]}"
            done
        else
            log "INFO" "Per $image non ci sono container da eliminare."
        fi
        log "INFO" "------"
    done
}

# Inizio dello script
log "INFO" "Inizio aggiornamenti..."

reload_systemd_and_dpkg
update_apt_packages
clean_apt_packages
unblock_wifi
install_snapd
enable_firewall

# Pulizia dei vecchi container Docker
clean_old_docker_containers

# Aggiungi comandi per aggiornare il sistema Ubuntu 24.10
log "INFO" "Aggiornamento del sistema Ubuntu 24.10..."
update_apt_packages
execute_command "apt-get full-upgrade -y" "Aggiornamento completo"
execute_command "apt-get autoremove -y" "Rimozione pacchetti non necessari"
upgrade_ubuntu

log "INFO" "Aggiornamenti completati!"
