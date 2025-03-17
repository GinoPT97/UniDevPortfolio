#!/bin/bash
set -e

echo "Aggiornamento del sistema..."
sudo apt update && sudo apt upgrade -y

# Installa curl (se non è già presente)
sudo apt install -y curl

# Definizione della lista dei pacchetti da installare (inclusi OpenJDK, Node.js e npm)
packages=(
  openjdk-17-jdk openjdk-21-jdk openjdk-17-jre openjdk-21-jre
  ca-certificates curl gnupg lsb-release wget apt-transport-https
  wireshark kate zram-config preload bluetooth bluez blueman flatpak git gparted
  clamav clamtk postgresql-16 postgresql-client-16 postgresql-client-common postgresql-common
  arduino vlc cmake aptitude doxygen graphviz net-tools
  gdebi dos2unix texlive-latex-base texlive-latex-extra git-lfs cryptsetup
  lvm2 synaptic stacer build-essential libvips-dev jest
  nodejs npm
  postgresql postgresql-contrib
  tor redis
  linux-tools-common linux-tools-generic cpupower-gui indicator-cpufreq
)

echo "Installazione dei pacchetti: ${packages[*]}"
sudo apt install -y "${packages[@]}"

git lfs install

echo "Configurazione della password PostgreSQL..."
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'postgres';"

echo "Avvio e abilitazione dei servizi Tor e Redis..."
sudo systemctl enable --now tor
sudo systemctl enable --now redis

echo "Installazione di Docker..."
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io
sudo systemctl start docker
sudo systemctl enable docker

echo "Installazione e configurazione di NordVPN..."
sh <(wget -qO - https://downloads.nordcdn.com/apps/linux/install.sh)
nordvpn login
nordvpn connect
nordvpn set autoconnect on

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
code --install-extension ms-azuretools.azure-dev --force
code --install-extension ms-azuretools.vscode-azureappservice --force
code --install-extension ms-azuretools.vscode-docker --force
code --install-extension christian-kohler.npm-intellisense --force
code --install-extension dbaeumer.vscode-eslint --force
code --install-extension esbenp.prettier-vscode --force
code --install-extension ms-python.python --force
code --install-extension ms-vscode.cpptools --force
code --install-extension eamodio.gitlens --force
code --install-extension ritwickdey.LiveServer --force
code --install-extension amazonwebservices.aws-toolkit-vscode --force
code --install-extension ms-azuretools.vscode-azurestaticwebapps --force
code --install-extension netlify.netlify-vscode --force
code --install-extension dsznajder.es7-react-js-snippets --force
code --install-extension steoates.autoimport --force
code --install-extension coenraads.bracket-pair-colorizer-2 --force

echo '{"locale":"it"}' > ~/.config/Code/User/locale.json
code --locale=it

echo "Installazione applicazioni Snap..."
sudo snap install --classic dbeaver-ce
sudo snap install --classic openjdk
sudo snap install --classic code
sudo snap install --classic android-studio
sudo snap install --classic eclipse
sudo snap install telegram-desktop
sudo snap install spotify
sudo snap install whatsdesk
sudo snap install teams-for-linux
sudo snap install pgadmin4

echo "Installazione completata!"
