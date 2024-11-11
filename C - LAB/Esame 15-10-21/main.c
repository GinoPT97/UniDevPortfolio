#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "libreria.h"

int main(int argc, char *argv[]) {
	
	FILE *fp;
	studente *lista=NULL;
	
	fp = fopen("dati.txt","r");
	if(fp == NULL) printf("File input non aperto");
	lista = leggiFile(fp,lista);
	fclose(fp);
	
	scriviLista(lista);
	
	lista = rimuoviPeso(lista,12);
	scriviLista(lista);
	
	lista = duplicaElementi(lista);
	
	fp = fopen("output.txt","w");
	if(fp == NULL) printf("File output non aperto");
	scriviFile(fp,lista);
	fclose(fp);
	
	freeLista(lista);
	return 0;
}
