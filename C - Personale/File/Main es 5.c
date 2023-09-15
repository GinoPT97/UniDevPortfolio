#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(){
	FILE *fpr1, *fpr2, *fpo;
	char c;
	int r1,r2;
	r1=r2=0;
	
	fpr1 = fopen("dati1.txt", "r");
	fpr2 = fopen("dati2.txt", "r");
    fpo = fopen("output5.txt", "w");
	
	if(fpr1 == NULL || fpr2 == NULL){
      perror("File non aperto");
      return(-1);
    }
    
    while ((fscanf(fpr1, "%c", &c)) != EOF){
    if(c == '\n')
      r1++;
    }
    
    while ((fscanf(fpr2, "%c", &c)) != EOF){
    if(c == '\n')
      r2++;
    }
    
    fprintf(fpo,"Il primo file (dati1) ha %d righe\n", r1);
    printf("Il primo file (dati1) ha %d righe\n", r1);
    fprintf(fpo,"Il secondo file (dati2) ha %d righe\n", r2);
    printf("Il secondo file (dati2) ha %d righe\n", r2);
	
	fclose(fpr1);
	fclose(fpr2);
    fclose(fpo);
}
