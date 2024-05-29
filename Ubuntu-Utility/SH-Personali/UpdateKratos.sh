#!/bin/bash

# Funzione per gestire gli errori
handle_error() {
    echo "Errore alla linea $1"
    exit 1
}

# Trap per catturare errori e chiamare la funzione handle_error
trap 'handle_error $LINENO' ERR

# Nome dell'ambiente virtuale
ENV_NAME="kratos"

# Pulizia di tutti i pacchetti e le cache di conda
echo "Pulizia dei pacchetti e delle cache di conda..."
conda clean --all -y

# Aggiornamento di tutti i pacchetti nell'ambiente specificato
echo "Aggiornamento di tutti i pacchetti nell'ambiente $ENV_NAME..."
conda update --all -n "$ENV_NAME" -y

# Aggiornamento di conda nell'ambiente base
echo "Aggiornamento di conda nell'ambiente base..."
conda update -n base -c defaults conda -y

echo "Operazioni di pulizia e aggiornamento completate per l'ambiente $ENV_NAME!"
