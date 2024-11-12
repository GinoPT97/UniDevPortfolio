int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int sock, i;
    int reset = 0;
    ...
    /* call sockaddr to get a connected socket */
    if ( (sock = sockconn(argv[optind], "echo", 6, SOCK_STREAM)) < 0) {
	if (errno) perror("Socket creation error");
	return 1;
    }
    ...
    /* do read/write operations */
    ClientEcho(stdin, sock);
    /* normal exit */
    return 0;
}
