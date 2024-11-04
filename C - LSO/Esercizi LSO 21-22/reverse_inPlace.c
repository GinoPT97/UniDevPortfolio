#include <stdio.h>
#include <errno.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>
#include <string.h>
#include <stdio_ext.h>
#include <fenv.h>
#include <ctype.h>

// Funzione: data una stringa inverte il suo contenuto
void reverseString(char *string, int dim);

// Funzione: data una stringa restituisce 1 se è un numero, 0 altrimenti
int isNumber(char *s);


int main(int argc, char **argv)
{
	int fd1; // File Descriptor
	struct stat infoFile;

	/* CONTROLLI - APERTURA FILE */
	if(argc != 3){printf("Uso corretto: %s <filename> <buf_size>\n", argv[0]), exit(EXIT_FAILURE);} 	// 3 argomenti
	if(!isNumber(argv[2])){printf("Il secondo argomento deve essere un intero!\n"), exit(EXIT_FAILURE);} 	// il 2° argomento è un intero?
	if((fd1=open(argv[1], O_RDWR)) < 0){perror("Errore apertura file"), exit(EXIT_FAILURE);} 		// Apertura file
	if(lstat(argv[1], &infoFile) < 0){perror("Errore lstat"), exit(EXIT_FAILURE);} 				// Recupero info sul file
	if(!S_ISREG(infoFile.st_mode)){printf("Il file %s non è regolare!\n", argv[0]), exit(EXIT_FAILURE);} 	// File regolare?
	if(!infoFile.st_size){printf("Il file %s è vuoto!\n", argv[0]), exit(EXIT_FAILURE);} 			// File vuoto?
	if(infoFile.st_size == 1){printf("La dimensione del file è di un byte\n"), exit(EXIT_FAILURE);} 	// Dimensione del file maggiore di un byte?
	if(atoi(argv[2]) > infoFile.st_size/2){printf("Non è possibile usare un buffer di dimensione %s (dimensione massima %ld)\n", argv[2], infoFile.st_size/2), exit(EXIT_FAILURE);} // Dimensione massima buffer
	/* ------------------------ */

	ssize_t bytesRead;		// Bytes letti (read)
	ssize_t bytesWrite;		// Bytes scritti (write)
	size_t count;			// Bytes da leggere per ogni chiamata read/write
	off_t bytesToRead;		// Bytes totali da leggere dal file
	off_t currentOffset;		// Offset corrente (ritorno di lseek)

	// Apertura stesso file con un altro File Descriptor fd2	
	int fd2;

	if((fd2=open(argv[1], O_RDWR)) < 0){perror("Errore apertura file"), exit(EXIT_FAILURE);}
			
	/* File Descriptor 1 (fd1) parte dall'inizio | File Descriptor 2 (fd2) parte dalla fine */
	int BUF_SIZE = atoi(argv[2]); 	// Dimensione buffer
	char buffer1[BUF_SIZE+1];	// Dichiarazione buffer1
	char buffer2[BUF_SIZE+1];	// Dichiarazione buffer2	

	bytesToRead = infoFile.st_size; 	// Bytes totali da leggere dal file
	count = BUF_SIZE;			// Bytes da leggere per ogni chiamata read/write uguale, inizialmente, alla dimensione del buffer

	// Offset iniziale fd2
	if( (currentOffset = lseek(fd2, -1, SEEK_END)) < 0){perror("Errore lseek (0)"), exit(EXIT_FAILURE);}
	
	while(bytesToRead > 1) // Finché ci sono bytes da leggere
	{
		// Se è vero questo if è necessario aggiornare la quantità di bytes letti/scritti in questo passo
		if(bytesToRead/2 < count) 
		{
			count = bytesToRead/2;
		}
		
		/* BUFFER 1 (fd1): Lettura -> Ritorno indietro */
		bytesRead = read(fd1, buffer1, count);
		if(bytesRead != count){perror("Errore read (0)"), exit(EXIT_FAILURE);}

		buffer1[count] = '\0';

		currentOffset = lseek(fd1, -count, SEEK_CUR);
		if(currentOffset < 0){perror("Errore lseek (1)"), exit(EXIT_FAILURE);}
		/* -------------------------------------- */
		

		/* BUFFER 2 (fd1): Spostamento offset indietro di [count] -> Lettura -> Spostamento offset indietro di [count] */
		currentOffset = lseek(fd2, -count, SEEK_CUR);
		if(currentOffset < 0){perror("Errore lseek (2)"), exit(EXIT_FAILURE);}	

		bytesRead = read(fd2, buffer2, count);
		if(bytesRead != count){perror("Errore read (1)"), exit(EXIT_FAILURE);}

		buffer2[count] = '\0';

		currentOffset = lseek(fd2, -count, SEEK_CUR);
		if(currentOffset < 0){perror("Errore lseek (3)"), exit(EXIT_FAILURE);}
		/* ----------------------------------------------- */


		/* BUFFER 1: Inversione stringa buffer2 -> Scrittura con fd1 stringa buffer2*/
		reverseString(buffer2, count);

		bytesWrite = write(fd1, buffer2, count);
		if(bytesWrite != count){perror("Errore write (0)"), exit(EXIT_FAILURE);}
		/* ----------------------------------------------- */


		/* BUFFER 2: Inversione stringa buffer 1 -> Scrittura con fd2 stringa buffer1 -> Spostamento offset indietro di [count]*/
		reverseString(buffer1, count);

		bytesWrite = write(fd2, buffer1, count);
		if(bytesWrite != count){perror("Errore write (1)"), exit(EXIT_FAILURE);}

		currentOffset_fd2 = lseek(fd2, -count, SEEK_CUR);
		if(currentOffset < 0){perror("Errore lseek (4)"), exit(EXIT_FAILURE);}
		/* ----------------------------------------------- */

		bytesToRead = bytesToRead - (count*2); // Aggiornamento bytes ancora da leggere
	}

	if(close(fd1) < 0){perror("Errore chiusura fd1"), exit(EXIT_FAILURE);}
	if(close(fd2) < 0){perror("Errore chiusura fd2"), exit(EXIT_FAILURE);}

	exit(EXIT_SUCCESS);
}




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







