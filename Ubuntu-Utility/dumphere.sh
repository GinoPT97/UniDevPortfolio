find . -type f | while read f; do
  echo "===== $f ====="; cat "$f"; echo
done > ~/Documenti/codice.txt

#nella cartella in cui ti trovi genera un txt con il contenuto di tutti i file 
