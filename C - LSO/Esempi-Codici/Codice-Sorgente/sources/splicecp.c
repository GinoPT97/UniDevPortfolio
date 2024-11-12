/* splicecp.c
 * 
 * Copyright (C) 2007 Simone Piccardi
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
 * Program splicecp.c: 
 * A sample program that copy a file using splice
 *
 * Author: Simone Piccardi
 * Aug. 2007
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#define _GNU_SOURCE
#include <fcntl.h>       /* file control functions */
#include <unistd.h>      /* unix standard library */
#include <stdlib.h>      /* C standard library */
#include <errno.h>       /* error definitions and routines */
#include <stdio.h>       /* standard I/O library */
#include <string.h>      /* C strings library */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <sys/types.h>   /* primitive system data types */

#include "macros.h"
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
    int size = 4096;
    int pipefd[2];
    int in_fd, out_fd;
    int nread, nwrite;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;  /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hs:")) != -1) {
        switch (i) {
        /* 
         * Handling options 
         */ 
        case 'h':      /* help option */
            printf("Wrong -h option use\n");
            usage();
            return -1;
            break;
        case 's':      /* take wait time for childen */
            size = strtol(optarg, NULL, 10);    /* convert input */
            break;
	case '?':      /* unrecognized options */
            printf("Unrecognized options -%c\n", optopt);
            usage();
        default:       /* should not reached */
            usage();
        }
    }
   /*
    * Main body
    */
    if ((argc - optind) != 2) { /* There must two argument */
        printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    /* open pipe, input and output file */
    in_fd = open(argv[optind], O_RDONLY);
    if (in_fd < 0) {
	printf("Input error %s on %s\n", strerror(errno), argv[optind]);
	exit(EXIT_FAILURE); 
    }
    out_fd = open(argv[optind+1], O_CREAT|O_RDWR|O_TRUNC, 0644);
    if (out_fd < 0) {
	printf("Cannot open %s, error %s\n", argv[optind+1], strerror(errno));
	exit(EXIT_FAILURE); 
    }
    if (pipe(pipefd) == -1) {
	perror("Cannot create buffer pipe"); 
	exit(EXIT_FAILURE); 
    }
    /* copy loop */
    debug("Size %d\n", size);
    while (1) {
	nread = splice(in_fd, NULL, pipefd[1], NULL, size, 
		       SPLICE_F_MOVE|SPLICE_F_MORE);
	debug("read %d bytes\n", nread);
	if (nread == 0)	break;
	if (nread < 0) {
	    if (errno == EINTR) {
		continue;
	    } else {
		perror("read error");
		exit(EXIT_FAILURE);
	    } 
	}
	while (nread > 0) {
	    nwrite = splice(pipefd[0], NULL, out_fd, NULL, nread, 
			    SPLICE_F_MOVE|SPLICE_F_MORE);
	    debug("write %d bytes\n", nwrite);
	    if (nwrite < 0) {
		if (errno == EINTR)
		    continue;
		else {
		    perror("write error");
		    exit(EXIT_FAILURE);
		}
	    }
	    nread -= nwrite;
	    debug("left %d bytes\n", nread);
	}
    }
    return EXIT_SUCCESS;
}
/*
 * routine to print usage info and exit 
 */
void usage(void) {
    printf("Program splicecp: copy two file using splice syscall\n");
    printf("Usage:\n");
    printf("  splicecp [-h] [-s N] filesrc filedst \n");
    printf("  -h           print this help\n");
    printf("  -s N         set a buffer size of N bytes \n");
    exit(1);
}
