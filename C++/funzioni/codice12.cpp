#include <iostream>
using namespace std;

int fibonacci(int n){
    if(n==1 || n==2)
        return 1;  //Base
    else
        return fibonacci(n-2)+fibonacci(n-1);  //Ricorsione
}

main(){
    int n;
    do{
        cout<<"Inserisci un numero intero maggiore di 0: ";
        cin>>n;
    }while(n<1);
    cout<<"Il numero di fibonacci numero "<<n<<" vale "<<fibonacci(n);
}
