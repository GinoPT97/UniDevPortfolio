#include <stdlib.h>   /* predefined types */
#include <unistd.h>      /* include unix standard library */
#include <arpa/inet.h>   /* IP addresses conversion utilities */
#include <sys/socket.h>  /* socket library */
#include <stdio.h>       /* include standard I/O library */
#include <string.h>	 /* C strings library */

int main(int argc, char *argv[])
{
    int sock_fd;
    int i, nread;
    struct sockaddr_in serv_add;
    char buffer[MAXLINE];
     ...
    /* create socket */
    if ( (sock_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("Socket creation error");
        return -1;
    }
    /* initialize address */
    memset((void *) &serv_add, 0, sizeof(serv_add)); /* clear server address */
    serv_add.sin_family = AF_INET;                   /* address type is INET */
    serv_add.sin_port = htons(13);                   /* daytime post is 13 */
    /* build address using inet_pton */
    if ( (inet_pton(AF_INET, argv[optind], &serv_add.sin_addr)) <= 0) {
        perror("Address creation error");
        return -1;
    }
    /* extablish connection */
    if (connect(sock_fd, (struct sockaddr *)&serv_add, sizeof(serv_add)) < 0) {
        perror("Connection error");
        return -1;
    }
    /* read daytime from server */
    while ( (nread = read(sock_fd, buffer, MAXLINE)) > 0) {
        buffer[nread]=0;
        if (fputs(buffer, stdout) == EOF) {          /* write daytime */
            perror("fputs error");
            return -1;
        }
    }
    /* error on read */
    if (nread < 0) {
        perror("Read error");
        return -1;
    }
    /* normal exit */
    return 0;
}
