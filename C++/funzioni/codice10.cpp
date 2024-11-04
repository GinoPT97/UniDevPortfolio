#include <iostream>
#include "libreria03.h"
using namespace std;

main(){
    int n;
    float a[MD][MD], b[MD], x[MD];
    cout<<"Inserisci la dimensione del sistema lineare\n";
    n=leggiDimensione();
    cout<<"Inserisci i coefficienti del sistema\n";
    leggiMatriceQuadrata(n, a);
    cout<<"Inserisci i termini noti\n";
    leggiVettore(n, b);
    triangolarizzaSistema(n, a, b);
    risolviSistemaTriangolare(n, a, b, x);
    cout<<"La soluzione dei sistema lineare e'\n";
    visualizzaVettore(n, x);
}
