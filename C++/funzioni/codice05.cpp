#include <iostream>
using namespace std;
void stampa(int x, int y){//void per non ritornare nulla. Da notare l'assenza del return
    if(x < y)
        cout<<"Hai inserito (in ordine crescente): "<<x<<"   "<<y;
    else
        cout<<"Hai inserito (in ordine crescente): "<<y<<"   "<<x;
}

main(){
    int a, b;
    cout<<"Inserisci il  primo valore: ";
    cin>>a;
    cout<<"Inserisci il  secondo valore: ";
    cin>>b;
    stampa(a, b);
}
