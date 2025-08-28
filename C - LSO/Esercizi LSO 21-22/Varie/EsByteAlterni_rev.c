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
#include <stdio_ext.h>

#define BUF_SIZE 10 // Dimensione buffer - è possibile modificarlo a piacimento


// Alterna la stringa in input
void alternateString(char *string, int dim)
{
	int I;

	for(I = 1; I < lround(dim/2); ++I)
	{
		string[I] = string[I*2];
	}
}


int main(int argc, char **argv)
{
	int fd; // File Descriptor
	struct stat infoFile; // Struttura di tipo stat

	/* CONTROLLI SUL FILE - APERTURA FILE */
	if(argc != 2) {printf("E' necessario passare un solo nome di un file in input!\n"); exit(EXIT_FAILURE);} 		// Controllo parametro input
	if( (fd = open(argv[1],O_RDONLY)) < 0){perror("Errore apertura file\n"); exit(EXIT_FAILURE);} 			// Apertura file solo in lettura con controllo
	if(lstat(argv[1], &infoFile) < 0) {perror("Errore lstat\n"); exit(EXIT_FAILURE);} 					// Riempimento campi struttura stat
	if(!S_ISREG(infoFile.st_mode)){printf("Il file passato in input non è un file regolare\n"); exit(EXIT_FAILURE);} 	// Il file deve essere un file regolare
	if(!infoFile.st_size){printf("Il file è vuoto\n"); exit(EXIT_FAILURE);} 					// Il file non deve essere vuoto
	/* --------------------------------- */

	ssize_t bytesRead; // Variabile usata per indicare la quantità di bytes letti (read)
	ssize_t bytesWrite; // Variabile usata per indicare la quantità di bytes scritti (write)
	off_t bytesToRead = infoFile.st_size; // Variabile che indica quanti bytes bisogna ancora leggere (inizialmente uguale alla dimensione del file)
	char buffer[BUF_SIZE+1];

	while(bytesToRead >= BUF_SIZE) // Finché i bytes da leggere sono più di BUF_SIZE
	{
		bytesRead = read(fd, buffer, BUF_SIZE); // Lettura 10 bytes
		if(bytesRead != BUF_SIZE){perror("\nErrore di lettura (1)\n"), exit(EXIT_FAILURE);} // controllo read

		buffer[BUF_SIZE] = '\0'; // Terminatore
		alternateString(buffer, BUF_SIZE); // Alterna stringa

		bytesWrite = write(STDOUT_FILENO, buffer, BUF_SIZE/2); // Scrittura metà array di caratteri su STDOUT
		if(bytesWrite != BUF_SIZE/2){perror("\nErrore di scrittura(1)\n"), exit(EXIT_FAILURE);} // controllo write

		bytesToRead = bytesToRead - BUF_SIZE; // Aggiornamento byte da leggere
	}

	if(bytesToRead != 0) // Bytes residui (minore di 10)
	{
		bytesRead = read(fd, buffer, bytesToRead); // Lettura [bytesToRead] bytes
		if(bytesRead != bytesToRead){perror("\nErrore di lettura (2)\n"), exit(EXIT_FAILURE);} // controllo read

		alternateString(buffer, bytesToRead); // Alterna stringa

		bytesWrite = write(STDOUT_FILENO, buffer, lround(bytesToRead/2)); // Scrittura metà [bytesToRoad] su STDOUT
		if(bytesWrite != bytesToRead/2){perror("\nErrore di scrittura (2)\n"), exit(EXIT_FAILURE);} // controllo write
	}

	printf("\n");
	close(fd);
	exit(EXIT_SUCCESS);
}
