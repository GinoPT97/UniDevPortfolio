#include <iostream>
#define MAX 10
using namespace std;

main(){
    int n, i;
    float v[MAX], temp;
    //Lettura dimensione
    do{
        cout<<"Quanti numeri? min 1, max "<<MAX<<": ";
        cin>>n;
    }while(n<1 || n>MAX);
    //Lettura valori dei vettori
    cout<<"Inserisci i valori del vettore\n";
    for(i=0;i<n;i++){
        cout<<"Inserisci un numero di indice "<<i<<": ";
        cin>>v[i];
    }
    //Elaborazione.
    //Devo fermarmi a n/2 altrimenti lo ribalto due volte tornando al vettore di partenza
    for(i=0;i<n/2;i++){
        temp=v[i];
        v[i]=v[n-i-1];
        v[n-i-1]=temp;
    }
    //Visualizzazione
    for(i=0;i<n;i++)
        cout<<v[i]<<"  ";
}
