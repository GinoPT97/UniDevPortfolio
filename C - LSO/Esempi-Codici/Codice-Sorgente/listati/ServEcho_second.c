void ServEcho(int sockfd) {
    char buffer[MAXLINE];
    int nread, nwrite;
    char debug[MAXLINE+20];
    /* main loop, reading 0 char means client close connection */
    while ( (nread = read(sockfd, buffer, MAXLINE)) != 0) {
	if (nread < 0) {
	    PrintErr("Errore in lettura");
	    return;
	}
	nwrite = FullWrite(sockfd, buffer, nread);
	if (nwrite) {
	    PrintErr("Errore in scrittura");
	    return;
	}
	if (debugging) {
	    buffer[nread] = 0;
	    snprintf(debug, MAXLINE+20, "Letti %d byte, %s", nread, buffer);
	    if (demonize) {          /* daemon mode */
		syslog(LOG_DEBUG, debug);
	    } else {
		printf("%s", debug);
	    }
	}
    }
    return;
}
