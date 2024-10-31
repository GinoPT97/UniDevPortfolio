#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "classe.h"

void gestioneErrore(){
	printf("File non aperto");
	exit(0);
}

classe *nuovoNodo(char *nome, int studenti, int promossi, int bocciati, int rimandati){
	classe *nodo;
	
	nodo = (classe *) malloc(sizeof(classe));
	if(nodo==NULL) return NULL;
	
	strcpy(nodo->nome,nome);
	nodo->studenti = studenti;
	nodo->promossi = promossi;
	nodo->bocciati = bocciati;
	nodo->rimandati = rimandati;
	nodo->next = NULL;
	
	return nodo;
}

void freeNodo(classe *nodo){
	free((void*) nodo);
}

classe *inserisciLista(classe *nodo, classe *lista){
	if(lista==NULL) return nodo;
	lista->next = inserisciLista(nodo,lista->next);
	return lista;
}

void freeLista(classe *lista){
	if(lista!=NULL){	
	freeLista(lista->next);
	freeNodo(lista);
    }
}

classe *leggiFile(FILE *fp,classe *lista){
	char nome[256];
	int studenti, promossi, bocciati, rimandati;
	classe *nodo;
	
	while(fscanf(fp,"%s %d %d %d %d", nome,&studenti,&promossi,&bocciati,&rimandati)==5){
		nodo = nuovoNodo(nome,studenti,promossi,bocciati,rimandati);
		if(nodo==NULL) gestioneErrore();
		lista = inserisciLista(nodo,lista);
	}
	return lista;
}

void scriviLista(classe *lista){
	while(lista!=NULL){
		printf("%s %d %d %d %d\n",lista->nome, lista->studenti,lista->promossi,lista->bocciati,lista->rimandati);
		lista = lista->next;
	}
	printf("\n");
}

classe *rimuoviElementi(classe *lista){
	classe *prev, *succ, *l = lista;
	
	prev=NULL;
	while(lista!=NULL){
		if(lista->studenti != lista->promossi + lista->bocciati + lista->rimandati){
			succ =lista->next;
			if(prev!=NULL){
				prev->next = succ;
			} else l = succ;
			freeNodo(lista);
			lista =succ;
		}else{
			prev=lista;
			lista=lista->next;
		}
	}
	return l;
}

int lengthLista(classe *lista){
	int n = 0;
	while(lista!=NULL){
		n++;
		lista = lista->next;
	}
	return n;
}

classe *ordinaLista(classe *l){
	
    int scambio;
    classe *bubble;
	
    do
    {
        scambio = 0;
        //il primo è speciale
        if ( l->promossi < l->next->promossi )
        {
            scambio =! scambio;
            //tolgo dalla lista il secondo elemento
            bubble = l->next;
            l->next = bubble->next;
            //metto l'elemento tolto in testa alla lista
            bubble->next = l;
            l = bubble;
        }
        //tutti gli altri confronto il sucessivo con il sucessivosucessivo
        //in questo modo non perdo il riferimento al precedente
        for(bubble = l; bubble->next->next; bubble = bubble->next) 
        {
            if ( bubble->next->promossi < bubble->next->next->promossi )
            {
                if ( !scambio ) scambio =! scambio;
				
                //tolgo dalla lista l'elemento sucessivosucessivo
                classe *swap = bubble->next->next;
                bubble->next->next = swap->next;
                //swappo
                swap->next = bubble->next;
                bubble->next = swap;
            }       
        }
    }while(scambio);
    
    return l;
}

void scriviFile(FILE *fp,classe *lista){
	lista = ordinaLista(lista);
	while(lista!=NULL){
		fprintf(fp,"%s %d %d %d %d\n",lista->nome, lista->studenti,lista->promossi,lista->bocciati,lista->rimandati);
		printf("%s %d %d %d %d\n",lista->nome, lista->studenti,lista->promossi,lista->bocciati,lista->rimandati);
		lista = lista->next;
	}
}
