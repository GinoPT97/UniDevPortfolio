#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX 256

struct studente{
	char nome[MAX];
	char cognome[MAX];
	int matricola;
	char esame[MAX];
	int voto;
	struct studente *next;
};

typedef struct studente studente;

void gestioneErrore();
void freeLista(studente *lista);
void scriviLista(studente *lista);
void scriviFile(FILE *fp, studente *lista);
void scriviFile2(FILE *fp, studente *lista);
studente *leggiFile(FILE *fp, studente *lista);
studente *rimuovi18(studente *lista);
