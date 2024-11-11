#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int main(){
	
	FILE *fp1,*fp2,*fp3;
	char s1[100],s2[100];
	
	fp1 = fopen("dati1.txt","r");
	fp2 = fopen("dati2.txt","r");
	fp3 = fopen("output3.txt","w");
	
	if(fp1 == NULL || fp2 == NULL || fp3 == NULL){
		printf("File non aperto\n");
		exit(-1);
	}
	
	while(fscanf(fp1, "%s[^" "]", s1) != EOF && fscanf(fp2, "%s[^" "]", s2) != EOF){
		if(strstr(s1,s2))
		  fprintf(fp3,"%s\n",s1);
		  printf("%s\n",s1);
	}
	
	fclose(fp1);
	fclose(fp2);
	fclose(fp3);
	return 0;
}
