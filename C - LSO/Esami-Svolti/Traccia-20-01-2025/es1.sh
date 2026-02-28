#es 1.a
# Elenca tutte le porte in ascolto con IP associati, rimuove duplicati, mostra solo i campi utili
netstat -tlnp | sort -u | awk '{ print $1, $4 }'

#es 1.b
# Mostra i 5 processi con più uso di memoria, ordinati in modo decrescente, solo nome del comando
# Filtra quelli che contengono "python" nel nome
ps aux --sort=-%mem | head -6 | tail -5 | awk '{ print $11 }' | grep "python"

#es 1.c
# Trova file .log modificati negli ultimi 7 giorni e con dimensione < 500KB a partire da /
find / -name "*.log" -mtime -7 -size -500k 2>/dev/null

#es 1.d
# Estrae le righe contenenti "error" e "info" da system.log,
# inverte la parola "error" in "rorre" e salva in errors.log
grep -i "error\|info" system.log | sed 's/error/rorre/gi' > errors.log
