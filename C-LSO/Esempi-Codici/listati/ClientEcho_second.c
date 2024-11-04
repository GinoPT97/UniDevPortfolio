void ClientEcho(FILE * filein, int socket) 
{
    char sendbuff[MAXLINE+1], recvbuff[MAXLINE+1];
    int nread, nwrite; 
    while (fgets(sendbuff, MAXLINE, filein) != NULL) {
	nwrite = FullWrite(socket, sendbuff, strlen(sendbuff)); 
	if (nwrite < 0) {
	    printf("Errore in scrittura: %s", strerror(errno));
	    return;
	}
        nread = read(socket, recvbuff, strlen(sendbuff));
	if (nread < 0) {
	    printf("Errore in lettura: %s\n", strerror(errno));
	    return;
	}
	if (nread == 0) {
	    printf("End of file in lettura %s\n");
	    return;
	}
	recvbuff[nread] = 0;
	if (fputs(recvbuff, stdout) == EOF) {
	    perror("Errore in scrittura su terminale");
	    return;
	}
    }
    return;
}
