#include <iostream>
#include <string.h>
#define MD 20
using namespace std;

main(){
    int i, n;
    char parola[MD];
    cout<<"Inserire una parola (senza spazi): ";
    cin>>parola;
    n=strlen(parola);
    i=0;
    while(parola[i]==parola[n-i-1] && i<n/2)
        i++;
    if(i<n/2)
        cout<<"Non e' palindroma";
    else
        cout<<"E' palindroma";
}
