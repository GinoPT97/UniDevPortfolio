#include <stdio.h>
#include <stdlib.h>
#include "matrice.h"

void gestioneErrore(){
	printf("Errore!");
	exit(0);
}

int dim(FILE *fp){
	int n;
	fscanf(fp,"%d", &n);
	return n;
}

int **allocaMatrice(int n){
  int **A;
  int i;

  A = (int **) malloc(n*sizeof(float *));
  A[0] = (int *) calloc(n*n,sizeof(int));
  for (i=1; i<n; i++)
    A[i] = A[i-1] + n;

  return A;
}

int **leggiFile(FILE *fp,int **matrice,int n){
	int i,j;
	matrice =NULL;
	
	matrice = allocaMatrice(n);
	
	for(i=0; i<n; i++)
     for(j=0; j<n; j++)
	    fscanf(fp, "%d", &matrice[i][j]);
	
	printf("Ecco la matrice di partenza : \n%d",n);
	
	return matrice;
}

void scriviMatrice(int **matrice, int n){
  	int i,j;
    for(i = 0; i < n; i++){
      printf("\n");
      for(j = 0; j < n; j++){
        printf("%d ", matrice[i][j]);
      }
    }
    printf("\n");
  }
  
  void freeMatrice(int **A){
  free((void *) A[0]);
  free((void *) A);
}

int **trasposta(int **a, int n){
	int b,i,j;
	
	for(i=0; i<n; i++){
	 for(j=i+1; j<n; j++){
	 	b = a[j][i];
	 	a[j][i] = a[i][j];
	 	a[i][j] = b;
	 }
	}
	    
	printf("\nEcco la matrice Trasposta : ");
	
	return a;
}
matrice *nuovoNodo(int i,int j, int a){
	matrice *nodo;
	
	nodo = (matrice*)malloc(sizeof(matrice));
	if(nodo==NULL) printf("Errore creazione nodo!");
	
	nodo->i=i;
	nodo->j=j;
	nodo->a=a;
	nodo->next=NULL;
	
	return nodo;
}
  
void freeNodo(matrice* nodo){
	free((void*)nodo);
}

void freeLista(matrice *l){
	if(l!=NULL){
		freeNodo(l);
		freeLista(l->next);
	}
}
  
matrice *inserisciLista(matrice *nodo,matrice *l){
  if (l==NULL) return nodo;
  l->next = inserisciLista(nodo, l->next);
  return l;
}

matrice *MtoL(int **mat,matrice *l, int n){
	int i,j;
	matrice *nodo;
	
	for(i=0;i<n;i++){
		for(j=0;j<n;j++){
			nodo = nuovoNodo(i,j, mat[i][j]);
			if(nodo==NULL) printf("Errore creazione nodo nell'inserimento!");
			l = inserisciLista(nodo,l);
		}
	}
	return l;
}

void scriviLista(matrice *l){
  printf("\n");
  while(l!=NULL && l->next!=NULL) {
  	printf("(%d, %d, %d)->",l->i,l->j,l->a);
    l = l->next;
  } printf("(%d, %d, %d)",l->i,l->j,l->a);
  printf("\n");
}

matrice *rimuoviNodo(matrice *l, matrice *nodo){
  matrice *tmp;
  
  if (l == nodo) {
    tmp = l->next;
    freeNodo(l);
    return tmp;
  }
  l->next = rimuoviNodo(l->next,nodo);
}

matrice *rimuoviDispari(matrice *l){
  if (l==NULL) return l;
  if (l->a%2 !=0) {
    l = rimuoviNodo(l, l);
    l = rimuoviDispari(l);
  } else l->next = rimuoviDispari(l->next);
  
  return l;
}

int retrieveNumber(int i,int j, matrice *l){
   	if(l->i == i && l->j == j) return l->a;
   	else if(l->next) retrieveNumber(i, j, l->next);
   	else return 0;
}

int **LtoM(int **mat,matrice *l,int n){
	int i,j;
	
	for(i=0; i<n;i++)
	 for(j=0;j<n;j++)
	  mat[i][j] = retrieveNumber(i,j,l);

	return mat;
}

void scriviFile(FILE *fp,int **mat,int n){
    int i,j;
    
    fprintf(fp, "\n%d", n);
    printf("\n%d", n);
    
    for(i=0; i<n;i++){
    	fprintf(fp,"\n");
    	printf("\n");
    	for(j=0;j<n;j++){
    		fprintf(fp, "%d ", mat[i][j]);
    		printf("%d ", mat[i][j]);
		}
	}
}
