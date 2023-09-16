void ClientEcho(FILE * filein, int socket, struct sockaddr_in *serv_add);
void SigTERM_hand(int sig);

/* Program begin */
int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int sock, i;
    struct sockaddr_in serv_add;
    ...
    /* create socket */
    if ( (sock = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
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
    /* do read/write operations */
    ClientEcho(stdin, sock, &serv_add);
    /* normal exit */
    return 0;
}
