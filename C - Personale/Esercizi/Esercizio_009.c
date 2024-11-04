/****************************************************
 * 
 * Esercizio_009.C
 * Risolve una equazione di secondo grado
 * 
 * **************************************************/
#include <stdio.h>
#include <math.h>

int main()
{
  float a, b, c;
  float delta;
  float w, w1, w2;
  float x, x1, x2;

  printf("inserisci il primo coefficiente a: ");
  scanf("%f", &a);
  printf("inserisci il secondo coefficiente b: ");
  scanf("%f", &b);
  printf("inserisci il terzo coefficiente c: ");
  scanf("%f", &c);
  
  if(a==0){
    if(b==0){
      if(c==0){
        printf("L'equazione è indeterminata");
      }
      else {
        printf("L'equazione è impossibile");
      }
    }
    else {
      x = -c/b;
      printf("L'equazione è di primo grado con soluzione %f\n", x);
    }
    }
    else {
      delta = b*b- 4*a*c;
      w = 2*a;
      w1= -(b / w);

      if(delta==0){
        x = w1;
        printf("L'equazione ha una radice doppia %f\n", x);
      }
      else if(delta<0){
        w2 = sqrt(fabs(delta))/w;
        printf("L'equazione ha radici complesse coniugate %f +- i%f\n", w1, w2);
      }
      else if(delta>0){
        w2 = sqrt(fabs(delta))/w;
        x1 = w1+w2;
        x2 = w1-w2;
        printf("L'equazione ha radici reali %f e %f\n", x1, x2);
      }
    }
  return 0;
}


