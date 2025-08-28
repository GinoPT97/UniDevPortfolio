#!/usr/bin/env bash

if [ $# -ne 1 ]; then
    echo "Inserisci il numero di client che vuoi lanciare come parametro"
    exit 1
fi


for i in $(seq "$1"); do
    # decommentare la porzione di riga successiva per non stampare su terminale l'output del client
    ./client #1>/dev/null
done
