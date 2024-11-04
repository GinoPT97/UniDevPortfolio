int sockbind(char *host, char *serv, int prot, int type) 
{
    struct addrinfo hint, *addr, *save;
    int res;
    int sock;
    char buf[INET6_ADDRSTRLEN];
    memset(&hint, 0, sizeof(struct addrinfo)); 
    hint.ai_flags = AI_PASSIVE;            /* address for binding */
    hint.ai_family = PF_UNSPEC;            /* generic address (IPv4 or IPv6) */
    hint.ai_protocol = prot;               /* protocol */
    hint.ai_socktype = type;               /* socket type */
    res = getaddrinfo(host, serv, &hint, &addr);   /* calling getaddrinfo */
    if (res != 0) {                                /* on error exit */
	fprintf(stderr, "sockbind: resolution failed:");
	fprintf(stderr, " %s\n", gai_strerror(res));
	errno = 0;                         /* clear errno */
	return -1;
    }
    save = addr;                           /* saving for freeaddrinfo */
    while (addr != NULL) {                 /* loop on possible addresses */
	sock = socket(addr->ai_family, addr->ai_socktype, addr->ai_protocol);
	if (sock < 0) {                    /* on error */
	    if (addr->ai_next != NULL) {   /* if other addresses */
		addr=addr->ai_next;	   /* take next */
		continue;		   /* restart cycle */
	    } else {			   /* else stop */
		perror("sockbind: cannot create socket");
		return sock;
	    }
	}
	if ( (res = bind(sock, addr->ai_addr, addr->ai_addrlen)) < 0) {
	    if (addr->ai_next != NULL) {   /* if other addresses */
		addr=addr->ai_next;        /* take next */
		close(sock);		   /* close socket */
		continue;		   /* restart cycle */
	    } else {			   /* else stop */
		perror("sockbind: cannot connect");
		close(sock);
		return res;
	    }
	} else break;                      /* ok, we are binded! */
    }
    freeaddrinfo(save);                    /* done, release memory */
    return sock;
}
