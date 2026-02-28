#es 1.b
awk '{ print $2 ", " substr($1,1,1) ". di genere " $3 }' persone.txt

# 1.d
grep -E '\b[A-Z][a-zA-Z]*[A-Z]\b' testo.txt | wc -l

#Gli altri esercizi sono stati già svolti nelle altre tracce