#include <iostream>
#include <stdio.h>
using namespace std;

main(){
    int i, x, n;
    FILE *in; //Dichiaro un puntatore a file
    in=fopen("input.txt", "rt"); //r=read, t=text
    fscanf(in, "%d", &n); //Come la scanf() con un parametro in più, il puntatore al file
    printf("%d\n", n);
    for(i=0;i<n;i++){
        fscanf(in, "%d", &x);
        printf("%d ", x);
    }
}
