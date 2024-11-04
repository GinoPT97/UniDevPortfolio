#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "partita.h"

partita *nuovonodo(char *nome, int punteggio, int giocate, int vinte, int pareggiate, int perse){
	partita *nodo;
	
	nodo = (partita *) malloc(sizeof(partita));
	if(nodo==NULL) return NULL;
	
	strcpy(nodo->nome,nome);
	nodo->punteggio = punteggio;
	nodo->giocate = giocate;
	nodo->vinte = vinte;
	nodo->pareggiate = pareggiate;
	nodo->perse = perse;
	nodo->next = NULL;
	
	return nodo;
}

void freeNodo(partita *nodo){
	free((void *)nodo);
}

partita *inserisciLista(partita *nodo, partita *lista){
	if(lista==NULL) return nodo;
	lista->next = inserisciLista(nodo,lista->next);
	return lista;
}

void freeLista(partita *lista){
	if(lista!=NULL){	
	freeLista(lista->next);
	freeNodo(lista);
    }
}

partita *leggiFile(FILE *fp,partita *lista){
	char nome[M];
	int punteggio, giocate, vinte, pareggiate, perse;
	partita *nodo;
	
	while(fscanf(fp,"%s %d %d %d %d %d", nome, &punteggio, &giocate, &vinte, &pareggiate, &perse)==6){
		nodo = nuovonodo(nome, punteggio, giocate, vinte, pareggiate, perse);
		if(nodo==NULL) printf("Errore inserimento nodo!");
		lista = inserisciLista(nodo,lista);
	}
	return lista;
}

void scriviLista(partita *lista){
	while(lista!=NULL){
		printf("%s %d %d %d %d\n",lista->nome, lista->punteggio ,lista->giocate, lista->vinte, lista->pareggiate, lista->perse);
		lista = lista->next;
	}
	printf("\n");
}

partita *rimuoviNodo(partita *nodo,partita *lista){
  partita *tmp;
  
  if (lista == nodo) {
    tmp = lista->next;
    freeNodo(lista);
    return tmp;
  }

  lista->next = rimuoviNodo(lista->next,nodo);
}

partita *rimuovipartite(partita *lista,int x){
	if(lista==NULL) return lista;
	if(lista->punteggio < x){
		lista = rimuoviNodo(lista,lista);
		lista = rimuovipartite(lista,x);
	}else lista->next = rimuovipartite(lista->next,x);
	return lista;
}

static int lengthLista(partita *lista)
{
  int n = 0;

  while(lista!=NULL) {
    n++;
    lista = lista->next;
  }

  return n;
}

void scriviFile(FILE *fp, partita *lista){
	partita **tmp, *buff;
    int n, i, j;

    n = lengthLista(lista);
    tmp = (partita **) malloc(n*sizeof(partita *));

    for (i=0; i<n; i++, lista = lista->next) {
      tmp[i] = lista;
    }

  for (i=0; i<n; i++) {
    for (j=i+1; j<n; j++) {
      if (tmp[i]->punteggio < tmp[j]->punteggio) {
	buff = tmp[i];
	tmp[i] = tmp[j];
	tmp[j] = buff;
      }
    }
  }

  for (i=0; i<n; i++) {
    buff = tmp[i];
    fprintf(fp,"%s %d %d %d %d %d\n",buff->nome,buff->punteggio,buff->giocate,buff->vinte,buff->pareggiate,buff->perse);
    printf("%s %d %d %d %d\n",buff->nome,buff->punteggio,buff->giocate,buff->vinte,buff->pareggiate,buff->perse);
  }

  free((void *) tmp);
}
