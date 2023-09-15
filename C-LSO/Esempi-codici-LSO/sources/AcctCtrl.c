/* AcctCtrl.c
 * 
 * Copyright (C) 2005 Simone Piccardi
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
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <dirent.h>      /* directory operation constants and functions */
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>	 /* standard I/O library */

/*
 * Program AcctCtrl
 *
 * Enable and disable BSD accounting
 */
/* Help printing routine */
void usage(void);

int main(int argc, char *argv[]) 
{
    int i;
    int enable = 0;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hed")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':                            /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
	case 'e':                            /* enable option */
	    enable = 1;
	    break;
	case 'd':                            /* disable option */
	    enable = 0;
	    break;
	case '?':                            /* unrecognized options */
	    printf("Unrecognized options -%c\n",optopt);
	    usage();
	default:                             /* should not reached */
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
    if (i == -1) {
	printf("Specify option\n");
	usage();
    }
    if (enable) {
	if ((argc - optind) != 1) {      /* There must be remaing parameters */
	    printf("Wrong number of arguments %d\n", argc - optind);
	    usage();
	}
	i = acct(argv[optind]);
    } else {
	i = acct(NULL);
    }
    if (i) 
	perror("Operazione Fallita");

    exit(0);
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program AcctCtrl: enable/disable BSD accounting \n");
    printf("Usage:\n");
    printf("  acctctrl [-hed] filename \n");
    printf("  -h	   print this help\n");
    printf("  -e	   enable BSD accounting\n");
    printf("  -d	   disable BSD accounting\n");
    exit(1);
}
