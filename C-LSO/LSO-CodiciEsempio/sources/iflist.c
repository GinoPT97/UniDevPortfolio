/* iflist.c
 * 
 * Copyright (C) 2006 Simone Piccardi
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
 * File ifist.c: to get a list of network interface and their addresses
 *
 * Author: S. Piccardi Sep. 2006
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <stdlib.h>      /* C standard library */
#include <stdio.h>       /* standard I/O library */
#include <unistd.h>      /* unix standard library */
#include <sys/socket.h>  /* socket constants, types and functions */
#include <arpa/inet.h>   /* IP addresses conversion utilities */
#include <sys/ioctl.h>   /* ioctl syscall and constants */
#include <net/if.h>      /* network interfaces constants and types */

/*
 * Program iflist
 *
 * Use ioctl to get a list of network interfaces and address
 */

/* Help printing routine */
void usage(void);

int main(int argc, char *argv[]) 
{
/* 
 * Variables definition
 */
    int i, num, ret, sock;
    struct ifconf iflist;
    char buffer[4096];
    struct sockaddr_in * address;
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
    if ((argc - optind) != 0) {
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    /* create a socket for the operation */
    sock = socket(PF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("Socket creation error");
        return 1;
    }
    /* init values for the ifcon structure and do SIOCGIFCONF */
    iflist.ifc_len = sizeof(buffer);
    iflist.ifc_buf = buffer;
    ret = ioctl(sock, SIOCGIFCONF, &iflist);
    if (ret < 0) {
	perror("ioctl failed");
        return 1;
    }
    /* check that we have all data */
    if (iflist.ifc_len == sizeof(buffer)) {
	printf("Probable overflow, too many interfaces, cannot read\n");
	return 1;
    } else {
	num = iflist.ifc_len/sizeof(struct ifreq);
	printf("Found %i interfaces \n", num);
    }
    /* loop on interface to write data */
    for (i=0; i < num; i++) {
	address = (struct sockaddr_in *) &iflist.ifc_req[i].ifr_addr;
	printf("Interface %s, address %s\n", iflist.ifc_req[i].ifr_name, 
	       inet_ntoa(address->sin_addr));
    }

    return 0;
}

/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program iflist: list active network interface with address \n");
    printf("Usage:\n");
    printf("  iflist [-h] \n");
    printf("  -h   print this help\n");
    exit(1);
}
