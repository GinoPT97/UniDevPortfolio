    /* remaining argument check */
    if ((argc - optind) != 2) {
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    /* main body */    
    ret = getaddrinfo(argv[optind], argv[optind+1], &hint, &res); 
    if (ret != 0) {
	printf("Resolution error %s\n", gai_strerror(ret));
	exit(1);
    }
    ptr = res;                                        /* init list pointer */
    printf("Canonical name %s\n", ptr->ai_canonname); /* print cname */
    while (ptr != NULL) {                             /* loop on list */
	if (ptr->ai_family == AF_INET) {              /* if IPv4 */
	    printf("IPv4 address: \n");
	    addr = (struct sockaddr_in *) ptr->ai_addr;     /* address */
	    port = ntohs(addr->sin_port);                   /* port */
	    string = inet_ntop(addr->sin_family, &addr->sin_addr, 
			       buffer, sizeof(buffer));
	} else if (ptr->ai_family == AF_INET6) {      /* if IPv6 */
	    printf("IPv6 address: \n");
	    addr6 = (struct sockaddr_in6 *) ptr->ai_addr;   /* address */
	    port = ntohs(addr6->sin6_port);                 /* port */
	    string = inet_ntop(addr6->sin6_family, &addr6->sin6_addr, 
			       buffer, sizeof(buffer));
	} else {                                      /* else is an error */
	    printf("Address family error\n");
	    exit(1);
	}	
	printf("\tIndirizzo %s\n", string);
	printf("\tProtocollo %i\n", ptr->ai_protocol);
	printf("\tPorta %i\n", port);
	ptr = ptr->ai_next;
    }
    exit(0);
