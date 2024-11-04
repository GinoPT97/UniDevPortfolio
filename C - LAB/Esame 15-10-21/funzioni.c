#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "libreria.h"

void gestioneErrore(){
	printf("Errore\n");
	exit(0);
}

studente *nuovoNodo(char *nome, char *cognome, int peso, int altezza, int eta){
	studente *nodo;
	
	nodo = (studente *) malloc(sizeof(studente));
	if(nodo==NULL) return NULL;
	
	strcpy(nodo->nome,nome);
	strcpy(nodo->cognome, cognome);
	nodo->peso = peso;
	nodo->altezza = altezza;
	nodo->eta = eta;
	nodo->next = NULL;
	
	return nodo;
}

void freeNodo(studente *nodo){
	free((void *)nodo);
}

studente *inserisciLista(studente *nodo, studente *lista){
	if(lista==NULL)return nodo;
	lista->next = inserisciLista(nodo,lista->next);
	return lista;
}

void freeLista(studente *lista){
	if(lista!=NULL){
	freeLista(lista->next);
	freeNodo(lista);
   }
}

studente *leggiFile(FILE *fp,studente *lista){
	char nome[MAX], cognome[MAX];
	int peso,altezza,eta;
	studente *nodo;
	
	while(fscanf(fp,"%s %s %d %d %d\n", nome, cognome, &peso, &altezza, &eta)==5){
		nodo = nuovoNodo(nome,cognome,peso,altezza,eta);
		if(nodo==NULL) printf("Errore creazione nodo");
		lista = inserisciLista(nodo,lista);
	}
	return lista;
}

void scriviLista(studente *lista){
	while(lista!=NULL){
		printf("%s %s %d %d %d\n", lista->nome,lista->cognome, lista->peso, lista->altezza, lista->eta);
		lista = lista->next;
	}
	printf("\n");
}

studente *rimuoviPeso(studente *lista, int k){
	if(!lista) return NULL;
	if(lista->peso < k) return rimuoviPeso(lista->next, k);
	lista->next = rimuoviPeso(lista->next, k);
	return lista;
}

studente *duplicaNodo(studente *nodo){
	studente *duplice;
	
	duplice = nuovoNodo(nodo->nome, nodo->cognome, nodo->peso, nodo->altezza, nodo->eta);
	
	return duplice;
}

studente *duplicaElementi(studente *lista){
   studente *nodo;
  
  if (lista==NULL) return NULL;
  lista->next = duplicaElementi(lista->next);
  if (lista->eta%2==0) {
  	//nodo = duplicaNodo(lista);
    nodo = nuovoNodo(lista->nome,lista->cognome, lista->peso, lista->altezza, lista->eta);
    nodo->next = lista->next;
    lista->next = nodo;
  }
  return lista;
}

void scriviFile(FILE *fp, studente *lista){
	while(lista!=NULL){
		fprintf(fp,"%s %s %d %d %d\n", lista->nome,lista->cognome, lista->peso, lista->altezza, lista->eta);
		printf("%s %s %d %d %d\n", lista->nome,lista->cognome, lista->peso, lista->altezza, lista->eta);
		lista = lista->next;
	}
}

