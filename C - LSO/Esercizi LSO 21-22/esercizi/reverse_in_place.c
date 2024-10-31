#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>

#include <err.h>
#include <errno.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

static void reverse(char *buffer, int len) {
  size_t i = 0, j = len - 1;
  char tmp;
  while (i < j) {
    tmp = buffer[i];
    buffer[i++] = buffer[j];
    buffer[j--] = tmp;
  }
  return;
}

size_t read_frame(int fd, char *buffer, size_t buffsize, off_t from);
size_t write_frame(int fd, char *buffer, size_t buffsize, off_t from);

int main(int argc, char *argv[]) {
  int fd_read, fd_write, n;

  struct stat info;

  char *buffer_read_from_start, *buffer_read_from_end;
  size_t buffsize;

  if (argc < 3)
    err(EXIT_FAILURE,
        "Usage: %s <input_filename> <buffer size>\nthis program doesn't any "
        "check on the second parameter, please use a positive integer",
        argv[0]);

  if (lstat(argv[1], &info) == -1)
    err(EXIT_FAILURE, "lstat error");

  if (!S_ISREG(info.st_mode))
    err(EXIT_FAILURE, "%s non e' un file regolare", argv[1]);

  buffsize = atoi(argv[2]);

  buffer_read_from_start = calloc(buffsize, sizeof(char));
  if (buffer_read_from_start == NULL)
    err(EXIT_FAILURE, "calloc error");

  buffer_read_from_end = calloc(buffsize, sizeof(char));
  if (buffer_read_from_end == NULL)
    err(EXIT_FAILURE, "calloc error");

  if ((fd_read = open(argv[1], O_RDWR, S_IRWXU ^ S_IXUSR)) == -1)
    err(EXIT_FAILURE, "open error");

  if ((fd_write = dup(fd_read)) == -1)
    err(EXIT_FAILURE, "dup error");

  off_t current_position_from_end = lseek(fd_read, -1, SEEK_END);
  if (current_position_from_end < 0)
    err(EXIT_FAILURE, "seek failure");

  off_t current_position_from_start = lseek(fd_read, 0, SEEK_SET);
  if (current_position_from_start < 0)
    err(EXIT_FAILURE, "seek failure");

  off_t num_byte = info.st_size - 1; // equivale a lseek(fd_read, 0, SEEK_END)

  size_t num_bytes_readed_from_start = 0;
  size_t num_bytes_readed_from_end = 0;

  off_t num_frame = num_byte / buffsize;
  off_t byte_remaining = num_byte % buffsize;

  off_t frame_to_consume = num_frame - num_frame / 2;
  off_t frame_consumed = 0;

  if (num_byte <= buffsize) {
    num_bytes_readed_from_start = read_frame(
        fd_read, buffer_read_from_end, buffsize, current_position_from_start);
    reverse(buffer_read_from_end, num_bytes_readed_from_start);
    write_frame(fd_write, buffer_read_from_end, num_bytes_readed_from_start,
                current_position_from_start);
  } else {
    for (; frame_consumed < frame_to_consume; frame_consumed++) {
      num_bytes_readed_from_start =
          read_frame(fd_read, buffer_read_from_start, buffsize,
                     current_position_from_start);

      num_bytes_readed_from_end =
          read_frame(fd_read, buffer_read_from_end, buffsize,
                     current_position_from_end - buffsize);

      reverse(buffer_read_from_start, num_bytes_readed_from_start);
      reverse(buffer_read_from_end, num_bytes_readed_from_end);

      current_position_from_start += num_bytes_readed_from_start;
      current_position_from_end -= num_bytes_readed_from_end;

      write_frame(fd_write, buffer_read_from_end, num_bytes_readed_from_end,
                  current_position_from_start - buffsize);
      write_frame(fd_write, buffer_read_from_start, num_bytes_readed_from_start,
                  current_position_from_end);
    }

    if (current_position_from_start < current_position_from_end - 1) {
      off_t from_where = current_position_from_start;
      if (frame_consumed < frame_to_consume) {
        num_bytes_readed_from_start =
            read_frame(fd_read, buffer_read_from_start, buffsize,
                       current_position_from_start);
        from_where = current_position_from_start + num_bytes_readed_from_start;
        reverse(buffer_read_from_start, num_bytes_readed_from_start);
      }
      num_bytes_readed_from_end =
          read_frame(fd_read, buffer_read_from_end, byte_remaining, from_where);
      reverse(buffer_read_from_end, num_bytes_readed_from_end);
      write_frame(fd_write, buffer_read_from_end, num_bytes_readed_from_end,
                  current_position_from_start);
      current_position_from_start += num_bytes_readed_from_end;
      if (frame_consumed < frame_to_consume) {
        write_frame(fd_write, buffer_read_from_start,
                    num_bytes_readed_from_start, current_position_from_start);
      }
    }
  }

  free(buffer_read_from_start);
  free(buffer_read_from_end);
  close(fd_read);
  close(fd_write);
  return 0;
}

size_t read_frame(int fd, char *buffer, size_t buffsize, off_t from) {
  if (lseek(fd, from, SEEK_SET) < 0)
    err(EXIT_FAILURE, "seek read failure");
  size_t num_byte_readed = 0;
  if ((num_byte_readed = read(fd, buffer, buffsize)) < 0)
    err(EXIT_FAILURE, "read frame failure");
  return num_byte_readed;
}

size_t write_frame(int fd, char *buffer, size_t buffsize, off_t from) {
  if (lseek(fd, from, SEEK_SET) < 0)
    err(EXIT_FAILURE, "seek write failure");
  size_t num_byte_written = 0;
  if ((num_byte_written = write(fd, buffer, buffsize)) < 0)
    err(EXIT_FAILURE, "write frame failure");
  return num_byte_written;
}
