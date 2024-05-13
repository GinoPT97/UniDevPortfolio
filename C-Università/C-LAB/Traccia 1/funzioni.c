#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "luogo.h"

static struct luogo *nuovoNodo(char *nome,float X, float Y)
{
  struct luogo *nodo;

  nodo = (struct luogo *) malloc(sizeof(struct luogo));
  if (nodo == NULL) return NULL;
  
  strcpy(nodo->nome, nome);
  nodo->X = X;
  nodo->Y = Y;
  nodo->next = NULL;
    
  return nodo;
}

static void freeNodo(struct luogo *nodo)
{
  free((void *) nodo);
}

static struct luogo *inserisciLista(struct luogo *nodo, struct luogo *lista)
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

void freeLista(struct luogo *lista)
{
  if (lista != NULL) {
    freeLista(lista->next);
    freeNodo(lista);
  }
}


void scriviLista(struct luogo *lista)
{
  while (lista!=NULL) {
    printf("%s %f %f\n",lista->nome,lista->X,lista->Y);
    lista = lista->next;
  }
  printf("\n");
}

struct luogo *leggiFile(FILE *fp, struct luogo *lista)
{
  char nome[256];
  float X, Y;
  struct luogo *nodo;

  while(fscanf(fp,"%s%f%f",nome,&X,&Y)==3){
    nodo = nuovoNodo(nome,X,Y);
    if (nodo==NULL) gestioneErrore();
    lista = inserisciLista(nodo, lista);
  }
  return lista;
}

static struct luogo *rimuoviNodo(struct luogo *lista, struct luogo *nodo)
{
  struct luogo **indirect, *l;

  indirect = &lista;

  while(*indirect != nodo) {
    indirect = &(*indirect)->next;
  }

  *indirect = nodo->next;
  if (nodo==lista) {
    l= lista->next;
  }
  else l = lista;

  free(nodo);
  
  return l;
}

int lengthLista(struct luogo *lista)
{
  int n = 0;

  while(lista!=NULL) {
    n++;
    lista = lista->next;
  }

  return n;
}

struct luogo *rimuoviDuplicati(struct luogo *lista)
{
  struct luogo *ptr1, *ptr2, *nodo;
  
  for (ptr1=lista; ptr1!=NULL; ptr1=ptr1->next) {
    for (ptr2=ptr1->next; ptr2!=NULL; ) {
      if (strcmp(ptr1->nome,ptr2->nome)==0) {
	nodo = ptr2;
	ptr2 = ptr2->next;
	rimuoviNodo(lista,nodo);
      }
      else ptr2 = ptr2->next;
    }
  }
  return lista;
}


float **allocaMatrice(int m, int n)
{
  float **A;
  int i;

  A = (float **) malloc(m*sizeof(float *));
  A[0] = (float *) calloc(m*n,sizeof(float));
  for (i=1; i<m; i++)
    A[i] = A[i-1] + n;

  return A;
}

void freeMatrice(float **A)
{
  free((void *) A[0]);
  free((void *) A);
}



void matriceDistanze(struct luogo *lista, float **A)
{
  int i, j;
  struct luogo *ptr1, *ptr2;

  for (ptr1=lista, i=0; ptr1!=NULL; ptr1=ptr1->next, i++) {
    for (ptr2=ptr1->next, j=i+1; ptr2!=NULL; ptr2=ptr2->next, j++) {
      A[i][j] = (ptr1->X - ptr2->X)*(ptr1->X - ptr2->X) +  (ptr1->Y - ptr2->Y)*(ptr1->Y - ptr2->Y);
      A[j][i] = A[i][j];
    }
  }
}


void scriviMatrice(float **A, int m, int n)
{
  int i, j;

  for (i=0; i<m; i++) {
    for (j=0; j<n; j++) {
      printf("%f  ", A[i][j]);
    }
    printf("\n");
  }
  printf("\n");
}

static int trovaMinimo(float **A, int riga, int n)
{
  float m, *ptr;
  int i=0, ind;

  ptr = A[riga];
  
  if (riga>0) m = ptr[0];
  else m = ptr[1];

  for (i=0, ind=0; i<n; i++)
    {
      if (ptr[i]==0) continue;
      if (ptr[i]<=m) ind = i;
    }
  return ind;
}

static struct luogo *trovaNodoIndice(struct luogo *lista, int n)
{
  int i=0;

  while(i<n) {
    lista=lista->next;
    i++;
  }
  return lista;
}

void scriviFile(FILE *fp, struct luogo *lista, float **A, int n)
{
  int i, j;
  struct luogo *ptr, *nodo;

  i = 0;
  ptr = lista;
  while (ptr!=NULL) {
    j = trovaMinimo(A,i,n);
    nodo = trovaNodoIndice(lista,j);
    fprintf(fp,"%s %s %f\n",ptr->nome, nodo->nome, A[i][j]);
    printf("%s %s %f\n",ptr->nome, nodo->nome, A[i][j]);
    ptr = ptr->next;
    i++;
  }
}

