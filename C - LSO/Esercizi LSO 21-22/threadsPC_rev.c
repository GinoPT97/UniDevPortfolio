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
#include <ctype.h>
#include <pthread.h>
#include <math.h>
#include <limits.h> // Limiti su costanti Integer e altro

#define DIM_BUF 101

// pthread_create() con controllo
void safe_pthreadCreate(pthread_t *tid, const pthread_attr_t *attr, void*(*start_funct)(void*), void *arg);
// pthread_join() con controllo
void safe_pthreadJoin(pthread_t tid, void **status);
// pthread_mutex_lock() con controllo
void safe_pthread_mutex_lock(pthread_mutex_t *mutex);
// pthread_mutex_unlock() con controllo
void safe_pthread_mutex_unlock(pthread_mutex_t *mutex);
// pthread_cond_wait() con controllo
void safe_pthread_cond_wait(pthread_cond_t *cond, pthread_mutex_t *mutex);
// pthread_cond_signal() con controllo
void safe_pthread_cond_signal(pthread_cond_t *cond);
// pthread_cond_broadcast() con controllo
void safe_pthread_cond_broadcast(pthread_cond_t *cond);
// pthread_cond_timedwait() con controllo
void safe_pthread_cond_timedwait(pthread_cond_t *cond, pthread_mutex_t *mutex, const struct timespec *abstime);

// data una stringa restituisce 1 se è un numero, 0 altrimenti
int isNumber(char *s);
// restituisce un numero intero random tra 0 e MAX
int randomNumber(int max);

/* VARIABILI GLOBALI E CONDIVISE */
int arrayCondiviso[DIM_BUF];
int inserisci = 0; // Indice per inserire nell'array condiviso per il produttore
int preleva = 0; // Indice per prelevare dall'array condiviso per il consumatore
int numeroProduttori;	// Indica il numero di produttori
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER; // Inizializzazione mutex
pthread_cond_t arrayNotEmpty = PTHREAD_COND_INITIALIZER; // Inizializzazione condition variable
/* ----------------------------- */

/* FUNZIONI DI AVVIO THREADS */
void *produci(); // Per un produttore
void *consuma(); // Per un consumatore
/* ------------------------- */

int main(int argc, char **argv)
{
	/* CONTROLLI PARAMETRI DI INPUT */
	if(argc!=3){fprintf(stderr, "Uso corretto: %s <num_produttori> <num_consumatori>\n", argv[0]), exit(EXIT_FAILURE);}
	if(!isNumber(argv[1]) || !isNumber(argv[2])){fprintf(stderr, "I parametri di input devono essere numeri interi positivi!\n"), exit(EXIT_FAILURE);}
	/* ---------------------------- */

	int numeroConsumatori; // Indica il numero di consumatori

	// Conversione stringa -> intero
	numeroProduttori = atoi(argv[1]);
	numeroConsumatori = atoi(argv[2]);

	srand(time(0));	// inizializzione del generatore sull'ora attuale dell'elaboratore (per la generazione di numeri random)

	pthread_t tid[numeroProduttori+numeroConsumatori]; // Array di Thread ID

	// CREAZIONE THREADS PRODUTTORI
	int I;

	for(I = 0; I < numeroProduttori; ++I)
	{
		safe_pthreadCreate(&tid[I], NULL, produci, NULL); 
	} 

	// CREAZIONE THREADS CONSUMATORI
	for(I = numeroProduttori; I < numeroProduttori+numeroConsumatori; ++I)
	{
		safe_pthreadCreate(&tid[I], NULL, consuma, NULL);
	}

	// ATTESA TERMINAZIONE DI TUTTI I THREADS
	for(I = 0; I < (numeroProduttori+numeroConsumatori)-1; I++)
	{
		safe_pthreadJoin(tid[I], NULL);
	}

	exit(EXIT_SUCCESS);
}


void *produci()
{
	int seconds; // Indica quanti secondi deve aspettare un produttore
	int numRandom_tot; // Indica la quantità di numeri random da generare
	int numero; // Contiene un numero random
	int *array; // Array di interi

	/* ASPETTA (1-10 secondi) */
	seconds = randomNumber(10)+1;
	printf("[PRODUTTORE] TID (0x%x) è stato appena creato, sta aspettando %d secondi.\n", (unsigned int)pthread_self(), seconds);
	sleep(seconds);
	/* ---------------------- */

	/* PRODUZIONE ELEMENTI */
	numRandom_tot = randomNumber(5)+1; // Generazione numero random (1-5) - indica la quantità di numeri random da generare e inserire nell'array condiviso

	printf("[PRODUTTORE] TID (0x%x) produrrà %d elementi.\n", (unsigned int)pthread_self(), numRandom_tot);

	array = (int*)malloc(sizeof(int)*numRandom_tot); // Allocazione memoria array di dimensione [numRandom_tot]
	if(!array){printf("Errore allocazione memoria\n"), exit(EXIT_FAILURE);} // controllo malloc()

	int I;

	for(I = 0; I < numRandom_tot; I++) // Riempimento array con numeri random
	{
		numero = randomNumber(100)+1; // numero random (1-100)
		array[I] = numero;
	}
	/* ------------------- */

	I = 0; // Indica l'I-esimo numero da inserire nell'array condiviso ( fino a [numRandom_tot] )

	/* --- !AREA CRITICA! --- */
	safe_pthread_mutex_lock(&mutex); // lock mutex 
	
	while(numRandom_tot && !(inserisci+1%DIM_BUF==preleva)) // Continua finché ci sono numeri da inserire e l'array non è pieno
	{
		arrayCondiviso[inserisci] = array[I]; // inserimento di un elemento nell'array condiviso
		printf("[PRODUTTORE] TID (0x%x) ha appena prodotto e inserito nell'array condiviso il numero %d.\n", (unsigned int)pthread_self(), arrayCondiviso[inserisci]);

		I++; // incremento indice I-esimo numero da inserire nell'array condiviso
		numRandom_tot--; // decremento variabile che indica la quantità di numeri da inserire nell'array condiviso
		inserisci = (inserisci+1)%DIM_BUF; // aggiornamento indice inserisci
	}

	if(inserisci+1%DIM_BUF==preleva) 
		printf("[PRODUTTORE] TID (0x%x): l'array è pieno, terminazione.\n", (unsigned int)pthread_self());

	numeroProduttori--; // Decremento numero di produttori -> terminazione
	safe_pthread_cond_broadcast(&arrayNotEmpty); // Risveglia tutti i consumatori in attesa
	
	free(array);
	safe_pthread_mutex_unlock(&mutex); // unlock mutex
	/* --- !FINE AREA CRITICA! --- */

	pthread_exit(NULL);
}


void *consuma()
{
	/* --- !AREA CRITICA! --- */
	safe_pthread_mutex_lock(&mutex); // lock mutex
	
	while(inserisci == preleva && numeroProduttori) // Continua finché l'array è vuoto ed esistono ancora produttori
	{
		safe_pthread_cond_wait(&arrayNotEmpty, &mutex); // rimane in attesa e libera il mutex
	}
	
	if(!(inserisci==preleva)) // Se ci sono elementi
	{
		printf("[CONSUMATORE] TID (0x%x) ha appena consumato il numero %d.\n", (unsigned int)pthread_self(), arrayCondiviso[preleva]);
		preleva = (preleva+1)%DIM_BUF; // aggiornamento indice preleva
	}
	else // altrimenti
	{
		printf("[CONSUMATORE] TID (0x%x): l'array è vuoto e non ci sono più produttori, terminazione.\n", (unsigned int)pthread_self());
	}
	
	safe_pthread_mutex_unlock(&mutex); // unlock mutex
	/*--- !FINE AREA CRITICA! --- */

	pthread_exit(NULL);
}

void safe_pthreadCreate(pthread_t *tid, const pthread_attr_t *attr, void*(*start_funct)(void*), void *arg)
{
	int err;
	err = pthread_create(tid, attr, start_funct, arg);
	if(err != 0)
		fprintf(stderr, "%s", strerror(err)),
		exit(EXIT_FAILURE);
}

void safe_pthreadJoin(pthread_t tid, void **status)
{
	int err;
	err = pthread_join(tid, status);
	if(err != 0)
		fprintf(stderr, "%s", strerror(err)),
		exit(EXIT_FAILURE);
}

void safe_pthread_mutex_lock(pthread_mutex_t *mutex)
{
	int err;
	if((err=pthread_mutex_lock(mutex)) != 0)
		fprintf(stderr, "%s", strerror(err)),
		exit(EXIT_FAILURE);
}

void safe_pthread_mutex_unlock(pthread_mutex_t *mutex)
{
	int err;
	if((err=pthread_mutex_unlock(mutex)) != 0)
		fprintf(stderr, "%s", strerror(err)),
		exit(EXIT_FAILURE);
}

void safe_pthread_cond_wait(pthread_cond_t *cond, pthread_mutex_t *mutex)
{
	int err;
	if((err=pthread_cond_wait(cond, mutex)) != 0)
		fprintf(stderr, "%s", strerror(err)),
		exit(EXIT_FAILURE);
}

void safe_pthread_cond_signal(pthread_cond_t *cond)
{
	int err;
	if((err=pthread_cond_signal(cond)) != 0)
		fprintf(stderr, "%s", strerror(err)),
		exit(EXIT_FAILURE);
}

void safe_pthread_cond_broadcast(pthread_cond_t *cond)
{
	int err;
	if((err=pthread_cond_broadcast(cond)) != 0)
		fprintf(stderr, "%s", strerror(err)),
		exit(EXIT_FAILURE);
}

void safe_pthread_cond_timedwait(pthread_cond_t *cond, pthread_mutex_t *mutex, const struct timespec *abstime)
{
	int err;
	if((err=pthread_cond_timedwait(cond, mutex, abstime)) != 0)
		fprintf(stderr, "%s", strerror(err)),
		exit(EXIT_FAILURE);
	
}


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
	return rand()%(max); // da 0 a max
}