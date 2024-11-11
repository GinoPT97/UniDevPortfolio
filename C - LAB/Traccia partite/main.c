#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "partita.h"

int main(int argc, char *argv[]) {
	FILE *fp;
	partita *lista = NULL;
	
	fp = fopen("dati.txt","r");
	if(fp==NULL) printf("Errore apertura del File input!");
	lista = leggiFile(fp,lista);
	fclose(fp);
	
	scriviLista(lista);
	
	lista = rimuovipartite(lista,30);
	scriviLista(lista);
	
	fp = fopen("otuput.txt","w");
	if(fp==NULL) printf("Errore apertura del File output!");
	scriviFile(fp,lista);
	fclose(fp);
    
	freeLista(lista);
	
	return 0;
}
