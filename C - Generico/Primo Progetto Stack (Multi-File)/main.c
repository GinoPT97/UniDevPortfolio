#include "libreria.h"

int main(int argc, char *argv[]) {
	
	FILE *fplibro, *fpstudente;
	studente *lstudente;
	libro *llibro;
	
	popolaliste(fplibro,fpstudente,lstudente,llibro);
	
	menuprincipale(lstudente,llibro);
	
	freeListe(lstudente,llibro);
	
	return 0;
}
