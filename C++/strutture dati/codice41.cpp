#include <stdio.h>
using namespace std;

main(){
    char c;
    FILE *in;
    in=fopen("elenco.txt", "rt");
    printf("Il contenuto del file e' il seguente\n");
    while( (c=fgetc(in))!=EOF)
        printf("%c", c);
}
//la funzione fgetc() leggere un carattere alla volta e restituisce la costante EOF quando arriva alla fine del file
//il test del while funziona così; prima si effettua la lettura di un carattere e se questo è diverso da EOF
//va avanti mentre se è uguale a EOF termina
//da notare nella printf il formattatore %c per il singolo carattere
