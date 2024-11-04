#!/usr/bin/env bash

# Autore: Valentino Bocchetti - N86003405

# Traccia:
# Script che data una cartella e una estensione scelta elimini all'interno della cartella e di tutte le sue sottocartelle tutti i file che contengano quella estensione.

set -e # Uscita immediata nel caso in cui un comando dia un exit status diverso da 0
shopt -s globstar dotglob nullglob

TmpStack="/tmp/stack"
[ -f "$TmpStack" ] && rm $TmpStack

print_ok() {
    printf "\e[32m%b\e[0m" "$1"
    printf "\n"
}

print_info() {
    printf "\e[36m%b\e[0m" "$1"
    printf "\n"
}

print_error() {
    printf "\e[31m%b\e[0m" "$1"
    printf "\n"
}


usage() {
  print_info "Questo script elimina file che matchano una estensione (data dall'utente) dalla directory corrente ricorsivamente.\n"
  print_ok "\t\tUtilizzo: ./CleanDir.sh estensione-file"
  exit 1
}

estensione=$1

searchNdelete(){
  print_info "Controllo la presenza di file che matchano la richiesta"
  for f in **/*; do
    printf "%s\n" "$f" | grep $estensione >>  "$TmpStack"
  done 

  if [ -s "$TmpStack" ]; then
    echo " "
  else
    print_info "Nessun file trovato che matcha l'estensione richiesta!\n"
    exit 1
  fi

  sed -ie 's|^|./|g' "$TmpStack"  # Aggiungiamo ./ all'inizio di ogni riga

  print_error "Sto per eliminare i seguenti file che matchano la richiesta:\n"

  while read -r elem; do
    printf '%s\n' "$elem"
  done < "$TmpStack"


  print_error "\n\nSei sicuro? (Si/No)"

  read -r p

  if [[ "$p" != [Ss] ]]; then
    print_error "Uscita..."
    exit 1
  fi

  while read -r files; do
    rm "$files"
  done < "$TmpStack"

  [ -f "$TmpStack" ] && rm $TmpStack
  print_info "Operazione completata!"

}

if [ $# -eq 0  ]; then
  usage
  exit
else
  searchNdelete
fi

