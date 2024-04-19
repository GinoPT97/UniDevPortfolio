#include <iostream>
using namespace std;
int assoluto(int x){
    if(x>0)
        return x;
    else
        return -x;
}
main(){
    int i, n, v[10];
    int minimo, vicino;
    do{
        cout<<"Quanti numeri? min=1,  max=10 ";
        cin>>n;
    }while(n<1 || n>10);
    for(i=0;i<n;i++){
        cout<<"inserisci un numero intero ";
        cin>>v[i];
    }
    minimo=assoluto(v[0]);
    vicino=v[0];
    for(i=1;i<n;i++)
        if(assoluto(v[i])<minimo){
            minimo=assoluto(v[i]);
            vicino=v[i];
        }

    cout<<"Il valore più vicino allo zero e': "<<vicino;
}
