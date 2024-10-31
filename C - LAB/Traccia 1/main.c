#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "luogo.h"

int main(int argc, char *argv[]) {
	FILE *fp;
	struct luogo *lista = NULL;
	float **A;
	int n;
	
	fp = fopen("dati.txt","r");
	lista = leggiFile(fp,lista);
	fclose(fp);
	
	scriviLista(lista);
	
	lista = rimuoviDuplicati(lista);
	scriviLista(lista);
	
	n = lengthLista(lista);
	A = allocaMatrice(n,n);
	matriceDistanze(lista,A);
	
	fp = fopen("output.txt","w");
    scriviFile(fp, lista,A,n);
    fclose(fp);

    freeMatrice(A);
	freeLista(lista);
	return 0;
}

