#include <stdio.h>
#include <stdlib.h>

#define S 256

int maiuscolo(char *s){
	int i;
for( i=0;s[i]!='\0';i++)
if(islower(s[i]))
s[i]=toupper(s[i]);
}

int main(int argc, char *argv[]){
	
	char s[40];
    printf("Inserisci una stringa\n");
    gets(s);
    maiuscolo(s);
    printf("Stringa in maiuscolo %s\n", s);
	
    return 0;
}
