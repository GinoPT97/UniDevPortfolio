#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <errno.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#define BUFSIZE 4


int main(int argc, char* argv[]) {

   char buffer[BUFSIZE];
   int fd, n;
   struct stat inf;
   
   if(argv[1] == NULL || argc != 2) {
     printf("Error: Usage %s <filename>\n", argv[0]);
     exit(1);
   }
   if(lstat(argv[1], &inf) == -1) {
     perror("Lstat Error : ");
     exit(2);
   }
   if(! S_ISREG(inf.st_mode)) {
     printf("Error : input file should be a regoular file\n");
     exit(3);
   }
   if(access(argv[1], R_OK) != 0) {
     printf("Error, permission denied for file %s\n", argv[1]);
     exit(4);
   }
   if((fd = open(argv[1], O_RDONLY)) == -1) {
     perror("Open Error ");
     exit(5);
   }
   while((n = read(fd, buffer, 1)) != 0) {
     if(n == -1) {
       perror("Read Error ");
       exit(6);
     }
     if(write(STDOUT_FILENO, buffer, 1) == -1) {
       perror("Write Error ");
       exit(7);
     }
     if(lseek(fd, 1, SEEK_CUR) == -1) {
       perror("Seek Error ");
       exit(8);
     }
   }
   printf("\n");
   if(close(fd) == -1) 
     perror("Warning, Close Error : ");
   
return 0;
}
