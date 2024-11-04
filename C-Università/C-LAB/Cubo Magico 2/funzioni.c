#include <stdio.h>
#include <stdlib.h>
#include"cubo.h"

int dimensione(FILE *fp){
	int n;
	fscanf(fp,"%d", &n);
	return n;
}

int **leggiFile(FILE *fp,int **matrice,int n){
	int i,j;
	matrice =NULL;
	
	matrice = (int **) calloc(n,sizeof(int *));
	if(matrice==NULL) printf("Errore creazione matrice");
	
	for(i=0;i<n;i++){
		matrice[i] = (int *) calloc(n,sizeof(int));
	}
	
	for(i=0; i<n; i++)
     for(j=0; j<n; j++)
	    fscanf(fp, "%d", &matrice[i][j]);
	
	printf("Ecco la matrice di partenza : ");
	
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
	 for(j=1; j<n; j++){
	 	b = a[j][i];
	 	a[j][i] = a[i][j];
	 	a[i][j] = b;
	 }
	}
	    
	printf("\nEcco la matrice Trasposta : ");
	
	return a;
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

list *MtoL(int **mat,list *l, int n){
	int i,j;
	list *nodo;
	
	for(i=0;i<n;i++){
		for(j=0;j<n;j++){
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

list *eliminaNodo(list *l,list *nodo){
	list *tmp;
	
	if(l==nodo){
		tmp=l->next;
		freeNodo(l);
	    return tmp;
	}
	l->next = eliminaNodo(l->next,nodo);
}

list *eliminadispari(list *l){
	
	if(l==NULL) return l;
	if(l->a%2 != 0){
		l = eliminaNodo(l,l);
		l = eliminadispari(l);
	}else l->next = eliminadispari(l->next); 
	
	return l;
}

int retrieveNumber(int i,int j, list *l){
   	if(l->i == i && l->j == j) return l->a;
   	else if(l->next) retrieveNumber(i, j, l->next);
   	else return 0;
}

int **LtoM(int **mat,list *l,int n){
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
