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

/* Dichiarazione tipo sighandler: puntatore a funzione che
   accetta un intero come parametro e restituisce void */
typedef void (*sighandler_t)(int);

/* ---------------------------- DICHIARAZIONE FUNZIONI ----------------------------*/
/* close() con controllo */
void safeClose(int fd);
/* write() con controllo */
ssize_t safeWrite(int fd, char *stringa, size_t count);
/* read() con controllo */
ssize_t safeRead(int fd, char *stringa, size_t count);
/* fork() con controllo */
pid_t safeFork();
/* waitpid() con controllo */
void safe_waitpid(pid_t pid, int *status, int options);
/* Controlla lo status del figlio terminato e termina in caso di situazioni anomale */
void checkStatusChild(int status);
/* Restituisce un numero intero random tra 0 e MAX*/
int randomNumber(int MAX);
/* pipe() con controllo */
void safePipe(int *filedes);
/* signal() con controllo */
void safe_signal(int signum, sighandler_t handler);
/* kill() con controllo */
void safe_kill(pid_t pid, int sig);
/* data una stringa restituisce 1 se è un numero, 0 altrimenti */
int isNumber(char *s);
/* -------------------------------------------------------------------------------- */


/* Dichiarazioni signal handler utente */
void waitToWrite(int sig); // USATA DAL PADRE
void waitToRead(int sig); // USATA DAL FIGLIO
/* ----------------------------------- */

/* Variabili GLOBALI
(sono necessarie per usarle nei signal handler) */
int numeriInteri; // Indica la quantità di numeri da scrivere sulla pipe
int fd[2]; // File Descriptors per la pipe
pid_t pid;
/* ---------------- */


int main(int argc, char **argv)
{
	/* Controlli preliminari */
	if(argc != 2)	{printf("Uso corretto: %s <num_intero>\n", argv[0]), exit(EXIT_FAILURE);}	// Numero argomenti uguale a due
	if(!isNumber(argv[1]))	{printf("Il parametro di input deve essere un numero intero!\n"), exit(EXIT_FAILURE);}	// Il secondo argomento deve essere un intero
	/* --------------------- */

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

		/* Cattura segnali */
		safe_signal(SIGALRM, waitToWrite);
		safe_signal(SIGCHLD, waitToWrite);
		/* --------------- */

		alarm(1); // Invia al processo corrente il segnale SIGALRM dopo 1 secondo
		
		while(1); // LOOP

	}
	else
	{
		// FIGLIO

		safeClose(fd[1]); // Chiusura fd[1] per la scrittura su pipe		

		/* Cattura segnali */
		safe_signal(SIGUSR1, waitToRead);
		safe_signal(SIGUSR2, waitToRead);
		/* --------------- */

		while(1); // LOOP
	}


	// E' impossibile arrivare qui
	exit(EXIT_FAILURE);
}



/* IMPLEMENTAZIONE SIGNAL HANDLER PADRE/FIGLIO */

// SIGNAL HANDLER PADRE
void waitToWrite(int sig)
{
	if(sig == SIGALRM) // E' passato un secondo dall'ultimo alarm(1)
	{
		if(numeriInteri) // Bisogna ancora scrivere numeri sulla pipe?
		{
			/* Generazione di un numero random tra 0 e 100 e conversione del numero in stringa */
			char buffer[10];
			sprintf(buffer, "%d", randomNumber(100));
			/* --------------------------------------- */

			safeWrite(fd[1], buffer, 1); // Scrive il numero sulla pipe

			safe_kill(pid, SIGUSR1); // Invio segnale di permesso di lettura al figlio
		
			numeriInteri--; // Decremento
			alarm(1);	// Preparazione nuova scrittura su pipe 
		}
		else // Siamo arrivati a scrivere [numeriInteri] numeri sulla pipe
		{
			safe_kill(pid, SIGUSR2); // Segnale di uscita per il figlio
		}
	}

	if(sig == SIGCHLD) // Il figlio è appena terminato
	{
		/* Controllo stato uscita del figlio */
		int status;
		safe_waitpid(pid, &status, 0);
		checkStatusChild(status);
		/* --------------------------------- */
	
		safeClose(fd[1]); // Chiusura lato scrittura pipe
		exit(EXIT_SUCCESS);
	}
}

// SIGNAL HANDLER FIGLIO
void waitToRead(int sig)
{
	if(sig == SIGUSR1) // Segnale per permesso di lettura catturato (inviato dal padre)
	{
		// Lettura numero sulla pipe e conseguente scrittura su STDOUT_FILENO 
		char c[2];

		safeRead(fd[0], c, 1);
		c[1] = '\n';

		safeWrite(STDOUT_FILENO, c, 2);
	}

	if(sig == SIGUSR2) // Segnale per uscita catturato (inviato dal padre)
	{
		safeClose(fd[0]); // Chiusura lato lettura pipe
		exit(EXIT_SUCCESS);
	}
}

/* ------------------------------------------------------------ */




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


int randomNumber(int MAX)
{
	return rand()%(MAX+1);
}

void checkStatusChild(int status)
{
	if(!WIFEXITED(status) || WEXITSTATUS(status) == EXIT_FAILURE)
		perror("Terminazione anomala figlio"), exit(EXIT_FAILURE);	
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

ssize_t safeWrite(int fd, char *stringa, size_t count)
{
	ssize_t bytesWrite;
	if((bytesWrite = write(fd, stringa, count)) < 0)
		perror("Errore write()"), exit(EXIT_FAILURE);
	return bytesWrite;
}

ssize_t safeRead(int fd, char *stringa, size_t count)
{
	ssize_t bytesRead;
	if((bytesRead = read(fd, stringa, count)) < 0)
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

void safe_signal(int signum, sighandler_t handler)
{
	if(signal(signum, handler) == SIG_ERR)
		perror("Errore signal()"), exit(EXIT_FAILURE);
}

void safe_kill(pid_t pid, int sig)
{
	if(kill(pid, sig) < 0)
		perror("Errore kill()"), exit(EXIT_FAILURE);
}

/* -------------------------------------- */