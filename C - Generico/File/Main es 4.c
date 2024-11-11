#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX 4

int main(){
  FILE *fpr, *fpo;
  char strings[MAX][100];

  fpr = fopen("dati1.txt", "r");
  fpo = fopen("output4.txt", "w");
  
  if(fpr == NULL){
      perror("File non aperto");
      return(-1);
  }
  
  if(fpo == NULL){
      perror("File output non aperto");
      return(-1);
    }
    
  int i = 0;
  while ((fscanf(fpr, "%[^\n]\n", strings[i])) != EOF){
  	fprintf(stdout, "\n%s", strings[i]);
  	fprintf(fpo, "\n%s", strings[i]);
    i++;
  }
  
  fclose(fpr);
  fclose(fpo);
}
