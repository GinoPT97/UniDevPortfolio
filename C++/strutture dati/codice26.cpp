#include <iostream>
#include <string.h>
#define MD 20
using namespace std;
main(){
    int i, n;
    char parola[MD];
    cout<<"Inserire una parola (senza spazi): ";
    cin>>parola;
    cout<<"Visualizzo la parola inserita al contrario\n";
    n=strlen(parola);
    for(i=n-1;i>=0;i--)
        cout<<parola[i];
}
