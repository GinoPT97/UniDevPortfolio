#!/bin/bash

# Aggiornamento del sistema e installazione di wget
sudo apt update
sudo apt upgrade -y
sudo apt install wget -y
sudo snap install curl 

# Pacchetti per uso vario
sudo dpkg --add-architecture i386
sudo apt update
sudo apt install libnvidia-gl-535:i386

#Installazione dei jdk e jre
sudo apt install openjdk-11-jdk openjdk-11-jre -y

#Installazione di deborphan
sudo apt install deborphan -y

# Installazione di Zram
sudo apt install zram-config -y

# Installazione di Preload
sudo apt install preload -y

#Installazione di wine per giochi e cose varie Windows
sudo apt-get install wine -y
sudo apt-get install winetricks -y

#Installazione di ClamAV 
sudo apt-get install clamav -y
sudo systemctl enable clamav-freshclam
sudo apt-get install clamtk -y

#Installazione dello store basato su Flutter
sudo snap install flutter --classic
sudo snap remove snap-store
sudo snap install snap-store --channel=preview/edge

#Installa cmake
sudo snap install cmake --classic

# Installazione di Flatpak
sudo apt install flatpak -y

# Installazione di Git
sudo apt install git -y

#Installazione di gedit
sudo snap install gedit -y

#Installa tor e lo attiva all'avvio del sistema
sudo apt install tor -y
sudo systemctl enable tor

#Installazione di gparted
sudo apt install gparted -y
sudo apt-get install gpart -y

# Installazione di Eclipse
sudo snap install --classic eclipse

#Installazione di netbeans
sudo snap install netbeans --classic

# Installazione di GitHub Desktop
sudo wget https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb
sudo dpkg -i GitHubDesktop-linux-2.8.1-linux2.deb

# Installazione di Android Studio
sudo snap install android-studio --classic

# Installazione di Geany
sudo apt install geany -y

# Installazione di Google Chrome
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
sudo dpkg -i google-chrome-stable_current_amd64.deb

# Installazione di PostgreSQL
sudo apt install postgresql-15 postgresql-client-15 postgresql-client-common postgresql-common -y

# Configurazione della password dell'utente PostgreSQL
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'admin';"

# Installazione di pgAdmin 4
curl -fsS https://www.pgadmin.org/static/packages_pgadmin_org.pub | sudo gpg --dearmor -o /usr/share/keyrings/packages-pgadmin-org.gpg
sudo sh -c 'echo "deb [signed-by=/usr/share/keyrings/packages-pgadmin-org.gpg] https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/$(lsb_release -cs) pgadmin4 main" > /etc/apt/sources.list.d/pgadmin4.list && apt update'
sudo apt install pgadmin4 -y
sudo /usr/pgadmin4/bin/setup-web.sh

# Installazione di Tor Browser
sudo snap install tor
sudo systemctl start tor.service
sudo systemctl enable tor
sudo apt-get install torbrowser-launcher

# Installazione di swi-prolog con snap
sudo snap install swi-prolog

# Installazione di Code::Blocks
sudo apt install codeblocks -y

#installazione di visual studio code
sudo snap install --classic code

# Installazione di gnome-boxes
sudo apt install gnome-boxes -y

echo "Installazione completata!"
