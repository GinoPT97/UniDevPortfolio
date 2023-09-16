/* TCP_countd.c
 * 
 * Copyright (C) 2007 Simone Piccardi
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
 * Program countd: Elementary TCP server for teaching purpose, count
 * number of request to the server itself, use POSIX semaphores and
 * shared memory
 *
 * Author: Simone Piccardi
 * Feb. 2007
 *
 * Usage: countd -h give all info
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <sys/types.h>   /* primitive system data types */
#include <unistd.h>      /* unix standard library */
#include <arpa/inet.h>   /* IP addresses conversion utilities */
#include <sys/socket.h>  /* socket constants, types and functions */
#include <stdio.h>	 /* standard I/O library */
#include <time.h>        /* date and time constants, types and functions */
#include <sys/mman.h>
#include <semaphore.h>
#include <syslog.h>      /* syslog system functions */
#include <signal.h>      /* signal constants, types and functions */
#include <errno.h>       /* error definitions and routines */
#include <string.h>      /* C strings library */
#include <stdlib.h>      /* C standard library */

#include "Gapil.h"

#define BACKLOG 10
#define MAXLINE 256
int demonize  = 1;  /* daemon use option: default is daemon */
int debugging = 0;  /* debug info printing option: default is no debug */
/* Subroutines declaration */
void usage(void);
void ServEcho(int sockfd);
void PrintErr(char * error);
/* Program beginning */
int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int list_fd, conn_fd;
    int reuse = 0;
    int compat = 0;
    pid_t pid;
    struct sockaddr_in cli_add;
    socklen_t len;
    char debug[MAXLINE], ipaddr[20];
    /*
     * Input section: decode parameters passed in the calling 
     * Use getopt function
     */
    int i;
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hrdic")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':  
	    printf("Wrong -h option use\n");
	    usage();
	    return(0);
	    break;
	case 'i':
	    demonize = 0;
	    break;
	case 'r':
	    reuse = 1;
	    break;
	case 'c':
	    compat = 1;
	    break;
	case 'd':
	    debugging = 1;
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
    /* Main code begin here */
    if (compat) {                             /* install signal handler */
	Signal(SIGCHLD, HandSigCHLD);         /* non restarting handler */
    } else {
	SignalRestart(SIGCHLD, HandSigCHLD);  /* restarting handler */
    }
    /* create and bind socket */
    if ( (list_fd = sockbindopt(argv[optind], "1234", 6, 
				SOCK_STREAM, reuse)) < 0) {
	return 1;
    }   
    /* release privileges and go daemon */
    if (setgid(65534) !=0) { /* first give away group privileges */
	perror("cannot give away group privileges");
	exit(1);
    }
    if (setuid(65534) !=0) { /* and only after user ... */
	perror("cannot give away user privileges");
	exit(1);
    }
    if (demonize) {          /* go daemon */
        openlog(argv[0], 0, LOG_DAEMON); /* open logging */
 	if (daemon(0, 0) != 0) {
	    perror("cannot start as daemon");
	    exit(1);
	}
    }
    /* main body */
    if (listen(list_fd, BACKLOG) < 0 ) {
	PrintErr("listen error");
	exit(1);
    }
    /* handle echo to client */
    while (1) {
	/* accept connection */
	len = sizeof(cli_add);
	while (((conn_fd = accept(list_fd, (struct sockaddr *)&cli_add, &len)) 
		< 0) && (errno == EINTR)); 
	if (conn_fd < 0) {
	    PrintErr("accept error");
	    exit(1);
	}
	if (debugging) {
	    inet_ntop(AF_INET, &cli_add.sin_addr, ipaddr, sizeof(ipaddr));
	    snprintf(debug, MAXLINE, "Accepted connection form %s\n", ipaddr);
	    if (demonize) {
		syslog(LOG_DEBUG, debug);
	    } else {
		printf("%s", debug);
	    }
	}
	/* fork to handle connection */
	if ( (pid = fork()) < 0 ){
	    PrintErr("fork error");
	    exit(1);
	}
	if (pid == 0) {      /* child */
	    close(list_fd);          /* close listening socket */   
	    if (debugging) {
		snprintf(debug, MAXLINE, "Closed connection %s\n", ipaddr);
		if (demonize) {
		    syslog(LOG_DEBUG, debug);
		} else {
		    printf("%s", debug);
		}
	    }
	    exit(0);
	} else {             /* parent */
	    close(conn_fd);          /* close connected socket */
	}
    }
    /* normal exit, never reached */
    exit(0);
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Elementary connection counter server\n");
    printf("Usage:\n");
    printf("  countd [-h] \n");
    printf("  -h	   print this help\n");
    printf("  -d	   write debug info\n");
    printf("  -r	   enable SO_REUSEADDR\n");
    printf("  -i	   use interactively\n");
    printf("  -c	   disable BSD semantics\n");
    printf("  -w N	   wait N sec. before calling accept\n");
    exit(1);
}
/*
 * routine to print error on stout or syslog
 */
void PrintErr(char * error) {
    if (demonize) {                       /* daemon mode */
	syslog(LOG_ERR, "%s: %m", error); /* log string and error message */
    } else {
	perror(error);
    }
    return;
}

