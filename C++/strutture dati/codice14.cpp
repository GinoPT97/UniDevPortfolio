//Leggere un numero intero e calcolare le frequenze delle cifre
//Esempio: 81222456 -> 1-1, 3-2, 1-4, 1-5, 1-6, 1-8
#include <iostream>
using namespace std;
main(){
    long long x; //intero a 64 bit
    int i, cifra, f[10];
    for(i=0;i<10;i++)
        f[i]=0;
            do{
        cout<<"Inserisci un numero intero non negativo: ";
        cin>>x;
    }while(x<0);
    if(x==0)
        cout<<"La cifra 0 e' presente una volta"; //Corner case - caso particolare
    else{
    while(x>0){
        cifra=x%10;
        f[cifra]++;
        x=x/10;
    }
    for(i=0;i<10;i++)
        if(f[i]>0)
            cout<<"La cifra "<<i<<" e' presente "<<f[i]<<" volte\n";
    }
}
