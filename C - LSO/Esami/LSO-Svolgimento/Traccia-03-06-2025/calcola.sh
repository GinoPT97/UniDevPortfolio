#!/bin/bash

# se non c'è un argomento → errore
[ $# -ne 1 ] && echo "ERRORE" && exit 1

# normalizzo la frase
expr=$(echo "$1" | tr '[:upper:]' '[:lower:]' | sed 's/?//g')

# estraggo solo numeri e operatori
tokens=$(echo "$expr" | sed -E 's/quanto fa //; s/più/+/g; s/meno/-/g; s/per/*/g; s/diviso/\//g')

# controllo che inizi con un numero
echo "$tokens" | grep -Eq '^[0-9]+' || { echo "ERRORE"; exit 1; }

# calcolo sequenziale
result=$(echo "$tokens" | awk '
{
  res = $1
  for (i = 2; i <= NF; i += 2) {
    if ($(i) == "+") res += $(i+1)
    else if ($(i) == "-") res -= $(i+1)
    else if ($(i) == "*") res *= $(i+1)
    else if ($(i) == "/") res = int(res / $(i+1))
    else { print "ERRORE"; exit }
  }
  print res
}')

echo "$result"

#Esempi di esecuzione
#./calcola.sh "Quanto fa 5 più 13?"
# 18
#./calcola.sh "Quanto fa 5 più 13 diviso 2?"
# (5 + 13) / 2 = 9
#./calcola.sh "cosa è 5?"
# ERRORE