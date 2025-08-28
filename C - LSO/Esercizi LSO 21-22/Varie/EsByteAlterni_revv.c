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
#include <ctype.h>

/* DICHIARAZIONE FUNZIONI */
int roundDivision(int num);				// Arrotonda il risultato dell'operazione num/2
void alternateString(char *string, int dim, int flag);	// Alterna la stringa in input
int isEven(int num);					// Ritorna 1 se num è pari, 0 altrimenti
int isNumber(char s[]);					// Ritorna 1 se la stringa s è un numero intero, 0 altrimenti
/* ---------------------- */


int main(int argc, char **argv)
{
	int fd; // File Descriptor
	struct stat infoFile; // Struttura di tipo stat

	/* CONTROLLI SUL FILE - APERTURA FILE */
	int b = 0;
	if(argc != 3) {printf("Usage %s <filename> <buffer_size>\n", argv[0]), exit(EXIT_FAILURE);} 						// Controllo parametri input
	if(!isNumber(argv[2])){printf("Dimensione buffer errata\n"), exit(EXIT_FAILURE);}							// <buffer_size> è un numero intero?
	if(lstat(argv[1], &infoFile) < 0) {perror("Errore lstat"), exit(EXIT_FAILURE);} 							// Riempimento campi struttura stat
	if(atoi(argv[2]) > infoFile.st_size || (b = (atoi(argv[2]) > 4096)))									// Controllo dimensione buffer
	{printf("Dimensione massima supportata: "); if(b){printf("4096\n");}else{printf("%ld\n", infoFile.st_size);} exit(EXIT_FAILURE);}
	if( (fd = open(argv[1],O_RDONLY)) < 0){perror("Errore apertura file"), exit(EXIT_FAILURE);} 						// Apertura file solo in lettura con controllo
	if(!S_ISREG(infoFile.st_mode)){printf("Il file passato in input non è un file regolare\n"), exit(EXIT_FAILURE);} 			// Il file deve essere un file regolare
	if(!infoFile.st_size){printf("Il file è vuoto\n"), exit(EXIT_FAILURE);} 								// Il file non deve essere vuoto
	if(infoFile.st_size == 1){printf("La dimensione del file è di un byte!\n"), exit(EXIT_FAILURE);}					// Il file deve avere una dimensione di almeno 2 bytes
	/* --------------------------------- */

	ssize_t bytesRead; 		// Variabile usata per indicare la quantità di bytes letti (read)
	ssize_t bytesWrite; 		// Variabile usata per indicare la quantità di bytes scritti (write)
	off_t bytesToRead; 		// Variabile che indica quanti bytes bisogna ancora leggere (inizialmente uguale alla dimensione del file)
	char buffer[atoi(argv[2])];	// Dichiarazione buffer di dimensione presa in input (atoi converte una stringa in intero)

	int flag;			// Variabile flag usata per capire se bisogna scrivere o meno sullo STDOUT il primo carattere della stringa catturata ad ogni passo
	size_t count;			// Variabile usata per indicare la quantità di bytes da leggere/scrivere in una read/write

	count = atoi(argv[2]);	// Variabile count uguale, inizialmente, alla dimensione del buffer
	bytesToRead = infoFile.st_size; // Variabile bytesToRead uguale, inizialmente, alla dimensione del file
	flag = 1; // Flag inizialmente uguale a 1

	/* INIZIALIZZAZIONE - Lettura primi [count] bytes dal file */
	bytesRead = read(fd, buffer, count); // Lettura count bytes
	if(bytesRead != count){perror("Errore di lettura (1)"), exit(EXIT_FAILURE);} // controllo read

	buffer[count] = '\0'; // Terminatore
	alternateString(buffer, count, flag); // Alterna stringa - vedi implementazione funzione per capire [flag]

	bytesWrite = write(STDOUT_FILENO, buffer, roundDivision(count)); // Scrittura metà array di caratteri sullo STDOUT
	if(bytesWrite != roundDivision(count)){perror("Errore di scrittura(1)"), exit(EXIT_FAILURE);} // controllo write

	bytesToRead = bytesToRead - count; // Aggiornamento byte da leggere

	while(bytesToRead) // Finché ci sono bytes da leggere
	{
		if(isEven(count)){flag = 1;}	 // Se la quantità di bytes letti al passo precedente è pari, allora flag = 1
		else{flag = !flag;}		 // altrimenti flag = !flag

		/* Se è vero questo if, allora andiamo a gestire i bytes rimasti (minori di BUF_SIZE) */
		if(bytesToRead < count)
		{
			if(bytesToRead == 1) // Se è rimasto un solo byte
			{
				if(flag == 1) // Se è vero, vuol dire che dobbiamo leggerlo
				{
					bytesRead = read(fd, &buffer[0], 1);
					if(bytesRead != 1){perror("Errore di lettura (2)"), exit(EXIT_FAILURE);} // controllo read

					bytesWrite = write(STDOUT_FILENO, &buffer[0], 1);
					if(bytesWrite != 1){perror("Errore di scrittura(2)"), exit(EXIT_FAILURE);} // controllo write

					break;
				}
				else{break;} // altrimenti non ci interessa leggerlo -> break
			}
			else // Se non è rimasto un solo byte, la quantità di bytes da leggere in questo passo è bytesToRead
			{
				count = bytesToRead;
			}
		}
		/* ---------------------------------------------------------------------------------- */

		bytesRead = read(fd, buffer, count); // Lettura [count] bytes
		if(bytesRead != count){perror("Errore di lettura (1)"), exit(EXIT_FAILURE);} // controllo read

		buffer[count] = '\0'; // Terminatore
		alternateString(buffer, count, flag); // Alterna stringa - vedi implementazione funzione per capire [flag]

		// Scrittura metà array di caratteri su STDOUT | se flag = 0 allora andiamo a scrivere [count/2 arrotondato - 1] bytes, altrimenti [count/2 arrotondato] bytes
		bytesWrite = write(STDOUT_FILENO, buffer, roundDivision(count)-(!flag)); 
		if(bytesWrite != roundDivision(count)-(!flag)){perror("Errore di scrittura(1)"), exit(EXIT_FAILURE);} // controllo write

		bytesToRead = bytesToRead - count; // Aggiornamento byte da leggere
	}

	printf("\n");
	if(close(fd) < 0){perror("Errore chiusura file"), exit(EXIT_FAILURE);}
	exit(EXIT_SUCCESS);
}




/* IMPLEMENTAZIONE FUNZIONI */

// Arrotonda il risultato di una divisione per due di num
// esempio: 11/2 = 5.5 -> 6 (output funzione)
int roundDivision(int num)
{
	if(isEven(num)){return num/2;}
	else{return (num/2)+1;}
}

// Alterna la stringa in input
// Se FLAG == 1 --> alterna a partire dalla posizione 0 (cioè SI-NO-SI-NO...)
// Se FLAG == 0 --> alterna a partire dalla posizione 1 (cioè NO-SI-NO-SI...)
void alternateString(char *string, int dim, int flag)
{
	int I;

	if(flag == 1)
	{
		for(I = 1; I < roundDivision(dim); ++I)
		{
			string[I] = string[I*2];
		}
	}
	else
	{
		string[0] = string[1];
		for(I = 1; I < roundDivision(dim)-1; ++I)
		{
			string[I] = string[(I*2)+1];
		}
	}
}

// Ritorna 1 se num è pari, 0 altrimenti
int isEven(int num)
{
	if(num % 2 == 0){return 1;}
	else{return 0;}
}

// Funzione: data una stringa restituisce 1 se è un numero, 0 altrimenti
int isNumber(char s[])
{
	for (int i = 0; s[i]!= '\0'; i++)
	{
		if (isdigit(s[i]) == 0)
			return 0;
	}

	return 1;
}

/* ----------------------- */
