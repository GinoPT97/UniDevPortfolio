#!/bin/bash
set -e

echo "Aggiornamento del sistema..."
sudo apt update && sudo apt upgrade -y

# Installa curl (se non è già presente)
sudo apt update
sudo apt install -y curl

echo "Installazione pacchetti: ca-certificates curl gnupg lsb-release software-properties-common wget apt-transport-https wireshark kate zram-config preload bluetooth bluez blueman flatpak git gparted default-jre openjdk-11-jdk openjdk-11-jre clamav clamtk postgresql-16 postgresql-client-16 postgresql-client-common postgresql-common codeblocks arduino vlc cmake deja-dup libnvidia-gl-535:i386 tor aptitude doxygen graphviz net-tools gdebi dos2unix openjfx ssmtp texlive-latex-base texlive-latex-extra git-lfs cryptsetup lvm2 exfatprogs nvtop synaptic stacer tlp cpufrequtils nvidia-prime build-essential libvips-dev power-profiles-daemon jest"
sudo apt install -y \
  ca-certificates curl gnupg lsb-release software-properties-common wget \
  apt-transport-https wireshark kate zram-config preload bluetooth bluez blueman \
  flatpak git gparted default-jre openjdk-11-jdk openjdk-11-jre clamav clamtk \
  postgresql-16 postgresql-client-16 postgresql-client-common postgresql-common \
  codeblocks arduino vlc cmake deja-dup libnvidia-gl-535:i386 tor \
  aptitude doxygen graphviz net-tools gdebi dos2unix openjfx ssmtp texlive-latex-base \
  texlive-latex-extra git-lfs cryptsetup lvm2 exfatprogs nvtop synaptic stacer tlp \
  cpufrequtils nvidia-prime build-essential libvips-dev power-profiles-daemon jest

sudo powerprofilesctl set balanced

pip install pytesseract opencv-python pandas easyocr fastapi uvicorn celery redis aioredis SQLAlchemy databases python-multipart python-bidi

# Se "cpufreq-set" è presente, viene eseguito; altrimenti viene stampato un messaggio
# command -v cpufreq-set > /dev/null && sudo cpufreq-set -g powersave || echo "cpufrequtils non trovato, salto la configurazione della CPU."

git lfs install

echo "Installazione di Node.js..."
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
sudo apt install -y nodejs
sudo npm install -g n
sudo n latest

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

echo "Installazione di pgAdmin 4..."
sudo apt update
sudo apt install curl ca-certificates gnupg
curl https://www.pgadmin.org/static/packages_pgadmin_org.pub | sudo gpg --dearmor -o /usr/share/keyrings/packages-pgadmin-org.gpg
sudo sh -c 'echo "deb [signed-by=/usr/share/keyrings/packages-pgadmin-org.gpg] https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/mantic pgadmin4 main" > /etc/apt/sources.list.d/pgadmin4.list'
sudo snap install pgadmin4
sudo apt update
sudo apt install pgadmin4 pgadmin4-desktop pgadmin4-web


echo "Configurazione della password PostgreSQL..."
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'admin';"

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

echo "Avvio del servizio Tor..."
sudo apt update
sudo apt install tor
sudo systemctl start tor

sudo apt install npm

echo "Installazione pacchetti npm..."
npm install \
  express mongoose pg cors dotenv helmet morgan compression uuid axios lodash validator dayjs \
  passport passport-local passport-google-oauth20 passport-facebook passport-apple passport-jwt passport-linkedin-oauth2 \
  jsonwebtoken bcrypt bcryptjs express-session apollo-server-express graphql graphql-request express-validator \
  @types/express @types/mongoose @types/cors @types/helmet @types/morgan @types/compression @types/uuid @types/axios @types/lodash @types/validator @types/dayjs \
  axios newsapi apollo-server-express graphql jsonwebtoken bcryptjs \
  passport passport-jwt passport-google-oauth20 passport-facebook passport-apple \
  firebase-admin firebase-functions passport passport-google-oauth20 passport-facebook passport-apple stripe @paypal/checkout-server-sdk axios dotenv winston pino jest supertest sequelize pg pg-hstore cors helmet express lodash moment firebase @angular/fire \
  @angular/core @angular/common @angular/forms @angular/router @angular/platform-browser @angular/platform-server \
  react react-dom @types/react @types/react-dom react-router-dom \
  webpack webpack-cli eslint bootstrap tailwindcss postcss autoprefixer express-validator winston helmet sequelize pg pg-hstore bull config jsonwebtoken redis swagger-jsdoc swagger-ui-express http-errors connect-redis express-session prom-client csurf express-rate-limit

npx tailwindcss init

# Scarica e configura il repository per Node.js (versione 16, ad esempio)
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -

# Installa Node.js (npm viene installato insieme)
sudo apt install -y nodejs

npm install --save-dev typescript ts-node @types/node @angular/cli

sudo apt remove --purge gnome-mahjongg gnome-mines gnome-sudoku -y

sudo apt install -y redis
sudo systemctl start redis
sudo systemctl enable redis

echo "Esecuzione aggiornamenti personalizzati..."
sudo /home/kenobi/Documenti/GitHub/UniDevPortfolio/Ubuntu-Utility/Aggiornamenti.sh

echo "Pulizia file temporanei..."
rm -f *.deb *.run

echo "Installazione completata!"