#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#define BUFSIZE 10

// Function to revert a string
void str_rev(char *str, int len)
{
  char c;

  for (int i = len/2-1 ; i >= 0 ; --i) {
    c                = str[i];
    str[i]           = str[len - i - 1];
    str[len - i - 1] = c;
  }

  // Termination char to avoid bunch of problems :P
  str[len + 1] = '\0';
}

// Program
int main (int argc, char *argv[]) {

  // Check on execution syntax
  if(!argv[1])
    printf("Usage: %s <filename>", argv[0]), exit(EXIT_FAILURE);

  int    fd;  
  char   buf[BUFSIZE + 1];
  off_t  cur_off;          // This variable saves the offset from lseek
  size_t cnt_byte;         // This variable saves the bytes to read everytime
  int    start_times = 0;  // This is a pseudo-flag, explained later

  // Check on open
  if((fd = open(argv[1], O_RDONLY)) < 0)
    perror("Open Error"), exit(EXIT_FAILURE);

  // Check on file's content
  if((cur_off = lseek(fd, -1, SEEK_END)) == -1)
    perror("Seek Error"), exit(EXIT_FAILURE);

  // To improve, works for now
  while(1) {
    // If the position of current offset is less than BUFSIZE...
    if(cur_off - BUFSIZE < 0) {
      cnt_byte = cur_off;   // Set the bytes to read to the current offset
      cur_off  = 0;         // Then set the offset variable at the start of file

      // If enters this condition more than one time, then break
      if(++start_times > 1) 
	break;
    }
    // Else...
    else {
      cnt_byte = BUFSIZE;            // Set the bytes to read to BUFSIZE
      cur_off  = cur_off - BUFSIZE;  // Then set the offset variable at current offset - BUFSIZE 
    }

    // Check lseek (setting the offset previously found)
    if((cur_off = lseek(fd, cur_off, SEEK_SET)) == -1)
      perror("Seek Error"), exit(EXIT_FAILURE);

    // Check reading
    if((read(fd, buf, cnt_byte)) != cnt_byte)
      perror("Read Error"), exit(EXIT_FAILURE);

    // Termination char for buffer char array
    buf[cnt_byte] = '\0';

    // Reverse a string
    str_rev(buf, strlen(buf));

    // Check and write to stdout
    if((write(STDOUT_FILENO, buf, cnt_byte)) != cnt_byte)
      perror("Write Error"), exit(EXIT_FAILURE);
  }

  close(fd);

  printf("\n");
  
  return 0;
}
