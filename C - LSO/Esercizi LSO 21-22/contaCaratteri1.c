/*Scrivere un programma C contaCaratteri.c che prende in ingresso un
pathname "nomefile", un carattere "c" e due interi from, to e conta quante
volte compare il carattere "c" nel file "nomefile" nella porzione indicata dai
due interi: a partire da from byte fino a to
Usare una thread con funzione void * conta(void *) per contare i caratteri*/

#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#include <string.h>

#define BUFSIZE 15

struct arguments
{
    int fd;             //file descriptor
    size_t to, from;    //punto partenza, punto fine lettura
    size_t occurrences; //occorrenze del carattere c (return value)
    char c;             //carattere di cui trovare occorrenze
};
size_t countOccurrences(char c, char *s); //restituisce le occorrenze del carattere c nella stringa s
void *conta(void *arg);                   //funzione di avvio del thread conta

int main(int argc, char *argv[])
{
    pthread_t tid;          //identificativo thread
    struct stat st;         //struttura stat per ricavare la size del file
    int thread_err;         //eventuale codice di errore restituito dalla pthread_create
    struct arguments *args; //Puntatore alla struttura contentente argomenti del thread
    int fd;                 //file descriptor

    if (!argv[1])
        fprintf(stderr, "Pathname missing\n"), exit(EXIT_FAILURE);
    if (!argv[2])
        fprintf(stderr, "Character missing\n"), exit(EXIT_FAILURE);
    if (!argv[3])
        fprintf(stderr, "From integer missing\n"), exit(EXIT_FAILURE);
    if (!argv[4])
        fprintf(stderr, "To integer missing\n"), exit(EXIT_FAILURE);
    if (stat(argv[1], &st) < 0)
        perror("stat error"), exit(EXIT_FAILURE);
    if ((fd = open(argv[1], O_RDONLY)) < 0) //Apro il file
        perror("open error"), exit(EXIT_FAILURE);

    if ((args = (struct arguments *)malloc(sizeof(struct arguments))) == NULL)
        perror("Malloc error"), exit(EXIT_FAILURE);
    args->fd = fd;
    args->c = *argv[2];
    args->from = strtol(argv[3], NULL, 10);
    args->to = strtol(argv[4], NULL, 10);

    if ((long int)args->to >= st.st_size)
        fprintf(stderr, "To has to be < than file size\n"), exit(EXIT_FAILURE);
    if (args->to < args->from)
        fprintf(stderr, "From has to be <= than To\n"), exit(EXIT_FAILURE);

    //Genera il thread conta
    if ((thread_err = pthread_create(&tid, NULL, conta, (void *)args) != 0))
        fprintf(stderr, "Couldnt create thread! %s\n", strerror(thread_err)), exit(EXIT_FAILURE);

    //Attendo che il thread termini
    if((thread_err = pthread_join(tid, NULL))!=0)
        fprintf(stderr, "Join error! %s\n", strerror(thread_err)), exit(EXIT_FAILURE);

    //Stampo il numero di occorrenze
    printf("Number of occurrences of character '%c' : %ld\n", *argv[2], args->occurrences);
    //Rilascio la memoria dell' argomento
    free(args);
    return 0;
}

void *conta(void *arg)
{
    char buffer[BUFSIZE];
    ssize_t r;                                          //valori letti(possibilmente)
    struct arguments *args = ((struct arguments *)arg); //Struttura argomento passata come parametro

    size_t to_read = (args->to - args->from) + 1; //valori da leggere

    if (lseek(args->fd, args->from, SEEK_SET) < 0) //lettura inizia da 'from'
        perror("seek error"), exit(EXIT_FAILURE);
    do
    { //Buffered read
        if (to_read < BUFSIZE)
        {
            r = to_read;
            buffer[r] = '\0';
        }
        else
            r = BUFSIZE;

        if (read(args->fd, buffer, r) < r)
            perror("Read err"), exit(EXIT_FAILURE);

        //Aggiorno le occorrenze di c
        args->occurrences += countOccurrences(args->c, buffer);
        to_read -= r;
    } while (to_read > 0);

    close(args->fd);
    return ((void *)0);
}

size_t countOccurrences(char c, char *string)
{
    size_t ret = 0;

    if (!string)
        fprintf(stderr, "String missing"), exit(EXIT_FAILURE);

    for (size_t i = 0; i < strlen(string); i++)
    {
        if (string[i] == c)
            ret++;
    }
    return ret;
}