#!/bin/sh
## Genera l'indice con i collegamenti ipertestuali
## per tex4ht
tex '\def\filename{{gapil}{ind}{idx}{in}{out}} \input  idxmake.4ht'
makeindex -o gapil.out gapil.in
