#include <iostream>
#define MAX 10
using namespace std;

main(){
    int n, i;
    float v[MAX];
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
    i=0;
    while(v[i]==v[n-i-1] && i<n/2)
        i++;
    if(i<n/2)
        cout<<"Il vettore inserito non e' palindromo";
    else
        cout<<"Il vettore inserito e' palindromo";
}
