#!/bin/bash

# Funzione per installare pacchetti
install_packages() {
  echo "Installazione di pacchetti: $*"
  sudo apt install -y "$@"
}

# Funzione per installare estensioni di Visual Studio Code
install_vscode_extensions() {
  echo "Installazione di estensioni per VS Code..."
  for extension in "$@"; do
    code --install-extension "$extension" --force
  done
}

# Aggiornamento del sistema
echo "Aggiornamento del sistema..."
sudo apt update && sudo apt upgrade -y

# Installazione di pacchetti generali
install_packages \
  ca-certificates \
  curl \
  gnupg \
  lsb-release \
  software-properties-common \
  wget \
  deborphan \
  apt-transport-https \
  wireshark \
  kate \
  zram-config \
  preload \
  bluetooth \
  bluez \
  blueman \
  flatpak \
  git \
  gparted \
  default-jre \
  openjdk-11-jdk \
  openjdk-11-jre \
  clamav \
  clamtk \
  postgresql-16 \
  postgresql-client-16 \
  postgresql-client-common \
  postgresql-common \
  codeblocks \
  gnome-boxes \
  arduino \
  vlc \
  cmake \
  deja-dup \
  libnvidia-gl-535:i386 \
  tor \
  aptitude \
  doxygen \
  graphviz \
  net-tools \
  gdebi \
  dos2unix \
  openjfx \
  ssmtp \
  texlive-latex-base \
  texlive-latex-extra \
  git-lfs \
  cryptsetup \
  lvm2 \
  exfatprogs \
  nvtop \
  synaptic \
  stacer

# Configurazione di Git LFS
git lfs install

# Installazione di Node.js
echo "Installazione di Node.js..."
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
install_packages nodejs
sudo npm install -g n
sudo n latest

# Installazione di Docker
echo "Installazione di Docker..."
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update
install_packages docker-ce docker-ce-cli containerd.io
sudo systemctl start docker
sudo systemctl enable docker

# Scaricamento del file KVRT
echo "Scaricamento del file KVRT..."
wget -O kvrt.run https://bit.ly/4e8RLMg

# Installazione e configurazione di NordVPN
echo "Installazione e configurazione di NordVPN..."
sh <(wget -qO - https://downloads.nordcdn.com/apps/linux/install.sh)
nordvpn connect
nordvpn set autoconnect on

# Installazione di pgAdmin 4
echo "Installazione di pgAdmin 4..."
curl -fsS https://www.pgadmin.org/static/packages_pgadmin_org.pub | sudo gpg --dearmor -o /usr/share/keyrings/packages-pgadmin-org.gpg
echo "deb [signed-by=/usr/share/keyrings/packages-pgadmin-org.gpg] https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/$(lsb_release -cs) pgadmin4 main" | sudo tee /etc/apt/sources.list.d/pgadmin4.list
sudo apt update
install_packages pgadmin4 pgadmin4-desktop pgadmin4-web
sudo /usr/pgadmin4/bin/setup-web.sh

# Configurazione della password dell'utente PostgreSQL
echo "Configurazione della password dell'utente PostgreSQL..."
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'admin';"

# Installazione di Google Chrome
echo "Installazione di Google Chrome..."
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
sudo dpkg -i google-chrome-stable_current_amd64.deb
sudo apt-get install -f -y

# Installazione di GitHub Desktop
echo "Installazione di GitHub Desktop..."
wget https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb
sudo dpkg -i GitHubDesktop-linux-2.8.1-linux2.deb
sudo apt-get install -f -y

# Installazione di estensioni per VS Code
install_vscode_extensions \
  github.copilot github.copilot-chat github.remotehub github.vscode-pull-request-github \
  ms-azuretools.azure-dev ms-azuretools.vscode-azureappservice ms-azuretools.vscode-docker \
  christian-kohler.npm-intellisense dbaeumer.vscode-eslint esbenp.prettier-vscode \
  ms-python.python ms-vscode.cpptools eamodio.gitlens ritwickdey.liveserver

# Imposta la lingua italiana in Visual Studio Code
echo '{"locale":"it"}' > ~/.config/Code/User/locale.json
code --locale=it

# Installazione di applicazioni tramite Snap
echo "Installazione di applicazioni tramite Snap..."
sudo snap install dbeaver-ce openjdk --classic
sudo snap install --classic code android-studio eclipse pycharm-community
sudo snap install --classic telegram-desktop discord spotify vlc docker

# Avvio del servizio Tor
echo "Avvio del servizio Tor..."
sudo service tor start

# Esecuzione dello script per aggiornamenti personalizzati
echo "Esecuzione dello script per aggiornamenti personalizzati..."
sudo /home/kenobi/Documenti/GitHub/CodicePersonale/Ubuntu-Utility/SH-Personali/Aggiornamenti.sh

echo "Installazione completata!"
