#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>


int main(){
	char buf[16];
	char str[] = "ciao";
	int fd;
	if ( (fd=open("provola", O_RDWR)) < 0)
		perror("errore di open");
	read(fd, buf, 12);
	printf("%s\n", buf);
	lseek(fd, 12, SEEK_SET);
	write(fd, str, 4);
	lseek(fd, 0, SEEK_SET);
	read(fd, buf, 16);
	printf("%s\n", buf);

}
