#include <iostream>
#include <string.h>
#define MD 200
using namespace std;
main(){
    int i, n;
    string testo; //Il tipo string lo vedremo meglio nella prossima dispensa
    cout<<"Inserire un testo: ";
    getline(cin, testo); //Questa funzione legge finche non viene premuto il tasto INVIO
    n=testo.size();  //Anche questo sarà chiaro più avanti (Programmazione orientata agli oggetti)
    i=0;
    while(testo[i]==testo[n-i-1] && i<n/2)
        i++;
    if(i<n/2)
        cout<<"Non e' palindromo";
    else
        cout<<"E' palindromo";
}
