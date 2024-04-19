#include <iostream>
#define MD 20
using namespace std;

int main(){
    int i, j, n;
    float v[MD], temp;
    do{
        cout<<"Inserisci la dimensione del vettor da 1 a "<<MD<<": ";
        cin>>n;
    }while(n<1 || n>MD);
    for(i=0;i<n;i++){
        cout<<"Inserisci un numero. ";
        cin>>v[i];
    }
    //Inizio ordinamento
    for(i=0;i<n;i++)
        for(j=0;j<n-i-1;j++)
            if(v[j]>v[j+1]){
                temp=v[j];
                v[j]=v[j+1];
                v[j+1]=temp;
            }
    //Fine ordinamento
    cout<<"\nIl vettore ordinato e' il seguente:\n";
    for(i=0;i<n;i++)
        cout<<v[i]<<"  ";
    return 0;
}
