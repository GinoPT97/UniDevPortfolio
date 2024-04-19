#include <iostream>
#include <string.h>
#define MD 20
using namespace std;

main(){
    char parola1[MD], parola2[MD], parola3[2*MD];
    cout<<"Inserire la prima parola (senza spazi): ";
    cin>>parola1;
    cout<<"Inserire la seconda parola (senza spazi): ";
    cin>>parola2;
    strcpy(parola3, parola1);
    strcat(parola3, parola2);  //Concatena (aggiunge) parola2 a parola3
    cout<<"La concatenazione e'\n";
    cout<<parola3;
}
