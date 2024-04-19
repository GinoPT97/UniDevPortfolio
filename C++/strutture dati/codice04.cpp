#include <iostream>
#define MAX 10
using namespace std;

main(){
    int n, i;
    float v[MAX], minimo;
    //Lettura dimensione
    do{
        cout<<"Quanti numeri? min 1, max "<<MAX<<": ";
        cin>>n;
    }while(n<1 || n>MAX);
    //Lettura valori del vettore
    for(i=0;i<n;i++){
        cout<<"Inserisci un numero di indice "<<i+1<<": ";
        cin>>v[i];
    }
    //Elaborazione
    minimo=v[0];
    for(i=1;i<n;i++)
        if(v[i]<minimo)
            minimo=v[i];
    //Visualizzazione risultato
    cout<<"Il minimo vale: "<<minimo;
}
