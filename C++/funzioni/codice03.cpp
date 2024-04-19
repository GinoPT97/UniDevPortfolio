#include <iostream>
using namespace std;

int minimo(int x, int y){//due parametri separati da virgola. Occorre specificare il tipo per ogni parametro
    if(x<y)
        return x;
    else
        return y;
}

int massimo(int x, int y){//due parametri separati da virgola. Occorre specificare il tipo per ogni parametro
    if(x>y)
        return x;
    else
        return y;
}

main(){
    int i, a, b;
    cout<<"Inserisci il  primo valore: ";
    cin>>a;
    cout<<"Inserisci il  secondo valore: ";
    cin>>b;
    for(i=minimo(a, b);i<=massimo(a, b);i++)
        cout<<i<<"  ";
}
