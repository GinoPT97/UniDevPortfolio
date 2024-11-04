/****************************************************
 * 
 * Esercizio_002.C
 * Area del trapezio date le due basi e l'altezza
 * 
 * **************************************************/
#include <stdio.h>

int main()
{
  float base_minore = 5;
  float base_maggiore = 12;
  float altezza = 10;
  
  float Area = 0;
  
  
  Area = (base_maggiore+base_minore)*altezza/2;

  printf("L'area del trapezio è %f\n", Area);
  
  return 0;
}
