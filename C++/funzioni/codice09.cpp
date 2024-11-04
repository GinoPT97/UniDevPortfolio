#include <iostream>
#include "libreria02.h"
using namespace std;

main(){
    int n;
    float v[MD];
    cout<<"Inserisci la dimensione del vettore\n";
    n=leggiDimensione();
    cout<<"Inserisci gli elementi del vettore\n";
    leggiVettore(n, v); //Devo passare il vattore e quindi solo v
    cout<<"Hai inserito il seguente vettore\n";
    visualizzaVettore(n, v);
    cout<<"Il minimo del vettore inserito e': "<<minimoVettore(n, v)<<endl;
    ordinaVettore(n, v);
    cout<<"Il vettore ordinato e'\n";
    visualizzaVettore(n, v);
}
