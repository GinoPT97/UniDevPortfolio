#include <sys/types.h>   /* predefined types */
#include <unistd.h>      /* include unix standard library */
#include <arpa/inet.h>   /* IP addresses conversion utililites */
#include <sys/socket.h>  /* socket library */
#include <stdio.h>       /* include standard I/O library */
#include <time.h>

int main(int argc, char *argv[])
{
    int list_fd, conn_fd;
    int i;
    struct sockaddr_in serv_add, client;
    char buffer[MAXLINE];
    socklen_t len;
    time_t timeval;
    pid_t pid;
    int logging=0;
     ...
    /* write daytime to client */
    while (1) {
	len = sizeof(client);
        if ( (conn_fd = accept(list_fd, (struct sockaddr *)&client, &len)) 
             <0 ) {
            perror("accept error");
            exit(-1);
        }
        /* fork to handle connection */
        if ( (pid = fork()) < 0 ){
            perror("fork error");
            exit(-1);
        }
        if (pid == 0) {                 /* child */
            close(list_fd);
            timeval = time(NULL);
            snprintf(buffer, sizeof(buffer), "%.24s\r\n", ctime(&timeval));
            if ( (write(conn_fd, buffer, strlen(buffer))) < 0 ) {
                perror("write error");
                exit(-1);
            }
            if (logging) {
                inet_ntop(AF_INET, &client.sin_addr, buffer, sizeof(buffer));
                printf("Request from host %s, port %d\n", buffer,
                       ntohs(client.sin_port));
            }
            close(conn_fd);
            exit(0);
        } else {                        /* parent */
            close(conn_fd);
        }
    }
    /* normal exit, never reached */
    exit(0);
}
