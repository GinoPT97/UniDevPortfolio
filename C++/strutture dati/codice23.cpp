#include <iostream>
#define MD 20
using namespace std;

main(){
    int i, j, r, c, a, mat[MD][MD], minimo;
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
        cout<<"Di quale colonna vuoi trovare il minimo? min=0,  max="<<c-1<<": ";
        cin>>a;
    }while(a<0 || a>c-1);

    minimo=mat[0][a];
    for(i=1;i<r;i++)
        if(mat[i][a]<minimo)
            minimo=mat[i][a];
    cout<<"Il minimo della colonna "<<a<<" e' "<<minimo;
}
