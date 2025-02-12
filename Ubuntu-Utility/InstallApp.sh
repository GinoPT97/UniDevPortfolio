#!/bin/bash

# Funzione per installare pacchetti
install_packages() {
  echo "Installazione pacchetti: $*"
  sudo apt install -y "$@" || { echo "Errore durante l'installazione dei pacchetti: $*"; exit 1; }
}

# Funzione per installare estensioni di Visual Studio Code
install_vscode_extensions() {
  echo "Installazione estensioni per VS Code..."
  for extension in "$@"; do
    code --install-extension "$extension" --force || { echo "Errore nell'estensione: $extension"; exit 1; }
  done
}

# Funzione per installazione sicura di pacchetti .deb
install_deb_package() {
  local url=$1
  local pkg_name=$(basename "$url")
  wget -q "$url" -O "$pkg_name"
  sudo dpkg -i "$pkg_name"
  sudo apt-get install -f -y
  rm -f "$pkg_name"
}

# Funzione per eseguire e controllare comandi
run_command() {
  "$@" || { echo "Errore nell'esecuzione di: $*"; exit 1; }
}

# Aggiornamento del sistema
echo "Aggiornamento del sistema..."
run_command sudo apt update && sudo apt upgrade -y

# Installazione pacchetti generali
install_packages \
  ca-certificates curl gnupg lsb-release software-properties-common wget deborphan \
  apt-transport-https wireshark kate zram-config preload bluetooth bluez blueman \
  flatpak git gparted default-jre openjdk-11-jdk openjdk-11-jre clamav clamtk \
  postgresql-16 postgresql-client-16 postgresql-client-common postgresql-common \
  codeblocks arduino vlc cmake deja-dup libnvidia-gl-535:i386 tor \
  aptitude doxygen graphviz net-tools gdebi dos2unix openjfx ssmtp texlive-latex-base \
  texlive-latex-extra git-lfs cryptsetup lvm2 exfatprogs nvtop synaptic stacer tlp \
  cpufrequtils nvidia-prime build-essential libvips-dev power-profiles-daemon jest

# Installazione pacchetti Python
pip install pytesseract opencv-python pandas easyocr fastapi uvicorn celery redis aioredis SQLAlchemy databases python-multipart python-bidi || { echo "Errore durante l'installazione dei pacchetti Python"; exit 1; }

# Configurazione Git LFS
git lfs install

# Installazione Node.js e NPM
echo "Installazione di Node.js..."
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
install_packages nodejs
sudo npm install -g n || { echo "Errore durante l'installazione di npm"; exit 1; }
sudo n latest || { echo "Errore durante l'aggiornamento di Node.js"; exit 1; }

# Installazione Docker
echo "Installazione di Docker..."
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
run_command sudo apt update
install_packages docker-ce docker-ce-cli containerd.io
run_command sudo systemctl start docker
run_command sudo systemctl enable docker

# Installazione pgAdmin 4
echo "Installazione di pgAdmin 4..."
echo "deb [signed-by=/usr/share/keyrings/packages-pgadmin-org.gpg] https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/$(lsb_release -cs) pgadmin4 main" | sudo tee /etc/apt/sources.list.d/pgadmin4.list
run_command sudo apt update
install_packages pgadmin4 pgadmin4-desktop pgadmin4-web
run_command sudo /usr/pgadmin4/bin/setup-web.sh

# Configurazione della password PostgreSQL
echo "Configurazione della password PostgreSQL..."
run_command sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'admin';"

# Installazione di Google Chrome e GitHub Desktop
install_deb_package "https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb"
install_deb_package "https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb"

# Installazione estensioni VS Code
install_vscode_extensions \
  github.copilot github.copilot-chat github.remotehub github.vscode-pull-request-github \
  ms-azuretools.azure-dev ms-azuretools.vscode-azureappservice ms-azuretools.vscode-docker \
  christian-kohler.npm-intellisense dbaeumer.vscode-eslint esbenp.prettier-vscode \
  ms-python.python ms-vscode.cpptools eamodio.gitlens ritwickdey.LiveServer \
  amazonwebservices.aws-toolkit-vscode ms-azuretools.vscode-azurestaticwebapps \
  netlify.netlify-vscode dsznajder.es7-react-js-snippets steoates.autoimport coenraads.bracket-pair-colorizer-2

# Impostazione lingua italiana per VS Code
echo '{"locale":"it"}' > ~/.config/Code/User/locale.json
code --locale=it

# Installazione applicazioni tramite Snap
echo "Installazione applicazioni Snap..."
sudo snap install dbeaver-ce openjdk --classic
sudo snap install --classic code android-studio eclipse

# Avvio del servizio Tor
echo "Avvio del servizio Tor..."
run_command sudo service tor start

# Installazione pacchetti npm
npm install express mongoose pg cors dotenv helmet morgan compression uuid axios lodash validator dayjs \
  passport passport-local passport-google-oauth20 passport-facebook passport-apple passport-jwt passport-linkedin-oauth2 \
  jsonwebtoken bcrypt bcryptjs express-session apollo-server-express graphql graphql-request express-validator \
  @types/express @types/mongoose @types/cors @types/helmet @types/morgan @types/compression @types/uuid @types/axios @types/lodash @types/validator @types/dayjs \
  axios newsapi apollo-server-express graphql jsonwebtoken bcryptjs passport passport-jwt passport-google-oauth20 passport-facebook passport-apple \
  firebase-admin firebase-functions stripe @paypal/checkout-server-sdk

npx tailwindcss init || { echo "Errore durante l'inizializzazione di TailwindCSS"; exit 1; }

npm install --save-dev typescript ts-node @types/node @angular/cli

# Rimozione giochi preinstallati
sudo apt remove --purge gnome-mahjongg gnome-mines gnome-sudoku -y

# Installazione e avvio Redis
install_packages redis
run_command sudo systemctl start redis
run_command sudo systemctl enable redis

# Pulizia file temporanei
echo "Pulizia file temporanei..."
rm -f *.deb *.run

echo "Installazione completata!"
