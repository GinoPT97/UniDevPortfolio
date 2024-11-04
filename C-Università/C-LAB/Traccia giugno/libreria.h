#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct nodo {
	int info;
	struct nodo *next;
};

void stampaLista(struct nodo *head);
void freeLista(struct nodo *head);
struct nodo *leggiFile(FILE *fp, struct nodo *head);
struct nodo *eliminaElementi(struct nodo *head, int x);
void scriviFile(FILE *fp, struct nodo *head);
