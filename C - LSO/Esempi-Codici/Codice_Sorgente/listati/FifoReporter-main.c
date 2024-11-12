    /* Main body: wait something to report */
    while (1) {
        if ((n=epoll_wait(epfd, events, MAX_EPOLL_EV, -1)) < 0) 
	    die("error on epoll_wait");
	for (i=0; i<n; i++) {    // loop on ready file descriptors             
	    if (events[i].data.fd == sigfd) {  // look if signalfd ready 
		printf("Signal received:\n");
		while(nread=read(sigfd, &siginf, sizeof(siginf))) {
		    if (nread < 0) {
			if (errno != EAGAIN) 
			    die("signalfd read error");
			else 
			    break;
		    }
		    if (nread != sizeof(siginf)) {
			printf("Error on signal data read, '\n");
			continue;
		    }
		    printf("Got %s\n", sig_names[siginf.ssi_signo]);
		    printf("From pid %i\n", siginf.ssi_pid);
		    if(siginf.ssi_signo == SIGINT) { // SIGINT stop program
			unlink(fifoname);
			exit(0);
		    }
		}
	    } else if (events[i].data.fd == fifofd) { // look if fifofd ready 
		printf("Message from fifo:\n");
		while ((nread = read(fifofd, buffer, 5000))) {
		    if (nread < 0) {
			if (errno != EAGAIN)
			    die("fifo read error");
			else 
			    printf("end message\n");
			break;
		    }
		    buffer[nread] = 0;
		    if (fputs(buffer, stdout) == EOF)
			die("Errore in scrittura su terminale");
		}
	    } else {   // anything else is an error
		printf("epoll activity on unknown %i file descriptor\n", 
		       epev.data.fd);
		exit(-1);
	    }
	}
    }
