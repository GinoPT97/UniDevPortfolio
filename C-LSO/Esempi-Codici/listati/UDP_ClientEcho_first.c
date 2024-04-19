void ClientEcho(FILE * filein, int socket, struct sockaddr_in * serv_addr) 
{
    char sendbuff[MAXLINE+1], recvbuff[MAXLINE+1];
    int nread, nwrite; 
    /* initialize file descriptor set */
    while (1) {
	if (fgets(sendbuff, MAXLINE, filein) == NULL) {
	    return;                /* if no input just return */
	} else {                   /* else we have to write to socket */
	    nwrite = sendto(socket, sendbuff, strlen(sendbuff), 0,
			    (struct sockaddr *) serv_addr, sizeof(*serv_addr));
	    if (nwrite < 0) {      /* on error stop */
		printf("Errore in scrittura: %s", strerror(errno));
		return;
	    }
	}
	nread = recvfrom(socket, recvbuff, strlen(sendbuff), 0, NULL, NULL); 
	if (nread < 0) {  /* error condition, stop client */
	    printf("Errore in lettura: %s\n", strerror(errno));
	    return;
	}
	recvbuff[nread] = 0;   /* else read is ok, write on stdout */
	if (fputs(recvbuff, stdout) == EOF) {
	    perror("Errore in scrittura su terminale");
	    return;
	}
    }
}
