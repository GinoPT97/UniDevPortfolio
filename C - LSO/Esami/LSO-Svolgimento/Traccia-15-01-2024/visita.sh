#!/bin/bash

# visita.sh — visita tutti i file e directory nella directory corrente

for item in *; do
    [ -e "$item" ] || continue

    # Se è una directory
    if [ -d "$item" ]; then
        echo "[DIR] $item"
        read -p "Vuoi visitare la sottodirectory '$item'? (s/n): " risp
        if [ "$risp" = "s" ] || [ "$risp" = "S" ]; then
            (cd "$item" && bash "$OLDPWD/visita.sh")
        fi

    # Se è un file
    elif [ -f "$item" ]; then
        tipo=$(file -b "$item")
        echo "[FILE] $item — tipo: $tipo"

        # Se è leggibile e di tipo txt, doc, sh o c
        if [ -r "$item" ]; then
            ext="${item##*.}"
            if [[ "$ext" == "txt" || "$ext" == "doc" || "$ext" == "sh" || "$ext" == "c" ]]; then
                read -p "Vuoi vedere il contenuto di '$item'? (s/n): " risp
                if [ "$risp" = "s" ] || [ "$risp" = "S" ]; then
                    echo "--- Prime 3 righe di $item ---"
                    head -3 "$item"
                    echo "---"
                fi
            fi
        fi
    fi

    # Chiede se rimuovere il file/directory
    read -p "Vuoi rimuovere '$item'? (s/n): " risp_rm
    if [ "$risp_rm" = "s" ] || [ "$risp_rm" = "S" ]; then
        rm -rf "$item"
        echo "'$item' rimosso."
    fi

done
