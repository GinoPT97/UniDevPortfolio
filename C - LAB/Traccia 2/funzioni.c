#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "azienda.h"

void gestioneErrore(){
	printf("Errore\n");
	exit(0);
}

azienda *nuovoNodo(char *nome, int n){
	azienda *nodo;
	int i;
	
	nodo = (azienda *) malloc(sizeof(azienda));
	if(nodo==NULL) return NULL;
	
	strcpy(nodo->nome,nome);
	nodo->n=n;
	nodo->citta = (char **) malloc(n*sizeof(char *));
	for(i=0;i<n;i++)
	   nodo->citta[i] = (char *) malloc(256*sizeof(char));
	nodo->next=NULL;
	  
	return nodo;
}

void freeNodo(azienda *nodo){
	int i;

    for (i=0; i<nodo->n; i++)
      free((void *) nodo->citta[i]);
    free((void *) nodo->citta);
	free((void*)nodo);
}

azienda *inserisciLista(azienda *nodo, azienda *lista){
	if(lista==NULL) return nodo;
	lista->next = inserisciLista(nodo,lista->next);
	return lista;
}

void freeLista(azienda *lista){
	if(lista!=NULL){
		freeLista(lista->next);
	freeNodo(lista);
	}
}

void scriviLista(struct azienda *lista)
{
  int i;
  
  while (lista!=NULL) {
    printf("%s %d ",lista->nome,lista->n);
    for (i=0; i<lista->n; i++) {
      printf("%s ",lista->citta[i]);
    }
    printf("\n");
    lista = lista->next;
  }
  printf("\n");
}

azienda *leggiFile(FILE *fp, struct azienda *lista){
	char nome[256], citta[256];
	int n,i;
	azienda *nodo;
	
	while(fscanf(fp,"%s %d", nome, &n)==2){
		nodo = nuovoNodo(nome,n);
		if(nodo==NULL) gestioneErrore();
		for(i=0;i<n;i++)
		   fscanf(fp,"%s",nodo->citta[i]);
		lista = inserisciLista(nodo,lista);
	}
	return lista;
}
static azienda *rimuoviNodo(azienda *nodo,azienda *lista)
{
  struct azienda *tmp;
  
  if (lista == nodo) {
    tmp = lista->next;
    freeNodo(lista);
    return tmp;
  }

  lista->next = rimuoviNodo(lista->next,nodo);
}

azienda *rimuoviAzienda(azienda *lista,int soglia){
	if(lista==NULL) return lista;
	if(lista->n<soglia){
		lista = rimuoviNodo(lista,lista);
		lista = rimuoviAzienda(lista,soglia);
	}else lista->next = rimuoviAzienda(lista->next,soglia);
	return lista;
}

void scriviFile(FILE *fp, struct azienda *lista){
	int *buffer, n, i, j, count;
  struct azienda*ptr1, *ptr2;

  while (lista!=NULL) {
    n = lista->n;
    fprintf(fp,"%s %d ", lista->nome, n);
    printf("%s %d ", lista->nome, n);
    buffer = (int *) calloc(n,sizeof(int));

    for (i=0; i<n; i++) {
      count = 0;
      if (!buffer[i]) {
	buffer[i] = 1;
	count++;
	fprintf(fp,"%s ",lista->citta[i]);
	printf("%s ",lista->citta[i]);
	for (j=i+1; j<n; j++) {
	  if (strcmp(lista->citta[i],lista->citta[j])==0) {
	    buffer[j] = 1;
	    count++;
	  }
	}
	fprintf(fp,"%d ",count);
	printf("%d ",count);
      }
    }
    fprintf(fp,"\n");
    printf("\n");
    free((void *) buffer);
    lista = lista->next;
  }
}
