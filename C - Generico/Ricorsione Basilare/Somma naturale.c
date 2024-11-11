#include <stdio.h>
#include <stdlib.h>

int sommanaturale(int x){
	int ris;
	while(x != 0){
		ris += x % 10;
		x = x / 10;
	}
	if(ris < 10) return ris;
	else return sommanaturale(ris);
}

int main(int argc, char *argv[]){
	int i=0;
	printf("Passami un intero di cui faro la somma delle sue cifre : ");
	scanf("%d", &i);
	printf("Ecco il risultato : %d", sommanaturale(i));
	return 0;
}
