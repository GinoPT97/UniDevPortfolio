/* SockUtil.c
 * 
 * Copyright (C) 2004 Simone Piccardi
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
/***************************************************************
 *
 * File SockUtil.c 
 * Routines for socket operations. 
 *
 * Define routines for socket handling 
 *
 * Author: S. Piccardi
 *
 ***************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <stdio.h>	 /* standard I/O library */
#include <unistd.h>      /* unix standard library */
#include <string.h>      /* C strings library */
#include <errno.h>       /* error definitions and routines */
#include <sys/socket.h>  /* socket constants, types and functions */
#include <arpa/inet.h>   /* IP addresses conversion utilities */
#include <netdb.h>       /* C resolver library */

#include "macros.h"
/**************************************************************************
 *
 * Routine ip_ntop
 *
 * Return a string with the numeric address translation of the content
 * of an addrinfo stucture
 *
 * Author: Simone Piccardi
 * Dec. 2004
 *
 **************************************************************************/
char *ip_ntop(struct addrinfo *addr, char *dst, socklen_t cnt)
{
//    char buffer[INET6_ADDRSTRLEN];
    char * ret;
    struct sockaddr_in *ip4;
    struct sockaddr_in6 *ip6;
    switch (addr->ai_family) {
    case PF_INET:
	ip4 = (struct sockaddr_in *) addr->ai_addr;
	ret = inet_ntop(ip4->sin_family, &ip4->sin_addr, dst, cnt);
	break;
    case PF_INET6:
	ip6 = (struct sockaddr_in6 *) addr->ai_addr;
	ret = inet_ntop(ip6->sin6_family, &ip6->sin6_addr, dst, cnt);
	break;
    default:
	ret = NULL;
	errno = EAFNOSUPPORT;
    }
    return ret;
}
/****************************************************************
 *
 * Routine sockconn
 * Return a connected socket given hostname, service, and socket type
 *
 * Author: Simone Piccardi
 * Dec. 2004
 *
 ****************************************************************/
int sockconn(char *host, char *serv, int prot, int type) 
{
    struct addrinfo hint, *addr, *save;
    int res;
    int sock;
    /* initialize hint structure */
    memset(&hint, 0, sizeof(struct addrinfo)); 
    hint.ai_family = PF_UNSPEC;            /* generic address (IPv4 or IPv6) */
    hint.ai_protocol = prot;               /* protocol */
    hint.ai_socktype = type;               /* socket type */
    res = getaddrinfo(host, serv, &hint, &addr);    /* calling getaddrinfo */
    if (res != 0) {                                 /* on error exit */
	fprintf(stderr, "sockconn: resolution failed:");
//	fprintf(stderr, "host %s, service %s, protocol %d", host, serv, prot);
	fprintf(stderr, " %s\n", gai_strerror(res));
	errno = 0;                         /* clear errno */
	return -1;
    }
    save = addr;
    while (addr != NULL) {                 /* loop on possible addresses */
	/* get a socket */
	sock = socket(addr->ai_family, addr->ai_socktype, addr->ai_protocol);
	if (sock < 0) {                    /* on error */
	    if (addr->ai_next != NULL) {   /* if other addresses */
		addr=addr->ai_next;        /* take next */
	        continue;                  /* restart cycle */
	    } else {                       /* else stop */
		perror("sockconn: cannot create socket");
		return sock;
	    }
	}
	/* connect the socket */
	if ( (res = connect(sock, addr->ai_addr, addr->ai_addrlen) < 0)) {
	    if (addr->ai_next != NULL) {   /* if other addresses */
		addr=addr->ai_next;        /* take next */
		close(sock);               /* close socket */
		continue;                  /* restart cycle */
	    } else {                       /* else stop */
		perror("sockconn: cannot connect");
		close(sock);
		return res;
	    }
	} else break;                      /* ok, we are connected! */
    }
    freeaddrinfo(save);                    /* done, release memory */
    return sock;
}
/****************************************************************
 *
 * Routine sockbind
 * Return a binded socket given hostname, service, and socket type
 *
 * Author: Simone Piccardi
 * Dec. 2004
 *
 ****************************************************************/
int sockbind(char *host, char *serv, int prot, int type) 
{
    struct addrinfo hint, *addr, *save;
    int res;
    int sock;
    char buf[INET6_ADDRSTRLEN];
    /* initialize hint structure */
    memset(&hint, 0, sizeof(struct addrinfo)); 
    hint.ai_flags = AI_PASSIVE;            /* address for binding */
    hint.ai_family = PF_UNSPEC;            /* generic address (IPv4 or IPv6) */
    hint.ai_protocol = prot;               /* protocol */
    hint.ai_socktype = type;               /* socket type */
    res = getaddrinfo(host, serv, &hint, &addr);   /* calling getaddrinfo */
    if (res != 0) {                                /* on error exit */
	fprintf(stderr, "sockbind: resolution failed:");
//	fprintf(stderr, "host %s, service %s, protocol %d", host, serv, prot);
	fprintf(stderr, " %s\n", gai_strerror(res));
	errno = 0;                         /* clear errno */
	return -1;
    }
    save = addr;                           /* saving for freeaddrinfo */
    while (addr != NULL) {                 /* loop on possible addresses */
	/* get a socket */
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
	/* connect the socket */
	printf("Indirizzo %s\n", ip_ntop(addr, buf, sizeof(buf)));
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
/****************************************************************
 *
 * Routine sockbindopt
 * Return a binded socket given hostname, service, and socket type
 * Issue a SO_REUSEADDR on the socket before binding on reuse value.
 *
 * Author: Simone Piccardi
 * Mar. 2005
 *
 ****************************************************************/
int sockbindopt(char *host, char *serv, int prot, int type, int reuse) 
{
    struct addrinfo hint, *addr, *save;
    int res;
    int sock;
    char buf[INET6_ADDRSTRLEN];
    /* initialize hint structure */
    memset(&hint, 0, sizeof(struct addrinfo)); 
    hint.ai_flags = AI_PASSIVE;            /* address for binding */
    hint.ai_family = PF_UNSPEC;            /* generic address (IPv4 or IPv6) */
    hint.ai_protocol = prot;               /* protocol */
    hint.ai_socktype = type;               /* socket type */
    res = getaddrinfo(host, serv, &hint, &addr);   /* calling getaddrinfo */
    if (res != 0) {                                /* on error exit */
	fprintf(stderr, "sockbind: resolution failed:");
//	fprintf(stderr, "host %s, service %s, protocol %d", host, serv, prot);
	fprintf(stderr, " %s\n", gai_strerror(res));
	errno = 0;                         /* clear errno */
	return -1;
    }
    save = addr;                           /* saving for freeaddrinfo */
    while (addr != NULL) {                 /* loop on possible addresses */
	/* get a socket */
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
	/* connect the socket */
	if (setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, 
		       &reuse, sizeof(reuse))) {
	    printf("error on socket options\n");
	    return -1;
	}
	printf("Indirizzo %s\n", ip_ntop(addr, buf, sizeof(buf)));
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
