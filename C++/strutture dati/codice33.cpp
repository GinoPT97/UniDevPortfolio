#include <iostream>
#include <string.h>
using namespace std;

struct studente{ string nome;
				 float mediaVoti;
				 int crediti;
};
main(){
    struct studente s; //Dichiarazione della variabile s di tipo studente
    cout<<"Inserisci il nome dello studente: ";
    getline(cin, s.nome); //Utilizzo getline per leggere l'intera riga
    cout<<"Inserisci la media dei voti: ";
    cin>>s.mediaVoti;
    cout<<"Inserisci i crediti: ";
    cin>>s.crediti;
    cout<<"Lo studente si chiama: "<<s.nome<<endl;
    cout<<"Ha una media voti di: "<<s.mediaVoti<<endl;
    cout<<"Ha "<<s.crediti<<" crediti scolastici";
}
