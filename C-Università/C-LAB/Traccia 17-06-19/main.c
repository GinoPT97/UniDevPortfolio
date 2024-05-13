#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "classe.h"

int main(int argc, char *argv[]){
	
	FILE *fp;
	classe *lista;
	
	fp = fopen("dati.txt","r");
	if(fp==NULL) gestioneErrore();
	lista = leggiFile(fp,lista);
	fclose(fp);
	
	scriviLista(lista);
	
	lista = rimuoviElementi(lista);
	scriviLista(lista);
	
	fp = fopen("otuput.txt","w");
	scriviFile(fp,lista);
	fclose(fp);
    
	freeLista(lista);
	
	return 0;
}
