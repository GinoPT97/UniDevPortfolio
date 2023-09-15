#include <stdlib.h>
#include <stdio.h>
#define N 8

void max(int v[N]);
void min(int v[N]);

int main(int argc, char *argv[]) {
	
	int v[N],i;
	
	for(i=0; i<N; i++){
		printf("Inserisci elemento n.%d : ", i);
		scanf("%d", &v[i]);
	}
	
	max(v);
	min(v);
	
	return 0;
}

void max(int v[N]){
	int max,i,x;
	
	max=x=0;
	
	for(i=0; i<N; i++){
		if(v[i]>max){
			max = v[i];
			x = i; 
		}
	}
	
	printf("Il massimo e' %d ed e' in posizione n.%d", max,x);
}

void min(int v[N]){
	int min,i,x;
	
	x = 0;
	min = v[x];
	
	for(i=0; i<N; i++){
		if(v[i]<min){
			min = v[i];
			x = i; 
		}
	}
	
	printf("\nIl minimo e' %d ed e' in posizione n.%d", min,x);
}
