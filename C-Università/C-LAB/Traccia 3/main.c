#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "persona.h"


int main(int argc, char *argv[]) {
	FILE *fp;
	struct persona *lista = NULL;
	
	fp = fopen("dati.txt","r");
	if (fp == NULL) gestioneErrore();
	lista = leggiFile(fp,lista);
	fclose(fp);
	
	scriviLista(lista);
	
	lista = rimuoviPesoMinore(lista,12);
	scriviLista(lista);
	
	lista = duplicaElementi(lista);
	scriviLista(lista);
	
	fp = fopen("output.txt","w");
	scriviFile(fp,lista);
	fclose(fp);
	
	freeLista(lista);
	return 0;
}
