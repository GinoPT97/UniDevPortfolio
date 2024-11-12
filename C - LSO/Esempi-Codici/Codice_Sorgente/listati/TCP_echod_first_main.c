    ...
    /* main body */
    if (listen(list_fd, BACKLOG) < 0 ) { /* listen on socket */
	PrintErr("listen error");
	exit(1);
    }
    while (1) {                          /* handle echo to client */
	len = sizeof(cli_add);
	if ( (conn_fd = accept(list_fd, NULL, NULL)) < 0) { 
	    PrintErr("accept error");
	    exit(1);
	}
	if ( (pid = fork()) < 0 ) { 	 /* fork to handle connection */
	    PrintErr("fork error");
	    exit(1);
	}
	if (pid == 0) {      /* child */
	    close(list_fd);          /* close listening socket */   
	    ServEcho(conn_fd);       /* handle echo */
	    exit(0);
	} else {             /* parent */
	    close(conn_fd);          /* close connected socket */
	}
    }
    exit(0);     /* normal exit, never reached */
}
