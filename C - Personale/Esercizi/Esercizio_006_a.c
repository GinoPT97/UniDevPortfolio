/****************************************************
 * 
 * Esercizio_006_a.C
 * verifica se un numero letto in input è intero
 * 
 * **************************************************/
#include <stdio.h>
#include <math.h>

int main()
{
  float numero;
  int tmp;

  printf("inserisci un numero: ");
  scanf("%f", &numero);
  
  tmp = (int)numero;

  if(numero==tmp)
    printf("Il numero %f è un intero\n",numero);
  else
    printf("Il numero %f non è un intero\n",numero);
    
  return 0;
}
