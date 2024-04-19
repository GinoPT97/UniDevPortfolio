#include <iostream>
#include <string.h>
#define MD 20
using namespace std;

main(){
    int risposta;
    char parola1[MD], parola2[MD];
    cout<<"Inserire la prima parola (senza spazi): ";
    cin>>parola1;
    cout<<"Inserire la seconda parola (senza spazi): ";
    cin>>parola2;
    //Utilizziamo un'altra funzione che ci avrebbe semplificato l'esempio di prima
    //strcmp (string compare) restituisce:
    // -1 se la prima parola viene prima in ordine alfabetico
    //  0 se le due parole sono uguali
    //  1 se la prima parola viene dopo in ordine alfabetico
    risposta=strcmp(parola1, parola2);
    if(risposta==-1)
        cout<<"La prima parola precede la seconda in ordine alfabetico";
    else
        if(risposta==1)
            cout<<"La seconda parola precede la prima in ordine alfabetico";
        else
            cout<<"Le due parole sono uguali";
}
