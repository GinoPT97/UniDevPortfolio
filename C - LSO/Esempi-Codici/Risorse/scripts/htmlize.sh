#!/bin/sh
## htmlize.sh
##
## Copyright (C) 2003 Mirko Maischberger.
#*   This program is free software; you can redistribute it and/or modify
#*   it under the terms of the GNU General Public License as published by
#*   the Free Software Foundation; either version 2 of the License, or
#*   (at your option) any later version.
#*
#*   This program is distributed in the hope that it will be useful,
#*   but WITHOUT ANY WARRANTY; without even the implied warranty of
#*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#*   GNU General Public License for more details.
#*
#*   You should have received a copy of the GNU General Public License
#*   along with this program; if not, write to the Free Software
#*   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
#*   02111-1307  USA
##
## Script per la compilazione del GaPiL con htlatex
## ./htmlize.sh
##    genera i file HTML ed estrae le immagini
##
## ./htmlize.sh  -p
##    genera i file HTML ma non estrae le immagini
##
## $Id: htmlize.sh,v 1.2 2003/04/23 22:09:52 piccardi Exp $
##

# compila usando il file di configurazione htgapil.cfg
htlatex gapil htgapil " " "$1"

# rimuove gli width e height dalle immagini, l'impostazione
# di default di htlatex (che non so cambiare) è di usare la
# dimensione dell'immagine in punti pica.
perl -i.orig -pe 's/width=\".*\"//i' *.html
perl -i.orig -pe 's/height=\".*\"//i' *.html
perl -i.orig -pe 's/width=\".*\"//i' *.html
perl -i.orig -pe 's/height=\".*\"//i' *.html

# ripristina il backslash negli esempi di codice HTML
perl -i.orig -pe 's/INSERT_BACKSLASH_MAGIC8745/\\/g' *.html

# sostituisci il colore delle note (mouseover)
perl -i.orig -pe 's/:aqua/:#fcc/g' *.css

# Sposta i file generati nella directory htgapil
mv *.html htgapil/
mv *.png htgapil/
mv *.js htgapil/
mv *.css htgapil/

# Rimuove i file temporanei creati da htlatex
rm *.html~
rm *.html.orig

# Piccola correzione per le immagini mal convertite da htlatex
convert -density 110x110 -geometry 70% img/tcpip_overview.ps htgapil/gapil117x.png
convert -density 110x110 -geometry 50% img/iso_tcp_comp.ps htgapil/gapil114x.png
convert -density 110x110 -geometry 50% img/tcp_data_flux.ps htgapil/gapil116x.png

# Entra nella cartella di destinazione
cd htgapil

# Esegui lo script wwwis per correggere width e height delle immagini
wwwis *.html

# Pulizia finale: rimuove i file temporanei
rm *~
cd ..
