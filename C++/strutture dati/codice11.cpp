//Leggere "n" vettori della stessa lunghezza e sommarli.
#include <iostream>
#define MD 20
using namespace std;

int main(){
    int i, j, n, m;//n è la dimensione dei vettori, m è il numero dei vettori da sommare
    float v[MD], vSomma[MD];
    do{
        cout << "Quanti vettori vuoi sommare? ";
        cin>>m;
    }while(m<1);  //Non metto un limite superiore

    do{
        cout << "Inserisci la dimensione dei vettori da 1 a "<<MD<<": ";
        cin>>n;
    }while(n<1 || n>MD);

    for(i=0;i<n;i++)
        vSomma[i]=0.0; //Azzero il vettore somma
    for(i=0;i<m;i++){
        cout<<"Inserisci gli elementi del vettore n.: "<<i+1<<endl;
        for(j=0;j<n;j++){
            cout<<"Inserisci un numero: ";
            cin>>v[j];
        }
        for(j=0;j<n;j++)
            vSomma[j]=vSomma[j]+v[j];
    }
    //Visualizzo il vettore somma
    cout<<"Il vettore somma è il seguente\n";
    for(i=0;i<n;i++)
        cout<<vSomma[i]<<"  ";
}
