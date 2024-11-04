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

#include <time.h>

int global_var = 0;

void* start_function(){
    srand(time(NULL));
    int sec = rand()%10 + 1;
    sleep(sec);
    global_var++;
    printf("global var: %d\n", global_var);
    pthread_exit(NULL);
}

int main(int argc, char* argv[]){
    if(argc != 2){
        printf("Usage: %s <number of thread>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    int num_of_thread = atoi(argv[1]);

    pthread_t* thread_pool = (pthread_t*) calloc(num_of_thread, sizeof(pthread_t));
    if(!thread_pool){
        printf("Cannot allocate the thread pool\n");
        exit(EXIT_FAILURE);
    }

   for(int i=0; i<num_of_thread; i++){
       int err_ret;
       if((err_ret = pthread_create(&thread_pool[i], NULL, start_function, NULL))){
           printf("%s\n",strerror(err_ret));
           exit(EXIT_FAILURE);
       }
   }

   for(int i=0; i<num_of_thread; i++){
      pthread_join(thread_pool[i], NULL);
   }

   printf("Expected := %d\tGlobal var := %d\tConcurrent access := %d\n", num_of_thread, global_var, num_of_thread - global_var);

   return 0;
}
