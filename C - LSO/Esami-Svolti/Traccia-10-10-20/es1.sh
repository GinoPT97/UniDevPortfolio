#es 1.a
# Sostituisce la seconda stringa (separata da "-") con "%"
awk -F'-' 'BEGIN{OFS="-"} { $2 = "%"; print }' input.txt

#es 1.b
# Sostituisce ":" con "LSO" e rimuove l'ultima stringa se la riga contiene un numero
awk -F':' 'BEGIN{OFS=":"} /[0-9]/ { NF-- } { $1=$1; print }' campi.txt | sed 's/:/LSO/g'

#es 1.c
# Stampa le righe NON contenenti "LSO" o "lso" e la riga subito dopo
grep -vi "LSO" -A1 stringhe.txt | grep -v "^--$"

#es 1.d
# Calcola il costo totale per fiore in percentuale e stampa "fiore = percentuale%"
awk '{ tot[$1] += $2 * $3; grand += $2 * $3 }
     END { for(f in tot) printf "%s = %.2f%%\n", f, (tot[f]/grand)*100 }' fiori.txt
