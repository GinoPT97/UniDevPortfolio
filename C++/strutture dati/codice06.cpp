#include <iostream>
#define MAX 10
using namespace std;
main(){
    int n, i;
    float v1[MAX], v2[MAX];
    //Lettura dimensione
    do{
        cout<<"Quanti numeri? min 1, max "<<MAX<<": ";
        cin>>n;
    }while(n<1 || n>MAX);
    //Lettura valori dei vettori
    cout<<"Inserisci i valori del primo vettore\n";
    for(i=0;i<n;i++){
        cout<<"Inserisci un numero di indice "<<i<<": ";
        cin>>v1[i];
    }
    cout<<"Inserisci i valori del secondo vettore\n";
    for(i=0;i<n;i++){
        cout<<"Inserisci un numero di indice "<<i<<": ";
        cin>>v2[i];
    }
    //Elaborazione
    i=0;
    while(v1[i]==v2[i] && i<n)
        i++;
    if(i<n)
        cout<<"I due vettori sono diversi ";
    else
        cout<<"I due vettori sono uguali";
}
