#include <stdio.h>
#include <stdlib.h>

struct list{
	int i;
	int j;
	int a;
	struct list *next;
};

typedef struct list list;

list *eliminadispari(list *head);
list *MtoL(int **mat,list *l, int n);
list *inserisciLista(list *nodo,list *l);
int dimensione(FILE *fp);
int **leggiFile(FILE *fp,int **matrice,int n);
int **trasposta(int **a, int n);
int **LtoM(int **mat,list *l,int n);
void scriviMatrice(int **matrice, int n);
void freeMatrice(int **A);
void freeLista(list*l);
void scriviLista(list *l);
void scriviFile(FILE *fp,int **mat, int n);

