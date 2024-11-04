#include <stdio.h>
#include <stdlib.h>
#include "matrice.h"

int dim1(FILE *fp){
	int n;
	fscanf(fp,"%d", &n);
	return n;
}

int dim2(FILE *fp){
	int n;
	fscanf(fp,"%d", &n);
	return n;
}

int **allocaMatrice(int m, int n){
  int **A;
  int i;

  A = (int **) malloc(m*sizeof(float *));
  A[0] = (int *) calloc(m*n,sizeof(int));
  for (i=1; i<m; i++)
    A[i] = A[i-1] + n;

  return A;
}

int **leggiFile(FILE *fp,int **matrice,int n,int m){
	int i,j;
	matrice =NULL;
	
	matrice = allocaMatrice(m, n);
	
	for(i=0; i<n; i++)
     for(j=0; j<m; j++)
	    fscanf(fp, "%d", &matrice[i][j]);
	
	printf("\nEcco la matrice di partenza : \n%d %d",n,m);
	
	return matrice;
}

void scriviMatrice(int **matrice, int n,int m){
  	int i,j;
    for(i = 0; i < n; i++){
      printf("\n");
      for(j = 0; j < m; j++){
        printf("%d ", matrice[i][j]);
      }
    }
    printf("\n");
  }
  
void freeMatrice(int **A){
  free((void *) A[0]);
  free((void *) A);
}

list *nuovoNodo(int i,int j, int a){
	list *nodo;
	
	nodo = (list*)malloc(sizeof(list));
	if(nodo==NULL) printf("Errore creazione nodo");
	
	nodo->i=i;
	nodo->j=j;
	nodo->a=a;
	nodo->next=NULL;
	
	return nodo;
}
  
void freeNodo(list* nodo){
	free((void*)nodo);
}

void freeLista(list*l){
	if(l!=NULL){
		freeNodo(l);
		freeLista(l->next);
	}
}
  
list *inserisciLista(list *nodo,list *l){
  if (l==NULL) return nodo;
  l->next = inserisciLista(nodo, l->next);
  return l;
}

list *MtoL(int **mat,list *l, int n,int m){
	int i,j;
	list *nodo;
	
	for(i=0;i<n;i++){
		for(j=0;j<m;j++){
			nodo = nuovoNodo(i,j, mat[i][j]);
			if(nodo==NULL) printf("Errore creazione nodo nell'inserimento");
			l = inserisciLista(nodo,l);
		}
	}
	return l;
}

void scriviLista(list *l){
  printf("\n");
  while(l!=NULL && l->next!=NULL) {
  	printf("(%d, %d, %d)->",l->i,l->j,l->a);
    l = l->next;
  } printf("(%d, %d, %d)",l->i,l->j,l->a);
  printf("\n\n");
}

static list *rimuoviNodo(list *lista, list *nodo){
  list *tmp;
  
  if (lista == nodo) {
    tmp = lista->next;
    freeNodo(lista);
    return tmp;
  }
  lista->next = rimuoviNodo(lista->next,nodo);
}

list *rimuoviDispari(list *lista){
  if (lista==NULL) return lista;
  if (lista->a%2 !=0) {
    lista = rimuoviNodo(lista, lista);
    lista = rimuoviDispari(lista);
  } else lista->next = rimuoviDispari(lista->next);
  
  return lista;
}

int retrieveNumber(int i,int j, list *l){
   	if(l->i == i && l->j == j) return l->a;
   	else if(l->next) retrieveNumber(i, j, l->next);
   	else return 0;
}

int **LtoM(int **mat,list *l,int n,int m){
	int i,j;
	
	for(i=0; i<n;i++)
	 for(j=0;j<m;j++)
	  mat[i][j] = retrieveNumber(i,j,l);

	return mat;
}

void scriviFile(FILE *fp,int **mat,int n,int m){
    int i,j;
    
    fprintf(fp, "%d %d", n, m);
    printf("%d %d", n, m);
    
    for(i=0; i<n;i++){
    	fprintf(fp,"\n");
    	printf("\n");
    	for(j=0;j<m;j++){
    		fprintf(fp, "%d ", mat[i][j]);
    		printf("%d ", mat[i][j]);
		}
	}
}

void verifica(int **mat,int n, int m){

    int i,j,sommap=0,sommas=0,sommar[n],sommac[m],r;

    for(i=0;i<n;i++){
    	sommar[i] = 0;
        sommac[i] = 0;
	}

    for(i=0;i<n;i++)
        sommap += mat[i][i];

    for(i=0,j=n-1;i<n;i++,j--)
        sommas += mat[i][j];
        
    for(i=0;i<n;i++)
        for(j=0;j<m;j++)
            sommar[i] += mat[i][j];

    for(j=0;j<m;j++)
        for(i=0;i<n;i++)
            sommac[j] += mat[i][j];

    //verifica
    if(sommap == sommas){
        //verifica somma righe
        for(i=0;i<n;i++)
            if(sommap != sommar[i])
                r = 0;
                
        //verifica somma colonne
        for(i=0;i<n;i++)
            if(sommap != sommac[i])
                r = 0;
                
        printf("\nE' un quadrato magico\n");
    }else
        printf("\nNon e' un quadrato magico\n");
}
