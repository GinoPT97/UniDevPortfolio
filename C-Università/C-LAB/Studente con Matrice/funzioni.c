#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "libreria.h"

struct nodo *CreaNodo(char *nome, char *cognome, int peso, int altezza){
  struct nodo *nuovoNodo = (struct nodo *)malloc(sizeof(struct nodo));
  if(!nuovoNodo) return NULL;

  strcpy(nuovoNodo->nome, nome);
  strcpy(nuovoNodo->cognome, cognome);
  nuovoNodo->peso = peso;
  nuovoNodo->altezza = altezza;
  nuovoNodo->next = NULL;
  return nuovoNodo;
}

struct nodo *InserisciInCoda(struct nodo *lista, char *nome, char *cognome, int peso, int altezza){
  if(!lista) return CreaNodo(nome, cognome, peso, altezza);
  lista->next = InserisciInCoda(lista->next, nome, cognome, peso, altezza);
  return lista;
}

struct nodo *LeggiFile(struct nodo *lista, FILE *fp){
  char nome[MAX], cognome[MAX];
  int peso, altezza;

  while(!feof(fp)){
    if(fscanf(fp, "%s %s %d %d", nome, cognome, &peso, &altezza) == 4){
      lista = InserisciInCoda(lista, nome, cognome, peso, altezza);
    }
  }
  return lista;
}

struct nodo *EliminaNodi(struct nodo *lista){
  if(!lista) return NULL;
  if(lista->peso < 12) return EliminaNodi(lista->next);
  lista->next = EliminaNodi(lista->next);
  return lista;
}

int LunghezzaLista(struct nodo *lista){
  if(!lista) return 0;
  return(1 + LunghezzaLista(lista->next));
}

  float **CreaMatrice(struct nodo *lista){
    struct nodo *temp, *testa = lista;
    float **matrice = NULL;
    int n = LunghezzaLista(lista),i,j;
    float media = 0;

    matrice = (float**)malloc(n * sizeof(float*));
    for(i = 0; i < n; i++){
      matrice[i] = (float*)malloc(n * sizeof(float));
    }

    for(i = 0; i < n; i++){
      temp = testa;
      for(j = 0; j < i + 1; j++){
        if(j != i){
          media = (lista->peso + temp->peso) / 2.0;
          matrice[i][j] = media;
          matrice[j][i] = media;
          temp = temp->next;
        }
      }
      lista = lista->next;
    }
    return matrice;
  }

  void StampaMatrice(float **matrice, int n){
  	int i,j;
    printf("\nMatrice:");
    for(i = 0; i < n; i++){
      printf("\n");
      for(j = 0; j < n; j++){
        printf("%.2f ", matrice[i][j]);
      }
    }
    printf("\n");
  }

void StampaLista(struct nodo *lista){
  if(lista == NULL) printf("Errore nella stampa della lista");
  while(lista != NULL){
  	printf("%s %s %d %d\n", lista->nome, lista->cognome, lista->peso, lista->altezza);
  	lista = lista->next;
  }
}

void StampaListaToFile(struct nodo *lista, float **matrice, int n, FILE *fp){
  float max;
  struct nodo *temp, *testa = lista;
  int i,j;

  for(i = 0; i < n; i++){
    temp = testa;
    max = matrice[i][0];

    for(j = 1; j < n; j++) if(matrice[i][j] > max) max = matrice[i][j];

    for(j = 0; j < n; j++){
      if(matrice[i][j] != max){
        temp = temp->next;
      }else{
        break;
      }
    }

    fprintf(fp,"%s\t%s\t%s\t%s\t%.2f\n",lista->nome, lista->cognome, temp->nome, temp->cognome, max);
    printf("%s\t%s\t%s\t%s\t%.2f\n",lista->nome, lista->cognome, temp->nome, temp->cognome, max);
    lista = lista->next;
  }
}

