#include <iostream>
#include <stdio.h>
using namespace std;

main(){
    string s;
    freopen("elenco.txt", "r", stdin);
    while(getline(cin, s))  //getline legge l'intera linea fino all'invio (e quindi anche gli spazi)
        cout<<s<<endl;
}
