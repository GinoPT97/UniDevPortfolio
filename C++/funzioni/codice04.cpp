#include <iostream>
using namespace std;

void stampa(int x, int y){//void per non ritornare nulla. Da notare l'assenza del return
    cout<<"Hai inserito: "<<x<<"   "<<y;
}

main(){
    int a, b;
    cout<<"Inserisci il  primo valore: ";
    cin>>a;
    cout<<"Inserisci il  secondo valore: ";
    cin>>b;
    stampa(a, b); //La chiamata ad una funzione void NON si fa a destra di un =
}
