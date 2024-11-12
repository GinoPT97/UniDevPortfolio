/* UDP_echo_first.c
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
/****************************************************************
 *
 * Program UDP_echo_first.c
 * Simple UDP client for echo service (port 7)
 * first version, no connected UDP socket
 *
 * Author: Simone Piccardi
 * May 2004
 *
 * Usage: echo -h give all info's
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <sys/types.h>   /* primitive system data types */
#include <unistd.h>      /* unix standard library */
#include <arpa/inet.h>   /* IP addresses conversion utilities */
#include <sys/socket.h>  /* socket constants, types and functions */
#include <stdio.h>	 /* standard I/O library */
#include <errno.h>	 /* error definitions and routines */
#include <string.h>	 /* C strings library */
#include <stdlib.h>	 /* C standard library */

#include "macros.h"

#define MAXLINE 256
void usage(void);
void ClientEcho(FILE * filein, int socket, struct sockaddr_in *serv_add);
void SigTERM_hand(int sig);

/* Program begin */
int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int sock, i;
    struct sockaddr_in serv_add;
    /*
     * Input section: decode parameters passed in the calling 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "h")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':  
	    printf("Wrong -h option use\n");
	    usage();
	    return(1);
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
    /* create socket */
    if ( (sock = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
	perror("Socket creation error");
	return 1;
    }
    /* initialize address */
    memset((void *) &serv_add, 0, sizeof(serv_add)); /* clear server address */
    serv_add.sin_family = AF_INET;                   /* address type is INET */
    serv_add.sin_port = htons(7);                    /* echo port is 7 */
    /* build address using inet_pton */
    if ( (inet_pton(AF_INET, argv[optind], &serv_add.sin_addr)) <= 0) {
	perror("Address creation error");
	return 1;
    }
    /* do read/write operations */
    ClientEcho(stdin, sock, &serv_add);
    /* normal exit */
    return 0;
}
/*
 * routine to print usage info and exit 
 */
void usage(void) {
    printf("Take daytime from a remote host \n");
    printf("Usage:\n");
    printf("  daytime [-h] [-v] [host in dotted decimal form] \n");
//    printf("  -v	   set verbosity on\n");
    printf("  -r	   require reset on closing\n");
    printf("  -h	   print this help\n");
    exit(1);
}

void ClientEcho(FILE * filein, int socket, struct sockaddr_in * serv_addr) 
{
    char sendbuff[MAXLINE+1], recvbuff[MAXLINE+1];
    int nread, nwrite; 
    /* initialize file descriptor set */
    while (1) {
	if (fgets(sendbuff, MAXLINE, filein) == NULL) {
	    return;                /* if no input just return */
	} else {                   /* else we have to write to socket */
	    nwrite = sendto(socket, sendbuff, strlen(sendbuff), 0,
			    (struct sockaddr *) serv_addr, sizeof(*serv_addr));
	    if (nwrite < 0) {      /* on error stop */
		printf("Errore in scrittura: %s", strerror(errno));
		return;
	    }
	}
	nread = recvfrom(socket, recvbuff, strlen(sendbuff), 0, NULL, NULL); 
	if (nread < 0) {  /* error condition, stop client */
	    printf("Errore in lettura: %s\n", strerror(errno));
	    return;
	}
	recvbuff[nread] = 0;   /* else read is ok, write on stdout */
	if (fputs(recvbuff, stdout) == EOF) {
	    perror("Errore in scrittura su terminale");
	    return;
	}
    }
}
