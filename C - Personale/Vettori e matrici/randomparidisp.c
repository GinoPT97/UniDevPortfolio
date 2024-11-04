#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#define N 10

void paridispari(int v[N]);

int main(int argc, char *argv[]) {
	
	int v[N],i;
	
	for(i = 0; i < N; i++)
	   v[i]=1+rand()%100;
	
	paridispari(v);
	
	return 0;
}

void paridispari(int v[N]){
	
	int i,p,d;
	
	p=d=0;
	
	printf("Gli elementi del vettore sono");
	
	for(i = 0; i < N; i++)
	   printf("\nElemento in posizione n.%d : %d", i,v[i]);
	
	for(i = 0; i < N; i++){
      if(v[i]%2 == 0) p++;
      else d++;
	}
	
	printf("\nI numeri pari nel vettore sono %d", p);
	printf("\nI numeri dipari nel vettore sono %d", d);

}
