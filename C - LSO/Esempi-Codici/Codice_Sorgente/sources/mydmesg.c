/* mydmesg.c
 * 
 * Copyright (C) 2011 Simone Piccardi
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
 * File mydmesg.c: An example for klogctl function
 *
 * Author: S. Piccardi Aug. 2011
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <stdlib.h>      /* C standard library */
#include <stdio.h>       /* standard I/O library */
#include <unistd.h>      /* unix standard library */
#include <sys/klog.h>


/*
 * Program mydmesg
 */
/* Help printing routine */
void usage(void);

int main(int argc, char *argv[]) 
{
/* 
 * Variables definition
 */
    int i, len, cmd=0, level=7, third;
    char * buffer;
    char * err_msg[]= {
	"This is a NOP so it should'nt happen",
	"This is a NOP so it should'nt happen",
	"Error on reading kernel log buffer last messages",
	"Error on reading kernel log buffer",
	"Error on reading kernel and clearing log buffer",
	"Error on clearing log buffer",
	"Error on setting console log level to minimum",
	"Error on setting console log level to default",
	"Error on setting console log level",
	"Error on reading unread message size",
	"Error on reading log buffer size"
    };
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "c:l:h")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':                                  /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
        case 'c':                                  /* klogctl command */
            cmd = strtol(optarg, NULL, 10);        /* convert input */
            break;
        case 'l':                                  /* log level */
            level = strtol(optarg, NULL, 10);      /* convert input */
            break;
	case '?':                                  /* unrecognized options */
	    printf("Unrecognized options -%c\n",optopt);
	    usage();
	default:                                   /* should not reached */
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
    if ((argc - optind) != 0) {   /* There must be 0 remaing arguments */
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    if ((cmd < 1) || (cmd>10)) {
	printf("You must give a value between 1 and 10 for the -c option\n");
	usage();
    }
    i = klogctl(10, NULL, 0);
    if ((buffer = malloc(i+1)) == NULL) {
	printf("Cannot allocate buffer for %d bytes\n", i);
	exit(1);
    } else {
	printf("Allocated %d bytes\n", i);
    }

    /* setting third argument */
    if (cmd == 8) third=level;
    else third=i;

    if ((len = klogctl(cmd, buffer, third)) < 0) {
	    perror(err_msg[cmd]);
	    exit(0);		      
    }

    switch (cmd) 
    {
    case 2:
    case 3:
    case 4:
	printf("Debug, cmd = %d\n", cmd);
	printf("Debug, len = %d\n", len);
	buffer[len]=0;
	printf("%s", buffer);
	exit(0);
    case 5:
    case 6:
    case 7:
    case 8:
	printf("Operation %d executed\n", cmd);
	exit(0);
    case 9:
	printf("Kernel log buffer ring has %d bytes\n", len);
	exit(0);
    case 10:
	printf("Kernel log buffer ring is %d bytes\n", len);
	exit(0);
    }


    exit(0);
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program mydmesg: klogctl testing \n");
    printf("Usage:\n");
    printf(" mydmesg [-h] [-l N] -c command \n");
    printf("  -h   print this help\n");
    printf("  -c   klogctl command (mandatory)\n");
    printf("  -l   level\n");
    exit(1);
}
