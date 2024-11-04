#include <iostream>
using namespace std;

int main(){
    int i, j, n1, n2, cont, trovato;
    float v1[10], v2[10];
    //Lettura dimensioni e vettori
    cout<<"Inserisci la dimensione del primo vettore: ";
    cin>>n1;
    for(i=0;i<n1;i++){
        cout<<"Inserisci elemento del primo vettore: ";
        cin>>v1[i];
    }
    cout<<"Inserisci la dimensione del secondo vettore: ";
    cin>>n2;
    for(i=0;i<n2;i++){
        cout<<"Inserisci elemento del secondo vettore: ";
        cin>>v2[i];
    }
    //Prima versione
    cont=0;
    for(i=0;i<n1;i++){
        j=0;
        while(v1[i]!=v2[j]&&j<n2)
            j++;
        if(j<n2)
            cont++;
    }
    cout<<"Il numero degli elementi del primo vettore presenti nel secondo e': "<<cont<<endl;
    //Seconda versione
    cont=0;
    for(i=0;i<n1;i++){
        trovato=0;
        for(j=0;j<n2 && trovato==0;j++)
            if(v1[i]==v2[j])
                trovato=1;
        if(trovato==1)
            cont++;
    }
    cout<<"Il numero degli elementi del primo vettore presenti nel secondo e': "<<cont<<endl;
}
