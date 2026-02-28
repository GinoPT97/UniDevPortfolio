#!/bin/bash

# (a) Controlla se esiste la directory "backup"
if [ -d "backup" ]; then
    echo "La directory backup esiste, la cancello..."
    rm -rf backup
else
    echo "La directory backup non esiste, la creo..."
fi
mkdir backup

# (b) Sposta tutti i file .log nella directory backup e conta quanti sono
count=$(find . -maxdepth 1 -name "*.log" | wc -l)
find . -maxdepth 1 -name "*.log" | xargs -I{} mv {} backup/
echo "File trovati e spostati: $count"

# (c) Per ogni file spostato, verifica i permessi di lettura
# Se leggibile: stampa righe, parole, caratteri; altrimenti messaggio di errore
for f in backup/*.log; do
    [ -f "$f" ] || continue
    if [ -r "$f" ]; then
        wc "$f"
    else
        echo "ERRORE: $f non ha permessi di lettura per l'utente corrente"
    fi
done

# (d) Crea/aggiorna report.txt in backup con le info di ogni file
# Formato: nome | dimensione(byte) | righe | parole | proprietario | ultima modifica
report="backup/report.txt"
> "$report"
for f in backup/*.log; do
    [ -f "$f" ] || continue
    nome=$(basename "$f")
    dim=$(stat -c "%s" "$f")
    righe=$(wc -l < "$f")
    parole=$(wc -w < "$f")
    owner=$(stat -c "%U" "$f")
    modifica=$(stat -c "%y" "$f" | cut -d'.' -f1)
    echo "$nome | $dim bytes | $righe righe | $parole parole | $owner | $modifica" >> "$report"
done
echo "Report salvato in $report"
