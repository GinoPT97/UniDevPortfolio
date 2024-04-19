#include <iostream>
#include "libreria01.h"
using namespace std;

main(){
    int a, b;
    cout<<"Inserisci il  primo valore: ";
    cin>>a;
    cout<<"Inserisci il  secondo valore: ";
    cin>>b;
    cout<<"Il minimo vale "<<minimo(a, b)<<endl;
    cout<<"Il massimo vale "<<massimo(a, b);
}
