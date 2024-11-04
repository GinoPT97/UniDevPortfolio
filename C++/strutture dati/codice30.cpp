#include <iostream>
#include <string.h>
#define MD 20
#define MAXPAROLE 50
using namespace std;

main(){
    int n, i, j, pos;
    char parole[MAXPAROLE][MD], minimo[MD], temp[MD];
    do{
        cout<<"Quante parole vuoi inserire? min=1, max="<<MAXPAROLE<<": ";
        cin>>n;
    }while(n<1||n>MAXPAROLE);
    for(i=0;i<n;i++){
        cout<<"Inserire una (senza spazi): ";
        cin>>parole[i];
    }
    //Utilizziamo l'ordinamento per selezione
    for(int i=0;i<n;i++){
        //le stringhe sono vettori quindi non si può scrivere minimo=parole[i]. strcpy = string copy
        strcpy(minimo, parole[i]);
        pos=i;
        for(j=i+1;j<n;j++)
            if(strcmp(parole[j], minimo)==-1){
                strcpy(minimo, parole[j]);
                pos=j;
        }
        strcpy(temp, parole[pos]);
        strcpy(parole[pos], parole[i]);
        strcpy(parole[i], temp);
    }
    cout<<"Le parole in ordine alfabetico sono\n";
    for(i=0;i<n;i++)
        cout<<parole[i]<<endl;
}
