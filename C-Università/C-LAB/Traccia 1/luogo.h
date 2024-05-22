#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct luogo {
  char nome[256];
  float X;
  float Y;
  struct luogo *next;
};

void gestioneErrore();
struct luogo *leggiFile(FILE *fp, struct luogo *lista);
void scriviLista(struct luogo *lista);
struct luogo *rimuoviDuplicati(struct luogo *lista);
int lengthLista(struct luogo *lista);
float **allocaMatrice(int m, int n);
void freeMatrice(float **A);
void matriceDistanze(struct luogo *lista, float **A);
void scriviMatrice(float **A, int m, int n);
void scriviFile(FILE *fp, struct luogo *lista, float **A, int n);
void freeLista(struct luogo *lista);
