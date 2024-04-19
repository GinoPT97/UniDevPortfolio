#include <iostream>
#include <stdio.h>
using namespace std;

main(){
    int i1, i2, n1, n2, x1, x2;
    FILE *in1, *in2, *out;
    in1=fopen("input.txt", "rt");
    in2=fopen("input2.txt", "rt");
    out=fopen("output.txt", "wt");
    fscanf(in1, "%d", &n1);
    fscanf(in2, "%d", &n2);
    fprintf(out, "%d\n", n1+n2);
    i1=i2=0;
    fscanf(in1, "%d", &x1);
    fscanf(in2, "%d", &x2);
    while(i1<n1 && i2<n2)
        if(x1<x2){
            fprintf(out, "%d ", x1);
            fscanf(in1, "%d", &x1);
            i1++;
        }
        else{
            fprintf(out, "%d ", x2);
            fscanf(in2, "%d", &x2);
            i2++;
        }
    if(i1<n1)
        for(;i1<n1;i1++){
            fprintf(out, "%d ", x1);
            fscanf(in1, "%d", &x1);
        }
    else
        for(;i2<n2;i2++){
            fprintf(out, "%d ", x2);
            fscanf(in2, "%d", &x2);
        }
}
