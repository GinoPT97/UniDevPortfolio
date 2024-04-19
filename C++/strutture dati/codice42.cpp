#include <stdio.h>
using namespace std;

main(){
    char c;
    int i, f[256]; //256 contatori per ognuno dei codici ASCII
    FILE *in;
    in=fopen("elenco.txt", "rt");
    for(i=0;i<256;i++)
        f[i]=0;
    while( (c=fgetc(in))!=EOF)
        f[c]++; //per il linguaggio C caratteri e interi sono la stessa cosa a parte il fatto che char è di 8 bit
    for(i=32;i<256;i++) //parto da 32 per evitare i caratteri non stampabili come l'invio presente nel file
        if(f[i]>0)
            printf("Il carattere %c e' presente %d volte\n", i, f[i]);
}

