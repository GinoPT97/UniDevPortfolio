int sockbindopt(char *host, char *serv, int prot, int type, int reuse) 
{
    struct addrinfo hint, *addr, *save;
    int res;
    int sock;
    char buf[INET6_ADDRSTRLEN];
    ...
    while (addr != NULL) {                 /* loop on possible addresses */
	/* get a socket */
	sock = socket(addr->ai_family, addr->ai_socktype, addr->ai_protocol);
	...
	/* connect the socket */
	if (setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, 
		       &reuse, sizeof(reuse))) {
	    printf("error on socket options\n");
	    return -1;
	}
	...

    return sock;
}
