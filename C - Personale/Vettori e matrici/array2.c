#include <stdio.h>

int main(){
   int v1[10], v2[10];
   int i, j, comune;
   int num;
   
   printf("Inserisci il primo vettore (10 numeri interi) : ");
   for(i=0;i<10;i++){
   do{
    scanf("%d",&num);
    comune=0;
    
	for(j=0; j<i; j++)
     if(v1[j]==num)
       comune=1;
   }while(comune);
     printf("\nInserito numero %d", num);
     v1[i]=num;
   }
   
   printf("Inserisci il secondo vettore (10 numeri interi) : \n");
   
   for(i=0;i<10;i++){
    do{
     scanf("%d",&num);
     comune=0;
     for(j=0; j<i; j++)
      if(v2[j]==num)
       comune=1;
    }while(comune);
     printf("Inserito numero %d\n", num);
     v2[i]=num;
   }

   for(i=0;i<10;i++)
    for(j=0; j<10; j++)
     if(v1[i]==v2[j])
      printf("Elemento in comune %d, in posizione %d e %d\n", v1[i], i, j);

   return 0;
}
