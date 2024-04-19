#include <iostream>
#define MD 20
using namespace std;

int main(){
    int i, j, i1, i2, i3, n1, n2, pos;
    float v1[MD], v2[MD], v3[2*MD], temp, minimo;
    do{
        cout<<"Inserisci la dimensione del primo vettore da 1 a "<<MD<<": ";
        cin>>n1;        
    }while(n1<1||n1>MD);
    cout<<"Inserisci i valori del primo vettore\n";
    for(i=0;i<n1;i++){
        cout<<"Inserisci un numero: ";
        cin>>v1[i];
    }
    do{
        cout<<"Inserisci la dimensione del secondo vettore da 1 a "<<MD<<": ";
        cin>>n2;        
    }while(n2<1||n2>MD);
    cout<<"Inserisci i valori del secondo vettore\n";
    for(i=0;i<n2;i++){
        cout<<"Inserisci un numero: ";
        cin>>v2[i];
    }
    
    for(i=0;i<n1;i++){
        minimo=v1[i];
        pos=i;
        for(j=i+1;j<n1;j++)
            if(v1[j]<minimo){
                minimo=v1[j];
                pos=j;
            }
        temp=v1[pos];
        v1[pos]=v1[i];
        v1[i]=temp;
    }
    
    for(i=0;i<n2;i++){
        minimo=v2[i];
        pos=i;
        for(j=i+1;j<n2;j++)
            if(v2[j]<minimo){
                minimo=v2[j];
                pos=j;
            }
        temp=v2[pos];
        v2[pos]=v2[i];
        v2[i]=temp;
    }
    //Elaborazione
    i1=0;
    i2=0;
    i3=0;
    while(i1<n1 && i2<n2)
        if(v1[i1]<v2[i2]){
            v3[i3]=v1[i1];
            i1++;
            i3++;
        }
        else{
            v3[i3]=v2[i2];
            i2++;
            i3++;
        }
    if(i1<n1)
        while(i1<n1){
            v3[i3]=v1[i1];
            i1++;
            i3++;
        }
    else
        while(i2<n2){
            v3[i3]=v2[i2];
            i2++;
            i3++;
        }
    cout<<"Il vettore ordinato ottenuto per fusione e' il seguente:\n";
    for(i=0;i<n1+n2;i++)
        cout<<v3[i]<<"  ";
    return 0;
}
