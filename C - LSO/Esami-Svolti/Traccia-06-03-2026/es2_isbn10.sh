#!/bin/bash
# Es 2: Script BASH che calcola e verifica il codice di controllo ISBN-10.
#
# Algoritmo:
#   - Prendere le prime 9 cifre del codice
#   - Moltiplicare la 1a per 10, la 2a per 9, ..., la 9a per 2
#   - Sommare tutti i prodotti
#   - C = somma mod 11
#   - Se C = 10, il carattere di controllo è X; altrimenti è la cifra C
#
# Uso: ./es2_isbn10.sh <9 cifre>
# Esempio: ./es2_isbn10.sh 020103807  -> ISBN completo: 0201038079

if [ $# -ne 1 ]; then
    echo "Uso: $0 <9 cifre ISBN>"
    exit 1
fi

isbn9="$1"

# Validazione: deve essere esattamente 9 cifre
if ! [[ "$isbn9" =~ ^[0-9]{9}$ ]]; then
    echo "Errore: inserire esattamente 9 cifre numeriche."
    exit 1
fi

somma=0
for i in $(seq 0 8); do
    cifra=${isbn9:$i:1}
    peso=$((10 - i))
    prodotto=$((cifra * peso))
    somma=$((somma + prodotto))
done

C=$((somma % 11))

if [ $C -eq 10 ]; then
    controllo="X"
else
    controllo=$C
fi

echo "Cifre inserite : $isbn9"
echo "Somma ponderata: $somma"
echo "Somma mod 11   : $C"
echo "Cifra controllo: $controllo"
echo "ISBN completo  : ${isbn9}${controllo}"
