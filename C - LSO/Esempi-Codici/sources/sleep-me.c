/* sleep-me.c
 * 
 * Copyright (C) 2012 Simone Piccardi
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
 * File sleep-me.c: An example on how NOT to wait for a time...
 * thanks to M.E. for suggesting it (for serious) some time ago in the
 * FLUG (Firenze Linux User Group) mailing list
 *
 * Author: S. Piccardi Jan. 2012
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>
#include <time.h>
#include <stdlib.h>

/*
 * Program sleep-me.c
 *
 * Wait for N second (burning CPU...)
 */
/* Help printing routine */
void usage(void);
/* computation function for dir_scan */
int main(int argc, char *argv[]) 
{
    time_t now, end;
    int i, wait=0;
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
    if ((argc - optind) != 1) {          /* There must be remaing parameters */
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    printf("I will burn CPU for %s seconds\n", argv[1]);
    wait = atoi(argv[1]);
    printf("wait %d sec.\n", wait);
    if (wait != 0) {
	end = time(NULL)+ wait;
    } else {
	usage();
    }
    printf("now %d, end %d sec.\n",now, end);
    now = time(NULL);
    while (now < end) { 
	now = time(NULL);
    }
    exit(0);
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program sleep-me: wait burning CPU cicles \n");
    printf("Usage:\n");
    printf("  sleep-me [-h] -t sec \n");
    printf("  -t sec	   wait sec seconds\n");
    printf("  -h	   print this help\n");
    exit(1);
}
