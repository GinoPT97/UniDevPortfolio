#include <stdio.h>
#include <stdlib.h>

int sommapari(int n){
	if(n==1) return 2;
	else return 2*n+sommapari(n-1); //somma intesa come S = 2*1+2*2+....+2*n
}

int main(int argc, char *argv[]){
	
	int i = 0;
	printf("Faro la somma dei primi n numeri,passami l'intero : ");
	scanf("%d", &i);
	printf("Ecco la somma dei %d numeri pari = %d", i,sommapari(i));
    return 0;
}
