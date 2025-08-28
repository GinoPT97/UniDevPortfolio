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

#define BUF_SIZE 10 // Costante dimensione buffer

// Funzione: data una stringa inverte il suo contenuto
void reverseString(char *string, int dim)
{
	char tmp;
	int I;

	for(I = 0; I <= (dim/2)-1; ++I)
	{
		tmp = string[I];
		string[I] = string[(dim-1)-I];
		string[(dim-1)-I] = tmp;
	}
}


int main(int argc, char **argv)
{
	int fd; // File Descriptor
	struct stat infoFile;	// Struttura di tipo stat

	/* CONTROLLI SUL FILE - APERTURA FILE */
	if(argc != 2)	{printf("Usage: %s <file name>\n", argv[0]), exit(EXIT_FAILURE);}	// Controllo argomenti di input
	if( (fd=open(argv[1], O_RDONLY)) < 0 )	{perror("\nErrore apertura file\n"), exit(EXIT_FAILURE);}	// Apertura file con controllo
	if(lstat(argv[1], &infoFile) < 0 )	{perror("\nErrore lstat\n"), exit(EXIT_FAILURE);}	// Recupero informazioni sul file
	if(!S_ISREG(infoFile.st_mode))	{printf("\nFile non regolare\n"), exit(EXIT_FAILURE);}	// File regolare?
	if(!infoFile.st_size)	{printf("\nFile vuoto\n"), exit(EXIT_FAILURE);}	// File vuoto?
	/* ---------------------------------- */


	char buffer[BUF_SIZE+1];
	ssize_t bytesRead;	// Byte letti (read)
	ssize_t bytesWrite;	// Byte scritti (write)
	off_t currentOffset;	// Valore di ritorno di una lseek

	currentOffset = lseek(fd, -1, SEEK_END); // Posizionamento offset del file: meno uno dalla fine del file
	if(currentOffset < 0)	{perror("\nErrore seeking (1)\n"), exit(EXIT_FAILURE);}	// controllo lseek

	while( (BUF_SIZE) <= currentOffset) // Finché l'offset attuale è maggiore o uguale di BUF_SIZE (10)
	{
		currentOffset = lseek(fd, -10, SEEK_CUR); // Modifica offset: -10 dalla posizione corrente
		if(currentOffset < 0){perror("\nErrore seeking (2)\n"), exit(EXIT_FAILURE);} // controllo lseek

		bytesRead = read(fd, buffer, BUF_SIZE); // Lettura 10 bytes - l'offset è aumentato di 10
		if(bytesRead < 0){perror("\nErrore di lettura (1)\n"), exit(EXIT_FAILURE);} // controllo read

		buffer[BUF_SIZE] = '\0'; // Terminatore

		reverseString(buffer, BUF_SIZE); // Inversione stringa catturata

		bytesWrite = write(STDOUT_FILENO,buffer,BUF_SIZE); // Scrittura 10 bytes sullo STDOUT
		if(bytesWrite != 10){perror("\nErrore scrittura file (1)\n"), exit(EXIT_FAILURE);} // controllo write

		currentOffset = lseek(fd, -10, SEEK_CUR); // Modifica offset: -10 dalla posizione corrente
		if(currentOffset < 0){perror("\nErrore seeking (3)\n"), exit(EXIT_FAILURE);} // controllo lseek
	}

	// Se diverso da 0 è rimasto qualcosa da leggere
	if(currentOffset != 0)
	{
		int residuo = currentOffset; // Il residuo è l'offset corrente

		currentOffset = lseek(fd, -residuo, SEEK_CUR); // Modifica offset: -residuo dalla posizione corrente (oppure si può usare 0, SEEK_SET) 
		if(currentOffset < 0){perror("\nErrore seeking (4)\n"), exit(EXIT_FAILURE);} // controllo lseek

		bytesRead = read(fd, buffer, residuo); // Lettura [residuo] bytes - l'offset è aumentato di [residuo]
		if(bytesRead != residuo){perror("\nErrore di lettura (2)\n"), exit(EXIT_FAILURE);}

		buffer[residuo] = '\0'; // Terminatore

		reverseString(buffer, residuo); // Inversione stringa catturata

		bytesWrite = write(STDOUT_FILENO, buffer, residuo); // Scrittura [residuo] bytes sullo STDOUT
		if(bytesWrite != residuo){perror("\nErrore scrittura file (2)\n"), exit(EXIT_FAILURE);} // controllo write
	}

	printf("\n");
	close(fd);
	exit(EXIT_SUCCESS);
}
