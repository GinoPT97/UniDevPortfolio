#include <stdio.h>
#include <stdlib.h>

#include <errno.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#include <bits/signum-arch.h>
#include <err.h>
#include <signal.h>

#include <pthread.h>
#include <string.h>

#define BUFFSIZE 16 // dimensione a caso per il buffer

// more appropriate naming
struct args_for_thread {
  int fd;
  off_t to;
  off_t from;
  char char_input;
};

void* conta(void* param){
    struct args_for_thread* arg = (struct args_for_thread*) param;
    off_t num_byte_readed;
    off_t num_byte_already_readed = 0;
    off_t num_byte_to_read = arg->to - arg->from + 1;

    if(lseek(arg->fd, arg->from, SEEK_SET) == -1)
        err(EXIT_FAILURE, "seek error\n");

    unsigned int* count = malloc(sizeof(unsigned int));
    if(!count)
        err(EXIT_FAILURE, "malloc error\n");
    *count = 0;

    char buffer[BUFFSIZE];

    while(num_byte_already_readed < num_byte_to_read){
        off_t amount_to_read = num_byte_already_readed + BUFFSIZE > num_byte_to_read ? num_byte_to_read - num_byte_already_readed : BUFFSIZE;
        if((num_byte_readed = read(arg->fd, buffer, amount_to_read)) == -1)
            err(EXIT_FAILURE, "read conta error");
        for(off_t i=0; i<num_byte_readed; i++)
            *count = buffer[i] == arg->char_input ? *count + 1 : *count;
        num_byte_already_readed += num_byte_readed;
    }
    // non dealloco il puntatore count altrimenti il main non avrebbe niente da leggere

    pthread_exit((void*) count);
}

int main(int argc, char *argv[]) {

  if (argc != 3)
    printf("Usage: %s <file input> <character>\n", argv[0]), exit(EXIT_FAILURE);

  struct stat info;
  if (lstat(argv[1], &info) == -1)
    err(EXIT_FAILURE, "lstat error\n");

  if(!S_ISREG(info.st_mode))
      printf("%s is not regular\n", argv[1]), exit(EXIT_FAILURE);

  off_t dim = info.st_size - 1;
  off_t half_dim = dim / 2;

    int fd1 = open(argv[1], O_RDONLY, S_IRWXU ^ S_IXUSR);
    if(fd1 == -1){
        printf("open error in thread %u\n", pthread_self());
        exit(EXIT_FAILURE);
    }

    int fd2 = open(argv[1], O_RDONLY, S_IRWXU ^ S_IXUSR);
    if(fd2 == -1){
        printf("open error in thread %u\n", pthread_self());
        exit(EXIT_FAILURE);
    }

  struct args_for_thread arg1, arg2;

  arg1.fd = fd1;
  arg1.from = 0;
  arg1.to = half_dim;
  arg1.char_input = argv[2][0];

  arg2.fd = fd2;
  arg2.from = half_dim+1;
  arg2.to = dim;
  arg2.char_input = argv[2][0];

  pthread_t tid1, tid2;
  int error;

  if((error = pthread_create(&tid1, NULL, conta, (void*)&arg1)))
      printf("%s\n", strerror(error)), exit(EXIT_FAILURE);

  if((error = pthread_create(&tid2, NULL, conta, (void*)&arg2)))
      printf("%s\n", strerror(error)), exit(EXIT_FAILURE);

  unsigned int *result1, *result2;

  if((error = pthread_join(tid1, (void**)&result1)))
     printf("%s\n", strerror(error)), exit(EXIT_FAILURE);

  if((error = pthread_join(tid2, (void**)&result2)))
     printf("%s\n", strerror(error)), exit(EXIT_FAILURE);

  unsigned int total = *result1 + *result2;
  printf("In file \'%s\', \'%c\' appear %u time\n", argv[1], argv[2][0], total);

  close(fd1);
  close(fd2);
  free(result1);
  free(result2);
  return 0;
}
