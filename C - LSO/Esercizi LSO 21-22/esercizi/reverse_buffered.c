#include <stdio.h>
#include <stdlib.h>

#include <errno.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include <err.h>

static void reverse(char *buffer, int len) {
  unsigned int i = 0, j = len - 1;
  char tmp;
  while (i < j) {
    tmp = buffer[i];
    buffer[i++] = buffer[j];
    buffer[j--] = tmp;
  }
  return;
}

int main(int argc, char *argv[]) {
  int fd, n;
  char *buffer;
  unsigned int buffsize;

  char buff_input[32];

  if (argc != 3)
    err(EXIT_FAILURE,
        "Usage: %s <input_filename> <buffer size>\nthis program doesn't any "
        "check on the second parameter, please use a positive integer",
        argv[0]);

  buffsize = atoi(argv[2]);

  buffer = calloc(buffsize, sizeof(char));
  if (buffer == NULL)
    err(EXIT_FAILURE, "calloc error");

  if ((fd = open(argv[1], O_RDONLY)) == -1)
    err(EXIT_FAILURE, "open error");

  off_t cur_pos = lseek(fd, -1, SEEK_END);
  if (cur_pos < 0)
    err(EXIT_FAILURE, "exit failure");

  unsigned int num_bytes_readed = 0;
  while (cur_pos > 0) {
    off_t offset = cur_pos > buffsize ? cur_pos - buffsize : 0;
    unsigned int amount_to_read = cur_pos > buffsize ? buffsize : cur_pos;
    if ((cur_pos = lseek(fd, offset, SEEK_SET)) < 0)
      err(EXIT_FAILURE, "seek failure");
    if ((num_bytes_readed = read(fd, (void *)buffer, amount_to_read)) < 0)
      err(EXIT_FAILURE, "read failure");

    reverse(buffer, amount_to_read);
    if (write(STDOUT_FILENO, (void *)buffer, num_bytes_readed) < 0)
      err(EXIT_FAILURE, "write failure");
  }

  free(buffer);
  close(fd);
  exit(0);
}
