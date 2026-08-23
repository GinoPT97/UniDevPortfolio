#!/bin/bash

# ============================================================
# Es 1a: eliminare i numeri e stampare: frase = numeri
# ============================================================
while IFS= read -r riga; do
    frase=$(echo "$riga" | sed 's/[0-9]//g')
    numeri=$(echo "$riga" | grep -o '[0-9]\+' | tr '\n' ' ')
    echo "$frase = $numeri"
done < input.txt


# ============================================================
# Es 1b: inserire uno spazio prima e dopo ogni vocale
# ============================================================
sed 's/[aeiouAEIOU]/ & /g' input.txt | tr -s ' '


# ============================================================
# Es 1c: contare vocali e consonanti per ogni riga
# ============================================================
while IFS= read -r riga; do
    vocali=$(echo "$riga" | grep -oi '[aeiou]' | wc -l)
    consonanti=$(echo "$riga" | grep -oi '[a-z]' | grep -vi '[aeiou]' | wc -l)
    echo "$riga -> vocali=$vocali, consonanti=$consonanti"
done < file.txt


# ============================================================
# Es 1d: estrarre il terzo carattere di ogni parola (se esiste)
# ============================================================
grep -oE '\b[a-zA-Z0-9]{3,}\b' input.txt | cut -c3
