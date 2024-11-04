#include <iostream>
using namespace std;

int fattoriale(int n){
    if(n==0)
        return 1;  //Base
    else
        return n*fattoriale(n-1);  //Ricorsione
}

main(){
    int n;
    do{
        cout<<"Inserisci un numero intero positivo: ";
        cin>>n;
    }while(n<0);
    cout<<"Il fattoriale di "<<n<<" vale "<<fattoriale(n);
}
