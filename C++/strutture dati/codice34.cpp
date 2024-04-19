#include <iostream>
#include <string.h>
#define MD 35
using namespace std;

struct studente{ string nome;
				 float mediaVoti;
				 int crediti;
};
main(){
    int i, n;
    struct studente classe[MD];
    string invio;
    do{
        cout<<"Quanti studenti ci sono nella classe? (max "<<MD<<"): ";
        cin>>n;
    }while(n<1||n>MD);
    for(i=0;i<n;i++){
        cout<<"Inserisci i dati dello studente numero: "<<i+1<<endl;
        cout<<"Inserisci il nome dello studente: ";
        getline(cin, invio);
        getline(cin, classe[i].nome);
        cout<<"Inserisci la media dei voti: ";
        cin>>classe[i].mediaVoti;
        cout<<"Inserisci i crediti: ";
        cin>>classe[i].crediti;
    }
    for(i=0;i<n;i++){
        cout<<"Studente: "<<classe[i].nome;
        cout<<"; Media: "<<classe[i].mediaVoti;
        cout<<"; Crediti: "<<classe[i].crediti<<endl;
    }
}
