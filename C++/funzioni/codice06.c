#include <stdio.h>

void scambia(int *x, int *y){//* significa puntatore quindi *x significa un puntatore a intero
    int temp;
    temp=*x;  //x è un indirizzo quindi "punta" ad un valore
    *x=*y;    //possimo dire che & e * sono l'uno l'inverso dell'altro
    *y=temp;
}

main(){
    int a, b;
    printf("Inserisci il  primo valore: ");
    scanf("%d", &a);
    printf("Inserisci il  secondo valore: ");
    scanf("%d", &b); //La & significa "indirizzo" quindi &b è l'indirizzo di memoria dove è memorizzato b
    scambia(&a, &b); //Adesso è chiaro il significato di &
    printf("I valori scambiati sono: %d %d", a, b);
}
