/****************************************************
 * 
 * Esercizio_008.C
 * Stampa il bit in posizione i-esima di un numero
 * intero N
 * 
 * **************************************************/
#include <stdio.h>

int main()
{
  int numero;
  int i;
  int bit, bit_pos;

  printf("inserisci un numero: ");
  scanf("%d", &numero);
  printf("inserisci la posizione: ");
  scanf("%d", &i);
  
  if(i>(8*sizeof(int)))
    printf("La posizione indicata eccede il numero di bit di un intero\n");
  else {
    bit_pos = 1 << (i-1);
    bit = numero & bit_pos;
    bit >>= (i-1);
    printf("L'i-esimo bit di %d è %d\n",numero, bit);
  }
    
  return 0;
}
