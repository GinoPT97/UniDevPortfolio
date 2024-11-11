/****************************************************
 * 
 * Esercizio_004_b.C
 * prende in input tre valori interi positivi e 
 * stampa a video il valore massimo
 * 
 * **************************************************/

#include <stdio.h>

int main()
{
  int a;
  int b;
  int c;

  int Massimo = 0;
  
  // leggiamo i tre valori in input
  printf("inserisci il primo valore: ");
  scanf("%d", &a);
  printf("inserisci il secondo valore: ");
  scanf("%d", &b);
  printf("inserisci il terzo valore: ");
  scanf("%d", &c);

  if(a>Massimo)
    Massimo = a;
    
  if(b>Massimo)
    Massimo = b;
    
  if(c>Massimo)
    Massimo = c;

  printf("Il massimo fra %d, %d e %d è il valore %d\n", a, b, c, Massimo);
  
  return 0;
}


