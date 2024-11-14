#!/bin/sh
## Genera l'indice con i collegamenti ipertestuali
## per tex4ht

# Assicurati che il percorso di 'idxmake.4ht' sia corretto
tex '\def\filename{{gapil}{ind}{idx}{in}{out}} \input Risorse/idxmake.4ht'

# Assicurati che i file gapil.in e gapil.out siano nella posizione corretta
makeindex -o gapil.out gapil.in
