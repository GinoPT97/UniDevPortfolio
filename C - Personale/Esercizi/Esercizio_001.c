/****************************************************
 * 
 * Esercizio_001.C
 * Area e perimetro di un rettangolo dati i due lati
 * 
 * **************************************************/
#include <stdio.h>

int main()
{
  float lato1 = 12;
  float lato2 = 10;
  
  float Area = 0;
  float Perimetro = 0;
  
  int Massimo = 0;
  int divisore = 0;
  
  Area = lato1*lato2;
  Perimetro = 2*lato1 + 2*lato2;

  printf("L'area del rettangolo è %f\nIl perimetro del rettangolo è %f", Area, Perimetro);
  
  return 0;
}
