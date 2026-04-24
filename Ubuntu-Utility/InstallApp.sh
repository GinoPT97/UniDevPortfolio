#!/bin/bash
set -Eeuo pipefail

export DEBIAN_FRONTEND=noninteractive

log() {
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] $*"
}

trap 'log "ERRORE alla riga $LINENO (exit code: $?)"' ERR

wait_for_apt_locks() {
  local locks=(
    /var/lib/dpkg/lock-frontend
    /var/lib/dpkg/lock
    /var/cache/apt/archives/lock
    /var/lib/apt/lists/lock
  )

  for lock in "${locks[@]}"; do
    while sudo fuser "$lock" >/dev/null 2>&1; do
      log "Lock rilevato su $lock, attesa di 5 secondi..."
      sleep 5
    done
  done
}

install_snap_if_missing() {
  local pkg="$1"
  shift || true

  if snap list "$pkg" >/dev/null 2>&1; then
    log "Snap $pkg gia installato, salto."
  else
    sudo snap install "$pkg" "$@"
  fi
}

log "Aggiornamento del sistema..."

wait_for_apt_locks
sudo apt update && sudo apt upgrade -y
sudo apt install -y \
  ca-certificates curl gnupg lsb-release wget apt-transport-https net-tools \
  zram-config preload \
  build-essential git git-lfs git-filter-repo cmake gdebi dos2unix \
  openjdk-21-jdk \
  postgresql redis tor \
  virtualbox mpv ffmpeg ani-cli \
  vlc gparted \
  graphviz latexmk \
  texlive-latex-base texlive-latex-extra texlive-latex-recommended texlive-fonts-recommended texlive-pictures texlive-science \
  hplip hplip-gui \
  gnome-tweaks ptyxis stacer bleachbit \
  language-pack-it language-pack-it-base language-pack-gnome-it language-pack-gnome-it-base \
  mythes-it hyphen-it libreoffice-l10n-it libreoffice-help-it ripgrep

log "Installazione completata dei pacchetti APT principali."

git lfs install

log "Configurazione della password PostgreSQL..."
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'postgres';"

# Avvio e abilitazione servizi Tor e Redis
sudo systemctl enable --now tor
sudo systemctl enable --now redis-server

install_deb() {
  local url=$1
  local file="/tmp/$(basename "$url")"
  log ">>> Installazione $(basename "$url")..."
  wget -q --show-progress "$url" -O "$file" || { echo "ERRORE: download fallito"; return 1; }
  sudo dpkg -i "$file" || sudo apt-get install -f -y
  rm -f "$file"
}

install_deb "https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb"
install_deb "https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb"

git config --global user.email "g.pandozzitrani@studenti.unina.it"                                  
git config --global user.name "kenobi1797"

# Installazione di VS Code tramite apt
wget -qO- https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.gpg
sudo install -o root -g root -m 644 microsoft.gpg /usr/share/keyrings/
rm -f microsoft.gpg

sudo sh -c 'echo "deb [arch=amd64 signed-by=/usr/share/keyrings/microsoft.gpg] \
https://packages.microsoft.com/repos/code stable main" \
> /etc/apt/sources.list.d/vscode.list'

sudo apt update
sudo apt install code

# Installazione di Rust
log "Installazione di Rust..."
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y

if [[ -f "$HOME/.cargo/env" ]]; then
  # shellcheck disable=SC1090
  source "$HOME/.cargo/env"
fi

log "Installazione applicazioni Snap..."
install_snap_if_missing openjdk --classic
install_snap_if_missing android-studio --classic
install_snap_if_missing eclipse --classic
install_snap_if_missing intellij-idea-ultimate --classic
install_snap_if_missing node --classic
install_snap_if_missing telegram-desktop
install_snap_if_missing teams-for-linux
install_snap_if_missing pgadmin4
install_snap_if_missing perplexity-desktop
install_snap_if_missing scrcpy
install_snap_if_missing docker

log "Installazione di Docker..."
sudo snap start docker
if ! getent group docker >/dev/null; then
  sudo groupadd docker
fi
sudo usermod -aG docker "$USER"

log "Installazione completata!"
