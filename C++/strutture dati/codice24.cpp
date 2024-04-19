#include <iostream>
#define MD 20
using namespace std;

main(){
    int i, j, r, c, mat[MD][MD], minimi[MD];
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
    for(i=0;i<r;i++){
        minimi[i]=mat[i][0];
        for(j=1;j<c;j++)
            if(mat[i][j]<minimi[i])
                minimi[i]=mat[i][j];
    }
    cout<<"Il vettore dei minimi per riga e' il seguente\n";
    for(int i=0;i<r;i++)
        cout<<minimi[i]<<endl;;
}
