#!/bin/bash
# =============================================================================
# setup_vm_test.sh
# Testa l'autoinstall.yaml di Ubuntu 25.10 in una VM VirtualBox
#
# USO:
#   chmod +x setup_vm_test.sh
#   ./setup_vm_test.sh
#
# REQUISITI:
#   - Il file autoinstall.yaml deve essere nella stessa cartella di questo script
#   - Connessione internet (~2GB download per la ISO server)
#   - ~25GB di spazio libero su disco
# =============================================================================
set -e

# --- Colori per output leggibile ---
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; NC='\033[0m'
info()    { echo -e "${GREEN}[INFO]${NC} $1"; }
warning() { echo -e "${YELLOW}[WARN]${NC} $1"; }
error()   { echo -e "${RED}[ERROR]${NC} $1"; exit 1; }

# --- Configurazione ---
VM_NAME="Ubuntu2510_AutoinstallTest"
VM_RAM=4096          # MB (aumenta se hai più RAM disponibile)
VM_DISK=20480        # MB (20GB per la VM)
VM_CPUS=2
WORK_DIR="$HOME/vm-autoinstall-test"
UBUNTU_ISO_URL="https://releases.ubuntu.com/25.10/ubuntu-25.10-live-server-amd64.iso"
UBUNTU_ISO="ubuntu-25.10-live-server-amd64.iso"
SEED_ISO="seed.iso"
AUTOINSTALL_SRC="$(dirname "$0")/autoinstall.yaml"

# =============================================================================
# 1. Verifica prerequisiti
# =============================================================================
info "Verifica prerequisiti..."

[[ -f "$AUTOINSTALL_SRC" ]] || error "autoinstall.yaml non trovato in $(dirname "$0")"
[[ $(id -u) -ne 0 ]]        || error "Non eseguire questo script come root"

# =============================================================================
# 2. Installazione dipendenze
# =============================================================================
info "Installazione VirtualBox e xorriso..."
sudo apt update -q
sudo apt install -y virtualbox xorriso wget curl

# Aggiunge l'utente al gruppo vboxusers (effettivo al prossimo login)
if ! groups "$USER" | grep -q vboxusers; then
  sudo usermod -aG vboxusers "$USER"
  warning "Aggiunto $USER al gruppo vboxusers — potrebbe servire un logout/login per effetto immediato"
fi

# =============================================================================
# 3. Preparazione cartella di lavoro
# =============================================================================
info "Preparazione cartella di lavoro in $WORK_DIR..."
mkdir -p "$WORK_DIR"
cd "$WORK_DIR"

# =============================================================================
# 4. Download ISO Ubuntu 25.10 Server
# =============================================================================
if [[ -f "$UBUNTU_ISO" ]]; then
  warning "ISO già presente, salto il download."
else
  info "Download Ubuntu 25.10 Server (~1.9GB)..."
  wget -q --show-progress "$UBUNTU_ISO_URL" -O "$UBUNTU_ISO"
fi

# =============================================================================
# 5. Creazione Seed ISO (contiene l'autoinstall)
# =============================================================================
# Il subiquity installer cerca i file user-data e meta-data
# nella sorgente "nocloud" montata come seconda ISO.
info "Creazione seed ISO con l'autoinstall..."

mkdir -p seed_files

# user-data = il nostro autoinstall.yaml
cp "$AUTOINSTALL_SRC" seed_files/user-data

# meta-data deve esistere ma può essere vuoto
touch seed_files/meta-data

# Crea la ISO seed
xorriso -as mkisofs \
  -volid "cidata" \
  -joliet -rock \
  -o "$SEED_ISO" \
  seed_files/

info "Seed ISO creata: $WORK_DIR/$SEED_ISO"

# =============================================================================
# 6. Creazione e configurazione VM VirtualBox
# =============================================================================
info "Creazione VM VirtualBox '$VM_NAME'..."

# Rimuove la VM se esiste già (utile per ri-testare)
if VBoxManage list vms | grep -q "\"$VM_NAME\""; then
  warning "VM '$VM_NAME' già esistente, la rimuovo per ripartire da zero..."
  VBoxManage unregistervm "$VM_NAME" --delete 2>/dev/null || true
fi

# Crea la VM
VBoxManage createvm \
  --name "$VM_NAME" \
  --ostype Ubuntu_64 \
  --register

# Parametri hardware
VBoxManage modifyvm "$VM_NAME" \
  --memory $VM_RAM \
  --cpus $VM_CPUS \
  --vram 128 \
  --graphicscontroller vmsvga \
  --boot1 dvd --boot2 disk \
  --nic1 nat \
  --audio-driver none

# Disco virtuale
VBoxManage createmedium disk \
  --filename "$WORK_DIR/$VM_NAME.vdi" \
  --size $VM_DISK \
  --format VDI

# Controller SATA (disco + dvd Ubuntu ISO)
VBoxManage storagectl "$VM_NAME" \
  --name "SATA" \
  --add sata \
  --controller IntelAhci \
  --portcount 4

VBoxManage storageattach "$VM_NAME" \
  --storagectl "SATA" \
  --port 0 --device 0 \
  --type hdd \
  --medium "$WORK_DIR/$VM_NAME.vdi"

VBoxManage storageattach "$VM_NAME" \
  --storagectl "SATA" \
  --port 1 --device 0 \
  --type dvddrive \
  --medium "$WORK_DIR/$UBUNTU_ISO"

# Controller IDE (seed ISO)
VBoxManage storagectl "$VM_NAME" \
  --name "IDE" \
  --add ide

VBoxManage storageattach "$VM_NAME" \
  --storagectl "IDE" \
  --port 0 --device 0 \
  --type dvddrive \
  --medium "$WORK_DIR/$SEED_ISO"

# Parametro di boot per attivare l'autoinstall nocloud
VBoxManage modifyvm "$VM_NAME" \
  --boot-args "autoinstall ds=nocloud"

# =============================================================================
# 7. Avvio VM
# =============================================================================
info "Avvio della VM..."
echo ""
echo -e "${YELLOW}============================================================${NC}"
echo -e "${YELLOW}  La VM si avvierà e l'installazione partirà in automatico.${NC}"
echo -e "${YELLOW}  Puoi seguire il progresso nella finestra VirtualBox.${NC}"
echo -e "${YELLOW}  Al termine, la VM si spegnerà da sola.${NC}"
echo -e "${YELLOW}============================================================${NC}"
echo ""

VBoxManage startvm "$VM_NAME" --type gui

info "Script completato. VM avviata."
echo ""
echo "Cartella di lavoro: $WORK_DIR"
echo "Per rieseguire il test (dalla stessa cartella):"
echo "  VBoxManage unregistervm '$VM_NAME' --delete && ./setup_vm_test.sh"
