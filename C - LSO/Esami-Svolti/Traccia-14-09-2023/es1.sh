#es 1.a
# Stampa il contenuto di ordine.txt lasciando una riga vuota DOPO ogni riga
awk '{ print; print "" }' ordine.txt

#es 1.b
# Equivalente con sed di: head -n 12 <input.txt | tail -n 7 | grep 'with'
# Seleziona righe 6-12 (ultime 7 delle prime 12), poi filtra quelle con "with"
sed -n '6,12p' input.txt | grep 'with'

#es 1.c
# Stampa tutti i file (inclusi nascosti) con permessi di lettura E scrittura
# per utente, gruppo o altri
ls -la | awk '$1 ~ /[rw][rw]/ { print $NF }'

#es 1.d
# Trova file .html o .json nella directory corrente
# Stampa: proprietario, gruppo e path completo
find . -maxdepth 1 -type f \( -name "*.html" -o -name "*.json" \) \
    -exec stat -c "%U %G %n" {} \;
