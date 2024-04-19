#include <iostream>
using namespace std;

void scambia(int &x, int &y){ //Così dico che i parametri sono passati per indirizzo
    int temp;
    temp=x;  //E' più semplice in C++ senza tutti gli *
    x=y;
    y=temp;
}

main(){
    int a, b;
    cout<<"Inserisci il  primo valore: ";
    cin>>a;
    cout<<"Inserisci il  secondo valore: ";
    cin>>b;
    scambia(a, b);  //Nessuna &
    cout<<"I valori dopo lo scambio sono: "<<a<<" "<<b;
}
