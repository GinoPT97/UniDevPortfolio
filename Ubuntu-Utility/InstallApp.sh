#!/bin/bash
set -e

echo "Aggiornamento del sistema..."

sudo apt update && sudo apt upgrade -y
sudo apt install -y \
  ca-certificates curl gnupg lsb-release wget apt-transport-https net-tools \
  zram-config preload \
  build-essential git git-lfs git-filter-repo cmake gdebi dos2unix zenity linux-tools-common \
  openjdk-21-jdk \
  postgresql redis tor \
  vlc gparted \
  graphviz latexmk \
  texlive-latex-base texlive-latex-extra texlive-latex-recommended texlive-fonts-recommended texlive-pictures texlive-science \
  hplip hplip-gui \
  gnome-tweaks ptyxis stacer bleachbit \
  language-pack-it language-pack-it-base language-pack-gnome-it language-pack-gnome-it-base \
  mythes-it hyphen-it libreoffice-l10n-it libreoffice-help-it ripgrep scrcpy

echo "Installazione di VirtualBox e strumenti multimediali..."
sudo apt install virtualbox -y
sudo apt install mpv curl ffmpeg -y

git lfs install

echo "Configurazione della password PostgreSQL..."
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'postgres';"

# Avvio e abilitazione servizi Tor e Redis
sudo systemctl start tor && sudo systemctl enable tor
sudo systemctl start redis-server && sudo systemctl enable redis-server

install_deb() {
  local url=$1
  local file="/tmp/$(basename "$url")"
  echo ">>> Installazione $(basename "$url")..."
  wget -q --show-progress "$url" -O "$file" || { echo "ERRORE: download fallito"; return 1; }
  sudo dpkg -i "$file" || sudo apt-get install -f -y
  rm -f "$file"
}

install_deb "https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb"
install_deb "https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb"

git config --global user.email "g.pandozzitrani@studenti.unina.it"                                  
git config --global user.name "kenobi1797"

#Installazione di code tramite apt (formato deb822, coerente con quello che apt genera di default)
wget -qO- https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.gpg
sudo install -o root -g root -m 644 microsoft.gpg /usr/share/keyrings/
rm -f microsoft.gpg

sudo tee /etc/apt/sources.list.d/vscode.sources > /dev/null << 'EOF'
Types: deb
URIs: https://packages.microsoft.com/repos/code
Suites: stable
Components: main
Architectures: amd64
Signed-By: /usr/share/keyrings/microsoft.gpg
EOF

sudo apt update
sudo apt install code -y

#Installazione di Rust
echo "Installazione di Rust..."
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y
source $HOME/.cargo/env

echo "Installazione applicazioni Snap..."
sudo snap install --classic openjdk
#sudo snap install --classic code
sudo snap install --classic android-studio
sudo snap install --classic eclipse
sudo snap install intellij-idea-ultimate --classic
sudo snap install node --classic
sudo snap install telegram-desktop teams-for-linux pgadmin4 perplexity-desktop docker
sudo snap install firefox thunderbird steam

echo "Installazione di Docker..."
sudo snap start docker
sudo groupadd docker || true
sudo usermod -aG docker "$USER"

echo "Installazione completata!"
