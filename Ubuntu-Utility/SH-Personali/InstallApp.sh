#!/bin/bash

# Aggiornamento del sistema
sudo apt update && sudo apt upgrade -y

# Installazione di pacchetti di sistema essenziali
sudo apt install wget curl deborphan kate zram-config preload flatpak git gparted gpart default-jre openjdk-11-jdk openjdk-11-jre clamav clamtk postgresql-16 postgresql-client-16 postgresql-client-common postgresql-common codeblocks gnome-boxes arduino vlc cmake deja-dup libnvidia-gl-535:i386 tor -y
sudo apt install postgresql postgresql-contrib curl ca-certificates -y

# Rimozione di Firefox, installazione di GitHub Desktop e installazione di Google Chrome
# Rimozione di Firefox
sudo apt-get remove --purge firefox
sudo snap remove firefox

# Installazione di Google Chrome
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
sudo dpkg -i google-chrome-stable_current_amd64.deb

# Installazione di GitHub Desktop
wget https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb
sudo dpkg -i GitHubDesktop-linux-2.8.1-linux2.deb

# Installazione di applicazioni tramite Snap
sudo snap install --classic code
sudo snap install openjdk
sudo snap install --classic android-studio
sudo snap install --classic eclipse
sudo snap install --classic pycharm-community
sudo snap install --classic telegram-desktop
sudo snap install --classic vlc
sudo snap install --classic curl
sudo snap install --classic deja-dup
sudo snap install --classic discord
sudo snap install --classic docker
sudo snap install --classic skype
sudo snap install --classic swi-prolog
sudo snap install --classic teams-for-linux
sudo snap install --classic whatsie
sudo snap install --classic zoom-client
sudo snap install --classic spotify
sudo snap install --classic cmake

# Configurazione di pgAdmin 4
curl -fsS https://www.pgadmin.org/static/packages_pgadmin_org.pub | sudo gpg --dearmor -o /usr/share/keyrings/packages-pgadmin-org.gpg
echo "deb [signed-by=/usr/share/keyrings/packages-pgadmin-org.gpg] https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/$(lsb_release -cs) pgadmin4 main" | sudo tee /etc/apt/sources.list.d/pgadmin4.list
sudo apt update && sudo apt install pgadmin4 -y
sudo /usr/pgadmin4/bin/setup-web.sh

# Configurazione della password dell'utente PostgreSQL
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'admin';"

#Installazione dello store basato su Flutter
#sudo snap install flutter --classic
#sudo snap remove snap-store
#sudo snap install snap-store --channel=preview/edge

# Esecuzione dello script per gli aggiornamenti personalizzati
sudo /home/kenobi/Documenti/GitHub/CodicePersonale/Ubuntu-Utility/SH-Personali/Aggiornamenti.sh

echo "Installazione completata!"
