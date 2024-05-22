#include <stdio.h>
#include <stdlib.h>
#include "matrice.h"

int main(int argc, char *argv[]) {
	FILE *fp;
	int n, **mat=NULL;
	matrice *lista = NULL;
	
	fp=fopen("matrice.txt","r");
	if(fp==NULL) printf("File input non aperto");
	n = dim(fp);
	mat = leggiFile(fp,mat,n);
	fclose(fp);
	
	scriviMatrice(mat, n);
	
	mat = trasposta(mat,n);
	scriviMatrice(mat,n);
	
	lista = MtoL(mat,lista,n);
	scriviLista(lista);
	
	lista = rimuoviDispari(lista);
	scriviLista(lista);
	
	mat = LtoM(mat,lista,n);
	
	fp=fopen("output.txt","w");
	if(fp==NULL) printf("file output non aperto");
	scriviFile(fp,mat,n);
	fclose(fp);
	
	freeLista(lista);
	freeMatrice(mat);
	return 0;
}
