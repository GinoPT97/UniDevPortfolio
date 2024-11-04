#include <stdio.h>
#include <stdlib.h>
#include "libreria.h"

int main(int argc, char *argv[]) {
	FILE *fp;
	elem *lista = NULL;
	
	fp = fopen("dati.txt","r");
	if(fp==NULL) printf("Errore apertura File!");
	lista = leggiFile(fp,lista);
	fclose(fp);
	
	scriviLista(lista);
	
	lista = paridispari(lista);
	scriviLista(lista);
	
	lista = primo(lista);
	scriviLista(lista);
	
	fp = fopen("output.txt","w");
	if(fp==NULL) printf("Errore apertura File!");
	scriviFile(fp,lista);
	fclose(fp);
	
	freeLista(lista);
	
	return 0;
}
