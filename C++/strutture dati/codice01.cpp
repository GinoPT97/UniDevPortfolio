//Programma che legge da tastiera un vettore di 10 numeri interi
//Visualizza il vettore
//Visualizza il vettore al contrario
#include <iostream>
using namespace std;
main(){
    int i, n, v[10];
    //Lettura della dimensione con controllo che sia tra 1 e 10
    do{
        cout<<"Quanti numeri? min=1,  max=10 ";
        cin>>n;
    }while(n<1 || n>10);
    //Lettura degli elementi del vettore
    for(i=0;i<n;i++){
        cout<<"inserisci un numero intero ";
        cin>>v[i];
    }
    //Visualizzazione del vettore
    cout<<"I numeri inseriti sono i seguenti: \n";
    for(i=0;i<n;i++)
        cout<<v[i]<<"  ";
    //Visualizzazione del vettore al contrario
    cout<<"\nI numeri inseriti visualizzati al contrario sono: \n";
    for(i=n-1;i>=0;i--)
        cout<<v[i]<<"  ";
}
