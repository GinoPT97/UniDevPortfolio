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

int main(int argc, char *argv[]) {
  int pfd[2];
  if (pipe(pfd))
    err(EXIT_FAILURE, "pipe error");
  pid_t child = fork();
  if (child < 0)
    err(EXIT_FAILURE, "fork error");
  else if (child == 0) {
    close(pfd[1]);
    char buf[30];
    int num_read;
    int n;
    do {
      if ((num_read = read(pfd[0], buf, 30)) < 0)
        err(EXIT_FAILURE, "read error");
      n = atoi(buf);
      printf("il figlio legge: %d\n", n);
    } while (n != 0);
    close(pfd[0]);
    exit(0);
  } else {
    close(pfd[0]);
    int n;
    char buf[30];
    do {
      printf("inserisci un intero: \n");
      scanf("%d", &n);
      n *= n; // potrebbe generare un overflow
      sprintf(buf, "%d", n);
      write(pfd[1], buf, strlen(buf) + 1); // devo scrivere anche lo '\0'
    } while (n != 0);
    int status;
    wait(&status);
    if (WIFEXITED(status))
      printf("child terminated with: %d\n", WEXITSTATUS(status));
    else
      err(EXIT_FAILURE, "error in child process");
    close(pfd[1]);
  }
  return 0;
}
