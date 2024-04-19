int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int sock_fd, i;
    struct sockaddr_in serv_add;
    ...
    /* create socket */
    if ( (sock_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("Socket creation error");
        return 1;
    }
    /* initialize address */
    memset((void *) &serv_add, 0, sizeof(serv_add)); /* clear server address */
    serv_add.sin_family = AF_INET;                   /* address type is INET */
    serv_add.sin_port = htons(7);                    /* echo port is 7 */
    /* build address using inet_pton */
    if ( (inet_pton(AF_INET, argv[optind], &serv_add.sin_addr)) <= 0) {
        perror("Address creation error");
        return 1;
    }
    /* extablish connection */
    if (connect(sock_fd, (struct sockaddr *)&serv_add, sizeof(serv_add)) < 0) {
        perror("Connection error");
        return 1;
    }
    /* read daytime from server */
    ClientEcho(stdin, sock_fd);
    /* normal exit */
    return 0;
}
