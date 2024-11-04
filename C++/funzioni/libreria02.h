#include <iostream>
#define MD 20  //SI, anche le costanti posso inserire in una libreria
using namespace std;
int leggiDimensione(){
    int n;
    do{
        cout<<"Inserisci la dimensione da 1 a "<<MD<<": ";
        cin>>n;
    }while(n<1 || n>MD);
    return n;
}

void leggiVettore(int n, float v[]){//Posso non specificare la dimensione
    for(int i=0;i<n;i++){
        cout<<"Inserisci il valore di indice "<<i<<": ";
        cin>>v[i];
    }
}

void visualizzaVettore(int n, float v[]){
    for(int i=0;i<n;i++)
        cout<<v[i]<<"  ";
    cout<<endl;
}

float minimoVettore(int n, float v[]){
    float m;
    m=v[0];
    for(int i=0; i<n; i++)
        if(v[i]<m)
            m=v[i];
    return m;
}

void ordinaVettore(int n, float v[]){
    int pos;
    float m, temp;
    for(int i=0;i<n;i++){
        m=v[i];
        pos=i;
        for(int j=i+1;j<n;j++)
            if(v[j]<m){
                m=v[j];
                pos=j;
        }
        temp=v[i];
        v[i]=v[pos];
        v[pos]=temp;
    }
}
