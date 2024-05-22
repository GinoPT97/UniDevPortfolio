#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "azienda.h"

int main(int argc, char *argv[]) {
	FILE *fp;
	azienda *lista;
	
	fp = fopen("dati.txt","r");
	lista = leggiFile(fp,lista);
	fclose(fp);
	
	scriviLista(lista);
	
	lista = rimuoviAzienda(lista,2);
	scriviLista(lista);
	
	fp = fopen("output.txt","w");
	scriviFile(fp,lista);
	fclose(fp);
	
	freeLista(lista);
	return 0;
}
