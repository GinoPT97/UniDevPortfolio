#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void printrev(char car){ 
 if (car != '.'){ 
 printrev(getchar());
 putchar(car);
 } else return;
} 

int main(int argc, char *argv[]) {
	
	printf("Introdurre una sequenza terminata da . :\t");
    printrev(getchar());
	
	return 0;
}

