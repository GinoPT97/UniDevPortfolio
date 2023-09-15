#include "libreria.h"

int main(int argc, char *argv[]) {
	
	int n, i, *v;
	
	printf("Dimmi la dimensione del vettore : ");
	scanf("%d", &n);
	
	v = creavettore(v,n);
	
	selsort(v,n);
	
	freevettore(v);
	return 0;
}
