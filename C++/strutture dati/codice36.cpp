#include <iostream>
#include <stdio.h>  //Per la funzione freopen()
using namespace std;

main(){
    int i, x, n;
    //redirect dello standard output (stdout) sul file output.txt. "w" sta per write
    freopen("output.txt", "w", stdout);
    cin>>n;
    cout<<n<<endl;
    for(i=0;i<n;i++){
        cin>>x;
        cout<<x<<"  ";
    }
}
