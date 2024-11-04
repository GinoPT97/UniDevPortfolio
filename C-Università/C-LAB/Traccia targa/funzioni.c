#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "auto.h"

autom *nuovoNodo(char *targa,int anno, int cilindrata, int peso){
	autom *nodo;
	
	nodo = (autom*)malloc(sizeof(autom));
	if(nodo==NULL) printf("Errore creazione nodo");
	
	strcpy(nodo->targa,targa);
	nodo->anno=anno;
	nodo->cilindrata=cilindrata;
	nodo->peso=peso;
	nodo->next=NULL;
	
	return nodo;
}

void freeNodo(autom *nodo){
	free((void*)nodo);
}

autom *inserisciLista(autom *nodo, autom*lista){
	if(lista==NULL) return nodo;
	lista->next = inserisciLista(nodo,lista->next);
	return lista;
}

void freeLista(autom *lista){
	if(lista!=NULL){
		freeNodo(lista);
		freeLista(lista->next);
	}
}

autom *leggiFile(FILE *fp,autom *lista){
	char targa[MAX];
	int anno,cilindrata,peso;
	autom *nodo;
	
	while(fscanf(fp,"%s %d %d %d\n", targa, &anno, &cilindrata, &peso)==4){
		nodo = nuovoNodo(targa, anno, cilindrata, peso);
		if(nodo==NULL) printf("Errore");
		lista = inserisciLista(nodo,lista);
	}
	return lista;
}

void scriviLista(autom *lista){
	while(lista!=NULL){
		printf("%s %d %d %d\n", lista->targa,lista->anno, lista->cilindrata, lista->peso);
		lista = lista->next;
	}
	printf("\n");
}

void scriviFile(FILE *fp, autom *lista){
	while(lista!=NULL){
		fprintf(fp,"%s %d %d %d\n", lista->targa,lista->anno, lista->cilindrata, lista->peso);
		printf("%s %d %d %d\n", lista->targa,lista->anno, lista->cilindrata, lista->peso);
		lista = lista->next;
	}
}

autom *rimuoviAnno(autom *lista, int k){
	if(!lista) return NULL;
	if(lista->anno < k) return rimuoviAnno(lista->next, k);
	lista->next = rimuoviAnno(lista->next, k);
	return lista;
}

autom *duplicaElementi(autom *lista){
   autom *nodo;
  
  if (lista==NULL) return NULL;
  lista->next = duplicaElementi(lista->next);
  if (lista->cilindrata < 1500) {
    nodo = nuovoNodo(lista->targa,lista->anno, lista->cilindrata, lista->peso);
    nodo->next = lista->next;
    lista->next = nodo;
  }
  return lista;
}
