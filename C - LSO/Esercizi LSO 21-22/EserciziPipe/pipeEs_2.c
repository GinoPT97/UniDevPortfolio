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
#include <signal.h>
#include <ctype.h>

#define MAXBUFF 15 // Costante dim BUFFER (è possibile cambiarla - occhio overflow)


/* Dichiarazione tipo sighandler: puntatore a funzione che
   accetta un intero come parametro e restituisce void */
typedef void (*sighandler_t)(int);


/* close() con controllo */
void safeClose(int fd);
/* fork() con controllo */
pid_t safeFork();
/* Controlla lo stato di uscita del figlio terminato */
void checkStatusChild(int status);
/* waitpid() con controllo */
void safe_waitpid(pid_t pid, int *status, int options);
/* read() con controllo */
ssize_t safeRead(int fd, char *stringa, size_t count);
/* write() con controllo */
ssize_t safeWrite(int fd, char *stringa, size_t count);
/* signal() con controllo */
void safe_signal(int signum, sighandler_t handler);
/* kill() con controllo */
void safe_kill(pid_t pid, int sig);
/* pipe() con controllo */
void safePipe(int *filedes);
/* Data una stringa, restituisce 1 se è un numero intero relativo, 0 altrimenti */
int isNumber_Z(char *s);
/* Dato un numero intero relativo, restituisce il numero di cifre */
int numCifre_Z(int n);


/* SIGNAL HANDLER */
void unlock(int sig); // Usato dal padre per sincronizzarsi con il figlio
/*----------------*/

/* VARIABILI GLOBALI */
int block; // Settato a 0 da unlock dopo aver catturato un segnale (block = 1 -> blocca padre)
/* ----------------- */

int main(int argc)
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

		/* SIGNAL HANDLER */
		safe_signal(SIGUSR1, unlock);
		/*---------------*/

		char *BUFFER;

		int x;	// Numero intero relativo preso in input da moltiplicare per se stesso

		int validNumber; // Indica se il numero inserito in input è valido o meno
		int Equal_To_Zero; // Indica se il numero inserito è uguale a 0

		safeClose(fd[0]); // Chiusura lato lettura PIPE

		Equal_To_Zero = 0; // Il numero inserito non è uguale a 0
		
		block = 1; // Dopo la scrittura su PIPE tale variabile blocca l'esecuzione del padre

		do
		{
			if(validNumber){free(BUFFER);}

			validNumber = 0;

			/* Prelievo numero [x] da tastiera con relativi controlli */
			do
			{
				// Allocazione buffer di dimensione [MAXBUFF]
				BUFFER = (char *)malloc(sizeof(char)*MAXBUFF);
				if(!BUFFER){perror("Errore malloc()"), exit(EXIT_FAILURE);} // Controllo malloc

				printf("Inserisci un numero intero relativo: ");				
				scanf("%s", BUFFER);
				BUFFER[strlen(BUFFER)] = '\0';

				if(!isNumber_Z(BUFFER)) // Se non è un numero intero relativo
				{
					printf("Input sbagliato, inserire un numero intero!\n");
					free(BUFFER); // Libera memoria
				}
				else if(strlen(BUFFER) > MAXBUFF) // Se il numero di cifre ha superato [MAXBUFF]
				{
					printf("Il numero è troppo grande (max %d cifre)!\n", MAXBUFF);
					free(BUFFER); // Libera memoria
				}
				else if(strlen(BUFFER) > 1 && ( !strncmp(BUFFER, "0", 1) || !strncmp(BUFFER, "-0", 2) ) ) // Un numero del tipo '0453' oppure '-034' non è valido
				{
					printf("Il numero inserito non è valido!\n");
					free(BUFFER); // Libera memoria
				}
				else
				{
					validNumber = 1; // Numero corretto
				}

			}while(!validNumber); // Loop finché il numero non è corretto

			if(strncmp(BUFFER, "0", 1)) // Se il numero non è uguale a 0
			{
				x = atoi(BUFFER); // Conversione stringa -> intero
				x = x*x;	// x al quadrato

				free(BUFFER); // Libera memoria

				// Allocazione buffer di dimensione [numero cifre di x]
				BUFFER = (char *)malloc(sizeof(char)*numCifre_Z(x));
				if(!BUFFER){perror("Errore malloc()"), exit(EXIT_FAILURE);} // Controllo malloc
								
				BUFFER[numCifre_Z(x)] = '\0';

				sprintf(BUFFER, "%d", x); // Conversione intero -> stringa
			}
			else
			{
				Equal_To_Zero = 1; // Il numero è uguale a 0
			}
	
			safeWrite(fd[1], BUFFER, strlen(BUFFER)); // Scrittura numero sulla PIPE
			
			while(block); // BLOCCA IL PROCESSO FINCHE' IL FIGLIO NON INVIA IL SEGNALE SIGUSR1 AL PADRE (Vedi signal handler "block()")
			
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

		pid_t ppid;	// ppid (padre)
		ppid = getppid();	// prelievo ppid

		char *BUFFER;
		ssize_t bytesRead; // Indica la quantità di bytes letti dalla PIPE

		int Equal_To_Zero; // Indica se il numero è uguale a 0

		do
		{
			// Allocazione buffer di dimensione [MAXBUFF*2]
			BUFFER = (char *)malloc(sizeof(char)*(MAXBUFF*2));
			if(!BUFFER){perror("Errore malloc()"), exit(EXIT_FAILURE);} // controllo malloc

			bytesRead = safeRead(fd[0], BUFFER, MAXBUFF*2); // Legge dalla PIPE il numero elevato al quadrato

			safeWrite(STDOUT_FILENO, "Sono il figlio. Il quadrato del numero inserito è: ", 52);
			safeWrite(STDOUT_FILENO, BUFFER, bytesRead);
			safeWrite(STDOUT_FILENO, "\n", 1);

			Equal_To_Zero = strncmp(BUFFER, "0", 1); // 0 se è uguale, 1 se non è uguale
			
			safe_kill(ppid, SIGUSR1); // INVIO SEGNALE SIGUSR1 AL PADRE (Sblocca padre)
			
		}while(Equal_To_Zero); // Continua finché il numero letto non è uguale a 0

		safeClose(fd[0]); // Chiusura lato lettura PIPE
	}	

	exit(EXIT_SUCCESS);
}


/* ---------- SIGNAL HANDLER -------------*/
void unlock(int sig) // Usato dal padre
{
	if(sig == SIGUSR1)
	{
		block = 0; // Sblocca padre
	}
}
/* ---------------------------------------*/


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

void safe_kill(pid_t pid, int sig)
{
	if(kill(pid, sig) < 0)
		perror("Errore kill()"), exit(EXIT_FAILURE);
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

void safe_signal(int signum, sighandler_t handler)
{
	if(signal(signum, handler) == SIG_ERR)
		perror("Errore signal()"), exit(EXIT_FAILURE);
}
/* -------------------------------------------------------- */

