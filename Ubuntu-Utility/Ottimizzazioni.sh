#!/usr/bin/env bash
set -Eeuo pipefail

SCRIPT_NAME="Ubuntu 26.04 Cleaner"
SCRIPT_VERSION="1.0.0"
LOG_FILE="/var/log/ubuntu-cleaner.log"
DRY_RUN=false
SAFE_MODE=false
ASSUME_YES=false
KEEP_JOURNAL_TIME="7d"
KEEP_JOURNAL_SIZE="200M"
TMP_DAYS=3
VAR_TMP_DAYS=7

# Safety rails: these paths are never deleted recursively.
readonly -a PROTECTED_PATHS=(
  "/"
  "/boot"
  "/dev"
  "/etc"
  "/home"
  "/lib"
  "/lib64"
  "/proc"
  "/root"
  "/run"
  "/sbin"
  "/srv"
  "/sys"
  "/usr"
  "/var"
)

usage() {
  cat << USAGE
$SCRIPT_NAME v$SCRIPT_VERSION

Uso:
  $0 [opzioni]

Opzioni:
  --dry-run            Simula le operazioni senza modificare il sistema
  --safe               Salta le pulizie piu aggressive (docker/flatpak/dev cache)
  -y, --yes            Nessuna conferma interattiva
  --journal-time <v>   Retention log systemd (default: $KEEP_JOURNAL_TIME)
  --journal-size <v>   Limite size journal (default: $KEEP_JOURNAL_SIZE)
  --tmp-days <n>       Eta minima file da eliminare in /tmp (default: $TMP_DAYS)
  --var-tmp-days <n>   Eta minima file da eliminare in /var/tmp (default: $VAR_TMP_DAYS)
  -h, --help           Mostra questo aiuto

Esempi:
  sudo $0 --dry-run
  sudo $0 --safe -y
  sudo $0 -y --journal-time 10d --journal-size 300M
USAGE
}

is_available() {
  command -v "$1" >/dev/null 2>&1
}

require_value() {
  local flag="$1"
  local value="${2:-}"
  if [[ -z "$value" ]]; then
    echo "Valore mancante per $flag" >&2
    usage
    exit 1
  fi
}

log() {
  local level="$1"
  shift
  local msg="$*"
  local ts
  ts="$(date '+%Y-%m-%d %H:%M:%S')"
  echo "[$level] $ts - $msg" | tee -a "$LOG_FILE"
}

run_cmd() {
  local desc="$1"
  shift

  if $DRY_RUN; then
    log "DRY" "$desc :: $*"
    return 0
  fi

  log "INFO" "$desc"
  "$@"
}

run_shell() {
  local desc="$1"
  local cmd="$2"

  if $DRY_RUN; then
    log "DRY" "$desc :: $cmd"
    return 0
  fi

  log "INFO" "$desc"
  bash -lc "$cmd"
}

require_root() {
  if [[ ${EUID:-$(id -u)} -ne 0 ]]; then
    echo "Errore: eseguire come root (sudo)." >&2
    exit 1
  fi
}

validate_os() {
  if [[ ! -f /etc/os-release ]]; then
    log "WARN" "Impossibile verificare la distribuzione (manca /etc/os-release)."
    return 0
  fi

  # shellcheck disable=SC1091
  source /etc/os-release
  if [[ "${ID:-}" != "ubuntu" ]]; then
    log "WARN" "Sistema non Ubuntu: ${ID:-sconosciuto}. Continuo comunque."
  fi

  # 26.04 not always GA in every environment: warning only.
  if [[ "${VERSION_ID:-}" != "26.04" ]]; then
    log "WARN" "Versione rilevata ${VERSION_ID:-sconosciuta}; script ottimizzato per 26.04."
  fi
}

confirm_or_exit() {
  if $ASSUME_YES || $DRY_RUN; then
    return 0
  fi

  read -r -p "Procedere con la pulizia? [y/N] " answer
  case "$answer" in
    y|Y|yes|YES) ;;
    *)
      log "INFO" "Operazione annullata dall'utente."
      exit 0
      ;;
  esac
}

wait_for_apt_locks() {
  local -a locks=(
    /var/lib/dpkg/lock-frontend
    /var/lib/dpkg/lock
    /var/cache/apt/archives/lock
    /var/lib/apt/lists/lock
  )

  local lock
  for lock in "${locks[@]}"; do
    while fuser "$lock" >/dev/null 2>&1; do
      log "INFO" "Lock APT su $lock, attendo 5 secondi..."
      sleep 5
    done
  done
}

repair_package_state() {
  wait_for_apt_locks
  run_cmd "Ripristino stato dpkg" dpkg --configure -a
  run_cmd "Correzione dipendenze APT" apt-get -f install -y
  run_cmd "Verifica integrita APT" apt-get check
}

apt_cleanup() {
  wait_for_apt_locks
  run_cmd "Aggiornamento indici pacchetti" apt-get update

  # Dist-upgrade can remove/install packages and is useful for long-lived systems.
  run_cmd "Upgrade pacchetti installati" apt-get upgrade -y
  run_cmd "Full upgrade sistema" apt-get full-upgrade -y

  run_cmd "Pulizia cache APT" apt-get clean
  run_cmd "Pulizia cache obsoleta APT" apt-get autoclean -y
  run_cmd "Rimozione dipendenze inutilizzate" apt-get autoremove --purge -y
}

purge_rc_packages() {
  local pkgs
  pkgs="$(dpkg -l | awk '/^rc/{print $2}')"

  if [[ -z "${pkgs//[[:space:]]/}" ]]; then
    log "INFO" "Nessun pacchetto rc da rimuovere."
    return 0
  fi

  if $DRY_RUN; then
    log "DRY" "Rimozione pacchetti rc: $pkgs"
    return 0
  fi

  log "INFO" "Rimozione pacchetti rc."
  # shellcheck disable=SC2086
  dpkg --purge $pkgs
}

cleanup_journal() {
  if ! is_available journalctl; then
    log "INFO" "journalctl non disponibile, salto."
    return 0
  fi

  run_cmd "Uso corrente journal" journalctl --disk-usage
  run_cmd "Flush journal su disco" journalctl --flush
  run_cmd "Riduzione journal per tempo" journalctl --vacuum-time="$KEEP_JOURNAL_TIME"
  run_cmd "Riduzione journal per dimensione" journalctl --vacuum-size="$KEEP_JOURNAL_SIZE"
}

cleanup_snap_disabled_revisions() {
  if ! is_available snap; then
    log "INFO" "snap non disponibile, salto cleanup snap."
    return 0
  fi

  run_cmd "Imposto retain snap a 2 revisioni" snap set system refresh.retain=2

  local list
  list="$(snap list --all 2>/dev/null | awk '/disabled|disabilitato/{print $1, $3}')"
  if [[ -z "${list//[[:space:]]/}" ]]; then
    log "INFO" "Nessuna revisione snap disabilitata da rimuovere."
    return 0
  fi

  while read -r name rev; do
    [[ -z "${name:-}" || -z "${rev:-}" ]] && continue
    run_cmd "Rimozione snap disabilitato $name rev $rev" snap remove "$name" --revision="$rev"
  done <<< "$list"
}

for_each_regular_user_home() {
  while IFS=: read -r _ _ uid _ _ home _; do
    if [[ "$uid" -ge 1000 && "$uid" -lt 65534 && -d "$home" ]]; then
      echo "$home"
    fi
  done < <(getent passwd)
}

safe_find_delete() {
  local base="$1"
  local find_expr="$2"

  local protected
  for protected in "${PROTECTED_PATHS[@]}"; do
    if [[ "$base" == "$protected" ]]; then
      log "ERROR" "Rifiuto cancellazione su path protetto: $base"
      return 1
    fi
  done

  if [[ ! -d "$base" ]]; then
    return 0
  fi

  if $DRY_RUN; then
    run_shell "Simulazione pulizia in $base" "find '$base' $find_expr -print"
  else
    run_shell "Pulizia in $base" "find '$base' $find_expr -delete"
  fi
}

cleanup_tmp_dirs() {
  safe_find_delete "/tmp" "-xdev -type f -mtime +$TMP_DAYS"
  safe_find_delete "/var/tmp" "-xdev -type f -mtime +$VAR_TMP_DAYS"
}

cleanup_user_caches() {
  local -a user_cache_specs=(
    ".cache/thumbnails|-type f"
    ".cache/google-chrome|-type f \\( -path '*/Cache/*' -o -path '*/Code Cache/*' \\)"
    ".cache/chromium|-type f \\( -path '*/Cache/*' -o -path '*/Code Cache/*' \\)"
    ".cache/mozilla/firefox|-type f -path '*/cache2/*'"
  )

  local home
  local spec rel expr
  while IFS= read -r home; do
    for spec in "${user_cache_specs[@]}"; do
      rel="${spec%%|*}"
      expr="${spec#*|}"
      safe_find_delete "$home/$rel" "$expr"
    done
  done < <(for_each_regular_user_home)
}

cleanup_rotated_logs() {
  safe_find_delete "/var/log" "-type f \\( -name '*.log.[0-9]*' -o -name '*.log.old' -o -name '*.gz' \\) -mtime +7"
}

cleanup_dev_caches() {
  local -a dev_cache_paths=(
    ".npm"
    ".cache/pip"
    ".gradle/caches"
    ".cache/yarn"
  )

  local home
  local rel
  while IFS= read -r home; do
    for rel in "${dev_cache_paths[@]}"; do
      safe_find_delete "$home/$rel" "-type f"
    done
  done < <(for_each_regular_user_home)
}

cleanup_flatpak() {
  if ! is_available flatpak; then
    log "INFO" "flatpak non installato, salto."
    return 0
  fi

  run_cmd "Pulizia runtime Flatpak inutilizzati" flatpak uninstall --unused -y
}

cleanup_docker() {
  if ! is_available docker; then
    log "INFO" "docker non installato, salto."
    return 0
  fi

  run_cmd "Pulizia container docker fermati" docker container prune -f
  run_cmd "Pulizia immagini docker inutilizzate" docker image prune -a -f
  run_cmd "Pulizia volumi docker inutilizzati" docker volume prune -f
  run_cmd "Pulizia network docker inutilizzati" docker network prune -f
  run_cmd "Pulizia build cache docker" docker builder prune -a -f
}

print_summary() {
  log "INFO" "Pulizia completata."
  run_cmd "Spazio disco finale" df -h /
}

parse_args() {
  while [[ $# -gt 0 ]]; do
    case "$1" in
      --dry-run)
        DRY_RUN=true
        ;;
      --safe)
        SAFE_MODE=true
        ;;
      -y|--yes)
        ASSUME_YES=true
        ;;
      --journal-time)
        shift
        require_value "--journal-time" "${1:-}"
        KEEP_JOURNAL_TIME="${1:-}"
        ;;
      --journal-size)
        shift
        require_value "--journal-size" "${1:-}"
        KEEP_JOURNAL_SIZE="${1:-}"
        ;;
      --tmp-days)
        shift
        require_value "--tmp-days" "${1:-}"
        TMP_DAYS="${1:-}"
        ;;
      --var-tmp-days)
        shift
        require_value "--var-tmp-days" "${1:-}"
        VAR_TMP_DAYS="${1:-}"
        ;;
      --safe-y|--yes-safe)
        SAFE_MODE=true
        ASSUME_YES=true
        ;;
      -h|--help)
        usage
        exit 0
        ;;
      *)
        echo "Opzione non riconosciuta: $1" >&2
        usage
        exit 1
        ;;
    esac
    shift
  done
}

main() {
  parse_args "$@"
  require_root
  validate_os

  mkdir -p "$(dirname "$LOG_FILE")"
  touch "$LOG_FILE"

  log "INFO" "Avvio $SCRIPT_NAME v$SCRIPT_VERSION"
  log "INFO" "dry-run=$DRY_RUN safe-mode=$SAFE_MODE assume-yes=$ASSUME_YES"
  run_cmd "Spazio disco iniziale" df -h /

  confirm_or_exit

  repair_package_state
  apt_cleanup
  purge_rc_packages
  cleanup_journal
  cleanup_snap_disabled_revisions
  cleanup_tmp_dirs
  cleanup_rotated_logs
  cleanup_user_caches

  if ! $SAFE_MODE; then
    cleanup_dev_caches
    cleanup_flatpak
    cleanup_docker
  else
    log "INFO" "Safe mode attivo: salto pulizie aggressive (dev cache/flatpak/docker)."
  fi

  run_cmd "Sincronizzazione filesystem" sync
  print_summary
}

trap 'log "ERROR" "Errore alla riga $LINENO (exit=$?)"' ERR
main "$@"
