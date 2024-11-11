#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct studente {
	char nome[256];
	int eta;
	float media;
	struct studente *next;
};

typedef struct studente studente;

void gestioneErrore();
void freeLista(studente *lista);
void scriviLista(studente *lista);
studente *rimuoviEta(studente *lista, int x);
void scriviFile(FILE *fp,studente *lista);
studente *leggiFile(FILE *fp,studente *lista);
studente *rimuoviVoto(studente *lista, float x);
