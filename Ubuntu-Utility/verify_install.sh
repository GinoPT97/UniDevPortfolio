#!/bin/bash
# =============================================================================
# verify_install.sh
# Verifica che tutti i pacchetti, snap e servizi dell'autoinstall
# siano stati installati correttamente. Produce un report dettagliato.
#
# USO (dentro la VM, dopo il riavvio post-installazione):
#   chmod +x verify_install.sh
#   ./verify_install.sh
#   cat ~/install_report.txt
# =============================================================================

REPORT="$HOME/install_report.txt"
PASS=0; FAIL=0; WARN=0

GREEN='\033[0;32m'; RED='\033[0;31m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; NC='\033[0m'
ok()   { echo -e "  ${GREEN}✔${NC} $1"; echo "  [OK]  $1" >> "$REPORT"; ((PASS++)); }
fail() { echo -e "  ${RED}✘${NC} $1"; echo "  [FAIL] $1" >> "$REPORT"; ((FAIL++)); }
warn() { echo -e "  ${YELLOW}⚠${NC} $1"; echo "  [WARN] $1" >> "$REPORT"; ((WARN++)); }
section() {
  echo -e "\n${CYAN}━━━ $1 ━━━${NC}"
  echo -e "\n=== $1 ===" >> "$REPORT"
}

# Intestazione report
{
  echo "=============================================="
  echo " REPORT VERIFICA INSTALLAZIONE"
  echo " Data: $(date)"
  echo " Host: $(hostname)"
  echo " OS: $(lsb_release -ds 2>/dev/null || cat /etc/os-release | grep PRETTY_NAME | cut -d= -f2)"
  echo "=============================================="
} | tee "$REPORT"

# =============================================================================
# 1. Pacchetti APT
# =============================================================================
section "Pacchetti APT"

APT_PACKAGES=(
  ca-certificates curl gnupg lsb-release wget apt-transport-https
  zram-config preload flatpak git gparted
  clamav clamtk postgresql vlc cmake aptitude doxygen graphviz net-tools
  gdebi dos2unix texlive-latex-base texlive-latex-extra texlive-pictures texlive-science
  git-lfs cryptsetup lvm2 synaptic stacer bleachbit
  build-essential libvips-dev jest
  linux-tools-common linux-tools-generic gnome-tweaks
  imagemagick default-jdk hplip hplip-gui redis tor
  code
)

for pkg in "${APT_PACKAGES[@]}"; do
  if dpkg -l "$pkg" 2>/dev/null | grep -q "^ii"; then
    ok "apt: $pkg"
  else
    fail "apt: $pkg — NON installato"
  fi
done

# =============================================================================
# 2. Applicazioni Snap
# =============================================================================
section "Applicazioni Snap"

SNAP_PACKAGES=(
  dbeaver-ce openjdk android-studio eclipse
  intellij-idea-ultimate node telegram-desktop
  teams-for-linux pgadmin4 perplexity-desktop scrcpy docker
)

for snap in "${SNAP_PACKAGES[@]}"; do
  if snap list "$snap" 2>/dev/null | grep -q "$snap"; then
    ok "snap: $snap"
  else
    fail "snap: $snap — NON installato"
  fi
done

# =============================================================================
# 3. Servizi di sistema
# =============================================================================
section "Servizi di sistema"

SERVICES=(tor redis-server postgresql)

for svc in "${SERVICES[@]}"; do
  STATUS=$(systemctl is-active "$svc" 2>/dev/null)
  ENABLED=$(systemctl is-enabled "$svc" 2>/dev/null)
  if [[ "$STATUS" == "active" && "$ENABLED" == "enabled" ]]; then
    ok "servizio: $svc (attivo + abilitato)"
  elif [[ "$STATUS" == "active" ]]; then
    warn "servizio: $svc — attivo ma NON abilitato all'avvio"
  elif [[ "$ENABLED" == "enabled" ]]; then
    warn "servizio: $svc — abilitato ma NON attivo ora"
  else
    fail "servizio: $svc — inattivo e non abilitato"
  fi
done

# =============================================================================
# 4. Docker
# =============================================================================
section "Docker"

if snap list docker 2>/dev/null | grep -q docker; then
  ok "docker: installato via snap"
else
  fail "docker: NON installato"
fi

if groups "$USER" | grep -q docker; then
  ok "docker: utente $USER nel gruppo docker"
else
  warn "docker: utente $USER NON nel gruppo docker (potrebbe servire logout/login)"
fi

if docker info &>/dev/null; then
  ok "docker: daemon raggiungibile"
else
  warn "docker: daemon non raggiungibile (normale se il gruppo non è ancora attivo)"
fi

# =============================================================================
# 5. Strumenti da riga di comando
# =============================================================================
section "Strumenti CLI"

check_cmd() {
  local cmd=$1 label=${2:-$1}
  if command -v "$cmd" &>/dev/null; then
    ok "$label: $(command -v $cmd)"
  else
    fail "$label: comando non trovato"
  fi
}

check_cmd git "git"
check_cmd "git-lfs" "git-lfs"
check_cmd psql "psql (PostgreSQL)"
check_cmd redis-cli "redis-cli"
check_cmd tor "tor"
check_cmd wget "wget"
check_cmd curl "curl"
check_cmd cmake "cmake"
check_cmd java "java (default-jdk)"
check_cmd code "Visual Studio Code"
check_cmd flatpak "flatpak"
check_cmd imagemagick convert "imagemagick"

# Rust (installato per utente, non globale)
if [[ -f "$HOME/.cargo/bin/rustc" ]]; then
  ok "rust: $("$HOME/.cargo/bin/rustc" --version 2>/dev/null)"
else
  fail "rust: rustc non trovato in ~/.cargo/bin"
fi

# =============================================================================
# 6. Log di installazione del subiquity
# =============================================================================
section "Log installer (ultime 30 righe di errori)"

INSTALLER_LOG="/var/log/installer/subiquity-server-debug.log"
if [[ -f "$INSTALLER_LOG" ]]; then
  ok "Log installer trovato: $INSTALLER_LOG"
  echo "" >> "$REPORT"
  echo "--- Errori nel log installer ---" >> "$REPORT"
  grep -i "error\|fail\|exception" "$INSTALLER_LOG" | tail -30 >> "$REPORT" 2>/dev/null \
    && echo "(vedi report per dettagli)" \
    || echo "  Nessun errore trovato nel log."
else
  warn "Log installer non trovato in $INSTALLER_LOG"
fi

# =============================================================================
# Riepilogo finale
# =============================================================================
TOTAL=$((PASS + FAIL + WARN))
{
  echo ""
  echo "=============================================="
  echo " RIEPILOGO"
  echo " Totale controlli : $TOTAL"
  echo " Superati   ✔     : $PASS"
  echo " Falliti    ✘     : $FAIL"
  echo " Avvisi     ⚠     : $WARN"
  echo "=============================================="
} | tee -a "$REPORT"

echo ""
if [[ $FAIL -eq 0 ]]; then
  echo -e "${GREEN}✔ Installazione verificata con successo!${NC}"
else
  echo -e "${RED}✘ $FAIL componenti non installati correttamente.${NC}"
  echo -e "  Controlla il report completo: ${CYAN}$REPORT${NC}"
fi
echo ""
echo -e "Report salvato in: ${CYAN}$REPORT${NC}"
