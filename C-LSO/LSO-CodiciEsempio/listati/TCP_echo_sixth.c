    ...
    /* check if resetting on close is required */
    if (reset) {
	printf("Setting reset on close \n");
	ling.l_onoff = 1;
	ling.l_linger = 0;	
	if (setsockopt(sock, SOL_SOCKET, SO_LINGER, &ling, sizeof(ling))) {
	    perror("Cannot set linger");
	    exit(1);
	}
    }
    ...
