#!/bin/bash

# es2 - Script menu per gestione verifiche
# Formato file "verifica": giorno mese anno nome voto

while true; do
    echo ""
    echo "===== MENU ====="
    echo "1) Aggiungi verifica"
    echo "2) Conta"
    echo "3) Media"
    echo "4) Esci"
    read -p "Scelta: " scelta

    case $scelta in
        1)
            # (a) Inserisce una nuova riga nel file "verifica" (lo crea se non esiste)
            read -p "Giorno: " giorno
            read -p "Mese (gen/feb/mar/...): " mese
            read -p "Anno: " anno
            read -p "Nome studente: " nome
            read -p "Voto: " voto
            echo "$giorno $mese $anno $nome $voto" >> verifica
            echo "Verifica aggiunta."
            ;;
        2)
            # (b) Conta le prove effettuate in un dato mese per uno studente
            read -p "Mese (gen/feb/mar/...): " mese
            read -p "Nome studente: " nome
            count=$(grep "$mese" verifica | grep "$nome" | wc -l)
            echo "Prove di $nome in $mese: $count"
            ;;
        3)
            # (c) Calcola la media dei voti di uno studente
            read -p "Nome studente: " nome
            awk -v n="$nome" '
                $4 == n { sum += $5; count++ }
                END {
                    if (count > 0)
                        printf "Media di %s: %.2f\n", n, sum/count
                    else
                        print "Nessuna verifica trovata per " n
                }
            ' verifica
            ;;
        4)
            echo "Arrivederci."
            break
            ;;
        *)
            echo "Scelta non valida."
            ;;
    esac
done
