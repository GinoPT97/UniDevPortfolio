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

#include <string.h>

#include <time.h>

int main(int argc, char *argv[]) {
  int pfd[2];
  if (pipe(pfd))
    err(EXIT_FAILURE, "pipe error");

  pid_t child = fork();
  if (child < 0)
    err(EXIT_FAILURE, "fork error");
  else if (child == 0) {
    printf("child pid: %d\n", getpid());
    // cosi posso inviare un sigkill/abort e osservare una terminazione anonima
    close(pfd[1]);
    char buf[30];
    int byte_readed;
    // for (int i = 0; i < 10; i++) {
    for (int i = 0; i < 5; i++) {
      if ((byte_readed = read(pfd[0], buf, 30)) < 0)
        err(1, "read error");
      if (write(STDOUT_FILENO, buf, byte_readed) != byte_readed)
        err(1, "write error");
    }
    close(pfd[0]);
    exit(0);
  } else {
    close(pfd[0]);
    char num[30] = {0};
    srand(time(NULL));
    int rand_num = 0;
    int len_rand_num = 0;
    for (int i = 0; i < 10; i++) {
      rand_num = rand();
      sprintf(num, "%d", rand_num);
      len_rand_num = strlen(num);
      // Una cosa del genere andrebbe sempre evitata, in questo caso sono sempre
      // sicuro che il numero generato ha meno di 30 cifre (RAND_MAX solitamente
      // e' cira 22*10^6 [dipende dall' implementazione] )
      // Per evitare troppi controlli su una cosa che non e' l'obbiettivo
      // dell'esercizio
      num[len_rand_num] = ' ';
      num[len_rand_num + 1] = '\n';

      if (write(pfd[1], num, len_rand_num + 2) < 0)
        err(EXIT_FAILURE, "write error");
      sleep(1);
    }
    int status;
    wait(&status);
    if (WIFEXITED(status)) {
      printf("child terminated with %d\n", WEXITSTATUS(status));
    } else
      err(EXIT_FAILURE, "error in child process");
    close(pfd[1]);
  }

  return 0;
}
