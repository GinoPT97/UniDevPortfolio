#include <stdio.h>
#include <stdlib.h>

int cobin(int n, int k) {
    if(n<k || n<0 || k<0) return printf("Errore");
    if(k==0 || k==n) return 1;
    else return cobin(n-1,k-1) + cobin(n-1,k);
} 

int main(int argc, char *argv[]){
	
	int n,k,N = 0;
	
	printf("Inserire numero di righe del triangolo di tartaglia da visualizzare : ");
	scanf("%d", &N);
	
	for(n=0; n<=N;n++){
		printf("\n");
		for(k=0; k<=n;k++){
			printf("%5d", cobin(n, k));
		}
	}
    return 0;
}
