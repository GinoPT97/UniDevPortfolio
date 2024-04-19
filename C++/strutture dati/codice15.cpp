#include <iostream>
using namespace std;
main(){
    int i, n, x, resti[32];
    do{
        cout<<"Inserisci un numero positivo: ";
        cin>>n;
    }while(n<=0);
    x=n;
    for(i=0;i<32;i++){
        resti[i]=x%2;
        x=x/2;
    }
    cout<<"Il numero "<<n<<" in base 2 si scrive: ";

    for(i=31; i>=0 && resti[i]==0; i--);

    for(;i>=0;i--)
        cout<<resti[i];
}
