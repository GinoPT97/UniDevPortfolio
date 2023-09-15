#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int conta(char *stringa) {
    if (*stringa == '\0') return 0;
    return conta(stringa + 1) + 1;
}

int contaCaratteri() {
  char c;
  c = getchar();
  if(c == EOF) return 0;
  else return 1 + contaCaratteri();
}

int main(int argc, char *argv[]) {
	
	printf("Introdurre una sequenza e ti diro quanti caratteri ha : ");
	int c = contaCaratteri();
    printf("La sequenza ha %d caratteri : ", c);
	
	return 0;
}
