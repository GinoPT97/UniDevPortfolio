/* FifoReporter.c
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
 * Program reporterd
 * FIFO, signal, event and alarm reporter
 * an example program for I/O multiplexing with signalfd and Co.
 *
 * Author: Simone Piccardi
 * Jan. 2011
 *
 * Usage: reporterd -h give all info
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
#include <sys/epoll.h>   /* Linux epoll interface */
#include <sys/signalfd.h>/* Linux signalfd interface */

#include "macros.h"
#include "Gapil.h"

/* Subroutines declaration */
void usage(void);
void die(char *);

/* default name for the input fifo */
char *fifoname = "/tmp/reporter.fifo";

#define MAX_EPOLL_EV 10

int main(int argc, char *argv[])
{
/* Variables definition */
    int i, n, nread, t = 10;
    char buffer[4096];
    int fifofd, epfd, sigfd;
    sigset_t sigmask;
    struct epoll_event epev, events[MAX_EPOLL_EV];
    struct signalfd_siginfo siginf;
    char *sig_names[] = { // signal name declaration from bits/signum.h
	"-------       ", /*0        Filler for signal names array */
	"SIGHUP        ", /*1        Hangup (POSIX).  */
	"SIGINT        ", /*2        Interrupt (ANSI).  */
	"SIGQUIT       ", /*3        Quit (POSIX).  */
	"SIGILL        ", /*4        Illegal instruction (ANSI).  */
	"SIGTRAP       ", /*5        Trace trap (POSIX).  */
	"SIGABRT       ", /*6        Abort (ANSI).  */
	"SIGBUS        ", /*7        BUS error (4.2 BSD).  */
	"SIGFPE        ", /*8        Floating-point exception (ANSI).  */
	"SIGKILL       ", /*9        Kill, unblockable (POSIX).  */
	"SIGUSR1       ", /*10       User-defined signal 1 (POSIX).  */
	"SIGSEGV       ", /*11       Segmentation violation (ANSI).  */
	"SIGUSR2       ", /*12       User-defined signal 2 (POSIX).  */
	"SIGPIPE       ", /*13       Broken pipe (POSIX).  */
	"SIGALRM       ", /*14       Alarm clock (POSIX).  */
	"SIGTERM       ", /*15       Termination (ANSI).  */
	"SIGSTKFLT     ", /*16       Stack fault.  */
	"SIGCHLD       ", /*17       Child status has changed (POSIX).  */
	"SIGCONT       ", /*18       Continue (POSIX).  */
	"SIGSTOP       ", /*19       Stop, unblockable (POSIX).  */
	"SIGTSTP       ", /*20       Keyboard stop (POSIX).  */
	"SIGTTIN       ", /*21       Background read from tty (POSIX).  */
	"SIGTTOU       ", /*22       Background write to tty (POSIX).  */
	"SIGURG        ", /*23       Urgent condition on socket (4.2 BSD).  */
	"SIGXCPU       ", /*24       CPU limit exceeded (4.2 BSD).  */
	"SIGXFSZ       ", /*25       File size limit exceeded (4.2 BSD).  */
	"SIGVTALRM     ", /*26       Virtual alarm clock (4.2 BSD).  */
	"SIGPROF       ", /*27       Profiling alarm clock (4.2 BSD).  */
	"SIGWINCH      ", /*28       Window size change (4.3 BSD, Sun).  */
	"SIGIO         ", /*29       I/O now possible (4.2 BSD).  */
	"SIGPWR        ", /*30       Power failure restart (System V).  */
	"SIGSYS        "  /*31       Bad system call.  */
    };
    /*
     * Input section: decode parameters passed in the calling 
     * Use getopt function
     */
    opterr = 0;	                             /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "ht:f:")) != -1) {
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
	    fifoname = optarg;
	    break;
	case 't':
	    t = strtol(optarg, NULL, 10);
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
    /* 
     * Initial setup 
     */
    if ((epfd=epoll_create(5)) < 0)                          // epoll init
	die("Failing on epoll_create");
    /* Signal setup for signalfd and epoll use */
    sigemptyset(&sigmask);
    sigaddset(&sigmask, SIGINT);
    sigaddset(&sigmask, SIGQUIT);
    sigaddset(&sigmask, SIGTERM);
    if (sigprocmask(SIG_BLOCK, &sigmask, NULL) == -1)       // block signals
	die("Failing in signalfd");
    if ((sigfd=signalfd(-1, &sigmask, SFD_NONBLOCK)) == -1) // take a signalfd
	die("Failing in signalfd");
    epev.data.fd = sigfd;                                   // add fd to epoll
    epev.events = EPOLLIN;
    if (epoll_ctl(epfd, EPOLL_CTL_ADD, sigfd, &epev))
	die("Failing in signal epoll_ctl"); 
    /* Fifo setup for epoll use */
    if (mkfifo(fifoname, 0622)) {  // create well known fifo if does't exist 
	if (errno!=EEXIST)
	    die("Cannot create well known fifo");
    }
    if ((fifofd = open(fifoname, O_RDWR|O_NONBLOCK)) < 0)   // open fifo
	die("Cannot open well known fifo");
    epev.data.fd = fifofd;                                  // add fd to epoll
    epev.events = EPOLLIN;
    if (epoll_ctl(epfd, EPOLL_CTL_ADD, fifofd, &epev)) 
	die("Failing in fifo epoll_ctl");
    /* 
     * Main body: wait something to report 
     */
    printf("FifoReporter starting, pid %i\n", getpid());
    while (1) {
        if ((n=epoll_wait(epfd, events, MAX_EPOLL_EV, -1)) < 0) 
	    die("error on epoll_wait");
	debug("Got %i events\n", n);
	/* loop on epoll events */
	for (i=0; i<n; i++) {    
	    if (events[i].data.fd == sigfd) {             // if signalfd ready 
		printf("Signal received:\n");
		while(nread=read(sigfd, &siginf, sizeof(siginf))) {
		    if (nread < 0) {
			if (errno != EAGAIN) 
			    die("signalfd read error");
			else 
			    break;
		    }
		    if (nread != sizeof(siginf)) {
			printf("Error on signal data read, '\n");
			continue;
		    }
		    printf("Got %s\n", sig_names[siginf.ssi_signo]);
		    printf("From pid %i\n", siginf.ssi_pid);
		    if(siginf.ssi_signo == SIGINT) {      // SIGINT is exit
			printf("SIGINT means exit\n");
			unlink(fifoname);
			exit(0);
		    }
		}
	    } else if (events[i].data.fd == fifofd) {     // if fifofd ready 
		printf("Message from fifo:\n");
		while ((nread = read(fifofd, buffer, 5000))) {
		    debug("read %i from fifofd %i, event %i\n", 
			   nread, fifofd, events[i].data.fd );
		    if (nread < 0) {
			if (errno != EAGAIN)
			    die("fifo read error");
			else 
			    printf("end message\n");
			break;
		    }
		    buffer[nread] = 0;
		    if (fputs(buffer, stdout) == EOF)
			die("Error on terminal write");
		}
	    } else {   // anything else is an error
		printf("epoll activity on unknown %i file descriptor\n", 
		       epev.data.fd);
		exit(-1);
	    }
	}
    }
    debug("Exiting for unknown reasons\n");
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Elementary event reporter\n");
    printf("Usage:\n");
    printf("  reporterd [-h] [-f] -n XXX \n");
    printf("  -h\t print this help\n");
    printf("  -f\t filename   set fifo file\n");
    exit(1);
}
/*
 * Print error message and exit routine
 */
void die(char * mess) {
    perror(mess);
    exit(-1);
}
