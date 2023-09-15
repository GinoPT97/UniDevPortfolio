/* test_signalfd.c
 * 
 * Copyright (C) 2011 Simone Piccardi
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
 * Program test_signalfd.c: 
 * Program to test signalfd behaviour on fd close
 *
 * Author: Simone Piccardi
 * Jan. 2011
 *
 * Usage: test_signalfd -h give all info's
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
#include <signal.h>	 /* signal constants, types and functions */
#include <sys/signalfd.h>/* Linux signalfd interface */
#include <time.h>

#include "macros.h"
#include "Gapil.h"

/* Subroutines declaration */
void usage(void);
void die(char *);
void HandSIGINT(int signo);

int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int i, n, nomask=0, testwrite=0, sigfd;
    time_t t;
    struct signalfd_siginfo siginf;
    sigset_t sigmask;
    char buffer[] = "test write";
   /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hnw")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':   /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
	case 'n':   /* no mask option */
	    nomask = 1;
	    break;
	case 'w':   /* testwrite option */
	    testwrite = 1;
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
    Signal(SIGINT, HandSIGINT);
    if (nomask != 1) {
	sigemptyset(&sigmask);
	sigaddset(&sigmask, SIGINT);
	if (sigprocmask(SIG_BLOCK, &sigmask, NULL) == -1)   // block signals
	    die("Failing in singal masking");
    }
    if ((sigfd=signalfd(-1, &sigmask, SFD_NONBLOCK)) == -1) // take a signalfd
	die("Failing in signalfd");
    printf("Signalfd armed\n");
    if (testwrite) {
	if ( (n=write(sigfd, buffer, sizeof(buffer))) < 0)
	    perror("write on signal fd error");
	else
	    printf("write successfully %d bytes\n", n);	    
    }
    /* raise signal */
    if (raise(SIGINT) != 0)
	die("Failing in sending SIGINT");
    t = time(NULL);
    printf("Lanciato SIGINT, %s\n", ctime(&t));
    sleep(2);
    close(sigfd);
    if (sigprocmask(SIG_UNBLOCK, &sigmask, NULL) == -1)   // block signals
	die("Failing in signal unmasking");
    sleep(10);
    printf("ERROR:SHOULD BE END IN THE SIGNAL HANDLER\n");
    return 0;
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program testsignalfd : test signalfd on close \n");
    printf("Usage:\n");
    printf("  testsignalfd [-h] file mode \n");
    printf("  -h	   print this help\n");
    
    exit(1);
}
/*
 * Print error message and exit routine
 */
void die(char * mess) {
    perror(mess);
    exit(-1);
}
/*
 * Signal Handler to manage termination
 */
void HandSIGINT(int signo) {
    time_t t;
    debug("Terminated by %s\n", strsignal(signo));
    t = time(NULL);
    printf("Handler executed %s\n", ctime(&t));
    exit(0);
}
