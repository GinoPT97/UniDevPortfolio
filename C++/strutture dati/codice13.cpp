#include <iostream>
#define MD 20
using namespace std;

main(){
    int i, n, np, nd, v[MD], vp[MD], vd[MD];
    do{
        cout<<"Inserisci la dimensione del vettore da 1 a "<<MD<<": ";
        cin>>n;
    }while(n<1||n>MD);
    for(i=0;i<n;i++){
        cout<<"Inserisci un numero intero: ";
        cin>>v[i];
    }
    np=0;
    nd=0;
    for(i=0;i<n;i++)
        if(v[i]%2==0){
            vp[np]=v[i];
            np++;
        }
        else{
            vd[nd]=v[i];
            nd++;
        }
    cout<<"Il vettore dei pari e' il seguente\n";
    for(i=0;i<np;i++)
        cout<<vp[i]<<"  ";

    cout<<"Il vettore dei dispari e' il seguente\n";
    for(i=0;i<nd;i++)
        cout<<vd[i]<<"  ";
}
