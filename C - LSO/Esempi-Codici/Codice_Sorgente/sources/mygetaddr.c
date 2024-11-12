/* mygetaddr.c
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
/*****************************************************************************
 *
 * File mygetaddr.c: An example for getaddrinfo program
 *
 * Author: S. Piccardi Nov. 2004
 *
 *****************************************************************************/
#include <string.h>      /* C strings library */
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <unistd.h>      /* unix standard library */
#include <stdlib.h>      /* C standard library */
#include <stdio.h>       /* standard I/O library */
#include <arpa/inet.h>   /* IP addresses conversion utilities */
#include <sys/socket.h>  /* socket constants, types and functions */
#include <netinet/in.h>  /* IPv4 and IPv6 constants and types */
#include <netdb.h>       /* C resolver library */

#include "Gapil.h"
/*
 * Program mygetaddr
 *
 * Use getaddrinfo and print results
 */
/* Help printing routine */
void usage(void);

int main(int argc, char *argv[]) 
{
/* 
 * Variables definition
 */
    int i,j;
    struct addrinfo hint;
    struct addrinfo *res, *ptr;
    int ret, port;
    struct sockaddr_in *addr;
    struct sockaddr_in6 *addr6;
    char *string;
    char buffer[INET6_ADDRSTRLEN];
    char *protocols[] = { "tcp","udp", NULL };
    int protval[] = { 6, 17 };
    char *socktype[] = { "dgram","stream", NULL };
    int sockval[] = { SOCK_DGRAM, SOCK_STREAM };
    int debug = 0;
    /*
     * Init variables
     */
    memset(&hint, 0, sizeof(hint));
    hint.ai_family = AF_UNSPEC;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hdcp:t:v:")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':                                             /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    break;
	case 'c':                      /* set canonical host name resolution */
	    hint.ai_flags = hint.ai_flags | AI_CANONNAME;
	    break;
	case 'd':                      /* set canonical host name resolution */
	    debug = 1;
	    break;
	case 'p':                                      /* set protocol value */
	    j = 0;
	    while ( (string = protocols[j]) != NULL ) {
		if ( (strncmp(string, optarg, strlen(string)) == 0) ) {
		    hint.ai_protocol = protval[j];
		    break;
		}
		j++;
	    }
	    if (j>=2) {
		printf("Wrong protocol, use 'tcp' or 'udp'\n\n");
		usage();
	    }
	    break;
	case 't':                                   /* set socket type value */
	    j = 0;
	    while ( (string = socktype[j]) != NULL ) {
		if ( (strncmp(string, optarg, strlen(string)) == 0) ) {
		    hint.ai_socktype = sockval[j];
		    break;
		}
		j++;
	    }		
	    if (j>=2) {
		printf("Wrong socket type, use 'dgram' or 'stream'\n\n");
		usage();
	    }
	    break;
	case 'v':                                        /* set address type */
	    j = strtol(optarg, NULL, 10);
	    if (j == 4) {
		hint.ai_family = AF_INET;
		break;
	    } 
	    if (j == 6) {
		hint.ai_family = AF_INET6;
		break;
	    }
	    printf("Wrong IP protocol version, use 4 o 6\n\n");
	    usage();
	    break;
	case '?':                                    /* unrecognized options */
	    printf("Unrecognized options -%c\n",optopt);
	    usage();
	default:                                       /* should not reached */
	    usage();
	}
    }
    /* ***********************************************************
     * 
     *		 Options processing completed
     *
     *		      Main code beginning
     * 
     * ***********************************************************/
    /* if debug actived printout hint values*/
    if (debug) {
	printf("hint.ai_flag     = %d\n", hint.ai_flags);
	printf("hint.ai_family   = %d\n", hint.ai_family);
	printf("hint.ai_socktype = %d\n", hint.ai_socktype);
	printf("hint.ai_protocol = %d\n", hint.ai_protocol);
	printf("address = %s\n", argv[optind]);
	printf("port    = %s\n", argv[optind+1]);
    }
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
	    addr = (struct sockaddr_in *) ptr->ai_addr;          /* address */
	    port = ntohs(addr->sin_port);                        /* port */
	    string = inet_ntop(addr->sin_family, &addr->sin_addr, 
			       buffer, sizeof(buffer));
	} else if (ptr->ai_family == AF_INET6) {      /* if IPv6 */
	    printf("IPv6 address: \n");
	    addr6 = (struct sockaddr_in6 *) ptr->ai_addr;        /* address */
	    port = ntohs(addr6->sin6_port);                      /* port */
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
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program mygetaddr: do an hostname resolution \n");
    printf("Usage:\n");
    printf("mygetaddr [-h] [-p protocol] [-t socktype] hostname service\n");
    printf("  -h              print this help\n");
    printf("  -p udp,tcp      select a protocol\n");
    printf("  -t dgram,stream select a socket type\n");
    printf("  -v 4,6          select IPv4 or IPv6 \n");
    printf("  -c              require canonical name resolution\n");
    exit(1);
}
