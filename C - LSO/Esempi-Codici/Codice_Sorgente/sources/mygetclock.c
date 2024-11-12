/* mygetclock.c
 * 
 * Copyright (C) 2010 Simone Piccardi
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
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
 * File mygetclock.c: An example host command
 *
 * Author: S. Piccardi May. 2010
 *
 *****************************************************************************/
#include <stdlib.h>      /* C standard library */
#include <stdio.h>       /* standard I/O library */
#include <unistd.h>      /* unix standard library */
#include <time.h>        /* time library */


/*
 * Program mygetclock
 */
/* Help printing routine */
void usage(void);

int main(int argc, char *argv[]) 
{
/* 
 * Variables definition
 */
    int i;
    clockid_t clockid;
    pid_t pid;
    struct timespec time;
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
    
    pid = atoi(argv[optind]);
    if (pid > 0) {
	if (clock_getcpuclockid(pid, &clockid) != 0) {
	    perror("Cannot get clockid");
	    exit(1);
	}
    } else {
	clockid = CLOCK_PROCESS_CPUTIME_ID;
    }
    if (clock_gettime(clockid, &time) != 0) {
	perror("Cannot get time");
	exit(1);
    } else {
	printf("Tempo %s", ctime(&time.tv_sec));
    }
    exit(0);
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program mygetclock: prende un orologio \n");
    printf("Usage:\n");
    printf("  mygetclock [-h] PID \n");
    printf("  -h   print this help\n");
    exit(1);
}
