#include <stdlib.h>
#include <stdio.h>

int main(){
  FILE *fpr;
  char p, ch;
  int occorrenzeDiP = 0;

  fprintf(stdout, "Inserisci p: ");
  fscanf(stdin, "%c", &p);

  fpr = fopen("dati1.txt", "r");

  if(fpr == NULL){
    perror("File non aperto");
    return(-1);
  }

  while(fscanf(fpr, "%c", &ch) != EOF){
    if(ch == p)
      occorrenzeDiP++;
  }
  fclose(fpr);
  fprintf(stdout, "Numero di occorrenze di %c = %d", p, occorrenzeDiP);
}
