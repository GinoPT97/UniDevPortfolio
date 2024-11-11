//Lettura di un file a caratteri alterni.
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#define BUFSIZE 4

int main(void){

	char buffer[BUFSIZE];

	int fd = open("es1.txt", O_RDONLY);
	if(fd < 0){
		perror("Opening error");
		exit(0);
	}

	int n = 0;
	while((n=read(fd, buffer, BUFSIZE)) > 0){
		for(int i = 0; i < n; i = i+2) printf("%c", buffer[i]);
	}

	close(fd);
}
