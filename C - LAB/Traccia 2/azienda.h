#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct azienda {
	char nome[256];
	int n;
	char **citta;
	struct azienda *next;
};

typedef struct azienda azienda;

void gestioneErrore();
void freeLista(azienda *lista);
azienda *leggiFile(FILE *fp, struct azienda *lista);
void scriviLista(struct azienda *lista);
azienda *rimuoviAzienda(azienda *lista,int soglia);
void scriviFile(FILE *fp, struct azienda *lista);
