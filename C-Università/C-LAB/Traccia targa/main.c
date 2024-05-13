#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "auto.h"

int main(int argc, char *argv[]) {
	
	FILE *fp;
	autom *lista;
	
	fp = fopen("dati.txt","r");
	if(fp==NULL) printf("File input non aperto");
	lista = leggiFile(fp,lista);
	fclose(fp);
	
	scriviLista(lista);
	
	lista = rimuoviAnno(lista,2000);
	scriviLista(lista);
	
	lista = duplicaElementi(lista);
	
	fp = fopen("output.txt","w");
	if(fp==NULL) printf("File output non aperto");
	scriviFile(fp,lista);
	fclose(fp);
	
	freeLista(lista);
	
	return 0;
}
