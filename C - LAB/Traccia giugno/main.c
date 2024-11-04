#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "libreria.h"

int main(int argc, char *argv[]) {
	
	struct nodo *head=NULL;
	FILE *fp;
	
	fp = fopen("dati.txt","r");
	if(fp==NULL) printf("File input non aperto");
	head = leggiFile(fp,head);
	fclose(fp);
	stampaLista(head);	 
	
	head = eliminaElementi(head,30);
	stampaLista(head);
	
	fp = fopen("output.txt","w");
	if(fp==NULL) printf("File output non aperto");
	scriviFile(fp,head);
	fclose(fp);
	 
	freeLista(head);
	
	return 0;
}

