/****************************************************
 * 
 * Esercizio_003.C
 * Area e circonferenza del cerchio dato il raggio
 * 
 * **************************************************/
#include <stdio.h>

int main()
{
  float raggio = 8.5;
  const float pi_greco = 3.141592;

  float circonferenza;
  float area;

  circonferenza = 2*pi_greco*raggio;
  area = pi_greco*raggio*raggio;
  

  printf("La circonferenza è %f\nL'area del cerchio è %f", circonferenza, area);
  
  return 0;
}
