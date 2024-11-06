#!/bin/bash

set -e

# Install snapd if not installed
command -v snapd &> /dev/null || sudo apt install -y snapd

# Uninstall unused Flatpak packages
flatpak uninstall --unused -y

# Reload system daemons and install proprietary drivers
sudo systemctl daemon-reload
sudo ubuntu-drivers autoinstall

# Configure APT packages and update
sudo dpkg --configure -a
sudo apt update && sudo apt upgrade -y && sudo apt dist-upgrade -y

# Unblock wifi suspension
sudo rfkill unblock wifi
sudo rfkill unblock all

# Clean unnecessary packages and APT cache
sudo apt autoremove --purge -y
sudo apt clean

# Update Snap packages and enable firewall
sudo snap refresh
sudo ufw enable

# Restart snapd and finish
sudo systemctl restart snapd
echo "Aggiornamenti completati e Ubuntu Software riavviato!"


