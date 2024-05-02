#!/bin/bash

# Aggiornamento del sistema
sudo apt-get update && sudo apt-get upgrade -y

# Installazione dei pacchetti con apt-get
sudo apt-get install -y wget curl libnvidia-gl-535:i386 openjdk-11-jdk openjdk-11-jre deborphan zram-config preload clamav clamtk git gparted geany postgresql-15 postgresql-client-15 postgresql-client-common postgresql-common codeblocks gnome-boxes

# Installazione dei pacchetti con snap
sudo snap install curl cmake flutter --classic
sudo snap refresh snap-store --channel=preview/edge
sudo snap install snapcraft --classic
sudo snap install gedit eclipse android-studio tor torbrowser-launcher swi-prolog code telegram-desktop

# Rimozione di Firefox
sudo snap remove --purge firefox

# Installazione di GitHub Desktop
sudo wget https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb -O GitHubDesktop.deb
sudo dpkg -i GitHubDesktop.deb
sudo rm GitHubDesktop.deb

# Installazione di Google Chrome
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb -O google-chrome.deb
sudo dpkg -i google-chrome.deb
sudo rm google-chrome.deb

# Configurazione della password dell'utente PostgreSQL
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'admin';"

# Installazione di pgAdmin 4
#curl -fsS https://www.pgadmin.org/static/packages_pgadmin_org.pub | sudo gpg --dearmor -o /usr/share/keyrings/packages-pgadmin-org.gpg
#sudo sh -c 'echo "deb [signed-by=/usr/share/keyrings/packages-pgadmin-org.gpg] https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/$(lsb_release -cs) pgadmin4 main" > /etc/apt/sources.list.d/pgadmin4.list'
#sudo apt update
#sudo apt install pgadmin4 -y
#sudo /usr/pgadmin4/bin/setup-web.sh

# Avvio e abilitazione di Tor
sudo systemctl start tor.service
sudo systemctl enable tor

echo "Installazione completata!"
