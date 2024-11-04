#include <stdio.h>
#include <stdlib.h>

int coefbinom(int n,int k){
	if(k == 0)return 1; 
    if(n <= k) return 0; 
    return (n*coefbinom(n-1,k-1))/k; 
}

int main(int argc, char *argv[]) {
	int n,k;
	
	printf("Passami il primo intero : ");
	scanf("%d", &n);
	printf("Passami il secondo intero : ");
    scanf("%d", &k);
    printf("Coefficente Binomiale : %d\n", coefbinom(n, k)); 
	
	return 0;
	
}
