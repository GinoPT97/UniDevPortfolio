#include <iostream>
#include <stdio.h>
using namespace std;

main(){
    int x, s;
    freopen("input3.txt", "r", stdin);
    s=0;
    while(cin>>x)  //cin restituisce false quando arriva alla fine del file
        s=s+x;
    cout<<"La somma dei valori contenuti nel file e': "<<s;
}
