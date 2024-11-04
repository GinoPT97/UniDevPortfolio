#include <stdio.h>

void copiaRicorsiva() {
  int c;
  c = getchar();
  if(c != EOF) {
    putchar(c);
    copiaRicorsiva();
  }
}

int main () {
	
    copiaRicorsiva();
    return 0;
}
