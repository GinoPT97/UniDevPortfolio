/* FortuneClient.c
 * 
 * Copyright (C) 2002 Simone Piccardi
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
 * Program fortune 
 * Fortune client
 *
 * Author: Simone Piccardi
 * Aug. 2002
 *
 * Usage: fortune -h give all info
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <sys/types.h>   /* primitive system data types */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>	 /* standard I/O library */
#include <stdlib.h>	 /* C standard library */
#include <errno.h>       /* error definitions and routines */
#include <fcntl.h>       /* file control functions */
#include <string.h>	 /* C strings library */
#include <limits.h>      /* system limits constants, types and functions */
#include <sys/stat.h>

#include "macros.h"

/* Subroutines declaration */
void usage(void);

int main(int argc, char *argv[])
{
/* Variables definition */
    int n = 0;
    char *fortunefilename = "/tmp/fortune.fifo";
    char line[80];
    int fifo_server, fifo_client;
    char fifoname[80];
    int nread;
    char buffer[PIPE_BUF];
    /*
     * Input section: decode parameters passed in the calling 
     * Use getopt function
     */
    int i;
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hf:")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':  
	    printf("Wrong -h option use\n");
	    usage();
	    return(0);
	    break;
	case 'f':
	    fortunefilename = optarg;
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
    snprintf(fifoname, 80, "/tmp/fortune.%d", getpid());  /* compose name */
    if (mkfifo(fifoname, 0622)) {  /* open client fifo */
	if (errno!=EEXIST) {
	    perror("Cannot create well known fifo");
	    exit(-1);
	}
    }
    fifo_server = open(fortunefilename, O_WRONLY); /* open server fifo */
    if (fifo_server < 0) {
	perror("Cannot open well known fifo");
	exit(-1);
    }
    debug("%s\n", fifoname);
    nread = write(fifo_server, fifoname, strlen(fifoname)+1);  /* write name */
    close(fifo_server);                        /* close server fifo */
    fifo_client = open(fifoname, O_RDONLY);    /* open client fifo */
    if (fifo_client < 0) {
	perror("Cannot open well known fifo");
	exit(-1);
    }
    nread = read(fifo_client, buffer, sizeof(buffer)); /* read answer */
    printf("%s", buffer);                              /* print fortune */
    close(fifo_client);                                /* close client */
    unlink(fifoname);                                  /* remove client fifo */
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Elementary fortune client\n");
    printf("Usage:\n");
    printf("  fortune [-h] \n");
    printf("  -h   print this help\n");
    exit(1);
}
