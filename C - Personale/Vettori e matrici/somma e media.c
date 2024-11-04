#include <stdlib.h>
#include <stdio.h>
#define N 8

void sumavg(int v[N]);

int main(int argc, char *argv[]) {
	
	int v[N],i;
	
	for(i=0; i<N; i++){
		printf("Inserisci elemento n.%d : ", i);
		scanf("%d", &v[i]);
	}
	
	sumavg(v);
	
	return 0;
}

void sumavg(int v[N]){
	
	int s,a,i;
	
	for(i=0; i<N; i++)
	  s += v[i];
	  
	a = s/N;
	
	printf("La somma degli elementi e' : %d", s);
	printf("\nLa somma degli elementi e' : %d", a);
	
}
