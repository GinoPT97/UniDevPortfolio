//Leggere al contrario il contenuto di un file.
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#define BUFSIZE 2

int main(){

	int fd = open("es2.txt", O_RDWR);

	char buffer[2] = "";

	printf("----*Il file al contrario e':*----\n\n");
	off_t filelen = lseek(fd, 0, SEEK_END);								//Salvo il valore di <SEEK_END> per *.
	off_t os = lseek(fd, -2, SEEK_END);
	int n = read(fd, buffer, BUFSIZE);
	while(os != -1){
		for(int i = n-1; i >= 0; i--) printf("%c", buffer[i]);		//Semplice stampa al contrario di quello che al momento sta sul buffer.
		os = lseek(fd, -4, SEEK_CUR);															//Mi pongo sull'offset da cui dovrò leggere la prossima volta.
		n = read(fd, buffer, BUFSIZE);
	}
	if(filelen%2!=0){											//*. Per leggere il primo carattere di un file di dimensione dispari.
		lseek(fd, 0, SEEK_SET);
		read(fd, buffer, 1);
		printf("%c\n", buffer[0]);
	}

	close(fd);

}
