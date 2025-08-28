#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <errno.h>
#include <err.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <string.h>

#define MIN(X, Y) (X < Y ? X : Y) //Macro per calcolare il minimo tra due numeri
#define BUFFSIZE 8 //Grandezza del buffer, modificabile

void revString(char *string); //Funzione per invertire una stringa
off_t safe_lseek(int fd, off_t offset, int whence); //Piccolo DRY per via delle numerose chiamate a lseek

//Main function
int main(int argc, char *argv[]) {
	
	int fd;
	off_t off, len;
	char buffer[BUFFSIZE+1];

	//Controllo del corretto utilizzo del programma
	if(argc != 2)
		err(EXIT_FAILURE, "Incorretto utilizzo del programma\n");
	//Tentativo di apertura del file descritto in argv[1]
	if((fd = open(argv[1], O_RDONLY)) == -1)
		err(EXIT_FAILURE, "Impossibile accedere al file\n");

	len = safe_lseek(fd, 0, SEEK_END); //Ottengo la lunghezza del testo scritto nel file aperto, portando il puntatore del testo alla fine del file
	
	//Ciclo nel testo
	do {
		off = safe_lseek(fd, - MIN(BUFFSIZE, len), SEEK_CUR); //Se la lunghezza del testo rimasto da invertire è minore della grandezza del buffer, allora viene puntato l'inizio del testo, in modo da evitare "posizioni negative"
		
		//Tentativo di lettura del file (len-off ci permette di leggere solo fino all'ultimo carattere del testo ancora da invertire)
		if(read(fd, buffer, len-off) != len-off)
			err(EXIT_FAILURE, "ERRORE DI LETTURA");
		
        buffer[len-off] = '\0';

		//Inverto la stringa contrenuta nel buffer e provo a sacriverla in STDOUT
		revString(buffer);
		if(write(STDOUT_FILENO, buffer, len-off) != len-off)
			err(EXIT_FAILURE, "ERRORE DI SCRITTURA");

		//La lunghezza del testo diventa quella del testo ancora da invertire 
		len = safe_lseek(fd, -(len-off), SEEK_CUR);
		
	} while(len > 0); //Continuo finchè non finisco il file

    write(STDOUT_FILENO, "\n", 1);
	close(fd); //Chiudere sempre il file a fine programma

	return 0;
}

//Semplice funzione di inversione di un array (in questo caso una stringa)
void revString(char *string) {
	int len = strlen(string);
	char tmp;
	for(int i = 0; i < len/2; i++) {
		tmp = string[i];
		string[i] = string [len-1-i];
		string[len-1-i] = tmp;
	}
}

//lseek + controllo ed eventuale interruzione del programma in caso di errore
off_t safe_lseek(int fd, off_t offset, int whence) {
		off_t off;
		if((off = lseek(fd, offset, whence)) == -1)
				err(EXIT_FAILURE, "LSEEK ERROR");
		return off;
}
