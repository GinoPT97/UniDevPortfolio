void ClientEcho(FILE * filein, int socket) 
{
    char sendbuff[MAXLINE+1], recvbuff[MAXLINE+1];
    int nread, nwrite; 
    /* initialize file descriptor set */
    while (1) {
	if (fgets(sendbuff, MAXLINE, filein) == NULL) {
	    return;                /* if no input just return */
	} else {                   /* else we have to write to socket */
	    nwrite = write(socket, sendbuff, strlen(sendbuff));
	    if (nwrite < 0) {      /* on error stop */
		printf("Errore in scrittura: %s", strerror(errno));
		return;
	    }
	}
	nread = read(socket, recvbuff, strlen(sendbuff)); 
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
