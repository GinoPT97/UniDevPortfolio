#include <iostream>
#include <string.h>
#define MD 20
using namespace std;
main(){
    int i, n1, n2;
    char parola1[MD], parola2[MD];
    cout<<"Inserire la prima parola (senza spazi): ";
    cin>>parola1;
    cout<<"Inserire la seconda parola (senza spazi): ";
    cin>>parola2;
    n1=strlen(parola1);
    n2=strlen(parola2);
    if(n1==n2){
        i=0;
        while(parola1[i]==parola2[i] && i<n1)
            i++;
        if(i<n1)
            cout<<"Sono diverse";
        else
            cout<<"Sono uguali";
    }
    else
        cout<<"Le due parole hanno lunghezza diversa e quindi sono diverse";
}
