#!/bin/bash
set -e

echo "Aggiornamento del sistema..."
sudo apt update && sudo apt upgrade -y

# Installa curl (se non è già presente)
sudo apt install -y curl

# Aggiorna i pacchetti e installa ImageMagick
sudo apt update
sudo apt install imagemagick

# Definizione della lista dei pacchetti da installare
packages=(
  ca-certificates curl gnupg lsb-release wget apt-transport-https
  zram-config preload bluetooth bluez blueman flatpak git gparted
  clamav clamtk postgresql git
  vlc cmake aptitude doxygen graphviz net-tools
  gdebi dos2unix texlive-latex-base texlive-latex-extra git-lfs cryptsetup
  lvm2 synaptic stacer build-essential libvips-dev jest
  linux-tools-common linux-tools-generic
  gnome-tweaks
)

echo "Installazione dei pacchetti: ${packages[*]}"
sudo apt install -y "${packages[@]}"

sudo apt install default-jdk

sudo apt update
sudo apt install redis
sudo apt install tor
sudo apt install hplip hplip-gui

# Installa git-lfs e inizializza
sudo apt install -y git-lfs
git lfs install

echo "Configurazione della password PostgreSQL..."
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'postgres';"

echo "Avvio e abilitazione dei servizi Tor e Redis..."
sudo systemctl start tor
sudo systemctl enable tor
sudo systemctl start redis-server
sudo systemctl enable redis-server

# Funzione per scaricare, installare e pulire un pacchetto .deb
install_deb() {
  local url=$1
  local file=$(basename "$url")

  echo "Scaricamento di $file..."
  wget -q --show-progress "$url" -O "$file"

  echo "Installazione di $file..."
  sudo dpkg -i "$file" || sudo apt-get install -f -y

  echo "Pulizia di $file..."
  rm -f "$file"
  echo "$file installato correttamente."
}

echo "Installazione di Google Chrome..."
install_deb "https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb"

echo "Installazione di GitHub Desktop..."
install_deb "https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb"

echo "Installazione applicazioni Snap..."
sudo snap install --classic dbeaver-ce
sudo snap install --classic openjdk
sudo snap install --classic code
sudo snap install --classic android-studio
sudo snap install --classic eclipse
sudo snap install intellij-idea-ultimate --classic
sudo snap install node --classic
sudo snap install telegram-desktop
sudo snap install teams-for-linux
sudo snap install pgadmin4
sudo snap install perplexity-desktop
sudo snap install scrcpy

echo "Installazione di Docker..."
sudo snap install docker
sudo snap start docker
sudo groupadd docker
sudo usermod -aG docker "$USER"

echo "Installazione completata!"

echo "Abilitazione della visualizzazione della percentuale della batteria..."
gsettings set org.gnome.desktop.interface show-battery-percentage true
