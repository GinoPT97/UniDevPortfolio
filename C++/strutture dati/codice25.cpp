#include <iostream>
#include <string.h>
#define MD 20
using namespace std;

main(){
    int i, n;
    char parola[MD];
    cout<<"Inserire una parola (senza spazi): ";
    cin>>parola;  //Da notare che leggo l'intero vettore di caratteri
    cout<<"Visualizzo la parola inserita in verticale\n";
    n=strlen(parola);  //Funzione che mi restituisce la lunghezza length della stringa
    for(i=0;i<n;i++)
        cout<<parola[i]<<endl;  //Da notare che essendo un vettore posso utilizzare il singolo elemento
}
