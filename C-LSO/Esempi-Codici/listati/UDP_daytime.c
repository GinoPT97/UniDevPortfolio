int main(int argc, char *argv[])
{
    int sock;
    int i, nread;
    struct sockaddr_in addr;
    char buffer[MAXLINE];
    ...
    /* create socket */
    if ( (sock = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
	perror("Socket creation error");
	return -1;
    }
    /* initialize address */
    memset((void *) &addr, 0, sizeof(addr));    /* clear server address */
    addr.sin_family = AF_INET;                  /* address type is INET */
    addr.sin_port = htons(13);                  /* daytime port is 13 */
    /* build address using inet_pton */
    if ( (inet_pton(AF_INET, argv[optind], &addr.sin_addr)) <= 0) {
	perror("Address creation error");
	return -1;
    }
    /* send request packet */
    nread = sendto(sock, NULL, 0, 0, (struct sockaddr *)&addr, sizeof(addr));
    if (nread < 0) {
	perror("Request error");
	return -1;
    }
    nread = recvfrom(sock, buffer, MAXLINE, 0, NULL, NULL);
    if (nread < 0) {
	perror("Read error");
	return -1;
    }
    /* print results */
    if (nread > 0) {
	buffer[nread]=0;
	if (fputs(buffer, stdout) == EOF) {          /* write daytime */
	    perror("fputs error");
	    return -1;
	}
    }
    /* normal exit */
    return 0;
}
