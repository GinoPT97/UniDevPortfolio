#!/bin/bash
# =============================================================================
# setup_vm_test.sh
# Testa l'autoinstall.yaml di Ubuntu 25.10 in una VM VirtualBox.
# Al termine dell'installazione la VM si RIAVVIA (non si spegne),
# così puoi accedere e verificare manualmente o con verify_install.sh
#
# USO:
#   chmod +x setup_vm_test.sh
#   ./setup_vm_test.sh
#
# REQUISITI:
#   - autoinstall.yaml e verify_install.sh nella stessa cartella
#   - Connessione internet (~1.9GB per la ISO)
#   - ~25GB di spazio libero su disco
# =============================================================================
set -e

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; NC='\033[0m'
info()    { echo -e "${GREEN}[INFO]${NC} $1"; }
warning() { echo -e "${YELLOW}[WARN]${NC} $1"; }
error()   { echo -e "${RED}[ERROR]${NC} $1"; exit 1; }
step()    { echo -e "\n${CYAN}━━━ $1 ━━━${NC}"; }

# --- Configurazione ---
VM_NAME="Ubuntu2510_AutoinstallTest"
VM_RAM=4096
VM_DISK=20480
VM_CPUS=2
WORK_DIR="$HOME/vm-autoinstall-test"
UBUNTU_ISO_URL="https://releases.ubuntu.com/25.10/ubuntu-25.10-live-server-amd64.iso"
UBUNTU_ISO="ubuntu-25.10-live-server-amd64.iso"
SEED_ISO="seed.iso"
SCRIPT_DIR="$(dirname "$0")"
AUTOINSTALL_SRC="$SCRIPT_DIR/autoinstall.yaml"
VERIFY_SRC="$SCRIPT_DIR/verify_install.sh"

# =============================================================================
# 1. Verifica prerequisiti
# =============================================================================
step "1. Verifica prerequisiti"
[[ -f "$AUTOINSTALL_SRC" ]]  || error "autoinstall.yaml non trovato in $SCRIPT_DIR"
[[ -f "$VERIFY_SRC" ]]       || warning "verify_install.sh non trovato — la VM si avvierà ma senza script di verifica precaricato"
[[ $(id -u) -ne 0 ]]         || error "Non eseguire come root"

# =============================================================================
# 2. Installazione dipendenze
# =============================================================================
step "2. Installazione VirtualBox e xorriso"
sudo apt update -q
sudo apt install -y virtualbox xorriso wget curl

if ! groups "$USER" | grep -q vboxusers; then
  sudo usermod -aG vboxusers "$USER"
  warning "Aggiunto $USER al gruppo vboxusers — potrebbe servire un logout/login"
fi

# =============================================================================
# 3. Cartella di lavoro
# =============================================================================
step "3. Preparazione cartella $WORK_DIR"
mkdir -p "$WORK_DIR"
cd "$WORK_DIR"

# =============================================================================
# 4. Download ISO
# =============================================================================
step "4. Download Ubuntu 25.10 Server"
if [[ -f "$UBUNTU_ISO" ]]; then
  warning "ISO già presente, salto il download."
else
  info "Download in corso (~1.9GB)..."
  wget -q --show-progress "$UBUNTU_ISO_URL" -O "$UBUNTU_ISO"
fi

# =============================================================================
# 5. Seed ISO
# Aggiunge "shutdown: reboot" all'autoinstall così la VM si riavvia
# invece di spegnersi — al riavvio potrai fare login e verificare.
# =============================================================================
step "5. Creazione seed ISO"
mkdir -p seed_files

# Aggiunge la direttiva reboot se non già presente
if grep -q "shutdown:" "$AUTOINSTALL_SRC"; then
  cp "$AUTOINSTALL_SRC" seed_files/user-data
else
  awk '/version: 1/{print; print "  shutdown: reboot"; next}1' "$AUTOINSTALL_SRC" \
    > seed_files/user-data
  info "Aggiunta direttiva 'shutdown: reboot' all'user-data"
fi

# Copia verify_install.sh dentro la seed ISO se disponibile
if [[ -f "$VERIFY_SRC" ]]; then
  cp "$VERIFY_SRC" seed_files/verify_install.sh
  info "verify_install.sh incluso nella seed ISO"
fi

touch seed_files/meta-data

xorriso -as mkisofs \
  -volid "cidata" \
  -joliet -rock \
  -o "$SEED_ISO" \
  seed_files/

info "Seed ISO creata: $WORK_DIR/$SEED_ISO"

# =============================================================================
# 6. Creazione VM
# =============================================================================
step "6. Creazione VM VirtualBox '$VM_NAME'"

if VBoxManage list vms | grep -q "\"$VM_NAME\""; then
  warning "VM già esistente, la rimuovo..."
  VBoxManage unregistervm "$VM_NAME" --delete 2>/dev/null || true
fi

VBoxManage createvm --name "$VM_NAME" --ostype Ubuntu_64 --register

VBoxManage modifyvm "$VM_NAME" \
  --memory $VM_RAM \
  --cpus $VM_CPUS \
  --vram 128 \
  --graphicscontroller vmsvga \
  --boot1 dvd --boot2 disk \
  --nic1 nat \
  --audio-driver none

VBoxManage createmedium disk \
  --filename "$WORK_DIR/$VM_NAME.vdi" \
  --size $VM_DISK \
  --format VDI

# Controller SATA
VBoxManage storagectl "$VM_NAME" --name "SATA" --add sata \
  --controller IntelAhci --portcount 4

VBoxManage storageattach "$VM_NAME" --storagectl "SATA" \
  --port 0 --device 0 --type hdd \
  --medium "$WORK_DIR/$VM_NAME.vdi"

VBoxManage storageattach "$VM_NAME" --storagectl "SATA" \
  --port 1 --device 0 --type dvddrive \
  --medium "$WORK_DIR/$UBUNTU_ISO"

# Controller IDE per la seed ISO
VBoxManage storagectl "$VM_NAME" --name "IDE" --add ide

VBoxManage storageattach "$VM_NAME" --storagectl "IDE" \
  --port 0 --device 0 --type dvddrive \
  --medium "$WORK_DIR/$SEED_ISO"

VBoxManage modifyvm "$VM_NAME" --boot-args "autoinstall ds=nocloud"

# =============================================================================
# 7. Avvio VM
# =============================================================================
step "7. Avvio VM"
echo ""
echo -e "${YELLOW}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${YELLOW}║  L'installazione partirà automaticamente.                ║${NC}"
echo -e "${YELLOW}║  Al termine la VM si RIAVVIERÀ (non si spegnerà).        ║${NC}"
echo -e "${YELLOW}║                                                          ║${NC}"
echo -e "${YELLOW}║  Dopo il riavvio:                                        ║${NC}"
echo -e "${YELLOW}║  1. Fai login con le credenziali scelte durante          ║${NC}"
echo -e "${YELLOW}║     l'installazione (sezione identity)                   ║${NC}"
echo -e "${YELLOW}║  2. Copia verify_install.sh dalla seed ISO:              ║${NC}"
echo -e "${YELLOW}║     sudo mount /dev/sr1 /mnt                             ║${NC}"
echo -e "${YELLOW}║     cp /mnt/verify_install.sh ~/                         ║${NC}"
echo -e "${YELLOW}║  3. Eseguilo:                                            ║${NC}"
echo -e "${YELLOW}║     chmod +x ~/verify_install.sh && ~/verify_install.sh  ║${NC}"
echo -e "${YELLOW}║  4. Controlla il report: ~/install_report.txt            ║${NC}"
echo -e "${YELLOW}╚══════════════════════════════════════════════════════════╝${NC}"
echo ""

VBoxManage startvm "$VM_NAME" --type gui

info "VM avviata. Segui il progresso nella finestra VirtualBox."
echo ""
echo "Per eliminare tutto al termine dei test:"
echo "  VBoxManage unregistervm '$VM_NAME' --delete"
echo "  rm -rf $WORK_DIR"
