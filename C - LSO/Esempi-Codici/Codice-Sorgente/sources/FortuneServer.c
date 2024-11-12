/* FortuneServer.c
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
 * Program fortuned 
 * Fortune server
 *
 * Author: Simone Piccardi
 * Aug. 2002
 *
 * Usage: fortuned -h give all info
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>       /* standard I/O library */
#include <stdlib.h>	 /* C standard library */
#include <string.h>	 /* C strings library */
#include <errno.h>	 /* error definitions and routines */
#include <signal.h>	 /* signal constants, types and functions */
#include <fcntl.h>	 /* file control functions */

#include "macros.h"
#include "Gapil.h"

/* Subroutines declaration */
void usage(void);
void HandSIGTERM(int signo);
int FortuneParse(char *file, char **fortune, int n);

/* name of well known fifo */
char *fifoname = "/tmp/fortune.fifo";
int main(int argc, char *argv[])
{
/* Variables definition */
    int i, n = 0;
    char *fortunefilename = "/usr/share/games/fortunes/linux";
    char **fortune;
    char line[80];
    int fifo_server, fifo_client;
    int nread;
    /*
     * Input section: decode parameters passed in the calling 
     * Use getopt function
     */
    opterr = 0;	                             /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hn:f:")) != -1) {
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
	case 'n':
	    n = strtol(optarg, NULL, 10);
	    fortune = (char **) calloc(sizeof(*fortune), n);
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
    if (n==0) usage();          /* if no pool depth exit printing usage info */
    Signal(SIGTERM, HandSIGTERM);            /* set handlers for termination */
    Signal(SIGINT, HandSIGTERM);
    Signal(SIGQUIT, HandSIGTERM);
    i = FortuneParse(fortunefilename, fortune, n);          /* parse phrases */
    for (n=0; n<i; n++) debug("%s%%\n", fortune[n]);
    /* 
     * Comunication section 
     */
    if (mkfifo(fifoname, 0622)) {  /* create well known fifo if does't exist */
	if (errno!=EEXIST) {
	    perror("Cannot create well known fifo");
	    exit(1);
	}
    }
    daemon(0, 0);
    /* open fifo two times to avoid EOF */
    fifo_server = open(fifoname, O_RDONLY);
    if (fifo_server < 0) {
	perror("Cannot open read only well known fifo");
	exit(1);
    }
    if (open(fifoname, O_WRONLY) < 0) {
	perror("Cannot open write only well known fifo");
	exit(1);
    }
    /* Main body: loop over requests */
    while (1) {
	nread = read(fifo_server, line, 79);                 /* read request */
	if (nread < 0) {
	    perror("Read Error");
	    exit(1);
	}
	line[nread] = 0;                       /* terminate fifo name string */
	debug("%s %d\n", line,nread);
	n = random() % i;                             /* select random value */
	debug("fortune[%d]=%s\n", n, fortune[n]);
	fifo_client = open(line, O_WRONLY);              /* open client fifo */
	if (fifo_client < 0) {
	    debug("Client fifo is %s\n", line);
	    perror("Cannot open");
	    exit(1);
	}
	nread = write(fifo_client,                           /* write phrase */
		      fortune[n], strlen(fortune[n])+1);
	close(fifo_client);                             /* close client fifo */
    }
    debug("Exiting for unknown reasons\n");
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Elementary fortune server\n");
    printf("Usage:\n");
    printf("  fortuned [-h] [-f] -n XXX \n");
    printf("  -h   print this help\n");
    printf("  -f filename   set file for fortunes\n");
    printf("  -n XXX        set pool depth\n");
    exit(1);
}
/*
 * Signal Handler to manage termination
 */
void HandSIGTERM(int signo) {
    debug("Terminated by %s\n", strsignal(signo));
    unlink(fifoname);
    exit(0);
}
