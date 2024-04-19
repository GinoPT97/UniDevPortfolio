#include <iostream>
#define MAX 10
using namespace std;
main(){
    int n, i;
    float v[MAX], x;
    //Lettura dimensione
    do{
        cout<<"Quanti numeri? min 1, max "<<MAX<<": ";
        cin>>n;
    }while(n<1 || n>MAX);
    //Lettura valori del vettore
    for(i=0;i<n;i++){
        cout<<"Inserisci un numero di indice "<<i<<": ";
        cin>>v[i];
    }
    cout<<"Inserisci il valore da ricercare: ";
    cin>>x;
    //Elaborazione
    i=0;
    while(v[i]!=x && i<n)
        i++;
    if(i<n)
        cout<<"Il valore cercato e' presente all'indice "<<i;
    else
        cout<<"Il valore cercato non e' presente nel vettore";
}
