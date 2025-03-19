#!/bin/bash

# Imposta la directory corrente
DIR="$(pwd)"

# Estensioni delle immagini da considerare
estensioni=("jpg" "jpeg" "png" "tiff" "bmp")

# Loop attraverso ogni estensione
for est in "${estensioni[@]}"; do
    # Trova e processa ogni file con l'estensione corrente
    find "$DIR" -maxdepth 1 -type f -iname "*.$est" | while read -r file; do
        # Estrai il nome del file senza estensione
        nome_file="${file%.*}"
        # Converte l'immagine in PDF
        convert "$file" "$nome_file.pdf"
        # Verifica se la conversione è riuscita
        if [ $? -eq 0 ]; then
            # Elimina l'immagine originale
            rm "$file"
        else
            echo "Errore nella conversione di $file"
        fi
    done
done

