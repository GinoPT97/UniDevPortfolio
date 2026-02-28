#!/bin/bash

# Leggo la frase dall'utente
read -p "Inserisci la frase: " frase

# Sostituisco tutte le vocali (maiuscole e minuscole) con *
risultato=$(echo "$frase" | tr 'aeiouAEIOU' '*')

#risultato=$(echo "$frase" | sed 's/[aeiouAEIOU]/*/g') #alternativa con sed

echo "Risultato: $risultato"



