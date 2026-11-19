#!/usr/bin/env bash
# Prestazioni.sh — tuning prestazioni Ubuntu (desktop, NVMe, zram, 16 GB RAM)
# Uso: sudo ./Prestazioni.sh
set -euo pipefail

if [[ $EUID -ne 0 ]]; then
  echo "Esegui con sudo: sudo $0"; exit 1
fi

ask() { read -rp "$1 [s/N] " r; [[ "${r,,}" == s* ]]; }

echo "== 1. Avvio: disattivo NetworkManager-wait-online (circa 6 s di boot in meno) =="
systemctl disable --now NetworkManager-wait-online.service || true

echo "== 2. ModemManager (nessun modem 3G/4G rilevato) =="
if ! mmcli -L 2>/dev/null | grep -q '/Modem/'; then
  systemctl disable --now ModemManager.service || true
else
  echo "Modem presente, lascio ModemManager attivo."
fi

echo "== 3. Memoria: swappiness e cache pressure ottimizzate per zram =="
cat > /etc/sysctl.d/99-prestazioni.conf <<'EOF'
# Con zram lo swap compresso in RAM costa poco: meglio usarlo prima di
# scartare la page cache. Valori consigliati per sistemi con zram.
vm.swappiness = 150
vm.vfs_cache_pressure = 50
vm.page-cluster = 0
EOF
sysctl --system >/dev/null
sysctl vm.swappiness vm.vfs_cache_pressure vm.page-cluster

echo "== 4. Snap: tengo solo 2 revisioni (meno loop device e meno spazio) =="
snap set system refresh.retain=2

echo "== 5. Servizi di sviluppo avviati al boot =="
echo "Disattivarli all'avvio libera RAM/CPU; si avviano a mano con: sudo systemctl start <nome>"
for svc in postgresql redis-server tor; do
  if systemctl is-enabled "$svc" &>/dev/null; then
    if ask "Disattivare $svc all'avvio?"; then
      systemctl disable --now "$svc" && echo "  $svc disattivato."
    fi
  fi
done

echo "== 6. Telemetria Ubuntu (ubuntu-report) =="
if ask "Disattivare ubuntu-report?"; then
  sudo -u "${SUDO_USER:-$USER}" XDG_RUNTIME_DIR="/run/user/$(id -u "${SUDO_USER:-$USER}")" \
    systemctl --user disable --now ubuntu-report.path || true
fi

echo
echo "Fatto. Riavvia per vedere il tempo di boot con: systemd-analyze"
