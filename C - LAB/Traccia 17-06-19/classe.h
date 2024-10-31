#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct classe {
	char nome[256];
	int studenti;
	int promossi;
	int bocciati;
	int rimandati;
	struct classe *next;
};

typedef struct classe classe;

void gestioneErrore();
void freeLista(classe *lista);
classe *leggiFile(FILE *fp,classe *lista);
void scriviLista(classe *lista);
void scriviFile(FILE *fp,classe *lista);
classe *rimuoviElementi(classe *lista); 
int lengthLista(classe *lista);
classe *ordinaLista(classe *lista);
