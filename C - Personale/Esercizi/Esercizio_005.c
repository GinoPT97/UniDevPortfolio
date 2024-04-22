/****************************************************
 * 
 * Esercizio_005.C
 * stampa a video l’architettura a N bit 
 * del processore su cui esso viene eseguito 
 * 
 * **************************************************/
#include <stdio.h>

int main()
{
  int *a=NULL;
  int bit = 0;

  bit = sizeof(a)*8;

  printf("Il programma esegue su una architettura a %d\n", bit);
  
  return 0;
}

