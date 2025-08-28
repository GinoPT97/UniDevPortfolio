/*
  Stampare un file invertito - Versione con il buffer

  Valentino Bocchetti N86003405

 */
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <errno.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>

int main(int argc, char *argv[]) {
  if (argv[1] == NULL) {
    printf("Error: Usage %s <filename>\n", argv[0]);
    exit(EXIT_FAILURE);
  }

  struct stat st; // Nel caso in cui conoscessimo il nome del file
  stat(argv[1], &st);
  off_t size = st.st_size;
  char buffer[size];
  int fd, n;

  // Apriamo il file
  if ((fd = open(argv[1], O_RDONLY)) == -1) {
    perror("Open Error");
    exit(EXIT_FAILURE);
  }

  // Ci spostiamo alla fine del file
  if ((lseek(fd, -2, SEEK_END)) == -1) {
    perror("Fseek Error");
    exit(EXIT_FAILURE);
  }

  /*

    Usiamo un ciclo per eseguire in ordine:
    1. Lettura del carattere nella posizione corrente
    2. Scrittura del carattere nella posizione corrente
    3. Spostamento dell'offset nella posizione precedente

  */

  for (int i = (size - 2); i >= 0; i--) {
    if ((n = read(fd, buffer, 1)) == -1) {
      perror("Read Error");
      exit(EXIT_FAILURE);
    }

    if ((write(STDOUT_FILENO, buffer, 1)) == -1) {
      perror("Write Error");
      exit(EXIT_FAILURE);
    }

    if (i != 0) { // Eseguiamo l'istruzione successiva solo nel caso in cui non sia arrivati all'inizio del file
      if ((lseek(fd, -2, SEEK_CUR)) == -1) {
	perror("Lseek Error");
	exit(EXIT_FAILURE);
      }
    }
  }
  // Chiudiamo il buffer
  close(fd);

  printf("\n");
}
