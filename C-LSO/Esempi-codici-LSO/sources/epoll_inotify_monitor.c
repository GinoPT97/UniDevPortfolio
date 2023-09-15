/* epoll_inotify_monitor.c
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
/*****************************************************************************
 *
 * File epoll_inotify_monitor.c: 
 *
 * An example of the inotify and epoll interface: use inotify to watch the
 * status of a directory or a file and epoll to wait for inotify events.
 *
 * Author: S. Piccardi Jul. 2007
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <sys/inotify.h> /* Linux inotify interface */
#include <sys/epoll.h>   /* Linux epoll interface */
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <errno.h>       /* error definitions and routines */ 
#include <stdio.h>       /* standard I/O library */
#include <string.h>      /* C strings library */
#include <fcntl.h>       /* file control function */
#include <sys/ioctl.h>   /* ioctl syscall and constants */


#include "macros.h"

/* Help printing routine */
void usage(void);
void printevent(unsigned int mask);

int main(int argc, char *argv[]) 
{
    int i, size, nread;
    int fd, wd, epfd;
    char buffer[1024 + sizeof(struct inotify_event)];
    unsigned int mask=0;
    struct inotify_event * event;
    struct epoll_event epev;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ((i = getopt(argc, argv, "hrwcda")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':       /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	case 'r':       /* read access */
	    mask |= IN_ACCESS;
	    break;
	case 'w':       /* write access */
	    mask |= IN_MODIFY;
	    break;
	case 'c':       /* creation */
	    mask |= IN_CREATE;
	    break;
	case 'd':       /* deletion */
	    mask |= IN_DELETE;
	    break;
	case 'a':       /* all events */
	    mask |= IN_ALL_EVENTS;
	    break;
	case '?':       /* unrecognized options */
	    printf("Unrecognized options -%c\n",optopt);
	    usage();
	default:        /* should not reached */
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
    if ((argc - optind) != 1) {           /* There must be one argument */
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    epfd = epoll_create(5);               /* initialize epoll */
    if (epfd < 0) {
        perror("Failing on epoll_create");
	exit(-1);
    }
    fd = inotify_init();                  /* initialize inotify */
    if (fd < 0) {
        perror("Failing on inotify_init");
	exit(-1);
    }
    if (fcntl(fd, F_SETFL, O_NONBLOCK)) { /* no blocking I/O on inotify */
	perror("Cannot set noblocking I/O on inotify fd");
	exit(-1);
    }
    wd = inotify_add_watch(fd, argv[optind], mask);  /* add watch */
    if (wd <= 0) {
	printf("Failing to add watched file %s, mask %i; %s\n", 
	      argv[optind], mask, strerror(errno));
	exit(-1);
    }
    epev.data.fd = fd;        /* add inotify fd to epoll */
    epev.events = EPOLLIN;
    if (epoll_ctl(epfd, EPOLL_CTL_ADD, fd, &epev)) {
        perror("Failing on epoll_ctl");
	exit(-1);
    }
    /* 
     * Main Loop: read events and print them
     */
    while (1) {
	if (epoll_wait(epfd, &epev, 1, -1) < 0) {
	    perror("error on epoll_wait");
	    exit(-1);
	}
	if (epev.data.fd != fd) 
	    printf("something wrong, epoll activity on %i instead of %i\n",
		   epev.data.fd, fd);
	
	if (ioctl(fd, FIONREAD, &size)) {
	    perror("error on getting inotify event size");
	    exit(-1);
	}
	if (size > sizeof(buffer)) {
	    printf("Too many %i data to read, something wrong\n", size);
	    exit(-1);
	}
	i = 0;
	while (i < size) {
	    nread = read(fd, buffer, size);
	    if (nread < 0) {
		perror("error reading inotify data");
		exit(1);
	    }
	    i += nread; 
	    event = (struct inotify_event *) buffer;
	    if (wd != event->wd) {
		printf("Getting different watch descriptor, %i and %i\n",
		       wd, event->wd); 
	    } else {
		printf("Observed event on %s\n", argv[optind-1+event->wd]);
		if (event->name != NULL)
		    printf("On file %s\n", event->name);
		printevent(event->mask);
	    }
	}
    }
    return 0;
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program inotify_monitor: monitor file changes \n");
    printf("Usage:\n");
    printf(" inotify_monitor [-h] -rwcd dirname/filename dir/file ... \n");
    printf("  -h	   print this help\n");
    printf("  -w	   watch write\n");
    printf("  -r	   watch read\n");
    printf("  -c	   watch create\n");
    printf("  -d	   watch delete\n");
    printf("  -a	   watch all\n");
    exit(1);
}

void printevent(unsigned int mask) {
    int i;
    int val;
    char * inotify_event[] = {
	"IN_ACCESS", 
	"IN_MODIFY", 
	"IN_ATTRIB", 
	"IN_CLOSE_WRITE", 
	"IN_CLOSE_NOWRITE", 
	"IN_OPEN", 
	"IN_MOVED_FROM", 
	"IN_MOVED_TO",
	"IN_CREATE", 
	"IN_DELETE", 
	"IN_DELETE_SELF", 
	"IN_MOVE_SELF", 
	"ERROR!!!", 
	"IN_UNMOUNT", 
	"IN_Q_OVERFLOW", 
	"IN_IGNORED"
    };
    
    val=1;
    for (i=0; i<16; i++) {
	if (mask & val) 
	    printf("%s, ", inotify_event[i]); 
	val = val << 1;
    }
    printf("\n");
}
