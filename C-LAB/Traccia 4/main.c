#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "studente.h"

int main(int argc, char *argv[]) {
	
	FILE *fp;
	studente *lista = NULL;
	
	fp = fopen("dati.txt","r");
	if(fp == NULL) gestioneErrore();
	lista = leggiFile(fp,lista);
	fclose(fp);
	
	scriviLista(lista);
	
	lista = rimuovi18(lista);
	scriviLista(lista);
	
	fp = fopen("output.txt","w");
	if(fp == NULL) gestioneErrore();
	scriviFile(fp,lista);
	fclose(fp);
	
	freeLista(lista);
	return 0;
}
