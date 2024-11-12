int main(int argc, char *argv[])
{
    int sock;
    int i, n, len, verbose=0;
    struct sockaddr_in addr;
    char buffer[MAXLINE];
    time_t timeval;
    ...
    /* create socket */
    if ( (sock = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
	perror("Socket creation error");
	exit(-1);
    }
    /* initialize address */
    memset((void *)&addr, 0, sizeof(addr));     /* clear server address */
    addr.sin_family = AF_INET;                  /* address type is INET */
    addr.sin_port = htons(13);                  /* daytime port is 13 */
    addr.sin_addr.s_addr = htonl(INADDR_ANY);   /* connect from anywhere */
    /* bind socket */
    if (bind(sock, (struct sockaddr *)&addr, sizeof(addr)) < 0) {
	perror("bind error");
	exit(-1);
    }
    /* write daytime to client */
    while (1) {
	n = recvfrom(sock, buffer, MAXLINE, 0, (struct sockaddr *)&addr, &len);
	if (n < 0) {
	    perror("recvfrom error");
	    exit(-1);
	}
	if (verbose) {
	    inet_ntop(AF_INET, &addr.sin_addr, buffer, sizeof(buffer));
	    printf("Request from host %s, port %d\n", buffer,
		   ntohs(addr.sin_port));
	}
	timeval = time(NULL);
	snprintf(buffer, sizeof(buffer), "%.24s\r\n", ctime(&timeval));
	n = sendto(sock, buffer, strlen(buffer), 0, 
		   (struct sockaddr *)&addr, sizeof(addr));
	if (n < 0) {
	    perror("sendto error");
	    exit(-1);
	}
    }
    /* normal exit */
    exit(0);
}
