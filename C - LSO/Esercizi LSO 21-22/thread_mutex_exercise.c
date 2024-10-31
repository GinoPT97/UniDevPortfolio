#include <stdio.h>
#include <stdlib.h>

#include <errno.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include <sys/wait.h>

#include <bits/signum-arch.h>
#include <err.h>
#include <signal.h>

#include <pthread.h>
#include <string.h>
#include <time.h>

#include <ctype.h>

#define ARRAY_SIZE 100

typedef struct thread_arg {
  int num_item;
  int cur_pos_read;
  int cur_pos_write;
  int array[ARRAY_SIZE];
  pthread_mutex_t arg_mutex;
  int producers_terminated;
} * thread_arg;

void *produce_job(void *param);
void *consume_job(void *param);

int check_input(char *s);

int main(int argc, char *argv[]) {
  if (argc != 3) {
    printf("Usage: %s n m\n", argv[0]);
    exit(EXIT_FAILURE);
  }
  if (check_input(argv[1])) {
    printf("%s is not an integer\n", argv[1]);
    exit(EXIT_FAILURE);
  }
  if (check_input(argv[2])) {
    printf("%s is not an integer\n", argv[2]);
    exit(EXIT_FAILURE);
  }
  int n = atoi(argv[1]);
  int m = atoi(argv[2]);

  if (!(n > 0 && m > 0)) {
    printf("hey you need at least one producer and one consumer!\n");
    exit(EXIT_FAILURE);
  }

  struct thread_arg arg = {0};
  int err_init_mutex;
  if ((err_init_mutex = pthread_mutex_init(&arg.arg_mutex, NULL))) {
    printf("Error init struct arg mutex: %s\n", strerror(err_init_mutex));
    exit(EXIT_FAILURE);
  }

  pthread_t *producers = (pthread_t *)calloc(n, sizeof(pthread_t));
  pthread_t *consumers = (pthread_t *)calloc(m, sizeof(pthread_t));

  int err_start_thread;
  for (int i = 0; i < n; i++) {
    if ((err_start_thread =
             pthread_create(&producers[i], NULL, produce_job, (void *)&arg))) {
      printf("Error starting producer thread [%d]: %s\n", i,
             strerror(err_start_thread));
      exit(EXIT_FAILURE);
    }
  }
  for (int i = 0; i < m; i++) {
    if ((err_start_thread =
             pthread_create(&consumers[i], NULL, consume_job, (void *)&arg))) {
      printf("Error starting consumer thread [%d]: %s\n", i,
             strerror(err_start_thread));
      exit(EXIT_FAILURE);
    }
  }

  int err_join_thread;
  for (int i = 0; i < n; i++) {
    if ((err_join_thread = pthread_join(producers[i], NULL))) {
      printf("Error joining producer thread [%d]: %s\n", i,
             strerror(err_join_thread));
      exit(EXIT_FAILURE);
    }
  }
  pthread_mutex_lock(&arg.arg_mutex);
  printf("producers terminated\n");
  arg.producers_terminated = 1;
  pthread_mutex_unlock(&arg.arg_mutex);

  for (int i = 0; i < m; i++) {
    if ((err_join_thread = pthread_join(consumers[i], NULL))) {
      printf("Error joining consumer thread [%d]: %s\n", i,
             strerror(err_join_thread));
      exit(EXIT_FAILURE);
    }
  }

  int err_destroy;
  if ((err_destroy = pthread_mutex_destroy(&arg.arg_mutex))) {
    printf("Error destroying struct arg mutex: %s\n", strerror(err_destroy));
    exit(EXIT_FAILURE);
  }

  free(producers);
  free(consumers);

  return 0;
}

void *produce_job(void *param) {
  thread_arg arg = (thread_arg)param;
  srand(time(NULL));
  sleep(rand() % 10 + 1);
  int rand_iter = rand() % 5;
  printf("tid: %lu\twill produce (at most) %d value\n", pthread_self(),
         rand_iter + 1);
  int i = 0;
  while (i <= rand_iter) {
    if (pthread_mutex_trylock(&arg->arg_mutex) == 0) {
      if (arg->num_item != ARRAY_SIZE) {
        arg->array[arg->cur_pos_write] = rand();
        arg->cur_pos_write = (arg->cur_pos_write + 1) % ARRAY_SIZE;
        arg->num_item++;
        i++;
      }
      pthread_mutex_unlock(&arg->arg_mutex);
    } else { /* EBUSY*/
    }
  }
  return NULL;
}

void *consume_job(void *param) {
  thread_arg arg = (thread_arg)param;
  while (1) {
    if (pthread_mutex_trylock(&arg->arg_mutex) == 0) {
      if (arg->num_item != 0) {
        printf("tid: %lu\tconsumed:%d\n", pthread_self(),
               arg->array[arg->cur_pos_read]);
        arg->cur_pos_read = (arg->cur_pos_read + 1) % ARRAY_SIZE;
        arg->num_item--;
      }
      if (arg->num_item == 0 && arg->producers_terminated) {
        pthread_mutex_unlock(&arg->arg_mutex);
        break;
      }
      pthread_mutex_unlock(&arg->arg_mutex);
    } else {
      // wait for producer to do some job
      // sleep(1); // just to test different behaviour
    }
  }
  return NULL;
}

int check_input(char *s) {
  for (int i = 0; s[i] != '\0'; i++)
    if (!isdigit(s[i]))
      return 1;
  return 0;
}
