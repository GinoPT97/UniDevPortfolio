#!/bin/bash

# Funzione per installare pacchetti
install_packages() {
  echo "Installazione di pacchetti necessari..."
  sudo apt install -y "$@"
}

# Aggiornamento del sistema
echo "Aggiornamento del sistema..."
sudo apt update && sudo apt upgrade -y

# Installazione di pacchetti necessari per Docker e Node.js
install_packages \
  ca-certificates \
  curl \
  gnupg \
  lsb-release \
  software-properties-common \
  wget \
  deborphan \
  apt-transport-https

# Installazione di Node.js (versione 16 e gestione delle versioni)
echo "Installazione di Node.js..."
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
install_packages nodejs
sudo npm install -g n
sudo n latest
sudo apt-get update

# Installazione di Docker
echo "Installazione di Docker..."
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update
install_packages docker-ce docker-ce-cli containerd.io
sudo systemctl status docker

# Installazione di pacchetti di sistema essenziali
install_packages \
  deborphan \
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
  gparted \
  cryptsetup \
  lvm2

git lfs install

# Scarica il file KVRT
echo "Scaricamento del file KVRT..."
wget -O kvrt.run https://bit.ly/4e8RLMg

# Installazione e configurazione di NordVPN
sh <(wget -qO - https://downloads.nordcdn.com/apps/linux/install.sh)

# Configurazione di NordVPN
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
sudo apt-get install -f -y  # Risolve eventuali dipendenze mancanti

# Installazione di GitHub Desktop
echo "Installazione di GitHub Desktop..."
wget https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb
sudo dpkg -i GitHubDesktop-linux-2.8.1-linux2.deb
sudo apt-get install -f -y  # Risolve eventuali dipendenze mancanti

# Installazione estensioni di Visual Studio Code con opzione force
install_vscode_extensions() {
  for extension in "$@"; do
    code --install-extension "$extension" --force
  done
}

# Estensioni GitHub
install_vscode_extensions \
  github.copilot \
  github.copilot-chat \
  github.remotehub \
  github.vscode-pull-request-github

# Estensioni Azure
install_vscode_extensions \
  ms-azuretools.azure-dev \
  ms-azuretools.vscode-azureappservice \
  ms-azuretools.vscode-azurecontainerapps \
  ms-azuretools.vscode-azurefunctions \
  ms-azuretools.vscode-azureresourcegroups \
  ms-azuretools.vscode-azurestaticwebapps \
  ms-azuretools.vscode-azurestorage \
  ms-azuretools.vscode-azurevirtualmachines \
  ms-azuretools.vscode-bicep \
  ms-azuretools.vscode-cosmosdb \
  ms-azuretools.vscode-docker \
  ms-azuretools.vscode-logicapps \
  ms-vscode.azure-account \
  ms-vscode.azure-repos \
  ms-vscode.azurecli

# Estensioni per Docker e Container
install_vscode_extensions \
  ms-azuretools.vscode-docker \
  ms-vscode-remote.remote-containers

# Estensioni Node.js e strumenti JavaScript
install_vscode_extensions \
  christian-kohler.npm-intellisense \
  dbaeumer.vscode-eslint \
  esbenp.prettier-vscode \
  xabikos.javascriptsnippets \
  ms-vscode.vscode-node-azure-pack \
  ms-vscode.vscode-typescript-next

# Estensioni Remote Development
install_vscode_extensions \
  ms-vscode-remote.remote-ssh \
  ms-vscode-remote.remote-ssh-edit \
  ms-vscode-remote.remote-wsl \
  ms-vscode-remote.vscode-remote-extensionpack

# Linguaggi e runtime per applicazioni web
install_vscode_extensions \
  ms-python.python \
  ms-python.vscode-pylance \
  ms-python.debugpy \
  redhat.java

# Strumenti per server locali e debugging di applicazioni web
install_vscode_extensions \
  ritwickdey.liveserver \
  vadimcn.vscode-lldb

# Strumenti di sviluppo e linting
install_vscode_extensions \
  eamodio.gitlens \
  ms-ceintl.vscode-language-pack-it \
  visualstudioexptteam.vscodeintellicode

# Estensioni per lo sviluppo in C/C++
install_vscode_extensions \
  ms-vscode.cpptools \
  ms-vscode.cpptools-extension-pack \
  ms-vscode.cpptools-themes \
  twxs.cmake \
  vadimcn.vscode-lldb

# Imposta la lingua italiana in Visual Studio Code
echo '{"locale":"it"}' > ~/.config/Code/User/locale.json
code --locale=it

# Installazione di applicazioni tramite Snap
echo "Installazione di applicazioni tramite Snap..."
sudo snap install dbeaver-ce openjdk --classic
sudo snap install --classic code android-studio eclipse pycharm-community
sudo snap install --classic telegram-desktop vlc curl deja-dup discord
sudo snap install --classic docker skype swi-prolog teams-for-linux whatsie
sudo snap install --classic zoom-client spotify cmake
sudo snap install bfg-repo-cleaner

# Avvio del servizio Tor
echo "Avvio del servizio Tor..."
sudo service tor start

# Esecuzione dello script per aggiornamenti personalizzati
echo "Esecuzione dello script per aggiornamenti personalizzati..."
sudo /home/kenobi/Documenti/GitHub/CodicePersonale/Ubuntu-Utility/SH-Personali/Aggiornamenti.sh

echo "Installazione completata!"

