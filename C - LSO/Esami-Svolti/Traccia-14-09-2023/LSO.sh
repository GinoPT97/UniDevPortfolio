#!/bin/bash

# (a) Spostati nella home directory e crea la directory LSO
cd ~
mkdir -p LSO

# (b) Copia ricorsivamente tutto /etc in LSO
cp -r /etc/* LSO/

# (c) Assicura permessi di lettura e scrittura per utente, gruppo e altri
chmod -R a+rw LSO/

# (d) Spostati nella nuova directory LSO
cd LSO

# (e) Crea una directory chiamata "Maiuscole" per i file che iniziano con lettera maiuscola
mkdir -p Maiuscole

# (f) Sposta tutti i file che iniziano con lettera minuscola nella directory Maiuscole
#     NB: la traccia dice "iniziano con lettera minuscola" ma la directory si chiama Maiuscole
#     Interpretiamo: crea dir per file con maiuscola, sposta quelli con minuscola dentro
for f in [a-z]*; do
    [ -f "$f" ] && mv "$f" Maiuscole/
done
