//Programma che legge un vettore e calcola
//la somma dei valori assoluti degli elementi
#include <iostream>
using namespace std;

main(){
    float x, somma;
    int cont, n;
    do{
        cout<<"Quanti numeri? ";
        cin>>n;
    }while(n<1);
    somma=0.0;
    for(cont=0;cont<n;cont++){
        cout<<"Inserisci un numero ";
        cin>>x;
        if(x>0.0)
            somma=somma+x;
        else
            somma=somma-x;
    }
    cout<<"La somma dei valori assoluti vale: "<<somma;
}
