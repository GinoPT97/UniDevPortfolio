/*
- realizzare un programma che accetta da riga di comando
due numeri interi n ed m, e crea n produttori ed m
consumatori
- produttori e consumatori condividono un array di 100 interi
- ogni produttore aspetta un numero casuale di secondi tra
1 e 10, e poi produce (cioe' inserisce nell'array) da 1 a 5
numeri casuali. Se il produttore trova l'array pieno, salta il
turno
- ogni consumatore aspetta che ci sia un numero da
consumare, e poi stampa a video il proprio tid e il valore
consumato
*/
#include <stdio.h>   //fprintf()
#include <stdlib.h>  //error macros
#include <string.h>  //strtoul(), strerror()
#include <pthread.h> //thread stuff
#include <unistd.h>  //sleep()

#define BUFFERSIZE 100

struct args
{
    int array[BUFFERSIZE];     //array
    unsigned long in;          //indice scrittura
    unsigned long out;         //indice lettura
    unsigned long count;       //numero effettivo elementi
    pthread_mutex_t mutex;     //mutex
    pthread_cond_t cond_full;  //condition variable produttore (array saturo)
    pthread_cond_t cond_empty; //condition variable consumatore (array vuoto)
};

int isUlong(const char *string); //controlla se il contenuto della stringa è un ulong
int isDigit(const char c);       //controlla se il carattere è numerico

struct args *allocArgs(); //restituisce una struttura args allocata dinamicamente

void *produce(void *arg); //job dei thread produttori
void *consume(void *arg); //job dei thread consumatori

int main(int argc, char *argv[])
{
    unsigned long prod_n, cons_n;     //numero produttori e consumatori
    pthread_t *producers, *consumers; //array di produttori e consumatori
    int err;                          //codice eventuale errore
    struct args *arg;                 //argomento threads

    if (!argv[1])
        fprintf(stderr, "Productors missing\n"), exit(EXIT_FAILURE);
    if (!argv[2])
        fprintf(stderr, "Consumers missing\n"), exit(EXIT_FAILURE);
    if ((!isUlong(argv[1])) || (*argv[1] == '0') || (!isUlong(argv[2])) || (*argv[2] == '0'))
        fprintf(stderr, "Only positive numbers pls\n"), exit(EXIT_FAILURE);

    prod_n = strtoul(argv[1], NULL, 10); //Converte in ulong la stringa contenente i produttori
    cons_n = strtoul(argv[2], NULL, 10); //Converte in ulong la stringa contenente i consumatori

    if ((producers = (pthread_t *)malloc(prod_n * sizeof(pthread_t))) == NULL) //Alloca spazio per i produttori
        perror("malloc err"), exit(EXIT_FAILURE);
    if ((consumers = (pthread_t *)malloc(cons_n * sizeof(pthread_t))) == NULL) //Alloca spazio per i consumatori
        perror("malloc err"), exit(EXIT_FAILURE);

    arg = allocArgs(); //Alloca l' argomento dei thread

    srand(time(NULL)); //inizializza il seed, servirà per generare numeri casuali

    for (unsigned long i = 0; i < prod_n; i++) //Genera i thread produttori
    {
        if ((err = pthread_create(&producers[i], NULL, produce, (void *)arg) != 0))
            fprintf(stderr, "Couldn't create producer thread %ld! %s\n", i, strerror(err)), exit(EXIT_FAILURE);
    }

    for (unsigned long i = 0; i < cons_n; i++) //Genera i thread consumatori
    {
        if ((err = pthread_create(&consumers[i], NULL, consume, (void *)arg) != 0))
            fprintf(stderr, "Couldn't create consumer thread %ld! %s\n", i, strerror(err)), exit(EXIT_FAILURE);
    }

    for (unsigned long i = 0; i < prod_n; i++) //Aspetta la terminazione dei thread produttori
    {
        if ((err = pthread_join(producers[i], NULL) != 0))
            fprintf(stderr, "thread_join err: %s", strerror(err));
    }
    for (unsigned long i = 0; i < cons_n; i++) //Aspetta la terminazione dei thread consumatori
    {
        if ((err = pthread_join(consumers[i], NULL) != 0))
            fprintf(stderr, "thread_join err: %s", strerror(err));
    }

    if ((err = pthread_mutex_destroy(&arg->mutex)) != 0) //Rilascia risorse mutex
        fprintf(stderr, "mutex_destroy err: %s", strerror(err));
    if ((err = pthread_cond_destroy(&arg->cond_full)) != 0) //Rilascia risorse condition variable produttore
        fprintf(stderr, "cond_destroy err: %s", strerror(err));
    if ((err = pthread_cond_destroy(&arg->cond_empty)) != 0) //Rilascia risorse condition variable consumatore
        fprintf(stderr, "cond_destroy err: %s", strerror(err));
    free(producers); //Distrugge array di id produttori
    free(consumers); //Distrugge array di id consumatori
    free(arg);       //Distrugge argomento threads

    return 0;
}

struct args *allocArgs()
{
    struct args *s;
    int err;

    if ((s = (struct args *)malloc(sizeof(struct args))) == NULL)
        fprintf(stderr, "malloc err"), exit(EXIT_FAILURE);

    //Inizializza i valori numerici a 0
    s->in = 0;
    s->out = 0;
    s->count = 0;

    if ((err = pthread_mutex_init(&s->mutex, NULL)) != 0) //Inizializza risorse mutex
    {
        free(s);
        fprintf(stderr, "mutex_init err: %s", strerror(err)), exit(EXIT_FAILURE);
    }

    if ((err = pthread_cond_init(&s->cond_full, NULL)) != 0) //Inizializza risorse condition variable producer
    {
        free(s);
        fprintf(stderr, "cond_init prod err: %s", strerror(err)), exit(EXIT_FAILURE);
    }

    if ((err = pthread_cond_init(&s->cond_empty, NULL)) != 0) //Inizializza risorse condition variable consumer
    {
        free(s);
        fprintf(stderr, "cond_init cons err: %s", strerror(err)), exit(EXIT_FAILURE);
    }

    return s;
}

void *produce(void *arg)
{
    struct args *arg_ = (struct args *)arg;

    for (;;) //Cicla indefinitamente (Ctrl-C per terminare)
    {
        sleep(rand() % 10); //Attende da 0 a 10 secondi

        pthread_mutex_lock(&arg_->mutex); //Acquisisce il lock

        while (arg_->count == BUFFERSIZE) //Se l' array è saturo manda il thread in wait queue
            pthread_cond_wait(&arg_->cond_full, &arg_->mutex);

        for (int i = 0; i < rand() % 5 && arg_->count != BUFFERSIZE; i++) //Inserisco da 1 a 5 interi
        {
            arg_->array[arg_->in] = rand() % 100;
            printf("Produttore: [%ld]; Valore inserito: arr[%ld] = %d\n", pthread_self(), arg_->in, arg_->array[arg_->in]);
            arg_->in = (arg_->in + 1) % BUFFERSIZE; //Sposta di uno l' indice di inserimento
            arg_->count++;                          //Incrementa di uno il numero di elementi
        }

        pthread_mutex_unlock(&arg_->mutex); //Sblocca il mutex

        pthread_cond_signal(&arg_->cond_empty); //Sblocca eventuale thread consumatore in attesa

    }

    return (void *)0;
}

void *consume(void *arg)
{
    struct args *arg_ = (struct args *)arg;
    struct timespec t2;
    t2.tv_sec = 0;
    t2.tv_nsec = 99966699L;

    for (;;) //Cicla indefinitamente (Ctrl-C per terminare)
    {
        nanosleep(&t2, NULL); //Dovrebbe evitare che lo stesso thread faccia il lock immediatamente dopo l' unlock (opzionale)

        pthread_mutex_lock(&arg_->mutex); //Acquisisce il lock

        while (arg_->count == 0) //Se non ci sono elementi da consumare manda il thread in wait queue
            pthread_cond_wait(&arg_->cond_empty, &arg_->mutex);

        printf("Consumatore: [%ld]; Valore letto: arr[%ld] = %d\n", pthread_self(), arg_->out, arg_->array[arg_->out]);
        arg_->out = (arg_->out + 1) % BUFFERSIZE; //Sposta di uno l' indice di lettura
        arg_->count--;                            //Decrementa di uno il numero di elementi

        pthread_mutex_unlock(&arg_->mutex); //Sblocca il mutex

        pthread_cond_signal(&arg_->cond_full); //Sblocca eventuale thread produttore in attesa
    }

    return (void *)0;
}

int isDigit(const char c)
{
    return (c < 48 || c > 57) ? 0 : 1;
}

int isUlong(const char *string)
{
    for (unsigned long i = 0; string[i] != '\0'; i++)
    {
        if (!isDigit(string[i]))
            return 0;
    }
    return 1;
}
