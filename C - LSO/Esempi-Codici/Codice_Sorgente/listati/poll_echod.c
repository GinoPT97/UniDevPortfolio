...
    poll_set = (struct pollfd *) malloc(n * sizeof(struct pollfd));
    max_fd = list_fd;                 /* maximum now is listening socket */
    for (i=0; i<n; i++) {             /* initialize poll set */
	poll_set[i].fd = -1;
	poll_set[i].events = POLLRDNORM;
    }
    poll_set[max_fd].fd = list_fd;    
    while (1) {   /* main loop, wait for connection and data inside a poll */
	while ( ((n=poll(poll_set, max_fd + 1, -1)) < 0) && (errno == EINTR));
	if (n < 0) {                          /* on real error exit */
	    PrintErr("poll error");
	    exit(1);
	}
	if (poll_set[list_fd].revents & POLLRDNORM) {  /* if new connection */
	    n--;                              /* decrement active */
	    len = sizeof(c_addr);             /* and call accept */
	    if ((fd = accept(list_fd, (struct sockaddr *)&c_addr, &len)) < 0) {
		PrintErr("accept error");
		exit(1);
	    }
	    poll_set[fd].fd = fd;             /* set new connection socket */
	    if (max_fd < fd) max_fd = fd;     /* if needed set new maximum */
	}
	i = list_fd;                  /* first socket to look */
	while (n != 0) {              /* loop until active */
	    i++;                      /* start after listening socket */
	    if (poll_set[i].fd == -1) continue;   /* closed, go next */
	    if (poll_set[i].revents & (POLLRDNORM|POLLERR)) {
		n--;                         /* decrease active */
		nread = read(i, buffer, MAXLINE);     /* read operations */
		if (nread < 0) {
		    PrintErr("Errore in lettura");
		    exit(1);
		}
		if (nread == 0) {            /* if closed connection */
		    close(i);                /* close file */
		    poll_set[i].fd = -1;          /* mark as closed in table */
		    if (max_fd == i) {       /* if was the maximum */
			while (poll_set[--i].fd == -1);    /* loop down */
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
