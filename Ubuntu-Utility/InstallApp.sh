#!/bin/bash
set -e

echo "Aggiornamento del sistema..."
sudo apt update && sudo apt upgrade -y

# Installa curl (se non è già presente)
sudo apt install -y curl

echo "Installazione di PostgreSQL..."
sudo apt update
sudo apt install -y postgresql postgresql-contrib

echo "Configurazione della password PostgreSQL..."
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'postgres';"

echo "Avvio del servizio Tor..."
sudo apt install tor
sudo systemctl start tor

sudo apt install -y redis
sudo systemctl start redis
sudo systemctl enable redis

# Definizione della lista dei pacchetti da installare (inclusi OpenJDK, Node.js e npm)
packages=(
  openjdk-17-jdk openjdk-21-jdk openjdk-17-jre openjdk-21-jre
  ca-certificates curl gnupg lsb-release software-properties-common wget apt-transport-https
  wireshark kate zram-config preload bluetooth bluez blueman flatpak git gparted
  clamav clamtk postgresql-16 postgresql-client-16 postgresql-client-common postgresql-common
  arduino vlc cmake deja-dup tor aptitude doxygen graphviz net-tools
  gdebi dos2unix ssmtp texlive-latex-base texlive-latex-extra git-lfs cryptsetup
  lvm2 exfatprogs synaptic stacer tlp cpufrequtils
  build-essential libvips-dev jest
  nodejs npm
)

echo "Installazione dei pacchetti: ${packages[*]}"
sudo apt install -y "${packages[@]}"

git lfs install

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

echo "Installazione di Google Chrome..."
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
sudo dpkg -i google-chrome-stable_current_amd64.deb && \
sudo apt --fix-broken install -y && \
rm google-chrome-stable_current_amd64.deb

echo "Installazione di GitHub Desktop..."
pkg_name=$(basename "https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb")
wget -q "https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb" -O "$pkg_name"
sudo dpkg -i "$pkg_name"
sudo apt-get install -f -y
rm -f "$pkg_name"

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

echo "Pulizia file temporanei..."
rm -f *.deb *.run

echo "Installazione completata!"
