#Esercizio 1.a
awk '$1=="ASD" && $2=="LSO" && $3=="INGSW"' parole.txt

#Esercizio 1.b
awk '/^es.*E$/' parole.txt

#Esercizio 1.c
sed '/202[3-5]/! s/marzo/maggio/g' esami.txt

#Esercizio 1.d
ps -eo pid,comm,etimes --sort=-etimes | awk '$3 > 600 { print }' | head -5