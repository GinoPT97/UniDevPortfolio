#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "persona.h"

static struct persona *nuovoNodo(char *nome,char *cognome, int peso, int altezza, int eta)
{
  struct persona *nodo;

  nodo = (struct persona *) malloc(sizeof(struct persona));
  if (nodo == NULL) return NULL;
  
  strcpy(nodo->nome, nome);
  strcpy(nodo->cognome, cognome);
  nodo->peso =peso;
  nodo->altezza = altezza;
  nodo->eta = eta;
  nodo->next = NULL;
    
  return nodo;
}

static void freeNodo(struct persona *nodo)
{
  free((void *) nodo);
}

static struct persona *inserisciLista(struct persona *nodo, struct persona *lista)
{
  if (lista==NULL) return nodo;
  lista->next = inserisciLista(nodo, lista->next);
  return lista;
}


void gestioneErrore()
{
  printf("Errore\n");

  exit(0);
}

void freeLista(struct persona *lista)
{
  if (lista != NULL) {
    freeLista(lista->next);
    freeNodo(lista);
  }
}

void scriviLista(struct persona *lista)
{
  while (lista!=NULL) {
    printf("%s %s %d %d %d\n",lista->nome,lista->cognome,lista->peso,lista->altezza,lista->eta);
    lista = lista->next;
  }
  printf("\n");
}

struct persona *leggiFile(FILE *fp, struct persona *lista)
{
  char nome[256], cognome[256];
  int peso, altezza, eta;
  struct persona *nodo;

  while(fscanf(fp,"%s%s%d%d%d",nome,cognome,&peso,&altezza,&eta)==5){
    nodo = nuovoNodo(nome,cognome,peso,altezza,eta);
    if (nodo==NULL) gestioneErrore();
    lista = inserisciLista(nodo, lista);
  }
  return lista;
}

void scriviFile(FILE *fp, struct persona *lista)
{
  while (lista!=NULL) {
    fprintf(fp,"%s %s %d %d %d\n",lista->nome,lista->cognome,lista->peso,lista->altezza,lista->eta);
    lista = lista->next;
  }
}

struct persona *rimuoviPesoMinore(struct persona *lista, int x)
{
  struct persona *prev, *succ, *l;

  l = lista;
  
  prev = NULL;
  while(lista!=NULL) {
    if (lista->peso < x) {
      succ = lista->next;
      if (prev != NULL) {
	prev->next = succ;
      }
      else {
	l = succ;
      }
      freeNodo(lista);
      lista = succ;
    }
    else {
      prev = lista;
      lista = lista->next;
    }
  }

  return l;
}

struct persona *duplicaNodo(struct persona *nodo)
{
  struct persona *nuovo;

  nuovo = nuovoNodo(nodo->nome,nodo->cognome,nodo->peso,nodo->altezza,nodo->eta);

  return nuovo;
}

struct persona *duplicaElementi(struct persona *lista)
{
  struct persona *nodo;
  
  if (lista==NULL) return NULL;
  lista->next = duplicaElementi(lista->next);
  if (lista->eta%2==0) {
    nodo = duplicaNodo(lista);
    nodo->next = lista->next;
    lista->next = nodo;
  }
  return lista;
}


