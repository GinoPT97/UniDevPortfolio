#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "persona.h"

int main(int argc, char *argv[]) {
	
	FILE *fp;
	persona* lista;
	
	fp = fopen("input.txt","r");
	if(fp==NULL) printf("Errore apertura file");
	lista = leggiFile(fp,lista);
	fclose(fp);
	
	scriviLista(lista);
	
	fp = fopen("output.txt","w");
	if(fp==NULL) printf("Errore apertura file");
	scriviFile(fp,lista);
	fclose(fp);
	
	freeLista(lista);
	
	return 0;
}
