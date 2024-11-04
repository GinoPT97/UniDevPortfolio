#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "libreria.h"

int main(int argc, char *argv[]) {
	FILE *fp;
	studente *lista;
	
	fp = fopen("dati.txt","r");
	if(fp==NULL) gestioneErrore();
	lista = leggiFile(fp,lista);
	fclose(fp);
	
	scriviLista(lista);
	
	lista = rimuoviEta(lista,27);
	scriviLista(lista);
	
	lista = rimuoviVoto(lista,23);
	scriviLista(lista);
	
	fp = fopen("output.txt","w");
	if(fp==NULL) gestioneErrore();
	scriviFile(fp,lista);
	fclose(fp);
	
	freeLista(lista);
	return 0;
}
