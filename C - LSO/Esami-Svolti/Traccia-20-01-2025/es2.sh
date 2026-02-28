#!/bin/bash

# Script Scrabble - calcola il punteggio di ogni riga di scrabble.txt

awk '{
    score = 0
    word = toupper($0)
    n = split(word, chars, "")
    for (i = 1; i <= n; i++) {
        c = chars[i]
        if (c ~ /[AEIOULNRST]/)     score += 1
        else if (c ~ /[DG]/)         score += 2
        else if (c ~ /[BCMP]/)       score += 3
        else if (c ~ /[FHVWY]/)      score += 4
        else if (c == "K")           score += 5
        else if (c ~ /[JX]/)         score += 8
        else if (c ~ /[QZ]/)         score += 10
    }
    printf "%-15s punteggio: %d\n", $0, score
    total += score
}
END { printf "Punteggio totale: %d\n", total }
' scrabble.txt
