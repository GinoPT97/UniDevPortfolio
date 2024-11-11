#include "libreria.h"

int scelta(){
	int x = 0;
	scanf("%d", &x);
	if(x>3) {
	   printf("Scelta non valida! Inserisci scelta : ");
	   return scelta();	
	} else return x;
}

void menuprincipale(studente *lstudente,libro *llibro){
	printf("Benvenuto nella biblioteca principale!\n");
	printf("Scegli l'operazione da fare : \n1)Prendi in prestito un libro. \n2)Restituisci un libro.\n3)Inserisci nuovo studente.\n\nInserisci scelta : ");
	
	switch(scelta()){
		case 1:
	     printf("\nEcco la lista dei libri\n");
	     scriviListalibro(llibro);
	    break;
	    case 2:
	     printf("Indica nome utente e libro");
	    break;
	    case 3 :
	     printf("Inserisci il nome e il cognome");
	}
	
}

void popolaliste(FILE *fplibro, FILE *fpstudente, studente *lstudente, libro *llibro){
	fplibro = fopen("libri.txt","r");
	fpstudente = fopen("studenti.txt","r");
	if(fplibro==NULL) printf("Errore apertura file dei libri\n");
	if(fpstudente==NULL) printf("Errore apertura file degli studenti\n");
	lstudente = leggiFilestudente(fpstudente,lstudente);
	llibro = leggiFilelibro(fplibro,llibro);
	fclose(fplibro);
	fclose(fpstudente);
}











