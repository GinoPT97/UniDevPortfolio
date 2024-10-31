#include <stdio.h>
#include <stdlib.h>
#include <string.h>


struct persona {
  char nome[256];
  char cognome[256];
  int peso;
  int altezza;
  int eta;
  struct persona *next;
};

void gestioneErrore();
struct persona *leggiFile(FILE *fp, struct persona *lista);
void scriviLista(struct persona *lista);
struct persona *rimuoviPesoMinore(struct persona *lista, int x);
struct persona *duplicaElementi(struct persona *lista);
void scriviFile(FILE *fp, struct persona *lista);
void freeLista(struct persona *lista);
