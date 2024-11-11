#include <stdio.h>
#include <stdlib.h>

struct matrice{
	int i;
	int j;
	int a;
	struct matrice *next;
};

typedef struct matrice matrice;

void scriviFile(FILE *fp,int **mat,int n);
void freeMatrice(int **A);
void gestioneErrore();
void scriviLista(matrice *l);
void freeLista(matrice *l);
void freeNodo(matrice* nodo);
int dim(FILE *fp);
int **allocaMatrice(int n);
int **leggiFile(FILE *fp,int **matrice,int n);
void scriviMatrice(int **matrice, int n);
int **trasposta(int **a, int n);
int **LtoM(int **mat,matrice *l,int n);
matrice *MtoL(int **mat,matrice *l, int n);
matrice *inserisciLista(matrice *nodo,matrice *l);
matrice *nuovoNodo(int i,int j, int a);
matrice *rimuoviNodo(matrice *l,matrice *nodo);
matrice *rimuoviDispari(matrice *l);
