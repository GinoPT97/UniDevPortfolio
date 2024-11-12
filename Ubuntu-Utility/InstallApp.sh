#!/bin/bash

# Aggiornamento del sistema
echo "Aggiornamento del sistema..."
sudo apt update && sudo apt upgrade -y

# Installazione di pacchetti necessari per Docker e Node.js
echo "Installazione di pacchetti necessari..."
sudo apt install -y \
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
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash - && \
sudo apt install -y nodejs && \
sudo npm install -g n && \
sudo n latest && \
sudo apt-get update

# Installazione di Docker
echo "Installazione di Docker..."
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update && \
sudo apt install -y docker-ce docker-ce-cli containerd.io && \
sudo systemctl status docker

# Installazione di pacchetti di sistema essenziali
echo "Installazione di pacchetti di sistema essenziali..."
sudo apt install -y \
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
  texlive-latex-base

sudo apt install git-lfs
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
sudo apt update && \
sudo apt install -y pgadmin4 pgadmin4-desktop pgadmin4-web && \
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

# Installazione estensioni di Visual Studio Code
code --install-extension GitHub.copilot \
  --install-extension GitHub.vscode-pull-request-github \
  --install-extension ms-vscode-remote.remote-containers \
  --install-extension redhat.java \
  --install-extension vscjava.vscode-java-pack \
  --install-extension ms-python.python \
  --install-extension ms-python.vscode-pylance \
  --install-extension ms-vscode.cpptools \
  --install-extension eamodio.gitlens \
  --install-extension ms-azuretools.vscode-docker \
  --install-extension esbenp.prettier-vscode \
  --install-extension dbaeumer.vscode-eslint \
  --install-extension ritwickdey.LiveServer \
  --install-extension ms-vscode.vscode-typescript-next \
  --install-extension xabikos.JavaScriptSnippets \
  --install-extension visualstudioexptteam.vscodeintellicode \
  --install-extension ms-vscode-remote.remote-ssh \
  --install-extension ms-vscode-remote.remote-ssh-edit \
  --install-extension ms-vscode-remote.remote-wsl \
  --install-extension ms-vscode-remote.vscode-remote-extensionpack \
  --install-extension ms-vscode.vscode-node-azure-pack \
  --install-extension ms-vscode.azure-account \
  --install-extension ms-vscode.azurecli \
  --install-extension ms-azuretools.vscode-azurefunctions \
  --install-extension ms-azuretools.vscode-azureresourcegroups \
  --install-extension ms-azuretools.vscode-azurestorage \
  --install-extension ms-azuretools.vscode-logicapps \
  --install-extension ms-azuretools.vscode-cosmosdb \
  --install-extension ms-azuretools.vscode-appservice \
  --install-extension ms-azuretools.vscode-bicep \
  --install-extension ms-vscode.azure-repos \
  --install-extension ms-ceintl.vscode-language-pack-it

# Attivazione della lingua italiana in Visual Studio Code
echo '{"locale":"it"}' > ~/.config/Code/User/locale.json

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

