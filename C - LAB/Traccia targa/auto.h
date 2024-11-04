#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX 256

struct autom{
	char targa[MAX];
	int anno;
	int cilindrata;
	int peso;
	struct autom *next;
};

typedef struct autom autom;

autom *inserisciLista(autom *nodo, autom*lista);
autom *leggiFile(FILE *fp,autom *lista);
autom *rimuoviAnno(autom *lista, int k);
autom *duplicaElementi(autom *lista);
void freeLista(autom *lista);
void scriviLista(autom *lista);
void scriviFile(FILE *fp, autom *lista);
