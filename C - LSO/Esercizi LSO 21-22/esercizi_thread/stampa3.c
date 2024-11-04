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

void* print(void* arg){
   int n = *(int*) arg;
   for(int i=0; i<n; i++)
       // sulla mia macchina pthread_t e' implementato come unsigned int
       // o almeno cosi' dice il mio compilatore
       printf("tid := %u\tnum := %d\n", pthread_self(), i);
   pthread_exit(NULL);
}

int main(int argc, char* argv[]){
    pthread_t tid1, tid2;
    int error;

    if(argc != 2)
        printf("Usage: %s <number>\n", argv[0]) , exit(EXIT_FAILURE);

    int n = atoi(argv[1]);

    if((error = pthread_create(&tid1, NULL, print, (void*) &n)))
        printf("%s\n", strerror(error)), exit(EXIT_FAILURE);

    if((error = pthread_create(&tid2, NULL, print, (void*) &n)))
        printf("%s\n", strerror(error)), exit(EXIT_FAILURE);

    if((error = pthread_join(tid1, NULL)))
        printf("%s\n", strerror(error)), exit(EXIT_FAILURE);

    if((error = pthread_join(tid2, NULL)))
        printf("%s\n", strerror(error)), exit(EXIT_FAILURE);

    return 0;
}
