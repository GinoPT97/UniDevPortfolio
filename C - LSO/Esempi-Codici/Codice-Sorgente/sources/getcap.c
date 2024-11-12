/* getcap.c
 * 
 * Copyright (C) 2006-2012 Simone Piccardi
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
 * Program getcap.c: 
 * Program to test capabilities functions
 *
 * Author: Simone Piccardi
 * Mar. 2006
 *
 * Usage: getcap -h give all info's
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
#include <sys/capability.h> /* need package libcap-dev */

/* Help printing routine */
void usage(void);

int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int i;
    pid_t pid = 0;
    cap_t capab;
    char *string;
    int res;

    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hp:")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':   /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
	case 'p':   /* specify pid */
	    pid = strtol(optarg, NULL, 10);
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
    if ( (argc-optind) != 0)  {
        printf("From %d arguments, removed %d options\n", argc, optind);
        usage();
    }
    
    if (!pid) {
	capab = cap_get_proc();
	if (capab == NULL) {
	    perror("cannot get current process capabilities");
	    return 1;
	}
    } else {
	capab = cap_get_pid(pid);
	if (capab == NULL) {
	    perror("cannot get process capabilities");
	    return 1;
	}
    }

    string = cap_to_text(capab, NULL);
    printf("Capability: %s\n", string);

    cap_free(capab);
    cap_free(string);
    return 0;
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program getcap: print current process capabilities \n");
    printf("Usage:\n");
    printf("  getcap [-h] [-p pid] \n");
    printf("  -h	   print this help\n");
    printf("  -p PID	   print process PID capabilities\n");
    
    exit(1);
}


