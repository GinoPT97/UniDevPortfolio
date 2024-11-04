#include <iostream>
#define MD 20
using namespace std;

main(){
    int i, j, r, c, mat[MD][MD];
    do{
        cout<<"Quante righe? min=1,  max="<<MD<<": ";
        cin>>r;
    }while(r<1 || r>MD);
    do{
        cout<<"Quante colonne? min=1,  max="<<MD<<": ";
        cin>>c;
    }while(c<1 || c>MD);
    //Lettura della matrice
    for(i=0;i<r;i++)
        for(j=0;j<c;j++){
            cout<<"inserisci un numero intero ";
            cin>>mat[i][j];
    }
    //Visualizzazione della matrice
    cout<<"I numeri inseriti sono i seguenti: \n";
    for(i=0;i<r;i++){
        for(j=0;j<c;j++)
            cout<<mat[i][j]<<"  ";
        cout<<endl;//Un acapo alla fine di ogni riga
    }
}
