//Programma che legge un vettore e calcola la somma degli elementi
#include <iostream>
#define MD 10		//Definizione di una costante
using namespace std;

main(){
    int n, i;
    float v[MD], somma;
    //Lettura della dimensione con controllo e uso della costante
    do{
        cout<<"Quanti numeri? min=1, max="<<MD<<": ";
        cin>>n;
    }while(n<1 || n>MD);
    //Lettura del vettore
    cout<<"Inserisci i valori del vettore:\n";
    for(i=0;i<n;i++){
        cout<<"Inserisci il valore di indice "<<i<<": ";
        cin>>v[i];
    }
    //Elaborazione: somma degli elementi del vettore
    somma=0.0;
    for(i=0;i<n;i++)
        somma=somma+v[i];
    //Visualizzazione risultato
    cout<<"La somma dei valori inseriti vale: "<<somma;
}
