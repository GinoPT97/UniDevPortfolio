#include <stdio.h>
#include <stdlib.h>
#include "libreria.h"

elem *nuovonodo(int x){
	
	elem *nodo;
	
	nodo = (elem *) malloc(sizeof(elem));
	
	nodo->x = x;
	nodo->next = NULL;
	
	return nodo;
}

void freeNodo(elem *nodo){
	free((void *)nodo);
}

void freeLista(elem *lista){
	if(lista!=NULL){	
	freeLista(lista->next);
	freeNodo(lista);
    }
}

elem *inserisciLista(elem *nodo, elem *lista){
	if(lista==NULL) return nodo;
	lista->next = inserisciLista(nodo, lista->next);
	return lista;
}

elem *leggiFile(FILE *fp,elem *lista){
	int x;
	elem *nodo;
	
	while(fscanf(fp,"%d", &x)==1){
		nodo = nuovonodo(x);
		if(nodo==NULL) printf("errore");
		lista = inserisciLista(nodo, lista);
	}
	return lista;
}

void scriviLista(elem *lista){
	while(lista != NULL){
			if(lista != NULL && lista->next == NULL){
			printf("%d", lista->x);
	     	lista = lista->next;
		} else {
			printf("%d->", lista->x);
		    lista  = lista->next;
		}
	}
	printf("\n\n");
}

elem *paridispari(elem *lista){
	elem *l1, *l2=lista;
	while(lista != NULL){
		if(lista->x%2==0){
			l1 = inserisciLista(nuovonodo(lista->x),l1);
			lista = lista->next;
		} else lista = lista->next;
	}
	
	while(l2 != NULL){
		if(l2->x%2==1){
			l1 = inserisciLista(nuovonodo(l2->x),l1);
			l2 = l2->next;
		} else l2 = l2->next;
	}
	
	return l1;
}

elem *primo(elem *lista){
	elem *l1, *l2;
	int div=1, conta=0;
        
	while(conta<3 && div<=lista->x/2){
		if(lista->x%div==0)  
			conta=conta+1;	
		div=div+1;
	}
	if (conta==1) {
		l1 = inserisciLista(nuovonodo(lista->x),l1);
		primo(lista->next);
	} else primo(l2->next);
	
	return l1;
}

void scriviFile(FILE *fp, elem *lista){
	while(lista != NULL){
			if(lista != NULL && lista->next == NULL){
			fprintf(fp,"%d",lista->x);
			printf("%d", lista->x);
	     	lista = lista->next;
		} else {
			fprintf(fp,"%d->",lista->x);
			printf("%d->", lista->x);
		    lista  = lista->next;
		}
	}
}
