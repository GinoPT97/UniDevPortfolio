/* gethost.c
 * 
 * Copyright (C) 2005 Simone Piccardi
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
/****************************************************************
 *
 * Program gethost.c: 
 * Program to test gethostbyname and getaddrinfo
 *
 * Author: Simone Piccardi
 * Aug. 2002
 *
 * Usage: gethost -h give all info's
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#define _GNU_SOURCE
#include <errno.h>       /* error definitions and routines */ 
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>	 /* standard I/O library */
#include <string.h>      /* C strings library */
#include <sys/types.h>   /* primitive system data types */
#include <sys/socket.h>  /* socket constants, types and functions */
#include <netdb.h>       /* C resolver library */


/* 
 * Function and globals definitions
 */
void usage(void);  /* Help printing routine */

/*
 * Main program
 */
int main(int argc, char *argv[])
{
    /* 
     * Variables definition  
     */
    int i;
    int use = 0;
    struct hostent *host;
    struct in_addr addr;
    struct addrinfo hint;
    struct addrinfo *address;
    struct sockaddr_in *socket;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "han")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':   /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
	case 'a':   /* help option */
	    use = 1;
	    break;
	case 'n':   /* help option */
	    use = 0;
	    break;
	case '?':   /* unrecognized options */
	    printf("Unrecognized options -%c\n",optopt);
	    usage();
	default:    /* should not reached */
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
    if ((argc-optind)!= 1)  {
        printf("From %d arguments, removed %d options\n", argc, optind);
        usage();
    }
    if (use == 0) {
	host = gethostbyname(argv[optind]);
	printf("gethostbyname %s\n", argv[optind]);
	printf("Official host name %s\n", host->h_name);
	printf("Address Type %d\n", host->h_addrtype);
	printf("Address Lenght %d\n", host->h_length);
	addr.s_addr = *( (unsigned long *)host->h_addr);
	printf("Address  %s\n", inet_ntoa(addr));
    } else {
/* 	host = getipnodebyname(argv[optind], AF_INET, 0, &i); */
/* 	if (!host) printf("Error\n"); */
/* 	printf("getipnodebyname %s, error %d\n", argv[optind], i); */
/* 	printf("Official host name %s\n", host->h_name); */
/* 	printf("Address Type %d\n", host->h_addrtype); */
/* 	printf("Address Lenght %d\n", host->h_length); */
/* 	addr.s_addr = *( (unsigned long *)host->h_addr); */
/* 	printf("Address  %s\n", inet_ntoa(addr)); */
	hint.ai_flags = 0;
	hint.ai_family = PF_INET;
	hint.ai_socktype = 0;
	hint.ai_protocol = 0;
	hint.ai_addrlen = 0;
	hint.ai_addr = NULL;
	hint.ai_canonname = NULL;
	hint.ai_next = NULL;
	if (i = getaddrinfo(argv[optind], "telnet", &hint, &address)) {
	    printf("getaddrinfo %s = %s \n", argv[optind], gai_strerror(i));
	} else {
	    printf("Address flag %d\n", address->ai_flags);
	    printf("Address family %d\n", address->ai_family);
	    printf("Address socket type %d\n", address->ai_socktype);
	    printf("Address protocol %d\n", address->ai_protocol);
	    printf("Address lenght %d\n", address->ai_addrlen);
	    printf("Canonical name %s\n", address->ai_canonname);
	    socket = (struct sockaddr_in *) address->ai_addr;
	    printf("Address %s\n", inet_ntoa(socket->sin_addr));
	    printf("Port %d\n", htons(socket->sin_port));
	    printf("Family %d\n", socket->sin_family);
	    
	}
    }
    return 0;
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program gehost: test host name functions  \n");
    printf("Usage:\n");
    printf("  gethost [-h] address \n");
    printf("  -h	   print this help\n");
    printf("  -a	   use getaddrinfo\n");
    printf("  -n	   use gethostbyname\n");
    
    exit(1);
}

