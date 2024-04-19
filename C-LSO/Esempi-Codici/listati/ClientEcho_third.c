void ClientEcho(FILE * filein, int socket) 
{
    char sendbuff[MAXLINE+1], recvbuff[MAXLINE+1];
    int nread, nwrite; 
    int maxfd;
    fd_set fset;
    /* initialize file descriptor set */
    FD_ZERO(&fset);
    maxfd = max(fileno(filein), socket) + 1;
    while (1) {
	FD_SET(socket, &fset);         /* set for the socket */
	FD_SET(fileno(filein), &fset); /* set for the standard input */
	select(maxfd, &fset, NULL, NULL, NULL); /* wait for read ready */
	if (FD_ISSET(fileno(filein), &fset)) {  /* if ready on stdin */
	    if (fgets(sendbuff, MAXLINE, filein) == NULL) { /* if no input */
		return;                /* we stopped client */
	    } else {                   /* else we have to write to socket */
		nwrite = FullWrite(socket, sendbuff, strlen(sendbuff)); 
		if (nwrite < 0) {      /* on error stop */
		    printf("Errore in scrittura: %s", strerror(errno));
		    return;
		}
	    }
	}
	if (FD_ISSET(socket, &fset)) { /* if ready on socket */ 
	    nread = read(socket, recvbuff, strlen(sendbuff)); /* do read */
	    if (nread < 0) {  /* error condition, stop client */
		printf("Errore in lettura: %s\n", strerror(errno));
		return;
	    }
	    if (nread == 0) { /* server closed connection, stop */
		printf("EOF sul socket\n");
		return;
	    }
	    recvbuff[nread] = 0;   /* else read is ok, write on stdout */
	    if (fputs(recvbuff, stdout) == EOF) {
		perror("Errore in scrittura su terminale");
		return;
	    }
	}
    }
}
