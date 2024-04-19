void ClientEcho(FILE * filein, int socket);
/* Program begin */
int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int sock, i;
    struct sockaddr_in dst_addr;
    ...
    /* create socket */
    if ( (sock = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
	perror("Socket creation error");
	return 1;
    }
    /* initialize address */
    memset((void *) &dst_addr, 0, sizeof(dst_addr)); /* clear address */
    dst_addr.sin_family = AF_INET;                   /* address type is INET */
    dst_addr.sin_port = htons(7);                    /* echo port is 7 */
    /* build address using inet_pton */
    if ( (inet_pton(AF_INET, argv[optind], &dst_addr.sin_addr)) <= 0) {
	perror("Address creation error");
	return 1;
    }
    connect(sock, (struct sockaddr *) &dst_addr, sizeof(dst_addr));
    /* do read/write operations */
    ClientEcho(stdin, sock);
    /* normal exit */
    return 0;
}
