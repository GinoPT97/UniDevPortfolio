    ...
    memset(fd_open, 0, FD_SETSIZE);   /* clear array of open files */
    max_fd = list_fd;                 /* maximum now is listening socket */
    fd_open[max_fd] = 1;
    while (1) {   /* main loop, wait for connection and data inside a select */
	FD_ZERO(&fset);                       /* clear fd_set */
	for (i = list_fd; i <= max_fd; i++)   /* initialize fd_set */
	    if (fd_open[i] != 0) FD_SET(i, &fset); 
	while ( ((n = select(max_fd + 1, &fset, NULL, NULL, NULL)) < 0) 
		&& (errno == EINTR));         /* wait for data or connection */
	if (n < 0) {                          /* on real error exit */
	    PrintErr("select error");
	    exit(1);
	}
	if (FD_ISSET(list_fd, &fset)) {       /* if new connection */
	    n--;                              /* decrement active */
	    len = sizeof(c_addr);             /* and call accept */	    
	    if ((fd = accept(list_fd, (struct sockaddr *)&c_addr, &len)) < 0) {
		PrintErr("accept error");
		exit(1);
	    }
	    fd_open[fd] = 1;                  /* set new connection socket */
	    if (max_fd < fd) max_fd = fd;     /* if needed set new maximum */
	}
	i = list_fd;      /* first socket to look */
	while (n != 0) {  /* loop on open connections */
	    i++;                             /* start after listening socket */
	    if (fd_open[i] == 0) continue;   /* closed, go next */
	    if (FD_ISSET(i, &fset)) {        /* if active process it*/
		n--;                         /* decrease active */
		nread = read(i, buffer, MAXLINE);     /* read operations */
		if (nread < 0) {
		    PrintErr("Errore in lettura");
		    exit(1);
		}
		if (nread == 0) {            /* if closed connection */
		    close(i);                /* close file */
		    fd_open[i] = 0;          /* mark as closed in table */
		    if (max_fd == i) {       /* if was the maximum */
			while (fd_open[--i] == 0);  /* loop down */
			max_fd = i;          /* set new maximum */
			break;               /* and go back to select */
		    }
		    continue;                /* continue loop on open */
		}
		nwrite = FullWrite(i, buffer, nread); /* write data */
		if (nwrite) {
		    PrintErr("Errore in scrittura");
		    exit(1);
		}
	    }
	}
    }
    ...
