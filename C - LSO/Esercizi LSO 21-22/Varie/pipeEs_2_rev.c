#include <stdio.h>	// printf(), sprintf(), perror()
#include <stdlib.h>	// exit()
#include <stdio_ext.h>	
#include <unistd.h>	// open(), fstat(), fork(), close(), dup2(), access(), exec()
#include <sys/types.h>	// waitpid()
#include <sys/stat.h>	// open(), fstat(), S_ISREG()
#include <sys/wait.h>	// waitpid()
#include <fcntl.h>	// open()
#include <string.h>
#include <errno.h>
#include <time.h>	// rand(), srand()
#include <ctype.h>
#include <limits.h> // Limiti su costanti Integer
#include <math.h>

#define MAXBUFF 10 // Costante dim BUFFER


/* close() con controllo */
void safeClose(int fd);
/* fork() con controllo */
pid_t safeFork();
/* Controlla lo stato di uscita del figlio terminato */
void checkStatusChild(int status);
/* waitpid() con controllo */
void safe_waitpid(pid_t pid, int *status, int options);
/* write() con controllo */
ssize_t safeWrite(int fd, const void *buf, size_t count);
/* read() con controllo */
ssize_t safeRead(int fd, void *buf, size_t count);
/* pipe() con controllo */
void safePipe(int *filedes);

/* Restituisce 1 se è un intero relativo, 0 altrimenti */
int isNumber_Z(char *s);


int main(int argc, char **argv)
{
	if(argc!=1){printf("Questo programma non ha bisogno di parametri di input!\n"), exit(EXIT_FAILURE);}

	int fd[2];	// File Descriptors PIPE
	pid_t pid;

	safePipe(fd);	// Creazione PIPE

	// FORK
	pid = safeFork();

	if(pid != 0)
	{
		// PADRE

		safeClose(fd[0]); // Chiusura lato lettura PIPE

		char BUFFER[MAXBUFF];
		long int x;	// Numero long int intero relativo preso in input da moltiplicare per se stesso
		int validNumber; // Indica se il numero inserito in input è valido
		long int Equal_To_Zero; // Indica se il numero inserito è uguale a 0

		Equal_To_Zero = 0; // Supponiamo che il numero non sia uguale a zero

		do
		{
			validNumber = 0; // Supponiamo che il numero non sia valido

			/* Prelievo numero [x] da tastiera con relativi controlli */
			do
			{
				printf("Inserisci un numero intero relativo: ");				
				scanf("%s", BUFFER);

				if(!isNumber_Z(BUFFER)) // Se non è un numero intero relativo
				{
					printf("Input sbagliato, inserire un numero intero!\n");
				}
				else if(strlen(BUFFER) > 1 && ( !strncmp(BUFFER, "0", 1) || !strncmp(BUFFER, "-0", 2) ) ) // Un numero del tipo '0453' oppure '-034' non è valido
				{
					printf("Il numero inserito non è valido!\n");
				}
				else if(labs(atol(BUFFER)) > sqrtl(LONG_MAX)) // Gestione overflow
				{
					printf("Il numero inserito è troppo grande! (MAX: %Lf)\n", sqrtl(LONG_MAX)); // Overflow
				}
				else
				{
					validNumber = 1;
				}

			}while(!validNumber); // Loop finché il numero non è corretto
			/* -------------------------------------------------------- */

			x = atol(BUFFER); // Conversione stringa -> long int

			Equal_To_Zero = !x; // Se x == 0, Equal_To_Zero = 1 (uguale a 0)

			x = x*x;	// x al quadrato

			safeWrite(fd[1], &x, sizeof(long int)); // Scrittura numero sulla PIPE	

			sleep(1); /* Il processo padre aspetta un secondo prima di proseguire per dare al figlio il tempo di leggere e stampare */

		}while(!Equal_To_Zero);	// Loop finché il numero non è uguale a 0

		int status;
		safe_waitpid(pid, &status, 0);	// Aspetta figlio
		checkStatusChild(status);	// Controlla stato figlio

		safeClose(fd[1]); // Chiusura lato scrittura PIPE
	}
	else
	{
		// FIGLIO

		safeClose(fd[1]); // Chiusura lato scrittura PIPE

		long int Equal_To_Zero; // Indica se il numero è uguale a 0

		long int x; // Numero da leggere

		do
		{
			safeRead(fd[0], &x, sizeof(long int)); // Legge dalla PIPE il numero elevato al quadrato

			printf("Il quadrato del numero inserito è: %ld\n", x); 

			Equal_To_Zero = !x; // Se x == 0, Equal_To_Zero = 1 (uguale a 0)
			
		}while(!Equal_To_Zero); // Continua finché il numero letto non è uguale a 0

		safeClose(fd[0]); // Chiusura lato lettura PIPE
	}	

	exit(EXIT_SUCCESS);
}


/*------------------ IMPLEMENTAZIONE FUNZIONI ----------------------*/
void safeClose(int fd)
{
	if(close(fd) < 0)
		perror("Errore chiusura file"), exit(EXIT_FAILURE);
}

pid_t safeFork()
{
	pid_t pid;
	if((pid = fork()) < 0)
		perror("Errore fork()"), exit(EXIT_FAILURE);
	else
		return pid;
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

void safePipe(int *filedes)
{
	if(pipe(filedes) < 0)
		perror("Errore pipe()"), exit(EXIT_FAILURE);
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


int isNumber_Z(char *s)
{
	int i = 0;
	if(s[0] == '-' && s[1] != '\0'){i = 1;}		
	for (i; s[i]!= '\0'; i++)
	{
		if (isdigit(s[i]) == 0)
			return 0;
	}

	return 1;
}

int numCifre_Z(int n)
{
	int num_cifre = 0;

	if(n<0){n = -n, printf("num: %d\n", n);}

	if(n!=0)
	{
		while(n > 0)
			n = n/10, num_cifre ++;
		return num_cifre;
	}
	else{return 1;}
}
/* -------------------------------------------------------- */

