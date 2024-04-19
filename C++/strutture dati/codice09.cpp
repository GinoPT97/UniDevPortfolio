#include <iostream>
#define MD 20
using namespace std;
int main(){
    int n, x, v[MD];
    int i, sx, dx, c;
    do{
        cout<<"Inserisci dimensione vettore da 1 a "<<MD<<": ";
        cin>>n;
    }while(n<1||n>MD);
    for(i=0;i<n;i++){
        cout<<"Inserisci valore intero in ordine crescente: ";
        cin>>v[i];
    }
    cout<<"Inserisci il valore da cercare: ";
    cin>>x;
    sx=0;
    dx=n-1;
    c=(sx+dx)/2;
    while(v[c]!=x && sx<=dx){
        if(x<v[c])
            dx=c-1;
        else
            sx=c+1;
        c=(sx+dx)/2;
    }
    if(x==v[c])
        cout<<"L'elemento e' presente";
    else
        cout<<"L'elemento non è presente";
    return 0;
}
