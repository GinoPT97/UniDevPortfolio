#include <stdio.h>	// printf(), sprintf(), perror()
#include <stdlib.h>	// exit()	
#include <unistd.h>	// open(), fstat(), fork(), close(), dup2(), access(), exec()
#include <sys/types.h>	// waitpid()
#include <sys/stat.h>	// open(), fstat(), S_ISREG()
#include <sys/wait.h>	// waitpid()
#include <fcntl.h>	// open()
#include <string.h>	


/* open() con controllo */ 
int safeOpen(char *pathname, int flags, int permissions);
/* fstat() con controllo */
void safe_fstat(int fd, struct stat *buf);
/* fork() con controllo */
pid_t safeFork();
/* close() con controllo */
void safeClose(int fd);
/* dup2() con controllo */
int safe_dup2(int old_fd, int new_fd);
/* waitpid() con controllo */
void safe_waitpid(pid_t pid, int *status, int options);
/* Controlla lo status del figlio terminato e termina in caso di situazioni anomale */
void checkStatusChild(int status);
/* Controlla i permessi specificati in mode che ha l'utente per il file identificato da pathname*/
void safeAccess(char *pathname, int mode);
/* Dato un numero intero, restituisce il numero di cifre */
int numCifre(int n);



int main(int argc, char **argv)
{
	/* Controllo quantità parametri di input */
	if(argc != 5)	{printf("Uso corretto: %s <pathname file1> <pathname file2> <pathname file3> <pathname file.out>\n", argv[0]), exit(EXIT_FAILURE);}	

	/* Apertura files */
	int fd1, fd2, fd3, fdOut;
	fd1 = safeOpen(argv[1], O_RDONLY, 0);
	fd2 = safeOpen(argv[2], O_WRONLY | O_CREAT | O_APPEND, 0666);
	fd3 = safeOpen(argv[3], O_WRONLY | O_CREAT | O_APPEND, 0666);
	fdOut = safeOpen(argv[4], O_RDONLY, 0);
	/* -------------- */

	/* Controllo permessi giusti per i files */
	safeAccess(argv[1], R_OK);
	safeAccess(argv[2], R_OK | W_OK);
	safeAccess(argv[3], W_OK);
	safeAccess(argv[4], X_OK);
	/* ------------------------------------- */

	/* Estrazione informazioni sui files */
	struct stat infoFile1, infoFile2, infoFile3, infoFileOut;
	safe_fstat(fd1, &infoFile1);
	safe_fstat(fd2, &infoFile2);
	safe_fstat(fd3, &infoFile3);
	safe_fstat(fdOut, &infoFileOut);
	/* --------------------------------- */

	/* Controlli preliminari sul files */
	if(!S_ISREG(infoFile1.st_mode))	{printf("%s non è un file regolare!\n", argv[1]), exit(EXIT_FAILURE);}
	if(!S_ISREG(infoFile2.st_mode))	{printf("%s non è un file regolare!\n", argv[2]), exit(EXIT_FAILURE);}
	if(!S_ISREG(infoFile3.st_mode))	{printf("%s non è un file regolare!\n", argv[3]), exit(EXIT_FAILURE);}
	if(!S_ISREG(infoFileOut.st_mode))	{printf("%s non è un file regolare!\n", argv[4]), exit(EXIT_FAILURE);}

	if(!infoFile1.st_size)	{printf("Il file %s è vuoto!\n", argv[1]);}
	if(!infoFileOut.st_size)	{printf("Il file %s è vuoto!\n", argv[4]);}

	safeClose(fdOut); // chiusura file.out
	/* ------------------------------- */

	// FORK
	pid_t pid;
	pid = safeFork();

	if(pid != 0)
	{
		// PADRE

		int status;
		safe_waitpid(pid, &status, 0); // Aspetta figlio

		checkStatusChild(status); // Controlla stato figlio
		
		safeClose(fd1); // Chiude fd1
		
		// FORK
		pid = safeFork(); 

		if(pid != 0)
		{
			// PADRE

			safe_waitpid(pid, &status, 0); // Aspetta figlio

			checkStatusChild(status); // Controlla stato figlio
		}
		else
		{
			// FIGLIO
			
			safe_fstat(fd2, &infoFile2); // Aggiornamento info fd2
			fd2 = safe_dup2(fd3, STDOUT_FILENO); // Duplica fd3 usando STDOUT_FILENO

			/* Il programma reverseBuffered ha bisogno della dimensione del buffer come parametro di input */
			char dimBuffer[numCifre(infoFile2.st_size/2)];	// Dichiarazione buffer di dimensione uguale al numero di cifre del numero che rappresenta la dimensione del file puntato da fd2
			sprintf(dimBuffer, "%ld", infoFile2.st_size/2); // Converte infoFile2.st_size/2 in stringa e la inserisce in dimBuffer
			/* --------------------------------------------- */

			execl(argv[4], argv[4], argv[2], dimBuffer, (char*)NULL); // Esecuzione programma reverseBuffered su fd2 e stampa su STDOUT_FILENO
			perror("Errore execl() [2]"), exit(EXIT_FAILURE);
		}
		
	}	
	else
	{
		// FIGLIO

		fd2 = safe_dup2(fd2, STDOUT_FILENO);	// Duplica fd2 usando STDOUT_FILENO		

		/* Il programma reverseBuffered ha bisogno della dimensione del buffer come parametro di input */
		char dimBuffer[numCifre(infoFile1.st_size/2)]; // Dichiarazione buffer di dimensione uguale al numero di cifre del numero che rappresenta la dimensione del file puntato da fd1
		sprintf(dimBuffer, "%ld", infoFile1.st_size/2); // Converte infoFile2.st_size/2 in stringa e la inserisce in dimBuffer
		/* --------------------------------------------- */

		execl(argv[4], argv[4], argv[1], dimBuffer, (char*)NULL); // Esecuzione programma reverseBuffered su fd1 e stampa su STDOUT_FILENO
		perror("Errore execl() [1]"), exit(EXIT_FAILURE);
	}


	safeClose(fd2);
	safeClose(fd3);

	exit(EXIT_SUCCESS);
	
}



/* IMPLEMENTAZIONE FUNZIONI */

int safeOpen(char *pathname, int flags, int permissions)
{
	int fd;
	if((fd = open(pathname, flags, permissions)) < 0)
		perror("Errore apertura file"), exit(EXIT_FAILURE);
	else
		return fd;
}

void safe_fstat(int fd, struct stat *buf)
{
	if(fstat(fd, buf) < 0)
		perror("Errore lstat()"), exit(EXIT_FAILURE);
}

pid_t safeFork()
{
	pid_t pid;
	if((pid = fork()) < 0)
		perror("Errore fork()"), exit(EXIT_FAILURE);
	else
		return pid;
}

void safeClose(int fd)
{
	if(close(fd) < 0)
		perror("Errore chiusura file"), exit(EXIT_FAILURE);
}

int safe_dup2(int old_fd, int new_fd)
{
	if(dup2(old_fd, new_fd) < 0)
		perror("Errore dup2()"), exit(EXIT_FAILURE);
	else
		return new_fd;
}

void safe_waitpid(pid_t pid, int *status, int options)
{
	if(waitpid(pid, status, options) < 0)
		perror("Errore waitpid()"), exit(EXIT_FAILURE);
}

void checkStatusChild(int status)
{
	if(!WIFEXITED(status) || WEXITSTATUS(status) == EXIT_FAILURE)
		perror("Terminazione anomala figlio"), exit(EXIT_FAILURE);	
}

void safeAccess(char *pathname, int mode)
{
	if(access(pathname, mode) < 0)
		perror("Permesso negato"), exit(EXIT_FAILURE);
}

int numCifre(int n)
{
	int num_cifre = 0;
	if(n)
	{
		while(num_cifre/10)
			num_cifre ++;
		return num_cifre;
	}
	else{return 1;}
}

/* -------------------------------- */