#include <iostream>
#define MD 20
using namespace std;
int leggiDimensione(){
    int n;
    do{
        cout<<"Inserisci la dimensione da 1 a "<<MD<<": ";
        cin>>n;
    }while(n<1 || n>MD);
    return n;
}

void leggiVettore(int n, float v[]){//Posso non specificare la dimensione
    for(int i=0;i<n;i++){
        cout<<"Inserisci il valore di indice "<<i<<": ";
        cin>>v[i];
    }
}

void leggiMatriceQuadrata(int n, float m[][MD]){  //Posso omettere solo la prima dimensione
    for(int i=0;i<n;i++)
        for(int j=0;j<n;j++){
            cout<<"Inserisci il vlore di indici "<<i<<" - "<<j<<": ";
            cin>>m[i][j];
    }
}

void triangolarizzaSistema(int n, float a[][MD], float b[]){
    int i, j, k, pos;
    float coeff;
    for(j=0;j<n-1;j++){
//        pos=cercaMaxDen(n, a);
//        scambiaRighe(n, a, j, pos);
        for(i=j+1;i<n;i++){
            coeff=-a[i][j]/a[j][j];
            for(k=j;k<n;k++)
                a[i][k]=coeff*a[j][k]+a[i][k];
            b[i]=coeff*b[j]+b[i];
        }
    }
}

void risolviSistemaTriangolare(int n, float a[][MD], float b[], float x[]){
    int i, j;
    float s;
    for(i=n-1;i>=0;i--){
        s=0.0;
        for(j=i+1;j<n;j++)
            s=s+a[i][j]*x[j];
        x[i]=(b[i]-s)/a[i][i];
    }
}

void visualizzaVettore(int n, float v[]){
    for(int i=0;i<n;i++)
        cout<<v[i]<<"  ";
    cout<<endl;
}
