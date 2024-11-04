void ServEcho(int sockfd) {
    char buffer[MAXLINE];
    int nread, nwrite;
    char debug[MAXLINE+20];
    /* main loop, reading 0 char means client close connection */
    while ( (nread = read(sockfd, buffer, MAXLINE)) != 0) {
	nwrite = FullWrite(sockfd, buffer, nread);
	if (nwrite) {
	    PrintErr("write error");
	}
    }
    return;
}
