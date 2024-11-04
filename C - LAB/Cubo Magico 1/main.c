#include <stdio.h>
#include <stdlib.h>
#include "matrice.h"

int main(int argc, char *argv[]) {
	
	FILE *fp;
	int n, m, **mat=NULL;
	list *l = NULL;
	
	fp=fopen("dati.txt","r");
	if(fp==NULL) printf("file non aperto");
	n = dim1(fp);
	m = dim2(fp);
	if(n==m) printf("E' un quadrato\n");
	mat = leggiFile(fp,mat,n,n);
	fclose(fp);
	
	scriviMatrice(mat,n,m);
	
	verifica(mat,n,m);
	
	l = MtoL(mat,l,n,m);
	scriviLista(l);
	
	l = rimuoviDispari(l);
	scriviLista(l);
	mat = LtoM(mat,l,n,m);
	
	fp=fopen("output.txt","w");
	if(fp==NULL) printf("file non aperto");
	scriviFile(fp,mat,n,m);
	fclose(fp);
	
	freeLista(l);
	freeMatrice(mat);
	
	return 0;
}
