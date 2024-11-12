/* test_timerfdfork.c
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
 * Program test_timerfdfork.c: 
 * Program to test timerfd behaviour across fork's
 *
 * Author: Simone Piccardi
 * Apr. 2011
 *
 * Usage: testtimerfdfork -h give all info's
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
#include <sys/timerfd.h> /* timerfd */
#include <sys/epoll.h>   /* Linux epoll interface */
#include <time.h>

#include "macros.h"
#include "Gapil.h"

/* Help printing routine */
void usage(void);
void die(char *);

#define MAX_EPOLL_EV 10

int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int i, n, nread, fd, epfd;
    int wait=5, interval=1, nswait=0, nsinter=0;   // timer default
    pid_t pid;
    struct epoll_event epev, events[MAX_EPOLL_EV];
    struct itimerspec expiring;
    uint64_t expired;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "ht:i:w:n:")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':   /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
        case 'i':      /* timer interval */
            interval = strtol(optarg, NULL, 10);    /* convert input */
            break;
        case 't':      /* timer expiring */
            wait = strtol(optarg, NULL, 10);    /* convert input */
            break;
        case 'n':      /* timer interval in ns */
            nsinter = strtol(optarg, NULL, 10);    /* convert input */
            break;
        case 'w':      /* timer expiring in ns */
            nswait = strtol(optarg, NULL, 10);    /* convert input */
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
    /* There must be 0 remaing arguments */
    if ( (argc-optind) != 0 )  {
	printf("From %d arguments, removed %d options\n", argc, optind);
	usage();
    }
    /* timerfd setup */
    fd = timerfd_create(CLOCK_MONOTONIC, TFD_NONBLOCK);
    expiring.it_interval.tv_sec=interval;
    expiring.it_interval.tv_nsec=nsinter;
    expiring.it_value.tv_sec=wait;
    expiring.it_value.tv_nsec=nswait;
    if (timerfd_settime(fd, 0, &expiring, NULL)) {
	die("Cannot set timer");
    }
    printf("Timer interval %i sec, timer time %i sec\n", wait, interval);
    pid = fork();
    /* epoll setup */
    if ((epfd=epoll_create(5)) < 0)
	die("Failing on epoll_create");
    epev.data.fd = fd;
    epev.events = EPOLLIN;
    if (epoll_ctl(epfd, EPOLL_CTL_ADD, fd, &epev))
	die("Failing in epoll_ctl"); 
    /* main loop */
    while (1) {
        while (n=epoll_wait(epfd, events, MAX_EPOLL_EV, -1)) { 
	    if (n < 0) {
		if (errno != EAGAIN) 
		    die("error on epoll_wait");
		else 
		    continue;
	    } else {
		printf("Got %i events, pid %i, time %i\n", n, pid, time(NULL));
		/* loop on epoll events */
		for (i=0; i<n; i++) {
		    if (events[i].data.fd == fd) {   // if timer expired
			printf("Timer expired in pid %i:\n", pid);
			while(nread=read(fd, &expired, sizeof(expired))) {
			    if (nread < 0) {
				if (errno != EAGAIN) 
				    die("signalfd read error");
				else 
				    break;
			    }
			    if (nread != sizeof(expired)) {
				printf("Error on timer data read, '\n");
				continue;
			    }
			    printf("Expired %llu times in pid %i\n", 
				   expired, pid);
			}
		    }
		}
	    }
	}
    }
    return 0;
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program testtimerfdfork : test timerfd across fork  \n");
    printf("Usage:\n");
    printf("  testtimerfdfork [-h]  \n");
    printf("  -i sec	   interval for repetition\n");
    printf("  -t sec	   time to wait to expire\n");
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

