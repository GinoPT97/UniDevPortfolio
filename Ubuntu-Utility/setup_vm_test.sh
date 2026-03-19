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
VM_RAM=6144
VM_DISK=20480
VM_CPUS=2
WORK_DIR="$HOME/vm-autoinstall-test"
UBUNTU_ISO_URL="https://releases.ubuntu.com/25.10/ubuntu-25.10-desktop-amd64.iso"
UBUNTU_ISO="ubuntu-25.10-desktop-amd64.iso"
SEED_ISO="seed.iso"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
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
  info "Download in corso..."
  wget -q --show-progress "$UBUNTU_ISO_URL" -O "$UBUNTU_ISO"
fi

# =============================================================================
# 5. Patch ISO Ubuntu
# Incorpora user-data e meta-data direttamente nella ISO di Ubuntu,
# e aggiunge "autoinstall ds=nocloud;s=/cdrom/" al GRUB.
# Questo è il metodo più affidabile — nessuna seed ISO separata necessaria.
# =============================================================================
step "5. Patch ISO Ubuntu (user-data + parametro GRUB)"
PATCHED_ISO="ubuntu-25.10-desktop-autoinstall.iso"

rm -f "$WORK_DIR/$PATCHED_ISO"

# --- Prepara user-data con direttiva reboot ---
mkdir -p "$WORK_DIR/seed_files"
if grep -q "shutdown:" "$AUTOINSTALL_SRC"; then
  cp "$AUTOINSTALL_SRC" "$WORK_DIR/seed_files/user-data"
else
  awk '/version: 1/{print; print "  shutdown: reboot"; next}1' "$AUTOINSTALL_SRC" \
    > "$WORK_DIR/seed_files/user-data"
  info "Aggiunta direttiva 'shutdown: reboot'"
fi
touch "$WORK_DIR/seed_files/meta-data"

# Copia verify_install.sh se disponibile
if [[ -f "$VERIFY_SRC" ]]; then
  cp "$VERIFY_SRC" "$WORK_DIR/seed_files/verify_install.sh"
  info "verify_install.sh incluso nella ISO"
fi

# --- Estrae grub.cfg dalla ISO originale e lo patcha ---
xorriso -osirrox on -indev "$WORK_DIR/$UBUNTU_ISO" \
  -extract /boot/grub/grub.cfg /tmp/grub_orig.cfg 2>/dev/null

# Aggiunge "autoinstall ds=nocloud;s=/cdrom/" prima di "---"
# Il \x3B è il punto e virgola URL-encoded, necessario in alcuni grub
sed 's|/casper/vmlinuz\(.*\) ---|/casper/vmlinuz\1 autoinstall ds=nocloud\;s=/cdrom/ ---|g' \
  /tmp/grub_orig.cfg > /tmp/grub_patched.cfg

info "Riga kernel patchata:"
grep "vmlinuz" /tmp/grub_patched.cfg | head -3

# --- Ricrea la ISO con grub.cfg patchato + user-data + meta-data ---
xorriso -indev "$WORK_DIR/$UBUNTU_ISO" \
  -outdev "$WORK_DIR/$PATCHED_ISO" \
  -update /tmp/grub_patched.cfg /boot/grub/grub.cfg \
  -update "$WORK_DIR/seed_files/user-data" /user-data \
  -update "$WORK_DIR/seed_files/meta-data" /meta-data \
  -boot_image any replay 2>/dev/null

[[ -f "$WORK_DIR/$PATCHED_ISO" ]] || error "Patch ISO fallita"
info "ISO patchata creata: $WORK_DIR/$PATCHED_ISO"

[[ -f "$PATCHED_ISO" ]] || error "Patch ISO fallita"
info "ISO patchata creata: $WORK_DIR/$PATCHED_ISO"

# Da qui in avanti usa la ISO patchata
UBUNTU_ISO="$PATCHED_ISO"


step "6. Creazione VM VirtualBox '$VM_NAME'"

if VBoxManage list vms | grep -q "\"$VM_NAME\""; then
  warning "VM già esistente, la rimuovo..."
  VBoxManage unregistervm "$VM_NAME" --delete 2>/dev/null || true
fi
# Rimuove eventuali file residui che bloccano la ricreazione
rm -rf "$HOME/VirtualBox VMs/$VM_NAME" 2>/dev/null || true

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
  --medium "$WORK_DIR/$PATCHED_ISO"

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
echo -e "${YELLOW}║  2. Copia verify_install.sh dal cdrom:                   ║${NC}"
echo -e "${YELLOW}║     sudo mount /dev/sr0 /mnt                             ║${NC}"
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
