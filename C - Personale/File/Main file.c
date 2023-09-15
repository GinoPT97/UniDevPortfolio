#include <stdlib.h>
#include <stdio.h>

int main(){
   FILE *fp1, *fp2;
   char ch;
   
   fp1 = fopen("dati1.txt","r");
   fp2 = fopen("output1.txt","w");
   
   if(fp1 == NULL || fp2 == NULL){
   	perror("Errore, file vuoto!");
   	exit(-1);
   }
   while(fscanf(fp1,"%c", &ch) != EOF){
   	 fprintf(fp2,"%c", ch);
   	 printf("%c", ch);
   }
   fclose(fp1);
   fclose(fp2);
 return 0;
}
