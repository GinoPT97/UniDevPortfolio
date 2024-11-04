/* mygethost.c
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
 * File mygethost.c: An example host command
 *
 * Author: S. Piccardi Jul. 2004
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <stdlib.h>      /* C standard library */
#include <stdio.h>       /* standard I/O library */
#include <unistd.h>      /* unix standard library */
#include <arpa/inet.h>   /* IP addresses conversion utilities */
#include <netdb.h>       /* C resolver library */
#include <netinet/tcp.h> /* TCP socket option */

extern int h_errno;

#include "Gapil.h"
/*
 * Program mygethost
 *
 * Use gethostbyname and print results
 */
/* Help printing routine */
void usage(void);

int main(int argc, char *argv[]) 
{
/* 
 * Variables definition
 */
    int i;
    struct hostent *data;

    struct sock_level {
	int level;
	char * name;
    } sock_level[] = {
	SOL_SOCKET, "SOL_SOCKET",
	SOL_IP,     "SOL_IP",
	SOL_TCP,    "SOL_TCP",
	SOL_IPV6,   "SOL_IPV6",
	SOL_ICMPV6, "SOL_ICMPV6"
    };

    char **alias;
    char *addr;
    char buffer[INET6_ADDRSTRLEN];
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "h")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':                                            /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
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
    if ((argc - optind) != 1) {
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    data = gethostbyname(argv[1]);
    if (data == NULL) {
	herror("Errore di risoluzione");
	exit(1);
    }
    printf("Canonical name %s\n", data->h_name);
    alias = data->h_aliases;
    while (*alias != NULL) {
	printf("Alias %s\n", *alias);
	alias++;
    }
    if (data->h_addrtype == AF_INET) {
	printf("Address are IPv4\n");
    } else if (data->h_addrtype == AF_INET6) {
	printf("Address are IPv6\n");
    } else {
	printf("Tipo di indirizzo non valido\n");
	exit(1);
    }
    alias = data->h_addr_list;
    while (*alias != NULL) {
	addr = inet_ntop(data->h_addrtype, *alias, buffer, sizeof(buffer));
	printf("Indirizzo %s\n", addr);
	alias++;
    }    
    exit(0);
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program mygethost: do an hostname resolution \n");
    printf("Usage:\n");
    printf("  mygethost [-h] hostname \n");
    printf("  -h   print this help\n");
    exit(1);
}
