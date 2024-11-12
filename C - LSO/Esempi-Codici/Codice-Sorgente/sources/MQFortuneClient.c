/* MQFortuneClient.c
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
 * Program fortuned 
 * Fortune Cleint - Using Message Queues
 *
 * Author: Simone Piccardi
 * Oct. 2002
 *
 * Usage: fortune -h give all info
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
#include <sys/ipc.h>     /* SysV IPC functions */
#include <sys/msg.h>     /* SysV message queues */


#include "macros.h"

/* Maximum message size */
#define MSGMAX 8192

/* Subroutines declaration */
void usage(void);

int main(int argc, char *argv[])
{
/* Variables definition */
    int i;
    struct msgbuf_read {          /* message struct to ask fortune to server */
	long mtype;                               /* message type, must be 1 */
	long pid;             /* message data, must be the pid of the client */
    } msg_read;
    struct msgbuf_write {        /* message struct to get result from server */
	long mtype;            /* message type, will be the pid of the client*/
	char mtext[MSGMAX];             /* message data, will be the fortune */
    } msg_write;
    int msgid;                                   /* Message queue identifier */
    key_t key;                                          /* Message queue key */
    int size;                                                /* message size */
    /*
     * Input section: decode parameters passed in the calling 
     * Use getopt function
     */
    opterr = 0;	                             /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "h")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':  
	    usage();
	    return(0);
	    break;
	case '?':                                    /* unrecognized options */
	    printf("Unrecognized options -%c\n", optopt);
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
     * Comunication section 
     */
    key = ftok("./MQFortuneServer.c", 1); 
    msgid = msgget(key, 0); 
    if (msgid < 0) {
	perror("Cannot find message queue");
	exit(1);
    }

    /* Main body: do request and write result */
    msg_read.mtype = 1;                      /* type for request is always 1 */
    msg_read.pid = getpid();                   /* use pid for communications */
    size = sizeof(msg_read.pid);  
    msgsnd(msgid, &msg_read, size, 0);               /* send request message */
    debug("sent request from %d\n", msg_read.pid);
    msgrcv(msgid, &msg_write, MSGMAX, msg_read.pid, MSG_NOERROR);
    printf("%s", msg_write.mtext);
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Elementary fortune server\n");
    printf("Usage:\n");
    printf("  fortuned [-h] [-f] -n XXX \n");
    printf("  -h   print this help\n");
    printf("  -f filename   set file for fortunes\n");
    printf("  -n XXX        set pool depth\n");
    exit(1);
}
