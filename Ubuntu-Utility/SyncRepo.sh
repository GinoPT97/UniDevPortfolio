#!/bin/bash

# Percorsi delle directory
BASE_DIR1='/home/kenobi/Documenti/GitHub/UniDevPortfolio'
BASE_DIR2='/home/kenobi/Documenti/Eclipse-Git'

# Funzione per fare il pull e push di una repository
function pull_push {
    local dir="$1"
    echo "Esegui pull e push per: $dir"
    git -C "$dir" pull origin main
    git -C "$dir" push origin main
}

# Esegui pull e push per entrambe le directory
pull_push "$BASE_DIR1"
pull_push "$BASE_DIR2"

echo "Operazione completata."
