#!/bin/bash

# =========================
# Controllo parametri
# =========================
[ $# -ne 4 ] && echo "ERRORE" && exit 1

fn=$1
n1=$2
n2=$3
n3=$4

# file fn esiste?
[ -f "$fn" ] || { echo "ERRORE"; exit 1; }

# numeri interi positivi
for n in $n1 $n2 $n3; do
  [[ $n =~ ^[0-9]+$ ]] || { echo "ERRORE"; exit 1; }
done

# n1 ≤ n2
[ "$n1" -le "$n2" ] || { echo "ERRORE"; exit 1; }

# =========================
# Elaborazione file
# =========================
while read -r path; do

  # deve essere file regolare
  [ -f "$path" ] || continue

  size=$(stat -c %s "$path")

  if [ "$size" -lt "$n1" ]; then
    rm "$path"
  elif [ "$size" -le "$n2" ]; then
    :
  else
    gzip "$path"
  fi

done < "$fn"