#!/bin/bash

set -Eeuo pipefail
LOG_FILE="/var/log/aggiornamenti.log"
export DEBIAN_FRONTEND=noninteractive

[[ $EUID -eq 0 ]] || { echo "[ERROR] $(date '+%Y-%m-%d %H:%M:%S') - Esegui come root" | tee -a "$LOG_FILE"; exit 1; }

log() {
    local type="$1"; shift
    echo "[$type] $(date '+%Y-%m-%d %H:%M:%S') - $*" | tee -a "$LOG_FILE"
}
trap 'log "ERROR" "Errore alla riga $LINENO (exit code: $?)"' ERR

command_exists() { command -v "$1" >/dev/null 2>&1; }

wait_for_apt_locks() {
    local lock
    for lock in \
        /var/lib/dpkg/lock-frontend \
        /var/lib/dpkg/lock \
        /var/cache/apt/archives/lock \
        /var/lib/apt/lists/lock
    do
        while fuser "$lock" >/dev/null 2>&1; do
            log "INFO" "Lock su $lock, attesa 5s..."
            sleep 5
        done
    done
}

repair_package_state() {
    log "INFO" "Riparazione stato pacchetti..."
    wait_for_apt_locks
    dpkg --configure -a
    apt-get -f install -y
    apt-get check
}

update_apt_packages() {
    wait_for_apt_locks
    log "INFO" "Aggiornamento APT..."
    apt-get update
    apt-get upgrade -y
}

clean_apt_packages() {
    log "INFO" "Pulizia APT..."
    apt-get autoremove --purge -y
    apt-get autoclean -y
    apt-get clean
}

unblock_network_interfaces() {
    command_exists rfkill && rfkill unblock all || log "INFO" "rfkill non disponibile, salto."
}

install_or_refresh_snap() {
    log "INFO" "Controllo Snapd..."
    if ! dpkg-query -W -f='${Status}' snapd 2>/dev/null | grep -q "ok installed"; then
        apt-get install -y snapd
    fi

    if command_exists snap; then
        snap refresh
        check_snap_warnings
    else
        log "INFO" "snap non disponibile, salto."
    fi
}

check_snap_warnings() {
    local out
    out="$(snap warnings --abs-time 2>/dev/null || true)"
    [[ -z "${out//[[:space:]]/}" || "$out" =~ ^([Nn]o[[:space:]]warnings|[Nn]essun[[:space:]]avviso) ]] \
        && log "INFO" "Nessun avviso Snap rilevato." \
        || log "WARN" "Sono presenti avvisi Snap. Esegui: snap warnings"
}

reload_systemd_and_dpkg() {
    systemctl daemon-reload
    wait_for_apt_locks
    dpkg --configure -a
}

enable_firewall() {
    if ! command_exists ufw; then
        log "INFO" "ufw non installato, salto."
        return
    fi

    ufw status | grep -q 'Status: active' || ufw --force enable
}

remove_rc_packages() {
    mapfile -t pkgs_rc < <(dpkg -l | awk '/^rc/{print $2}')
    (( ${#pkgs_rc[@]} == 0 )) && { log "INFO" "Nessun pacchetto rc da rimuovere."; return; }
    dpkg --purge "${pkgs_rc[@]}" 2>/dev/null
}

clean_thumbnails_cache() {
    find /root /home -type f -path '*/.cache/thumbnails/*' -delete 2>/dev/null || true
}

log "INFO" "Inizio aggiornamenti..."
repair_package_state
reload_systemd_and_dpkg
update_apt_packages
unblock_network_interfaces
install_or_refresh_snap
enable_firewall

log "INFO" "Aggiornamento completo sistema..."
wait_for_apt_locks
apt-get full-upgrade -y
clean_apt_packages
remove_rc_packages
clean_thumbnails_cache

if command_exists docker; then
    log "INFO" "Pulizia risorse Docker..."
    docker system prune --filter "until=24h" -f
fi

log "INFO" "Aggiornamenti completati!"
