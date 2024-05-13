#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define M 256

struct partita{
	char nome[M];
	int punteggio;
	int giocate;
	int vinte;
	int pareggiate;
	int perse;
	struct partita *next;
};

typedef struct partita partita;

void freeLista(partita *lista);
void scriviLista(partita *lista);
void scriviFile(FILE *fp, partita *lista);
partita *rimuovipartite(partita *lista, int x);
partita *leggiFile(FILE *fp,partita *lista);
