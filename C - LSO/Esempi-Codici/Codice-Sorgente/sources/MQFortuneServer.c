/* MQFortuneServer.c
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
 * Fortune server - Using Message Queues
 *
 * Author: Simone Piccardi
 * Aug. 2002
 *
 * Usage: fortuned -h give all info
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
#include "Gapil.h"

/* Maximum message size */
#define MSGMAX 8192

/* Subroutines declaration */
void usage(void);
void HandSIGTERM(int signo);
int FortuneParse(char *file, char **fortune, int n);

int msgid;                                       /* Message queue identifier */
int main(int argc, char *argv[])
{
/* Variables definition */
    int i, n = 0;
    char **fortune;                       /* array of fortune message string */
    char *fortunefilename = "/usr/share/games/fortunes/linux";  /* file name */
    struct msgbuf_read {      /* message struct to read request from clients */
	long mtype;                               /* message type, must be 1 */
	long pid;             /* message data, must be the pid of the client */
    } msg_read;
    struct msgbuf_write {       /* message struct to write result to clients */
	long mtype;            /* message type, will be the pid of the client*/
	char mtext[MSGMAX];             /* message data, will be the fortune */
    } msg_write;
    key_t key;                                          /* Message queue key */
    int size;                                                /* message size */
    /*
     * Input section: decode parameters passed in the calling 
     * Use getopt function
     */
    opterr = 0;	                             /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hn:f:")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':                                       /* print usage infos */
	    usage();
	    return(0);
	    break;
	case 'f':                                   /* set fortune file name */
	    fortunefilename = optarg;
	    break;
	case 'n':                     /* set number of fortune string to use */
	    n = strtol(optarg, NULL, 10);
	    fortune = (char **) calloc(sizeof(*fortune), n);
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
    if (n==0) usage();          /* if no pool depth exit printing usage info */
    Signal(SIGTERM, HandSIGTERM);            /* set handlers for termination */
    Signal(SIGINT, HandSIGTERM);
    Signal(SIGQUIT, HandSIGTERM);
    i = FortuneParse(fortunefilename, fortune, n);          /* parse phrases */
    for (n=0; n<i; n++) debug("%s%%\n", fortune[n]);
    /* 
     * Comunication section 
     */
    key = ftok("./MQFortuneServer.c", 1); 
    msgid = msgget(key, IPC_CREAT|0666);
    if (msgid < 0) {
	perror("Cannot create message queue");
	exit(1);
    }

    /* Main body: loop over requests */
    daemon(0, 0);
    while (1) {
	msgrcv(msgid, &msg_read, sizeof(int), 1, MSG_NOERROR);
	debug("received request from %d\n", msg_read.pid);
	n = random() % i;                             /* select random value */
	strncpy(msg_write.mtext, fortune[n], MSGMAX);
	size = min(strlen(fortune[n])+1, MSGMAX);  
	msg_write.mtype=msg_read.pid;             /* use request pid as type */
	msgsnd(msgid, &msg_write, size, 0);
    }
    debug("Exiting for unknown reasons\n");
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
    printf("  -n NNN        set pool depth\n");
    exit(1);
}
/*
 * Signal Handler to manage termination
 */
void HandSIGTERM(int signo) {
    debug("Terminated by %s\n", strsignal(signo));
    msgctl(msgid, IPC_RMID, NULL);                   /* remove message queue */
    exit(0);
}
