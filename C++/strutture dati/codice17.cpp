#include <iostream>
#define MD 20
using namespace std;

int main(){
    int i, n, v[MD], cont;
    int minimo, pos, j, temp;
    do{
        cout<<"Inserisci la dimensione del vettore da 1 a "<<MD<<": ";
        cin>>n;
    }while(n<1||n>MD);
    for(i=0;i<n;i++){
        cout<<"Inserisci un numero: ";
        cin>>v[i];
    }
    for(i=0;i<n;i++){
        minimo=v[i];
        pos=i;
        for(j=i+1;j<n;j++)
            if(v[j]<minimo){
                minimo=v[j];
                pos=j;
            }
        temp=v[pos];
        v[pos]=v[i];
        v[i]=temp;
    }
    cont=0;
    for(i=0;i<n-1;i++)
        if(v[i]!=v[i+1])
            cont++;
    cout<<"I valori distinti sono: "<<cont+1;
    return 0;
}
