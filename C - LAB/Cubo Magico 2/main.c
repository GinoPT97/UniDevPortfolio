#include <stdio.h>
#include <stdlib.h>
#include"cubo.h"


int main(int argc, char *argv[]) {
	
	FILE *fp;
	int n, **mat;
	list *l;
	
	fp=fopen("dati.txt","r");
	if(fp==NULL) printf("file non aperto");
	n = dimensione(fp);
	mat = leggiFile(fp,mat,n);
	fclose(fp);
	
	scriviMatrice(mat,n);
	
	mat = trasposta(mat,n);
	scriviMatrice(mat,n);
	
	l = MtoL(mat,l,n);
	scriviLista(l);
	
	l = eliminadispari(l);
	scriviLista(l);
	
	mat = LtoM(mat,l,n);
	scriviMatrice(mat,n);
	
	fp=fopen("output.txt","w");
	if(fp==NULL) printf("file non aperto");
	scriviFile(fp,mat,n);
	
	fclose(fp);
	freeLista(l);
	freeMatrice(mat);
	
	return 0;
}
