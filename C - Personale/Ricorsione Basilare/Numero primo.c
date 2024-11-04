#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int pari(int n)
{
 if (n%2 == 0) return 1; 
 else return 0;
}

int primo(int n) {
    int max,i;
    if (n>=1 && n<=3) return 1; 
    if (pari(n)) return 0;  
    max = sqrt(n);
    for(i=3; i<=max; i+=2)
        if (n%i==0) return printf("Non e' primo!");
    return printf("E' primo!");
}

void primo2(int numero){
	int div=1, conta=0;
        
	while(conta<3 && div<=numero/2){
		if(numero%div==0)  
			conta=conta+1;	
		div=div+1;
	}
	if (conta==1) printf("il numero e' primo\n ");
	else printf("il numero non e' primo\n ");
}

/*#include <stdio.h>

int main() { 
   int loop, number;
   int prime = 1;
   
   number = 11;

   for(loop = 2; loop < number; loop++) {
      if((number % loop) == 0) {
         prime = 0;
      }
   }

   if (prime == 1)
      printf("%d is prime number.", number);
   else
      printf("%d is not a prime number.", number);
   return 0;
}*/

int main(int argc, char *argv[]){
	
	int n=0;
	printf("Passami un valore e ti diro' se e' primo : ");
	scanf("%d", &n);
	primo2(n);
	
    return 0;
}
