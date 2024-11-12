#define MAXLINE 80
#define BACKLOG 10
int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int list_fd, conn_fd;
    int i;
    struct sockaddr_in serv_add;
    char buffer[MAXLINE];
    time_t timeval;
    ...
    /* create socket */
    if ( (list_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("Socket creation error");
        exit(-1);
    }
    /* initialize address */
    memset((void *)&serv_add, 0, sizeof(serv_add)); /* clear server address */
    serv_add.sin_family = AF_INET;                  /* address type is INET */
    serv_add.sin_port = htons(13);                  /* daytime port is 13 */
    serv_add.sin_addr.s_addr = htonl(INADDR_ANY);   /* connect from anywhere */
    /* bind socket */
    if (bind(list_fd, (struct sockaddr *)&serv_add, sizeof(serv_add)) < 0) {
        perror("bind error");
        exit(-1);
    }
    /* listen on socket */
    if (listen(list_fd, BACKLOG) < 0 ) {
        perror("listen error");
        exit(-1);
    }
    /* write daytime to client */
    while (1) {
        if ( (conn_fd = accept(list_fd, (struct sockaddr *) NULL, NULL)) <0 ) {
            perror("accept error");
            exit(-1);
        }
        timeval = time(NULL);
        snprintf(buffer, sizeof(buffer), "%.24s\r\n", ctime(&timeval));
        if ( (write(conn_fd, buffer, strlen(buffer))) < 0 ) {
            perror("write error");
            exit(-1);
        }
        close(conn_fd);
    }
    /* normal exit */
    exit(0);
}
