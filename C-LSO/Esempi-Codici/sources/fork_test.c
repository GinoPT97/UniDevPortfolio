/* ForkTest.c
 * 
 * Copyright (C) 2001 Simone Piccardi
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
 * Program ForkTest.c: 
 * Program to test process creation
 *
 * Author: Simone Piccardi
 * Sep. 2001
 *
 * Usage: forktest -h give all info's
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <errno.h>       /* error definitions and routines */ 
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>	 /* standard I/O library */
#include <string.h>      /* C strings library */

#include "Gapil.h"
#include "macros.h"

/* Help printing routine */
void usage(void);

int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int nchild, i;
    pid_t pid;
    int wait_child  = 0;
    int wait_parent = 0;
    int wait_end    = 0;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hsp:c:e:")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':   /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
	case 'c':   /* take wait time for childen */
	    wait_child = strtol(optarg, NULL, 10);    /* convert input */
	    break;
	case 'p':   /* take wait time for childen */
	    wait_parent = strtol(optarg, NULL, 10);   /* convert input */
	    break;
	case 'e':   /* take wait before parent exit */
	    wait_end = strtol(optarg, NULL, 10);      /* convert input */
	    break;
	case 's':
	    Signal(SIGCHLD, HandSigCHLD);
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
    /* There must be remaing parameters */
    if (optind == argc) {
	usage();
    }
    nchild = atoi(argv[optind]);
    printf("Process %d: forking %d child\n", getpid(), nchild);
    /* loop to fork children */
    for (i=0; i<nchild; i++) {
	if ( (pid = fork()) < 0) { 
	    /* on error exit */ 
	    printf("Error on %d child creation, %s\n", i+1, strerror(errno));
	    exit(-1); 
	}
	if (pid == 0) {   /* child */
	    printf("Child %d successfully executing\n", ++i);
	    if (wait_child) sleep(wait_child);
	    printf("Child %d, parent %d, exiting\n", i, getppid());
	    exit(0);
	} else {          /* parent */
	    printf("Spawned %d child, pid %d \n", i+1, pid);
	    if (wait_parent) sleep(wait_parent);
	    printf("Go to next child \n");
	}
    }
    /* normal exit */
    if (wait_end) sleep(wait_end);
    return 0;
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program forktest: fork a given number of child \n");
    printf("Usage:\n");
    printf("  forktest [-h] [-p sec] [-c sec] [-e sec] child to fork \n");
    printf("  -h	   print this help\n");
    printf("  -s	   install signal handler\n");
    printf("  -p sec       wait sec seconds before next fork\n");
    printf("  -c sec       wait sec seconds before child termination\n");
    printf("  -e sec       wait sec seconds before parent return\n");
    
    exit(1);
}
