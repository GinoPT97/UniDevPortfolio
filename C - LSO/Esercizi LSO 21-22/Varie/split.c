#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h> //bool type
#include <ctype.h> // isdigit function
#include <errno.h> // errno for check strtol errors
#include <sys/types.h> // open function
#include <sys/stat.h> // open function
#include <fcntl.h> // open function
#include <unistd.h> // lseek function

bool isNumber(char* string);

int main(int argc, char* argv[]) {

  long n;
  int file;
  int file_to_write;
  long size;

  if(argv[1]==NULL) {

    printf("Please, insert a file.\n");
    exit(1);

  }

  file = open(argv[1], O_RDONLY);
  if(file==-1) {

    printf("Open error.\n");
    exit(1);

  }
  
  if(argv[2]==NULL) {

    printf("Please, insert a number > 0.\n");
    exit(1);

  }
  
  if(isNumber(argv[2])) {

    n = strtol(argv[2], NULL, 10);
    if(errno==ERANGE) {

      printf("Error while converting string to int.\n");
      exit(1);

    }

  }
  else {

    printf("Please, insert a number.\n");
    exit(1);

  }

  // --------------------------------- //

  size = lseek(file, 0, SEEK_END);
  lseek(file, 0, SEEK_SET);
  
  file_to_write = open("part1", O_WRONLY | O_CREAT, S_IRWXU);
  if(file_to_write==-1) {

    printf("2. Open error.\n");
    exit(1);

  }

  if(n>=size) {

    char buffer[size];
    if(read(file, buffer, size)<=0) {

      printf("Read error.\n");
      exit(1);

    }
    close(file);

    if(write(file_to_write, buffer, size)<=0) {

      printf("Write error.\n");
      exit(1);

    }

  }
  else {

    char buffer_1[n];
    char buffer_2[size-n];

    if(read(file, buffer_1, n)<=0) {

      printf("Read error.\n");
      exit(1);

    }
    if(read(file, buffer_2, size-n)<=0) {

      printf("Read error.\n");
      exit(1);

    }
    close(file);

    if(write(file_to_write, buffer_1, n)<=0) {

      printf("Write error.\n");
      exit(1);

    }
    close(file_to_write);

    file_to_write = open("part2", O_WRONLY | O_CREAT, S_IRWXU);
    if(file_to_write==-1) {

      printf("Open error.\n");
      exit(1);

    }

    if(write(file_to_write, buffer_2, size-n)<=0) {

      printf("Write error.\n");
      exit(1);

    }
    close(file_to_write);

  }

  return 0;

}

bool isNumber(char* string) {

  while(*string!=0) {

    if(!isdigit(*string))
      return false;
    string++;
  }
  return true;

}