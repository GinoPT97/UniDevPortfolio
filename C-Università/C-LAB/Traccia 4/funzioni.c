#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "studente.h"

void gestioneErrore(){
	printf("File non aperto");
	exit(0);
}

studente *nuovoNodo(char *nome, char *cognome, int matricola, char *esame, int voto){
	studente *nodo;
	
	nodo = (studente *) malloc (sizeof(studente));
	if(nodo == NULL) return NULL;
	
	strcpy(nodo->nome, nome);
    strcpy(nodo->cognome, cognome);
    nodo->matricola = matricola;
    strcpy(nodo->esame, esame);
    nodo->voto = voto;
    nodo->next = NULL;
    
    return nodo;
}

void freeNodo(studente *nodo){
	free((void *) nodo);
}

studente *inserisciLista(studente *nodo, studente *lista){
	if(lista == NULL) return nodo;
	lista->next = inserisciLista(nodo, lista->next);
	return lista;
}

void freeLista(studente *lista){
	if(lista != NULL){
		freeLista(lista->next);
		freeNodo(lista);
	}
}

void scriviLista(studente *lista){
	while(lista!=NULL){
		printf("%s %s %d %s %d\n", lista->nome, lista->cognome, lista->matricola, lista->esame, lista->voto);
		lista = lista->next;
	}
	printf("\n");
}

studente *leggiFile(FILE *fp, studente *lista){
	char nome[MAX],cognome[MAX],esame[MAX];
	int matricola,voto;
	studente *nodo;
	
	while(fscanf(fp,"%s %s %d %s %d", nome, cognome, &matricola, esame, &voto)==5){
		nodo = nuovoNodo(nome, cognome, matricola, esame, voto);
		if(nodo==NULL) gestioneErrore();
		lista = inserisciLista(nodo,lista);
	}
	return lista;
}

studente *rimuovi18(studente *lista){
	if(lista == NULL) return lista;
	if(lista->voto<18){
		lista = lista->next;
		lista = rimuovi18(lista);
	}else lista->next = rimuovi18(lista->next);
	return lista;
}

int lengthLista(studente *lista){
	int n = 0;
	while(lista!=NULL){
		n++;
		lista = lista->next;
	}
	return n;
}

void scriviFile(FILE *fp, studente *lista){
	int *buffer, n, ind1, ind2;
  struct studente *ptr1, *ptr2;
  
  n = lengthLista(lista);
  buffer = (int *) calloc(n,sizeof(int));
  
  for (ptr1=lista, ind1=0; ptr1!=NULL; ptr1=ptr1->next, ind1++) {
    if (!buffer[ind1]) {
      buffer[ind1] = 1;
      fprintf(fp,"%s %s %d %s %d\n",ptr1->nome, ptr1->cognome, ptr1->matricola, ptr1->esame, ptr1->voto);
      printf("%s %s %d %s %d\n", ptr1->nome, ptr1->cognome, ptr1->matricola, ptr1->esame, ptr1->voto);
    } else continue;
    for (ptr2=ptr1->next, ind2=ind1+1; ptr2!=NULL; ptr2=ptr2->next, ind2++) {
      if (ptr2->matricola==ptr1->matricola) {
	buffer[ind2] = 1;
	fprintf(fp,"%s %s %d %s %d\n",ptr2->nome, ptr2->cognome, ptr2->matricola, ptr2->esame, ptr2->voto);
	printf("%s %s %d %s %d\n", ptr2->nome, ptr2->cognome, ptr2->matricola, ptr2->esame, ptr2->voto);
      }
    }
  }

  free((void *) buffer);
}
