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

echo "Installazione estensioni per VS Code..."
code --install-extension github.copilot --force
code --install-extension github.copilot-chat --force
code --install-extension github.remotehub --force
code --install-extension github.vscode-pull-request-github --force
code --install-extension ms-azuretools.vscode-docker --force
code --install-extension christian-kohler.npm-intellisense --force
code --install-extension dbaeumer.vscode-eslint --force
code --install-extension esbenp.prettier-vscode --force
code --install-extension ms-python.python --force
code --install-extension ms-vscode.cpptools --force
code --install-extension eamodio.gitlens --force
code --install-extension ritwickdey.LiveServer --force
code --install-extension amazonwebservices.aws-toolkit-vscode --force
code --install-extension netlify.netlify-vscode --force
code --install-extension dsznajder.es7-react-js-snippets --force
code --install-extension steoates.autoimport --force
code --install-extension jebbs.plantuml --force

echo '{"locale":"it"}' > ~/.config/Code/User/locale.json
code --locale=it

echo "Installazione applicazioni Snap..."
sudo snap install --classic dbeaver-ce
sudo snap install --classic openjdk
sudo snap install --classic code
sudo snap install --classic android-studio
sudo snap install --classic eclipse
sudo snap install node --classic
sudo snap install telegram-desktop
sudo snap install teams-for-linux
sudo snap install pgadmin4
sudo snap install perplexity-desktop

echo "Installazione di Docker..."
sudo snap install docker
sudo snap start docker
sudo groupadd docker
sudo usermod -aG docker "$USER"

echo "Installazione e configurazione di NordVPN..."
sudo snap install nordvpn
sudo snap connect nordvpn:hardware-observe
sudo snap connect nordvpn:network-control
sudo snap connect nordvpn:network-observe
sudo snap connect nordvpn:firewall-control
sudo snap connect nordvpn:login-session-observe
sudo snap connect nordvpn:system-observe
sudo snap install j-nordvpn-manager

echo "Installazione completata!"

echo "Abilitazione della visualizzazione della percentuale della batteria..."
gsettings set org.gnome.desktop.interface show-battery-percentage true
