#include <stdio.h>
#include <stdlib.h>

struct elem{
	int x;
	struct elem *next;
};

typedef struct elem elem;

void scriviLista(elem *lista);
void scriviFile(FILE *fp, elem *lista);
void freeLista(elem *lista);
elem *leggiFile(FILE *fp,elem *lista);
elem *paridispari(elem *lista);
elem *primo(elem *lista);
