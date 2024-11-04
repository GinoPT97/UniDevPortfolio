#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "libreria.h"

int main(int argc, char *argv[]) {
	
  struct nodo* lista = NULL;
  FILE *fp;
  float **matrice = NULL;

  fp = fopen("dati.txt", "r");
  if(!fp) printf("Errore apertura File!");

  lista = LeggiFile(lista, fp);
  printf("Lista in input:\n");
  StampaLista(lista);

  lista = EliminaNodi(lista);
  printf("\nLista dopo eliminazione nodi:\n");
  StampaLista(lista);
  fclose(fp);

  matrice = CreaMatrice(lista);
  StampaMatrice(matrice, LunghezzaLista(lista));
  
  printf("\nEcco il contenuto finale del file : \n");
  fp = fopen("output.txt", "w");
  if(!fp) {perror("Errore"), exit(0);}

  StampaListaToFile(lista, matrice, LunghezzaLista(lista), fp);
  fclose(fp);

	return 0;
}
