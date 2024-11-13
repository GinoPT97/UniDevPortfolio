#!/bin/bash

log_info() {
  echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# Verifica connessione di rete
log_info "Verificando connessione di rete..."
ping -c 1 google.com

# Disinstallazione dei pacchetti Flatpak non utilizzati
log_info "Disinstallazione dei pacchetti Flatpak non utilizzati..."
flatpak uninstall --unused -y

# Aggiornamento dei pacchetti APT
log_info "Configurando e aggiornando pacchetti APT..."
sudo dpkg --configure -a
sudo apt-get update -y
sudo apt-get full-upgrade -y
log_info "Aggiornamento dei pacchetti APT completato."

# Aggiornamento dei pacchetti Snap
log_info "Aggiornando pacchetti Snap..."
sudo snap refresh
log_info "Aggiornamento dei pacchetti Snap completato."

# Sblocco del wifi e delle interfacce di rete
log_info "Sbloccando il wifi e tutte le interfacce..."
sudo rfkill unblock wifi
sudo rfkill unblock all

# Pulizia dei pacchetti inutilizzati e cache APT
log_info "Rimuovendo pacchetti inutilizzati..."
sudo apt-get autoremove --purge -y
sudo apt-get clean

# Abilitazione del firewall
log_info "Abilitando il firewall..."
sudo ufw status | grep -q "active" || sudo ufw enable

# Riavvio di snapd
log_info "Riavviando snapd..."
sudo systemctl restart snapd

# Aggiornamento dei driver hardware
log_info "Aggiornamento dei driver hardware..."
sudo ubuntu-drivers autoinstall

# Esecuzione di npm audit fix --force
log_info "Esecuzione di npm audit fix --force..."
npm audit fix --force

# Aggiornamento di pip
log_info "Aggiornamento di pip..."
pip install --upgrade pip

# Aggiornamento degli ambienti virtuali pip
log_info "Aggiornamento degli ambienti virtuali pip..."
for venv in $(find ~ -name "venv" -type d); do
  source $venv/bin/activate
  pip install --upgrade pip
  deactivate
done

# Aggiornamento degli ambienti virtuali conda
log_info "Aggiornamento degli ambienti virtuali conda..."
conda update conda -y
for env in $(conda env list | awk '{print $1}' | grep -v "#"); do
  conda update --name $env --all -y
done

# Aggiornamento degli ambienti virtuali node.js
log_info "Aggiornamento degli ambienti virtuali node.js..."
for nvm_dir in $(find ~ -name ".nvm" -type d); do
  source $nvm_dir/nvm.sh
  nvm install node --reinstall-packages-from=node
done

log_info "Aggiornamenti completati e Ubuntu Software riavviato!"
