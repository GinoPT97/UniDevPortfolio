#include <iostream>
#include <stdio.h>  //Per la funzione freopen()
using namespace std;

main(){
    int i, x, n;
    //redirect dello standard input (stdin) sul file input.txt. "r" sta per read
    freopen("input.txt", "r", stdin);
    cin>>n;
    for(i=0;i<n;i++){
        cin>>x;
        cout<<x<<"  ";
    }
}
