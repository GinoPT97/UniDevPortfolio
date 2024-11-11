#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define N 256 

struct libro{
	char nome[N];
	struct libro* next;
};

struct studente{
	char nome[N];
	char cognome[N];
	struct studente* next;
};

typedef struct libro libro;
typedef struct studente studente;

void menuprincipale(studente *lstudente,libro *llibro);
void popolaliste(FILE *fplibro, FILE *fpstudente, studente *lstudente, libro *llibro);
void freeListe(studente *ls, libro *ll);
void freeListastudente(studente *lista);
void freeListalibro(libro *lista);
void scriviListalibro(libro *lista);
void scriviListastudente(studente *lista);
studente *leggiFilestudente(FILE *fp, studente *lista);
libro *leggiFilelibro(FILE *fp, libro *lista);
