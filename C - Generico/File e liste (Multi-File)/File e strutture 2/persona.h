#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct persona{
	char nome[256];
	char cognome[256];
	int voto;
	struct persona *next;
};

typedef struct persona persona;

void gestioneErrore();
void freeLista(persona *lista);
void scriviLista(persona *lista);
void scriviFile(FILE *fp,persona *lista);
persona *inserisciLista(persona *nodo, persona*lista);
persona *leggiFile(FILE *fp, persona *lista);
