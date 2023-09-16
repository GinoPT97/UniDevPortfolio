/* getparam.c
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
 * Program getparam.c: 
 * Program to read system parameters
 *
 * Author: Simone Piccardi
 * Jan. 2002
 *
 * Usage: getparam -h give all info's
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#define _GNU_SOURCE
#include <errno.h>       /* error definitions and routines */ 
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <string.h>      /* C strings library */
#include <limits.h>      /* system limits constants, types and functions */
#include <stdio.h>	 /* standard I/O library */
#include <time.h>        /* date and time constants, types and functions */


/* Table of constants for sysconf() */
char *sc_names[]={"_SC_ARG_MAX",
		  "_SC_CHILD_MAX",
		  "_SC_OPEN_MAX",
		  "_SC_STREAM_MAX",
		  "_SC_TZNAME_MAX",
		  "_SC_NGROUPS_MAX",
		  "_SC_SSIZE_MAX",
		  "_SC_CLK_TCK",
		  "_SC_JOB_CONTROL",
		  "_SC_SAVED_IDS",
		  "_SC_VERSION"};

int sc_argument[]={_SC_ARG_MAX,
		   _SC_CHILD_MAX,
		   _SC_OPEN_MAX,
		   _SC_STREAM_MAX,
		   _SC_TZNAME_MAX,
		   _SC_NGROUPS_MAX,
		   _SC_SSIZE_MAX,
		   _SC_CLK_TCK,
		   _SC_JOB_CONTROL,
		   _SC_SAVED_IDS,
		   _SC_VERSION};

/* 
   Set the defined[] array to true if the macro is defined else set it
   to false and define the macro to -1 as a marker
*/
int defined[]={
#ifdef  ARG_MAX
    1,
#else
#define ARG_MAX               -1  
    0,
#endif
#ifdef  CHILD_MAX
    1,
#else
#define CHILD_MAX             -1
    0,
#endif
#ifdef  OPEN_MAX
    1,
#else
#define OPEN_MAX              -1 
    0,
#endif
#ifdef  STREAM_MAX
    1,
#else
#define STREAM_MAX            -1
    0,
#endif
#ifdef  NGROUPS_MAX
    1,
#else
#define NGROUPS_MAX           -1
    0,
#endif
#ifdef  TZNAME_MAX
    1,
#else
#define TZNAME_MAX            -1
    0,
#endif
#ifdef  SSIZE_MAX
    1,
#else
#define SSIZE_MAX             -1
    0
#endif
};



/* values of stadard macros */
long values[]={ARG_MAX, 
	       CHILD_MAX,
	       OPEN_MAX,
	       STREAM_MAX,
	       TZNAME_MAX,
	       NGROUPS_MAX,
	       SSIZE_MAX,
	       CLOCKS_PER_SEC,
	       _POSIX_JOB_CONTROL,
	       _POSIX_SAVED_IDS,
	       _POSIX_VERSION};

/* Help printing routine */
void usage(void);

int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int i;
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
	case 'h':   /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
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
    if ( (argc-optind) != 1 )  {
	printf("From %d arguments, removed %d options\n", argc, optind);
	usage();
    }
    for (i=0; i<=4; i++) {
	printf("Response for %s is %ld, values is %ld\n",sc_names[i], 
	       sysconf(sc_argument[i]), values[i]);
    }
    return 0;
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program getparam : test fopen for a file  \n");
    printf("Usage:\n");
    printf("  getparam [-h] parameter \n");
    printf("  -h	   print this help\n");
    
    exit(1);
}

