#include <stdio.h>
#include <stdlib.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>
#include <sys/wait.h>

#include <bits/signum-arch.h>
#include <signal.h>
#include <err.h>

#include <pthread.h>
#include <string.h>

struct args{
    char* string;
    char character;
    // sarebbe meglio usare unsigned
    int from;
    int to;
};

void* conta(void* arg){
    struct args* arg_for_thread = (struct args *) arg;
    int* count = malloc(sizeof(int));
    if(!count)
        printf("error in malloc\n"), exit(EXIT_FAILURE);
    *count = 0;
    for(int i=arg_for_thread->from; i<arg_for_thread->to; i++)
        *count = arg_for_thread->string[i] == arg_for_thread->character ? *count + 1 : *count;

    pthread_exit((void*) count);
}

int main(int argc, char* argv[]){

    if(argc != 5)
        printf("Usage: %s <string or path> <character> <from> <to>\n", argv[0]), exit(EXIT_FAILURE);

    // mancano controlli sui parametri in input

    struct args arg_for_thread;
    arg_for_thread.string = argv[1];
    arg_for_thread.character = argv[2][0];
    arg_for_thread.from = atoi(argv[3]);
    arg_for_thread.to = atoi(argv[4]);

    int error;
    pthread_t tid;

    if((error = pthread_create(&tid, NULL, conta, (void*)&arg_for_thread)))
        printf("%s\n", strerror(error)), exit(EXIT_FAILURE);

    int* ptr;

    if((error = pthread_join(tid, (void**)&ptr)))
        printf("%s\n", strerror(error)), exit(EXIT_FAILURE);

    printf("In \'%s\', da %d a %d, \'%c\' compare %d volte\n", argv[1],
           arg_for_thread.from, arg_for_thread.to, argv[2][0], *ptr);
    return 0;
}
