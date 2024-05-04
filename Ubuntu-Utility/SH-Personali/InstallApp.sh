#!/bin/bash

# Aggiornamento del sistema e installazione di wget, curl, e pacchetti di sistema
sudo apt update && sudo apt upgrade -y && sudo apt install wget curl deborphan zram-config preload flatpak git gparted gpart openjdk-11-jdk openjdk-11-jre clamav clamtk postgresql-15 postgresql-client-15 postgresql-client-common postgresql-common codeblocks gnome-boxes telegram-desktop vlc -y

# Aggiunta architettura i386 e installazione di librerie
sudo dpkg --add-architecture i386 && sudo apt update && sudo apt install libnvidia-gl-535:i386 -y

# Rimozione di Firefox e installazione di Google Chrome
#sudo apt-get remove --purge firefox && wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && sudo dpkg -i google-chrome-stable_current_amd64.deb

# Installazione di Eclipse, GitHub Desktop, Android Studio, Geany, pgAdmin 4, Tor Browser, SWI-Prolog, Visual Studio Code
sudo snap install --classic eclipse android-studio code swi-prolog
#sudo wget https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb && sudo dpkg -i GitHubDesktop-linux-2.8.1-linux2.deb
curl -fsS https://www.pgadmin.org/static/packages_pgadmin_org.pub | sudo gpg --dearmor -o /usr/share/keyrings/packages-pgadmin-org.gpg && sudo sh -c 'echo "deb [signed-by=/usr/share/keyrings/packages-pgadmin-org.gpg] https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/$(lsb_release -cs) pgadmin4 main" > /etc/apt/sources.list.d/pgadmin4.list && apt update' && sudo apt install pgadmin4 -y
sudo /usr/pgadmin4/bin/setup-web.sh
sudo snap install tor torbrowser-launcher && sudo systemctl enable tor

sudo snap install android-studio --classic
sudo snap install code
sudo snap install eclipse --classic
sudo snap install pycharm-community --classic
sudo snap install telegram-desktop
sudo snap install vlc

sudo /home/kenobi/Documenti/GitHub/CodicePersonale/Ubuntu-Utility/SH-Personali/Aggiornamenti.sh

echo "Installazione completata!"

