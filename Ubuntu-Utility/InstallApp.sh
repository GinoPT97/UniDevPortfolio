#!/bin/bash

# Funzione per installare pacchetti
install_packages() {
  echo "Installazione di pacchetti: $*"
  sudo apt install -y "$@" || { echo "Errore durante l'installazione dei pacchetti: $*"; exit 1; }
}

# Funzione per installare estensioni di Visual Studio Code
install_vscode_extensions() {
  echo "Installazione di estensioni per VS Code..."
  for extension in "$@"; do
    code --install-extension "$extension" --force || { echo "Errore durante l'installazione dell'estensione: $extension"; exit 1; }
  done
}

# Aggiornamento del sistema
echo "Aggiornamento del sistema..."
sudo apt update && sudo apt upgrade -y || { echo "Errore durante l'aggiornamento del sistema"; exit 1; }

# Installazione di pacchetti generali
install_packages \
  ca-certificates curl gnupg lsb-release software-properties-common wget deborphan \
  apt-transport-https wireshark kate zram-config preload bluetooth bluez blueman \
  flatpak git gparted default-jre openjdk-11-jdk openjdk-11-jre clamav clamtk \
  postgresql-16 postgresql-client-16 postgresql-client-common postgresql-common \
  codeblocks gnome-boxes arduino vlc cmake deja-dup libnvidia-gl-535:i386 tor \
  aptitude doxygen graphviz net-tools gdebi dos2unix openjfx ssmtp texlive-latex-base \
  texlive-latex-extra git-lfs cryptsetup lvm2 exfatprogs nvtop synaptic stacer tlp \
  cpufrequtils nvidia-prime build-essential libvips-dev power-profiles-daemon

sudo powerprofilesctl set balanced

if command -v cpufreq-set &> /dev/null; then
  sudo cpufreq-set -g powersave
else
  echo "cpufrequtils non è installato, saltando la configurazione della frequenza della CPU"
fi

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

# Installazione e configurazione di NordVPN
echo "Installazione e configurazione di NordVPN..."
sh <(wget -qO - https://downloads.nordcdn.com/apps/linux/install.sh)
nordvpn connect
nordvpn set autoconnect on

# Installazione di pgAdmin 4
echo "Installazione di pgAdmin 4..."
echo "deb [signed-by=/usr/share/keyrings/packages-pgadmin-org.gpg] https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/$(lsb_release -cs) pgadmin4 main" | sudo tee /etc/apt/sources.list.d/pgadmin4.list
sudo apt update
install_packages pgadmin4 pgadmin4-desktop pgadmin4-web
sudo /usr/pgadmin4/bin/setup-web.sh || { echo "Errore durante la configurazione di pgAdmin 4"; exit 1; }

# Configurazione della password dell'utente PostgreSQL
echo "Configurazione della password dell'utente PostgreSQL..."
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'admin';"

# Installazione di Google Chrome
echo "Installazione di Google Chrome..."
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
sudo dpkg -i google-chrome-stable_current_amd64.deb
sudo apt-get install -f -y
rm -f google-chrome-stable_current_amd64.deb

# Installazione di GitHub Desktop
echo "Installazione di GitHub Desktop..."
wget https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb
sudo dpkg -i GitHubDesktop-linux-2.8.1-linux2.deb
sudo apt-get install -f -y
rm -f GitHubDesktop-linux-2.8.1-linux2.deb

# Installazione di estensioni per VS Code
install_vscode_extensions \
  github.copilot github.copilot-chat github.remotehub github.vscode-pull-request-github \
  ms-azuretools.azure-dev ms-azuretools.vscode-azureappservice ms-azuretools.vscode-docker \
  christian-kohler.npm-intellisense dbaeumer.vscode-eslint esbenp.prettier-vscode \
  ms-python.python ms-vscode.cpptools eamodio.gitlens ritwickdey.LiveServer \
  amazonwebservices.aws-toolkit-vscode ms-azuretools.vscode-azurestaticwebapps \
  netlify.netlify-vscode

# Imposta la lingua italiana in Visual Studio Code
echo '{"locale":"it"}' > ~/.config/Code/User/locale.json
code --locale=it

# Installazione di applicazioni tramite Snap
echo "Installazione di applicazioni tramite Snap..."
sudo snap install dbeaver-ce openjdk --classic
sudo snap install --classic code android-studio eclipse pycharm-community

# Avvio del servizio Tor
echo "Avvio del servizio Tor..."
sudo service tor start || { echo "Errore durante l'avvio del servizio Tor"; exit 1; }

echo "Installazione di pacchetti npm..."

npm install \
  express mongoose pg cors dotenv helmet morgan compression uuid axios lodash validator dayjs \
  passport passport-local passport-google-oauth20 passport-facebook passport-apple passport-jwt passport-linkedin-oauth2 \
  jsonwebtoken bcrypt bcryptjs express-session apollo-server-express graphql graphql-request express-validator \
  @types/express @types/mongoose @types/cors @types/helmet @types/morgan @types/compression @types/uuid @types/axios @types/lodash @types/validator @types/dayjs \
  axios newsapi apollo-server-express graphql jsonwebtoken bcryptjs \
  passport passport-jwt passport-google-oauth20 passport-facebook passport-apple
  firebase-admin firebase-functions passport passport-google-oauth20 passport-facebook passport-apple stripe @paypal/checkout-server-sdk axios dotenv winston pino jest supertest sequelize pg pg-hstore cors helmet express lodash moment firebase @angular/fire



npm install --save-dev \
  typescript ts-node @types/node @angular/cli

pip install fastapi uvicorn celery redis aioredis SQLAlchemy databases python-multipart

sudo apt install redis
sudo systemctl start redis
sudo systemctl enable redis

# Esecuzione dello script per aggiornamenti personalizzati
echo "Esecuzione dello script per aggiornamenti personalizzati..."
sudo /home/kenobi/Documenti/GitHub/UniDevPortfolio/Ubuntu-Utility/Aggiornamenti.sh

# Rimozione di file residui .deb e file scaricati
echo "Rimozione di file residui e temporanei..."
rm -f *.deb *.run

echo "Installazione completata!"
