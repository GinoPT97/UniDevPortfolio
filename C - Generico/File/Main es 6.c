#include <stdio.h>
#include <stdlib.h>
#include <string.h>


  #define MAX 4

  int main(){
    FILE *fpr, *fpo;
    char string[30];
    char citta[30];
    int secondi, scatti;
    int tempoMassimo = 0;
    int numeroDiscatti = 0;

    fprintf(stdout, "Inserisci il nome della citta\'(maiuscolo): ");
    fscanf(stdin, "%s", citta);


    fpr = fopen("datitelefono.txt", "r");
    fpo = fopen("output6.txt", "w");

    if(fpr == NULL){
        perror("File non aperto");
        return -1;
      }

    while(fscanf(fpr, "%s %d %d\n", string, &secondi, &scatti) != EOF){
      if(strcmp(string, citta) == 0){
        numeroDiscatti += scatti;
        if(secondi > tempoMassimo){
          tempoMassimo = secondi;
        }
      }
    }

    fclose(fpr);
    fprintf(stdout, "Citta\': %s \nMassima durata di una telefonata: %ds\nNumero di scatti totale: %d\n", citta, tempoMassimo, numeroDiscatti);
    fprintf(fpo, "Citta\': %s \nMassima durata di una telefonata: %ds\nNumero di scatti totale: %d\n", citta, tempoMassimo, numeroDiscatti);
    fclose(fpo);
  }
