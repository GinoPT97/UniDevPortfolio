#include <stdio.h>
#include <stdlib.h>

#include <errno.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include <err.h>
#include <libgen.h>

/*
 * Si implementi un programma C che prende come argomenti tre nomi di files
f1, f2 , f3 (f1 deve essere un file esistente) ed il nome di un eseguibile (es.
rev.out) che accetta a sua volta il path di un file e lo stampa in ordine
inverso di bytes sullo standard output. Il programma genera un primo figlio che
esegue rev.out su f1 e scrive l'output in f2. Successivamente, il programma
genera un secondo figlio che che esegue rev.out su f2 e scrive a sua volta
l'output in f3.*/

int main(int argc, char *argv[], char **envp) {
  if (argc != 5)
    err(EXIT_FAILURE, "Usage: %s <file1> <file2> <file3> <executable>",
        argv[0]);

  struct stat info_file_1, info_file_2, info_file_3, info_executable;

  if (lstat(argv[1], &info_file_1) == -1)
    err(EXIT_FAILURE, "lstat error");

  if (!S_ISREG(info_file_1.st_mode))
    err(EXIT_FAILURE, "%s non e' un file regolare", argv[1]);

  // nella traccia non e' specificato che questi file debbano esistere
  /* if (lstat(argv[2], &info_file_2) == -1) */
  /*   err(EXIT_FAILURE, "lstat error"); */

  /* if (!S_ISREG(info_file_2.st_mode)) */
  /*   err(EXIT_FAILURE, "%s non e' un file regolare", argv[2]); */

  /* if (lstat(argv[3], &info_file_3) == -1) */
  /*   err(EXIT_FAILURE, "lstat error"); */

  /* if (!S_ISREG(info_file_3.st_mode)) */
  /*   err(EXIT_FAILURE, "%s non e' un file regolare", argv[3]); */

  if (lstat(argv[4], &info_executable) == -1)
    err(EXIT_FAILURE, "lstat error");

  if ((info_executable.st_mode & S_IXUSR) == 0)
    err(EXIT_FAILURE, "you don't have the permission to execute this file");

  // non ho bisogno di aprire il file 1
  int fd2, fd3;

  pid_t child1, child2;
  int exit_status_child;

  if ((fd2 = open(argv[2], O_RDWR | O_CREAT, S_IRWXU ^ S_IXUSR)) == -1)
    err(EXIT_FAILURE, "open error fd2");

  if ((fd3 = open(argv[3], O_WRONLY | O_CREAT, S_IRWXU ^ S_IXUSR)) == -1)
    err(EXIT_FAILURE, "open error fd3");

  // imposto il path in cui trovare l'eseguibile, avrei potuto anche usare
  // un'altra exec con input il comando pwd e redirigere l'output in un buffer

  if (dup2(fd2, STDOUT_FILENO) < 0)
    err(EXIT_FAILURE, "dup2 error");

  if ((child1 = fork()) == 0) { // codice figlio 1

    if (execl(argv[4], basename(argv[4]), argv[1], "4096", (char *)0) < 0)
      err(EXIT_FAILURE, "execl failure");

  } else if (child1 > 0) { // codice padre

    waitpid(child1, &exit_status_child, 0);

    if (WIFEXITED(exit_status_child)) {
      // non posso stampare su stdout perche' e' redireto su fd2!
      // printf("figlio terminato con %d", WEXITSTATUS(status));

      // procedo con il figlio 2

      if (dup2(fd3, STDOUT_FILENO) < 0)
        err(EXIT_FAILURE, "dup2 error");

      close(fd2);

      if ((child2 = fork()) == 0) { // codice figlio 2
        if (execl(argv[4], basename(argv[4]), argv[2], "4096", (char *)0) < 0)
          err(EXIT_FAILURE, "execl failure");

      } else if (child2 > 0) { // sempre codice padre
        if (WIFEXITED(exit_status_child)) {
          // non posso stampare su stdout perche' e' redireto su fd2 e fd3!
          // printf("figlio terminato con %d", WEXITSTATUS(status));
          close(fd3);
        }

      } else { // errore
        err(EXIT_FAILURE, "fork error");
      }
    }
  } else {
    err(EXIT_FAILURE, "fork error");
  }

  return 0;
}
