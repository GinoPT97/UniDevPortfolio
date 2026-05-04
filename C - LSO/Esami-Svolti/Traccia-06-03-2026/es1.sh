#!/bin/bash

# ============================================================
# Es 1a: eliminare i numeri e stampare: frase = numeri
# ============================================================
awk '{f=$0; gsub(/[0-9]+/,"",f); gsub(/ +/," ",f); gsub(/^ | $/,"",f); n=""; 
      for(i=1;i<=NF;i++) 
       if($i~/^[0-9]+$/) n=n$i" "; 
      gsub(/ $/,"",n); print f" = "n}' input.txt


# ============================================================
# Es 1b: inserire uno spazio prima e dopo ogni vocale
# ============================================================
sed 's/[aeiouAEIOU]/ & /g' input.txt | tr -s ' '


# ============================================================
# Es 1c: contare vocali e consonanti per ogni riga
# ============================================================
awk '{v=0;c=0; s=tolower($0); 
      for(i=1;i<=length(s);i++){ch=substr(s,i,1); 
      if(ch~/[aeiou]/)v++; else if(ch~/[bcdfghjklmnpqrstvwxyz]/)c++}; 
      print "Vocali: "v", Consonanti: "c}' input.txt


# ============================================================
# Es 1d: estrarre il terzo carattere di ogni parola (se esiste)
# ============================================================
grep -oE '\b[a-zA-Z0-9]{3,}\b' input.txt | cut -c3
