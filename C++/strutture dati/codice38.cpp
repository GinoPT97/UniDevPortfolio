#include <iostream>
#include <stdio.h>
using namespace std;

main(){
    int i, x, n;
    FILE *out;
    out=fopen("output.txt", "wt"); //r=write, t=text
    printf("Quanti numeri? ");//Posso scrivere sul monitor e sul file a differenza del redirect
    scanf("%d", &n);
    fprintf(out, "%d\n", n);
    for(i=0;i<n;i++){
        cout<<"Inserisci un numero: ";  //Posso utilizzare entrambe le modalità di I/O
        cin>>x;
        fprintf(out, "%d ", x);
    }
}
