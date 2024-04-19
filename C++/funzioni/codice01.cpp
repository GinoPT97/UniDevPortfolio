#include <iostream>
using namespace std;
int assoluto(int x){ //x si chiama parametro formale
    if(x>0)
        return x;
    else
        return -x;
}
main(){
    int i, n, v[10];
    int somma;
    do{
        cout<<"Quanti numeri? min=1,  max=10 ";
        cin>>n;
    }while(n<1 || n>10);
    for(i=0;i<n;i++){
        cout<<"inserisci un numero intero ";
        cin>>v[i];
    }
    somma=0;
    for(i=0;i<n;i++)
        somma=somma+assoluto(v[i]); //v[i] si chiama parametro attuale
    cout<<"La somma dei valori assoluti vale: "<<somma;
}
