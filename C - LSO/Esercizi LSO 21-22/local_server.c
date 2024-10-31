#include <stdio.h>
#include <stdlib.h>

#include <errno.h>
#include <fcntl.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#include <bits/signum-arch.h>
#include <err.h>
#include <signal.h>

#include <pthread.h>

#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/un.h>

#include <time.h>

#define MY_SOCKET "/tmp/exercise_socket"
#define BACKLOG 4096
#define THREAD_POOL_SIZE 10

// DICHIARAZIONE QUEUE ///////////////////////////////////

typedef struct node {
  struct node *next;
  int *value;
} node;

typedef struct queue {
  node *head;
  node *tail;
  pthread_mutex_t queue_mutex;
} queue;

queue *init_queue();
void destroy_queue(queue *q);
node *makenode(int *value);
void enqueue(queue *q, int *value);
int *dequeue(queue *q);
///////////////////////////////////////////////////////////

pthread_t thread_pool[THREAD_POOL_SIZE];
pthread_cond_t cond;
queue *client_queue;
int continue_to_accept_new_connection = 1;
int sd;

void *handle_connection(void *arg);
void *check_for_shutdown(void *arg);
void handle_termination(int sig);

int main(int argc, char *argv[]) {
  int err_init;

  pthread_attr_t attr;
  if ((err_init = pthread_attr_init(&attr))) {
    printf("error thread attr init: %s\n", strerror(err_init));
    exit(EXIT_FAILURE);
  }
  if ((err_init =
           pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED))) {
    printf("error thread attr set cancel state async: %s\n",
           strerror(err_init));
    exit(EXIT_FAILURE);
  }

  if (signal(SIGUSR1, handle_termination) == SIG_ERR) {
    printf("error installing handler for server termination: %s\n",
           strerror(errno));
    exit(EXIT_FAILURE);
  }

  pthread_t thread_for_shutdown;
  if ((err_init = pthread_create(&thread_for_shutdown, &attr,
                                 check_for_shutdown, NULL))) {
    printf("error thread_for_shutdown init: %s\n", strerror(err_init));
    exit(EXIT_FAILURE);
  }

  client_queue = init_queue();

  if ((err_init = pthread_cond_init(&cond, NULL))) {
    printf("error condition variable init: %s\n", strerror(err_init));
    exit(EXIT_FAILURE);
  }

  for (int i = 0; i < THREAD_POOL_SIZE; i++) {
    if ((err_init =
             pthread_create(&thread_pool[i], &attr, handle_connection, NULL))) {
      printf("error thread init: %s\n", strerror(err_init));
      exit(EXIT_FAILURE);
    }
  }

  struct sockaddr_un addr;
  addr.sun_family = AF_LOCAL;
  strncpy(addr.sun_path, MY_SOCKET, sizeof(MY_SOCKET));

  sd = socket(PF_LOCAL, SOCK_STREAM, 0);

  if (sd == -1) {
    printf("Error creating socket: %s\n", strerror(errno));
    exit(EXIT_FAILURE);
  }

  unlink(MY_SOCKET);

  if (bind(sd, (struct sockaddr *)&addr, sizeof(addr)) == -1) {
    printf("Error binding on address: %s\n", strerror(errno));
    exit(EXIT_FAILURE);
  }

  if (listen(sd, BACKLOG) == -1) {
    printf("Error listenin on address: %s\n", strerror(errno));
    exit(EXIT_FAILURE);
  }

  int tmp_sd;
  int *arg;
  while (continue_to_accept_new_connection) {
    tmp_sd = accept(sd, NULL, NULL);
    if (tmp_sd == -1) {
      printf("failed to accept a new connection\n");
    } else {
      arg = (int *)malloc(sizeof(int));
      if (!arg) {
        printf("failed to process a new connection\n");
        // non credo sia necessario terminare il server
        continue;
      }
      printf("new connection\n");
      *arg = tmp_sd;
      pthread_mutex_lock(&client_queue->queue_mutex);
      enqueue(client_queue, arg);
      pthread_cond_signal(&cond);
      pthread_mutex_unlock(&client_queue->queue_mutex);
    }
  }

  destroy_queue(client_queue);
  int err_destroy;
  if ((err_destroy = pthread_attr_destroy(&attr))) {
   printf("error thread attr destroy: %s\n", strerror(err_destroy));
   exit(EXIT_FAILURE);
  }

  return 0;
}

void *handle_connection(void *arg) {
  char buffer[26];
  time_t actual_time;
  int fd;
  int *client_to_serve;
  while (1) { // don't let the thread die
    pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, NULL);
    pthread_mutex_lock(&client_queue->queue_mutex);
    if ((client_to_serve = dequeue(client_queue)) == NULL) {
      pthread_cond_wait(&cond, &client_queue->queue_mutex);
      client_to_serve = dequeue(client_queue);
    }
    pthread_mutex_unlock(&client_queue->queue_mutex);

    if (client_to_serve) {
      fd = *((int *)client_to_serve);
      free(client_to_serve); // dealloco la memoria del fd del client
      time(&actual_time);
      ctime_r(&actual_time, buffer);
      if (write(fd, buffer, strlen(buffer)) < 0) {
        printf("handle connection: write error\t(operation aborted)\n");
      }
      close(fd); // close client descriptor
    }
    pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
  }
  return NULL;
}

void *check_for_shutdown(void *arg) {
  char input;
  printf("Insert 'q' to shutdown the server.\n");
  while ((input = getchar()) != 'q') {
  }
  raise(SIGUSR1);
  return NULL;
}

void handle_termination(int sig) {
  if (sig == SIGUSR1) {
    printf("shutdown server, all remaining connection will be ignored\n");
    for (int i = 0; i < THREAD_POOL_SIZE; i++) {
        pthread_cancel(thread_pool[i]);
    }
    continue_to_accept_new_connection = 0;
    shutdown(sd, SHUT_RDWR);
    close(sd);
  }
}

//////////// IMPLEMENTAZIONE QUEUE/////////////////////////////

queue *init_queue() {
  queue *q = (queue *)malloc(sizeof(queue));
  if (!q) {
    printf("queue allocation error\n");
    exit(EXIT_FAILURE);
  }
  q->head = NULL;
  q->tail = NULL;
  int err_init;
  if ((err_init = pthread_mutex_init(&q->queue_mutex, NULL)) < 0) {
    printf("mutex init error: %s\n", strerror(err_init));
    exit(EXIT_FAILURE);
  }
  return q;
}

void destroy_queue(queue *q) {
  // assumo che q non sia NULL, che quindi e' inizializzta
  node *tmp;
  while (q->head) {
    tmp = q->head;
    q->head = q->head->next;
    free(tmp);
  }
  free(q);
  q = NULL;
}

node *makenode(int *value) {
  node *newnode = (node *)malloc(sizeof(node));
  if (!newnode) {
    printf("queue node allocation error\n");
    exit(EXIT_FAILURE);
  }
  newnode->next = NULL;
  newnode->value = value;
  return newnode;
}

void enqueue(queue *q, int *value) {
  // assumo che q non sia NULL, che quindi e' inizializzta
  node *newnode = makenode(value);
  if (q->tail) {
    q->tail->next = newnode;
    q->tail = newnode;
  } else {
    q->head = q->tail = newnode;
  }
}

int *dequeue(queue *q) {
  // assumo che q non sia NULL, che quindi e' inizializzta
  int *ret = NULL;
  if (q->head) {
    ret = q->head->value;
    node *tmp = q->head;
    q->head = q->head->next;
    if (!q->head)
      q->tail = NULL;
    free(tmp);
  }
  return ret;
}

///////////////////////////////////////////////////////////////
