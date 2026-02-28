#!/bin/bash

# controlla.sh
# Legge input da tastiera in loop
# Accetta solo combinazioni delle lettere "a", "b", "c" (più volte, in ordine sparso)
# Errore se contiene lettere diverse
# Termina con CTRL+C o con "Q"

trap "echo 'Uscita con CTRL+C'; exit 0" SIGINT

while true; do
    read -p "Inserisci stringa (solo a,b,c - Q per uscire): " input

    # Uscita con Q
    if [ "$input" = "Q" ] || [ "$input" = "q" ]; then
        echo "Uscita."
        break
    fi

    # Controlla che tutti i caratteri siano solo a, b o c
    if echo "$input" | grep -qP '^[abc]+$'; then
        echo "OK: '$input' e' una combinazione valida di a, b, c"
    else
        echo "ERRORE: '$input' contiene lettere non valide (solo a, b, c sono permesse)"
    fi
done
