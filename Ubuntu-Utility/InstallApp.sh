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
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
sudo apt install -y nodejs
sudo npm install -g n
sudo n latest
sudo apt-get update

# Installazione di Docker
echo "Installazione di Docker..."
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io
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
  texlive-latex-base \
  texlive-latex-extra

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
sudo apt update
sudo apt install -y pgadmin4 pgadmin4-desktop pgadmin4-web
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

# Estensioni GitHub
code --install-extension github.copilot --force \
  --install-extension github.copilot-chat --force \
  --install-extension github.remotehub --force \
  --install-extension github.vscode-pull-request-github --force

# Estensioni Azure
code --install-extension ms-azuretools.azure-dev --force \
  --install-extension ms-azuretools.vscode-azureappservice --force \
  --install-extension ms-azuretools.vscode-azurecontainerapps --force \
  --install-extension ms-azuretools.vscode-azurefunctions --force \
  --install-extension ms-azuretools.vscode-azureresourcegroups --force \
  --install-extension ms-azuretools.vscode-azurestaticwebapps --force \
  --install-extension ms-azuretools.vscode-azurestorage --force \
  --install-extension ms-azuretools.vscode-azurevirtualmachines --force \
  --install-extension ms-azuretools.vscode-bicep --force \
  --install-extension ms-azuretools.vscode-cosmosdb --force \
  --install-extension ms-azuretools.vscode-docker --force \
  --install-extension ms-azuretools.vscode-logicapps --force \
  --install-extension ms-vscode.azure-account --force \
  --install-extension ms-vscode.azure-repos --force \
  --install-extension ms-vscode.azurecli --force

# Estensioni per Docker e Container
code --install-extension ms-azuretools.vscode-docker --force \
  --install-extension ms-vscode-remote.remote-containers --force

# Estensioni Node.js e strumenti JavaScript
code --install-extension christian-kohler.npm-intellisense --force \
  --install-extension dbaeumer.vscode-eslint --force \
  --install-extension esbenp.prettier-vscode --force \
  --install-extension xabikos.javascriptsnippets --force \
  --install-extension ms-vscode.vscode-node-azure-pack --force \
  --install-extension ms-vscode.vscode-typescript-next --force

# Estensioni Remote Development
code --install-extension ms-vscode-remote.remote-ssh --force \
  --install-extension ms-vscode-remote.remote-ssh-edit --force \
  --install-extension ms-vscode-remote.remote-wsl --force \
  --install-extension ms-vscode-remote.vscode-remote-extensionpack --force

# Linguaggi e runtime per applicazioni web
code --install-extension ms-python.python --force \
  --install-extension ms-python.vscode-pylance --force \
  --install-extension ms-python.debugpy --force \
  --install-extension redhat.java --force

# Strumenti per server locali e debugging di applicazioni web
code --install-extension ritwickdey.liveserver --force \
  --install-extension vadimcn.vscode-lldb --force

# Strumenti di sviluppo e linting
code --install-extension eamodio.gitlens --force \
  --install-extension ms-ceintl.vscode-language-pack-it --force \
  --install-extension visualstudioexptteam.vscodeintellicode --force

# Estensioni per lo sviluppo in C/C++
code --install-extension ms-vscode.cpptools --force \
  --install-extension ms-vscode.cpptools-extension-pack --force \
  --install-extension ms-vscode.cpptools-themes --force \
  --install-extension twxs.cmake --force \
  --install-extension vadimcn.vscode-lldb --force

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

