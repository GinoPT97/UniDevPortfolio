#include <iostream>
#include <stdio.h>
using namespace std;

main(){
    int x, s;
    FILE *in;
    in=fopen("input3.txt", "rt");
    s=0;
    //La funzione feof restituisce false quando si arriva alla fine del file
    while(!feof(in)){
        fscanf(in, "%d", &x);
        s=s+x;
    }
    printf("La somma dei valori contenuti nel file e': %d", s);
}
