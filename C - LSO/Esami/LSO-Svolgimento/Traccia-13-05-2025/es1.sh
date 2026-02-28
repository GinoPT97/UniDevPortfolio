#es 1.a
# Sostituisce la seconda stringa (separata da "-") con "%"
awk -F'-' 'BEGIN{OFS="-"} { $2 = "%"; print }' input.txt

#es 1.b
# Rimuove l'ultimo campo se la riga contiene un numero, poi sostituisce ":" con "LSO"
awk -F':' 'BEGIN{OFS=":"} /[0-9]/ { NF-- } { $1=$1; print }' campi.txt | sed 's/:/LSO/g'

#es 1.c
# Stampa le righe NON contenenti "LSO"/"lso" e la riga immediatamente dopo
grep -vi "LSO" -A1 stringhe.txt | grep -v "^--$"

#es 1.d
# Calcola il costo totale per fiore (quantità * costo), lo esprime in percentuale sul totale
# e stampa "fiore = percentuale%"
awk '{ tot[$1] += $2 * $3; grand += $2 * $3 }
     END { for(f in tot) printf "%s = %.2f%%\n", f, (tot[f]/grand)*100 }' fiori.txt
