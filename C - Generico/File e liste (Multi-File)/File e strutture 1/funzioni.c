#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "libreria.h"

void gestioneErrore(){
	printf("Errore");
	exit(0);
}

studente *nuovoNodo(char *nome, int eta, float media){
	studente *nodo;
	
	nodo = (studente *) malloc (sizeof(studente));
	if(nodo==NULL) return NULL;
	
	strcpy(nodo->nome,nome);
	nodo->eta = eta;
	nodo->media = media;
	nodo->next = NULL;
	
	return nodo;
}

void freeNodo(studente *nodo){
	free((void *)nodo);
}

studente *inserisciLista(studente *nodo, studente *lista){
	if(lista==NULL) return nodo;
	lista->next = inserisciLista(nodo, lista->next);
	return lista;
}

void freeLista(studente *lista){
	if(lista !=NULL){
		freeLista(lista->next);
		freeNodo(lista);
	}
}

void scriviLista(studente *lista){
	while(lista!=NULL){
		printf("%s %d %.2f\n", lista->nome, lista->eta, lista->media);
		lista = lista->next;
	}
	printf("\n");
}

studente *leggiFile(FILE *fp,studente *lista){
    char nome[256];
	int eta;
	float media;
	studente *nodo;
	
	while(fscanf(fp, "%s %d %f", nome, &eta, &media)==3){
		nodo = nuovoNodo(nome,eta,media);
		if(nodo==NULL) gestioneErrore();
		lista = inserisciLista(nodo, lista);
	}
	return lista;
}

studente *rimuoviEta(studente *lista, int x){
	if(!lista) return NULL;
    if(lista->eta > x) return rimuoviEta(lista->next,x);
    lista->next = rimuoviEta(lista->next,x);
    return lista;
}

studente *rimuoviVoto(studente *lista, float x){
	if(!lista) return NULL;
	if(lista->media < x) return rimuoviVoto(lista->next, x);
	lista->next = rimuoviVoto(lista->next,x);
	return lista;
}

int lengthlista(studente *lista){
	int n=0;
	while(lista!=NULL){
		n++;
		lista=lista->next;
	}
	return n;
}

void scriviFile(FILE *fp,studente *lista){
  studente **tmp, *buff;
  int n, i, j;

  n = lengthlista(lista);
  tmp = (studente **) malloc(n*sizeof(studente *));

  for (i=0; i<n; i++, lista = lista->next) {
    tmp[i] = lista;
  }

  for (i=0; i<n; i++) {
    for (j=i+1; j<n; j++) {
      if (tmp[i]->eta < tmp[j]->eta) {
	buff = tmp[i];
	tmp[i] = tmp[j];
	tmp[j] = buff;
      }
    }
  }

  for (i=0; i<n; i++) {
    buff = tmp[i];
    fprintf(fp,"%s %d %.2f\n",buff->nome,buff->eta,buff->media);
    printf("%s %d %.2f\n",buff->nome,buff->eta,buff->media);
  }

  free((void *) tmp);
}
