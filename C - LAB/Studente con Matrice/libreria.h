#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX 100

struct nodo{
  char nome[MAX];
  char cognome[MAX];
  int peso;
  int altezza;
  struct nodo *next;
};

struct nodo *CreaNodo(char *nome, char *cognome, int peso, int altezza);
struct nodo *InserisciInCoda(struct nodo *lista, char *nome, char *cognome, int peso, int altezza);
struct nodo *LeggiFile(struct nodo *lista, FILE *fp);
struct nodo *EliminaNodi(struct nodo *lista);
int LunghezzaLista(struct nodo *lista);
float **CreaMatrice(struct nodo *lista);
void StampaMatrice(float **matrice, int n);
void StampaLista(struct nodo *lista);
void StampaListaToFile(struct nodo *lista, float **matrice, int n, FILE *fpw);
