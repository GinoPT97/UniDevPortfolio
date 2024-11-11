/****************************************************
 * 
 * Esercizio_007.C
 * verifica se un numero intero letto in input è pari 
 * o dispari
 * 
 * **************************************************/
#include <stdio.h>
#include <math.h>

int main()
{
  int numero;

  printf("inserisci un numero: ");
  scanf("%d", &numero);

  if((numero%2)==0)
    printf("Il numero %d è pari\n",numero);
  else
    printf("Il numero %d è dispari\n",numero);
    
  return 0;
}
