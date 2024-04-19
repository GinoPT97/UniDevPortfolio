#include <iostream>
#define MD 20
using namespace std;

main(){
    int i, j, r, c, a, b, mat[MD][MD], temp;
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
    do{
        cout<<"Quale riga vuoi scambiare? min=0,  max="<<r-1<<": ";
        cin>>a;
    }while(a<0 || a>r-1);
    do{
        cout<<"Con quale? min=0,  max="<<r-1<<": ";
        cin>>b;
    }while(b<0 || b>r-1);
    //Una riga è un vettore di lunghezza pari al numero delle colonne
    for(j=0;j<c;j++){
        temp=mat[a][j];
        mat[a][j]=mat[b][j];
        mat[b][j]=temp;
    }


    //Visualizzazione della matrice
    cout<<"I numeri inseriti dopo lo scambio delle righe sono i seguenti: \n";
    for(i=0;i<r;i++){
        for(j=0;j<c;j++)
            cout<<mat[i][j]<<"  ";
        cout<<endl;//Un acapo alla fine di ogni riga
    }
}
