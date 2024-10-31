#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <errno.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#define BUFSIZE 256


int main(int argc, char* argv[]) {

   char buffer[BUFSIZE];
   
   if(argv[1] == NULL) {
     printf("Error: Usage %s <filename>\n", argv[0]);
     exit(1);
   }
   int fd, n;
   if((fd = open(argv[1], O_RDONLY)) == -1) {
     perror("Open Error ");
     exit(2);
   }
   while((n = read(fd, buffer, 1)) != 0) {
     if(n == -1) {
       perror("Read Error ");
       exit(3);
     }
     if(write(STDOUT_FILENO, buffer, 1) == -1) {
       perror("Write Error ");
       exit(4);
     }
     if(lseek(fd, 1, SEEK_CUR) == -1) {
       perror("Seek Error ");
       exit(5);
     }
   }
   printf("\n");
   close(fd);
   
return 0;
}
