#es 1.a
# Sostituisce la terza stringa (separata da ",") con il numero 42
awk -F',' 'BEGIN{OFS=","} { $3 = 42; print }' input.txt

#es 1.b
# Cancella il delimitatore ":" e l'ultima stringa se la riga contiene un numero
awk -F':' 'BEGIN{OFS=":"} /[0-9]/ { NF-- } { $1=$1; print }' campi.txt | sed 's/://g'

#es 1.c
# Stampa le righe contenenti "LSO" o "lso" e la riga subito dopo
grep -i "LSO" -A1 stringhe.txt | grep -v "^--$"

#es 1.d
# Calcola il costo totale per fiore (quantità * costo) e stampa "fiore = costo_totale"
awk '{ tot[$1] += $2 * $3 }
     END { for(f in tot) printf "%s = %.2f\n", f, tot[f] }' fiori.txt
