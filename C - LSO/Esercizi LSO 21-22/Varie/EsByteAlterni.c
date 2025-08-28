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


int main(int argc, char *argv[])
{
	int fd; // File Descriptor
	struct stat buff; // Struttura di tipo stat

	if(argc != 2) {printf("E' necessario passare un solo nome di un file in input!\n"); exit(1);} 		// Controllo parametro input
	if( (fd = open(argv[1],O_RDONLY)) < 0){perror("Errore apertura file\n"); exit(2);} 			// Apertura file solo in lettura con controllo
	if(lstat(argv[1], &buff) < 0) {perror("Errore lstat\n"); exit(3);} 					// Riempimento campi struttura stat
	if(!S_ISREG(buff.st_mode)){printf("Il file passato in input non è un file regolare\n"); exit(5);} 	// Il file deve essere un file regolare
	if((int)buff.st_size < 1){printf("Il file è vuoto\n"); exit(6);} 					// Il file non deve essere vuoto

	char buffer[((int)buff.st_size) + 1]; // Dichiarazione di un buffer di dimensione uguale alla dimensione del file più uno
	int n; // Variabile usata per indicare la quantità di byte letti

	// Riempimento buffer con controllo
	if( (n = read(fd, buffer, (int)buff.st_size)) < (int)buff.st_size ) // Lettura file con controllo
	{
		printf("Errore lettura file\n");
		exit(7);
	}


	/* Andiamo a stampare sullo STDOUT tanti byte quant'è la dimensione del file diviso due (la funzione lround arrotonda tale operazione).
	   Precisamente andiamo a stampare il contenuto delle celle del buffer corrispondenti a indici pari.*/
	int I, J;

	for(I = 0; I < lround(((int)buff.st_size))/2; I++)
	{
		J = I*2;
		printf("%c",buffer[J]);
	}

	printf("\n");
	return 0;
}
