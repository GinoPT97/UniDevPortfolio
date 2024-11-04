#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "libreria.h"

void opbase(){
	int n,m,k;
	printf("\nPassami un intero : ");
	scanf("%d", &n);
	printf("\nScegli un operazione che vuoi fare : ");
	printf("\n1)Somma delle cifre di un numero naturale\n2)Fattoriale del numero\n3)Passo della serie di fibonacci\n4)Somma dei numeri primi pari\n5)Coefficente binomiale\n");
	scanf("\n%d", &m);
	switch(m){
		case 1:
	     printf("\nEcco la somma delle sue cifre : %d", sommanaturale(n));
	    break;
	    case 2:
	     printf("\nEcco il fattoriale : %d", fattoriale(n));
	    break;
	    case 3 :
	     printf("\nEcco la serie di fibonacci : %d", fibonacci(n));
	    break;
	    case 4:
	     printf("\nEcco la somma da 1 ad n : %d", somma(n));
	     break;
	    case 5:
	    	printf("Passami il secondo intero : ");
        	scanf("%d", &k);
            printf("Coefficente Binomiale : %d\n", coefbinom(n, k)); 
	     break;
	    default :
	     printf("Selezione non valida");
	}
}

void opstringhe(){
	int n;
	printf("\nScegli l'operazione da fare");
	printf("\n1)Inversa\n");
	scanf("%d", &n);
	switch(n){
		case 1:
		 printf("\nIntrodurre una sequenza terminata da .:\t");
         printrev(getchar());
         break;
	}	
}

int sommanaturale(int x){
	int ris;
	while(x != 0){
		ris += x % 10;
		x = x / 10;
	}
	if(ris < 10) return ris;
	else return sommanaturale(ris);
}

int fattoriale(int n){
 if (n>0) return n*fattoriale(n-1);
 else return 1; 
} 

int fibonacci(int n){
	if(n==1) return 1;
	if(n==0) return 0;
	else return fibonacci(n-1)+fibonacci(n-2);
}

int somma(int n){
	if(n<=0) return 0;
	else return n + somma(n-1); 
}

int coefbinom(int n,int k){
	if(k == 0)return 1; 
    if(n <= k) return 0; 
    return (n*coefbinom(n-1,k-1))/k; 
}

void printrev(char car){ 
 if (car != '.'){ 
 printrev(getchar());
 putchar(car);
 } else return;
} 

void opgenerale(){
	int n;
	printf("Scegli il tipo di operazioni che vuoi fare : ");
	printf("\n1)Operazioni su numeri\n2)operazioni su stringhe\n");
	scanf("%d", &n);
	switch(n){
		case 1:
		  opbase();
		  break;
		case 2:
		 opstringhe();
		 break;
		default:
		  printf("Selezione non valida");
	}
}
