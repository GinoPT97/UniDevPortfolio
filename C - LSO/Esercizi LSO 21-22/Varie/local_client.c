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

#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/un.h>

#define MY_SOCKET "/tmp/exercise_socket"

void handler(int sig);

int main(int argc, char *argv[]) {

  char buffer[26];

  struct sockaddr_un server_addr;
  server_addr.sun_family = AF_LOCAL;
  strncpy(server_addr.sun_path, MY_SOCKET, sizeof(MY_SOCKET));

  if (signal(SIGPIPE, handler) == SIG_ERR) {
    printf("error installing handler for server shutdown: %s\n",
           strerror(errno));
    exit(EXIT_FAILURE);
  }

  int sd = socket(PF_LOCAL, SOCK_STREAM, 0);
  if (sd == -1) {
    printf("(client) socket error\n");
    exit(EXIT_FAILURE);
  }

  //  sleep(1);

  if (connect(sd, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
    printf("(client) socket connect error\n");
    exit(EXIT_FAILURE);
  }
  int n_byte_readed = 0;
  if ((n_byte_readed = read(sd, buffer, 26)) < 0) {
    printf("(client) socket read error\n");
    exit(EXIT_FAILURE);
  }

  buffer[n_byte_readed] = '\0';

  printf("response received: %s", buffer);
  close(sd);
  return 0;
}

void handler(int sig) {
  if (sig == SIGPIPE) {
    printf("the server don't responde\n");
    exit(EXIT_FAILURE);
  }
}
