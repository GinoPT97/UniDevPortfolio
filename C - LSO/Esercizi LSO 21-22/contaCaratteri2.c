/*2. Scrivere un programma C contaCaratteri2.c che crea 2 thread il primo
thread cerca il carattere nella prima parte del file, il secondo thread
nella seconda parte
Utilizzare la funzione void * conta(void *)sviluppata per svolgere
l’esercizio precedente*/

#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#include <string.h>

#define BUFSIZE 15
#define NTHREADS 2

size_t countOccurrences(char c, char *s); //restituisce le occorrenze del carattere c della stringa s
void *conta(void *arg);                   //funzione di avvio del thread conta

struct arguments
{
    int fd;             //file descriptor
    size_t to, from;    //punto partenza, punto fine lettura
    size_t occurrences; //occorrenze del carattere c (return value)
    char c;             //carattere di cui trovare occorrenze
};

int main(int argc, char *argv[])
{
    pthread_t tid1;         //id thread1
    pthread_t tid2;         //id thread2
    struct stat st;         //struttura stat per ricavare la size del file
    int thread_err;         //eventuale codice di errore restituito dalla pthread_create
    struct arguments *args; //Puntatore alla struttura contentente argomenti dei threads
    int fd1, fd2;           //File descriptors

    if (!argv[1])
        fprintf(stderr, "Pathname missing\n"), exit(EXIT_FAILURE);
    if (!argv[2])
        fprintf(stderr, "Character missing\n"), exit(EXIT_FAILURE);
    if (stat(argv[1], &st) < 0)
        perror("stat error"), exit(EXIT_FAILURE);
    if (st.st_size == 0)
        exit(EXIT_SUCCESS);

    //Apro due volte il file
    if ((fd1 = open(argv[1], O_RDONLY)) < 0)
        perror("Open error"), exit(EXIT_FAILURE);
    if ((fd2 = open(argv[1], O_RDONLY)) < 0)
        perror("Open error"), exit(EXIT_FAILURE);

    //Alloca spazio per 2 argomenti
    if ((args = (struct arguments *)malloc(NTHREADS * sizeof(struct arguments))) == NULL)
        perror("Malloc error"), exit(EXIT_FAILURE);

    //Argomenti funzione avvio primo thread
    args[0].fd = fd1;
    args[0].c = *argv[2];
    //Il primo thread conta le occorrenze di c nella prima metà del file [0,(n/2)-1]
    args[0].from = 0;
    args[0].to = st.st_size / 2 - 1;
    args[0].occurrences = 0;

    //Argomenti funzione avvio secondo thread
    args[1].fd = fd2;
    args[1].c = *argv[2];
    //Il secondo thread conta le occorrenze di c nella seconda metà del file [n/2,n-1]
    args[1].from = st.st_size / 2;
    args[1].to = st.st_size - 1;
    args[1].occurrences = 0;

    //Primo thread
    if ((thread_err = pthread_create(&tid1, NULL, conta, (void *)&args[0]) != 0))
        fprintf(stderr, "Couldnt create thread1! %s\n", strerror(thread_err)), exit(EXIT_FAILURE);

    //Secondo thread
    if ((thread_err = pthread_create(&tid2, NULL, conta, (void *)&args[1]) != 0))
        fprintf(stderr, "Couldnt create thread2! %s\n", strerror(thread_err)), exit(EXIT_FAILURE);

    //Attendo i due thread
    if((thread_err = pthread_join(tid1, NULL))!=0)
        fprintf(stderr, "Join error! %s\n", strerror(thread_err)), exit(EXIT_FAILURE);
    if((thread_err = pthread_join(tid2, NULL))!=0)
        fprintf(stderr, "Join error! %s\n", strerror(thread_err)), exit(EXIT_FAILURE);

    //Stampo le occorrenze
    printf("Number of occurrences of character '%c' : %ld\n", *argv[2], args[0].occurrences + args[1].occurrences);
    //Rilascio la memoria degli argomenti
    free(args);
    return 0;
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

void *conta(void *arg)
{
    char buffer[BUFSIZE];
    ssize_t r; //valori letti(possibilmente)
    struct arguments *args = ((struct arguments *)arg);
    size_t to_read = (args->to - args->from) + 1; //valori da leggere

    //lettura inizia da 'from'
    if (lseek(args->fd, args->from, SEEK_SET) < 0)
        fprintf(stderr,"seek error"), exit(EXIT_FAILURE);
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
            fprintf(stderr,"Read err"), exit(EXIT_FAILURE);

        //Aggiorno le occorrenze di c
        args->occurrences += countOccurrences(args->c, buffer);
        to_read -= r;
    } while (to_read > 0);

    close(args->fd);
    return ((void *)0);
}
