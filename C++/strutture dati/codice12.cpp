#include <iostream>
#define MD 20
using namespace std;

int main(){
    int i, n1, n2;//Due vettori, due dimensioni diverse
    float v1[MD], v2[MD], v3[2*MD];//Il vettore finale può essere di 2MD valori
    do{
        cout << "Inserisci la dimensione del primo vettore da 1 a "<<MD<<": ";
        cin>>n1;
    }while(n1<1 || n1>MD);
    cout<<"Inserisci gli elementi del primo vettore\n";
    for(i=0;i<n1;i++){
        cout<<"Inserisci un numero: ";
        cin>>v1[i];
    }
    do{
        cout << "Inserisci la dimensione del secondo vettore da 1 a "<<MD<<": ";
        cin>>n2;
    }while(n2<1 || n2>MD);
    cout<<"Inserisci gli elementi del primo vettore\n";
    for(i=0;i<n2;i++){
        cout<<"Inserisci un numero: ";
        cin>>v2[i];
    }
    //Copio gli elementi del primo vettore nei primi posti del terzo vettore
    for(i=0;i<n1;i++)
        v3[i]=v1[i];
    //Copio gli elementi del secondo vettore nel terzo DOPO quelli già inseriti
    //quindi devo iniziare dall'indice n1 perchè da 0 a n1-1 ci sono gli elementi del primo vettore
    for(i=0;i<n2;i++)
        v3[n1+i]=v2[i];
    //Visualizzo il vettore v3
    //Avete notato che non ho dichiarato n3? non serve perchè n3=n1+n2 quindi...
    cout<<"Il vettore concatenazione è il seguente\n";
    for(i=0;i<n1+n2;i++)
        cout<<v3[i]<<"  ";
}
