#es 1.a
# Estrae righe con "error" ma senza "info", sostituisce "error" con "warning" e salva in errors.log
# NB: assicurarsi che system.log sia nella directory corrente (es. cp ../Traccia-20-01-2025/system.log .)
grep -i "error" system.log | grep -vi "info" | sed 's/error/warning/gi' > errors.log

#es 1.b
# Trova file .pdf appartenenti a un gruppo dato (sostituire "staff" con il gruppo reale)
# e con dimensione > 1MB a partire dalla root
find / -name "*.pdf" -group staff -size +1M 2>/dev/null

#es 1.c
# Mostra i 5 processi dell'utente corrente ordinati per tempo di CPU crescente
# Colonne: nome processo, %memoria, tempo CPU
ps -u "$USER" -o comm,%mem,time --sort=time | head -6 | tail -5

#es 1.d
# Trova file .txt creati prima del 20-02-2010 in /home e sottodirectory
# Aggiunge "DONE" alla fine di ogni file trovato
touch -t 201002200000 /tmp/ref2010
find /home -name "*.txt" ! -newer /tmp/ref2010 | xargs -I{} sh -c 'echo "DONE" >> "{}"'
