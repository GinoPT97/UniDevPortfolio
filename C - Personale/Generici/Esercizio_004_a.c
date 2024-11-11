/****************************************************
 * 
 * Esercizio_004_a.C
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
  
  int A = 0;
  int B = 0;
  int C = 0;
  
  int Massimo = 0;

  printf("Inserisci il primo valore: ");
  scanf("%d", &a);
  printf("Inserisci il secondo valore: ");
  scanf("%d", &b);
  printf("Inserisci il terzo valore: ");
  scanf("%d", &c);
  
  A = a*(a>=b)*(a>=c);
  B = b*(b>=a)*(b>=c)*(b!=a); 
  C = c*(c>=a)*(c>=b)*(b!=c)*(a!=c);

  Massimo = (A + B + C);

  printf("Il massimo fra %d, %d e %d è il valore %d\n", a, b, c, Massimo);
  
  return 0;
}

