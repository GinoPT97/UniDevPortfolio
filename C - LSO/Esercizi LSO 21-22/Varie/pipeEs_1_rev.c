#include <stdio.h>	// printf(), sprintf(), perror()
#include <stdlib.h>	// exit()
#include <stdio_ext.h>	
#include <unistd.h>	// open(), fork(), close()
#include <sys/types.h>	// waitpid()
#include <sys/stat.h>	// open(), fstat(), S_ISREG()
#include <sys/wait.h>	// waitpid()
#include <fcntl.h>	// open()
#include <string.h>
#include <errno.h>
#include <time.h>	// rand(), srand()
#include <signal.h>
#include <ctype.h>

/* ---------------------------- DICHIARAZIONE FUNZIONI ----------------------------*/
/* close() con controllo */
void safeClose(int fd);
/* write() con controllo */
ssize_t safeWrite(int fd, const void *buf, size_t count);
/* read() con controllo */
ssize_t safeRead(int fd, void *buf, size_t count);
/* fork() con controllo */
pid_t safeFork();
/* waitpid() con controllo */
void safe_waitpid(pid_t pid, int *status, int options);
/* Controlla lo status del figlio terminato e termina in caso di situazioni anomale */
void checkStatusChild(int status);
/* pipe() con controllo */
void safePipe(int *filedes);
/* signal() con controllo */
/* data una stringa restituisce 1 se è un numero, 0 altrimenti */
int isNumber(char *s);
/* Restituisce un numero intero random tra 0 e MAX*/
int randomNumber(int max);
/* -------------------------------------------------------------------------------- */


int main(int argc, char **argv)
{
	/* Controlli preliminari */
	if(argc != 2)	{printf("Uso corretto: %s <num_intero>\n", argv[0]), exit(EXIT_FAILURE);}	// Numero argomenti uguale a due
	if(!isNumber(argv[1]))	{printf("Il parametro di input deve essere un numero intero!\n"), exit(EXIT_FAILURE);}	// Il secondo argomento deve essere un intero
	/* --------------------- */

	int numeriInteri; // Indica la quantità di numeri da scrivere sulla pipe
	int fd[2]; // File Descriptors per la pipe
	pid_t pid;

	// Conversione in intero del parametro di input <num_intero>
	numeriInteri = atoi(argv[1]); // Indica la quantità di numeri da scrivere sulla pipe

	safePipe(fd); // Creazione pipe

	// FORK
	pid = safeFork(); 

	if(pid != 0)
	{
		// PADRE

		srand(time(0));	// inizializzione del generatore sull'ora attuale dell'elaboratore (per la generazione di numeri random)

		safeClose(fd[0]); // Chiusura fd[0] per la lettura su pipe

		int numero_Random; // Numero random

		// Scrittura di [numeriInteri] random numeri sulla pipe
		for(int I = 1; I <= numeriInteri; I++)
		{
			// Generazione di un numero random tra 0 e 100
			numero_Random = randomNumber(100);

			safeWrite(fd[1], &numero_Random, sizeof(int)); // Scrive il numero sulla pipe
		}

		/* Controllo stato uscita del figlio */
		int status;
		safe_waitpid(pid, &status, 0);
		checkStatusChild(status);
		/* --------------------------------- */
	
		safeClose(fd[1]); // Chiusura lato scrittura pipe
	}
	else
	{
		// FIGLIO

		safeClose(fd[1]); // Chiusura fd[1] per la scrittura su pipe		

		int numero; // Numero da leggere
		char buf[5]; // Dimensione di un intero

		buf[4] = '\n';


		// Lettura numero sulla pipe e conseguente scrittura su STDOUT_FILENO 
		do
		{
			sleep(1); // Aspetta un secondo

			safeRead(fd[0], &numero, sizeof(int)); // Lettura intero

			snprintf(buf, sizeof(int), "%d", numero); // Conversione in stringa di caratteri (è necessario se vogliamo stampare con write() altrimenti direttamente printf() )

			safeWrite(STDOUT_FILENO, buf, sizeof(int)+1); // Scrittura su STDOUT
			
			numeriInteri--; // Decremento quantità numeri da scrivere su STDOUT

		}while(numeriInteri); // Loop fino a quando ci sono numeri da scrivere

		safeClose(fd[0]); // Chiusura lato lettura pipe
	}

	exit(EXIT_SUCCESS);

}





/* IMPLEMENTAZIONE FUNZIONI */


int isNumber(char *s)
{
	for (int i = 0; s[i]!= '\0'; i++)
	{
		if (isdigit(s[i]) == 0)
			return 0;
	}

	return 1;
}


int randomNumber(int max)
{
	return rand()%(max+1);
}


void checkStatusChild(int status)
{
	if(!WIFEXITED(status) || WEXITSTATUS(status) == EXIT_FAILURE)
		printf("Terminazione anomala figlio\n"), exit(EXIT_FAILURE);
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

ssize_t safeWrite(int fd, const void *buf, size_t count)
{
	ssize_t bytesWrite;

	bytesWrite = write(fd, buf, count);
	if(bytesWrite < 0)
		perror("Errore write()"), exit(EXIT_FAILURE);

	return bytesWrite;
}


ssize_t safeRead(int fd, void *buf, size_t count)
{
	ssize_t bytesRead;

	bytesRead = read(fd, buf, count);
	if(bytesRead < 0)
		perror("Errore read()"), exit(EXIT_FAILURE);

	return bytesRead;
}


void safe_waitpid(pid_t pid, int *status, int options)
{
	if(waitpid(pid, status, options) < 0)
		perror("Errore waitpid()"), exit(EXIT_FAILURE);
}

void safePipe(int *filedes)
{
	if(pipe(filedes) < 0)
		perror("Errore pipe()"), exit(EXIT_FAILURE);
}

/* -------------------------------------- */