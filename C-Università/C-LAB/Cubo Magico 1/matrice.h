#include <stdio.h>
#include <stdlib.h>

struct list{
	int i;
	int j;
	int a;
	struct list *next;
};

typedef struct list list;

list *MtoL(int **mat,list *l, int n,int m);
list *inserisciLista(list *nodo,list *l);
list *rimuoviDispari(list *lista);
int dim1(FILE *fp);
int dim2(FILE *fp);
int **leggiFile(FILE *fp,int **matrice,int n ,int m);
int **LtoM(int **mat,list *l,int n,int m);
void verifica(int **mat,int n, int m);
void scriviMatrice(int **matrice, int n, int m);
void scriviLista(list *l);
void scriviFile(FILE *fp,int **mat,int n,int m);
void freeMatrice(int **A);
void freeLista(list*l);
