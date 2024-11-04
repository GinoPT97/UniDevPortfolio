int main(int argc, char *argv[])
{
    ...
    int waiting = 0;
    int compat = 0;
    ...

    /* Main code begin here */
    if (compat) {                             /* install signal handler */
	Signal(SIGCHLD, HandSigCHLD);         /* non restarting handler */
    } else {
	SignalRestart(SIGCHLD, HandSigCHLD);  /* restarting handler */
    }
    ...

    /* main body */
    if (listen(list_fd, BACKLOG) < 0 ) {
	PrintErr("listen error");
	exit(1);
    }
    if (waiting) sleep(waiting);
    /* handle echo to client */
    while (1) {
	/* accept connection */
	len = sizeof(cli_add);
	while (((conn_fd = accept(list_fd, (struct sockaddr *)&cli_add, &len)) 
		< 0) && (errno == EINTR)); 
	if ( conn_fd < 0) {
	    PrintErr("accept error");
	    exit(1);
	}
	if (debugging) {
	    inet_ntop(AF_INET, &cli_add.sin_addr, ipaddr, sizeof(ipaddr));
	    snprintf(debug, MAXLINE, "Accepted connection form %s\n", ipaddr);
	    if (demonize) {
		syslog(LOG_DEBUG, debug);
	    } else {
		printf("%s", debug);
	    }
	}
	/* fork to handle connection */
	...
    }
    return;
}
