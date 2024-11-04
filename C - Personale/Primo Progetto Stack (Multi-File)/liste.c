#include "libreria.h"

libro *nuovoNodolibro(char *nome){
	libro *nodo;
	
	nodo = (libro *) malloc(sizeof(libro));
	if(nodo==NULL) return NULL;
	
	strcpy(nodo->nome,nome);
	nodo->next = NULL;
	
	return nodo;
}

void freeNodolibro(libro *nodo){
	free((void *)nodo);
}

studente *nuovoNodostudente(char *nome, char *cognome){
	studente *nodo;
	
	nodo = (studente *) malloc(sizeof(studente));
	if(nodo==NULL) return NULL;
	
	strcpy(nodo->nome,nome);
	strcpy(nodo->cognome,cognome);
	nodo->next = NULL;
	
	return nodo;
}

void freeNodostudente(studente *nodo){
	free((void *)nodo);
}

studente *inserisciListastudente(studente *nodo, studente *lista){
	if(lista==NULL) return nodo;
	lista->next = inserisciListastudente(nodo,lista->next);
	return lista;
}

libro *inserisciListalibro(libro *nodo, libro *lista){
	if(lista==NULL) return nodo;
	lista->next = inserisciListalibro(nodo,lista->next);
	return lista;
}

void freeListalibro(libro *llibro){
	if(llibro!=NULL){	
	freeListalibro(llibro->next);
	freeNodolibro(llibro);
    }
}

void freeListastudente(studente *lstudente){
	if(lstudente!=NULL){	
	freeListastudente(lstudente->next);
	freeNodostudente(lstudente);
    }
}

void scriviListastudente(studente *lista){
	
	studente *l = lista;
	
	while(l!=NULL){
		printf("%s %s\n",l->nome,l->cognome);
		l = l->next;
	}
	printf("\n");
	
	freeListastudente(l);
}

void scriviListalibro(libro *lista){
	
	libro *l = lista;
	
	while(l!=NULL){
		printf("%s\n",l->nome);
		lista = l->next;
	}
	printf("\n");
	freeListalibro(l);
}

void freeListe(studente *lstudente, libro *llibro){
	freeListalibro(llibro);
	freeListastudente(lstudente);
}

libro *leggiFilelibro(FILE *fp, libro *lista){
	char nome[N];
	libro *nodo;
	
	while(fscanf(fp,"%[^\n]\n", nome) != EOF){
		nodo = nuovoNodolibro(nome);
		if(nodo==NULL) printf("Errore nell'inserimento il libro");
		lista = inserisciListalibro(nodo,lista);
	}
	return lista;
}

studente *leggiFilestudente(FILE *fp, studente *lista){
	char nome[N], cognome[N];
	studente *nodo;
	
	while(fscanf(fp,"%s %s", nome,cognome)==2){
		nodo = nuovoNodostudente(nome,cognome);
		if(nodo==NULL) printf("Errore nell'inserimento lo studente");
		lista = inserisciListastudente(nodo,lista);
	}
	return lista;
}
