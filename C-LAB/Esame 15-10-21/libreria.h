#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX 256

struct studente {
	char nome[MAX];
	char cognome[MAX];
	int peso;
	int altezza;
	int eta;
	struct studente *next;
};

typedef struct studente studente;

void gestioneErrore();
void freeLista(studente *lista);
studente *leggiFile(FILE *fp,studente *lista);
void scriviLista(studente *lista);
studente *rimuoviPeso(studente *lista, int k);
void scriviFile(FILE *fp, studente *lista);
studente *duplicaElementi(studente *lista);
