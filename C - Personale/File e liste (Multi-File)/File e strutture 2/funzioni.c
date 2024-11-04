#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "persona.h"

void gestioneErrore(){
     printf("errore");
	 exit(0);	
}

persona *nuovoNodo(char *nome, char *cognome, int voto){
	persona *nodo;
	
	nodo = (persona *) malloc(sizeof(persona));
	if(nodo==NULL) return NULL;
	
	strcpy(nodo->nome,nome);
	strcpy(nodo->cognome,cognome);
	nodo->voto = voto;
	nodo->next = NULL;
	
	return nodo;
}

void freeNodo(persona *nodo){
	free((void *)nodo);
}

persona *inserisciLista(persona *nodo, persona*lista){
	if(lista==NULL) return nodo;
	lista->next = inserisciLista(nodo,lista->next);
	return lista;
}

void freeLista(persona *lista){
	if(lista!=NULL){
		freeNodo(lista);
		freeLista(lista->next);
	}
}

persona *leggiFile(FILE *fp, persona *lista){
	char nome[256], cognome[256];
	int voto;
	persona *nodo;
	
	while(fscanf(fp,"%s %s %d", nome,cognome,&voto)==3){
		nodo = nuovoNodo(nome,cognome,voto);
		if(nodo==NULL) gestioneErrore();
		lista = inserisciLista(nodo,lista);
	}
	return lista;
}

void scriviLista(persona *lista){
	while(lista!=NULL){
		printf("%s %s %d\n",lista->nome,lista->cognome,lista->voto);
		lista = lista->next;
	}
	printf("\n");
}

int lung(persona *lista){
	int n = 0;
	while(lista!=NULL){
		n++;
		lista = lista->next;
	}
	return n;
}

int mediavoti(persona *lista){
	int n = 0,l = lung(lista);
	while(lista!=NULL){
		n += lista->voto;
		lista = lista->next;
	}
	return n/l;
}

void scriviFile(FILE *fp,persona *lista){
	int m = mediavoti(lista);
	while(lista != NULL){
		fprintf(fp,"%s %s\n",lista->nome,lista->cognome);
		printf("%s %s\n",lista->nome,lista->cognome);
		lista = lista->next;
	}
	fprintf(fp,"la media dei voti e' : %d\n", m);
	printf("la media dei voti e' : %d\n", m);
}
