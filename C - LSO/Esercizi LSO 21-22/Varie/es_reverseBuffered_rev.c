#include <stdio.h>
#include <errno.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <fenv.h>
#include <ctype.h>

// Funzione: data una stringa inverte il suo contenuto
void reverseString(char *string, int dim);

// Funzione: data una stringa restituisce 1 se è un numero, 0 altrimenti
int isNumber(char *s);


int main(int argc, char **argv)
{
	int fd; // File Descriptor
	struct stat infoFile;	// Struttura di tipo stat

	/* CONTROLLI SUL FILE - APERTURA FILE */
	if(argc != 3){printf("Uso corretto: %s <file name> <buf_size>\n", argv[0]), exit(EXIT_FAILURE);}					// Controllo argomenti di input
	if(!isNumber(argv[2])){printf("L'argomento <buf_size> deve essere un numero intero!\n"), exit(EXIT_FAILURE);}				// <buf_size> numero intero?
	if((fd=open(argv[1], O_RDONLY)) < 0){perror("Errore apertura file"), exit(EXIT_FAILURE);}						// Apertura file con controllo
	if(lstat(argv[1], &infoFile) < 0 ){perror("Errore lstat"), exit(EXIT_FAILURE);}								// Recupero informazioni sul file
	if(atoi(argv[2]) > infoFile.st_size || atoi(argv[2]) == 0)										// Dimensione buffer corretta?
	{printf("Dimensione buffer non valida (dimensione massima supportata %ld)\n", infoFile.st_size), exit(EXIT_FAILURE);}	
	if(!S_ISREG(infoFile.st_mode)){printf("File %s non regolare", argv[1]), exit(EXIT_FAILURE);}						// File regolare?
	if(!infoFile.st_size){printf("File %s vuoto", argv[1]), exit(EXIT_FAILURE);}								// File vuoto?
	if(infoFile.st_size == 1){printf("Il file %s ha una dimensione di un byte\n", argv[1]), exit(EXIT_FAILURE);}				// Dimensione file maggiore di 1 byte?
	/* ---------------------------------- */

	ssize_t bytesRead;		// Byte letti (read)
	ssize_t bytesWrite;		// Byte scritti (write)
	size_t count;			// Quantità di bytes da leggere/scrivere ad ogni read/write
	off_t bytesToRead;		// Quantità di bytes totali da leggere dal file
	off_t currentOffset;		// Valore di ritorno di una lseek

	size_t BUF_SIZE = atoi(argv[2]);	// Dimensione buffer (atoi converte stringa <buf_size> ricevuta in input in intero)
	char buffer[BUF_SIZE];			// Dichiarazione buffer di dimensione BUF_SIZE 

	// Posizionamento off_set alla fine del file
	currentOffset = lseek(fd, 0, SEEK_END); 
	if(currentOffset < 0){perror("\nErrore seeking (1)\n"), exit(EXIT_FAILURE);}	// controllo lseek

	bytesToRead = infoFile.st_size;	// Quantità di bytes totali da leggere dal file (inizialmente uguale alla dimensione del file)
	count = BUF_SIZE; 		// Quantità di bytes da leggere/scrivere ad ogni read/write (inizialmente uguale alla dimensione del buffer)

	while(bytesToRead > 0) // Finché ci sono bytes da leggere
	{
		if(bytesToRead < count)
		{
			count = bytesToRead;	
		}

		currentOffset = lseek(fd, -count, SEEK_CUR); // Modifica offset: -[count] dalla posizione corrente
		if(currentOffset < 0){perror("\nErrore seeking (2)\n"), exit(EXIT_FAILURE);} // controllo lseek

		bytesRead = read(fd, buffer, count); // Lettura [count] bytes - l'offset è aumentato di [count]
		if(bytesRead != count){perror("\nErrore di lettura (1)\n"), exit(EXIT_FAILURE);} // controllo read

		reverseString(buffer, count); // Inversione stringa catturata

		bytesWrite = write(STDOUT_FILENO, buffer, count); // Scrittura [count] bytes sullo STDOUT
		if(bytesWrite != count){perror("\nErrore scrittura file (1)\n"), exit(EXIT_FAILURE);} // controllo write

		currentOffset = lseek(fd, -count, SEEK_CUR); // Modifica offset: -[count] dalla posizione corrente
		if(currentOffset < 0){perror("\nErrore seeking (3)\n"), exit(EXIT_FAILURE);} // controllo lseek

		bytesToRead = bytesToRead - count; // Aggiornamento bytes da leggere
	}

	printf("\n");
	if(close(fd) < 0){perror("Errore chiusura file"), exit(EXIT_FAILURE);}
	exit(EXIT_SUCCESS);
}


// Funzione: data una stringa inverte il suo contenuto
void reverseString(char *string, int dim)
{
	char tmp;
	int I;

	for(I = 0; I < (dim/2); ++I)
	{
		tmp = string[I];
		string[I] = string[(dim-1)-I];
		string[(dim-1)-I] = tmp;
	}
}


// Funzione: data una stringa restituisce 1 se è un numero, 0 altrimenti
int isNumber(char *s)
{
	for (int i = 0; s[i]!= '\0'; i++)
	{
		if (isdigit(s[i]) == 0)
			return 0;
	}

	return 1;
}