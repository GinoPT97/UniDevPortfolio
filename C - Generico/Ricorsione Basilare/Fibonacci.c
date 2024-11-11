#include <stdio.h>
#include <stdlib.h>

int fibonacci(int n){
	if(n==1) return 1;
	if(n==0) return 0;
	else return fibonacci(n-1)+fibonacci(n-2);
}

int main(int argc, char *argv[]){
	
	int n=0;
	printf("Dimmi il passo di fibonacci a cui vuoi arrivare : ");
	scanf("%d", &n);
	printf("Ecco la succezzione : %d", fibonacci(n));
	
    return 0;
}
