#es 1.a
# Stampa il contenuto di ordine.txt aggiungendo una riga vuota prima di ogni riga contenente "Rosa"
awk '/Rosa/ { print "" } { print }' ordine.txt

#es 1.b
# Stampa le righe contenenti "LSO" o "lso" e la riga subito dopo
grep -i "LSO" -A1 stringhe.txt | grep -v "^--$"

#es 1.c
# Equivalente con sed di: echo "first:second:third" | awk -F ':' '{print $2}'
# Estrae il secondo campo separato da ":"
echo "first:second:third" | sed 's/[^:]*:\([^:]*\).*/\1/'

#es 1.d
# Trova tutti i file senza estensione .txt o .doc nella directory corrente
# Stampa: proprietario, gruppo e path completo
find . -maxdepth 1 -type f ! -name "*.txt" ! -name "*.doc" \
    -exec stat -c "%U %G %n" {} \;
