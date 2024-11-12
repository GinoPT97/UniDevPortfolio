/* IPCTestId.c
 * 
 * Copyright (C) 2001 Simone Piccardi
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
 * Program IPCTestId.c: 
 * Program to test IPC identifiers
 *
 * Author: Simone Piccardi
 * Aug. 2002
 *
 * Usage: ipctestid -h give all info's
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <errno.h>       /* error definitions and routines */ 
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>	 /* standard I/O library */
#include <string.h>      /* C strings library */
#include <sys/ipc.h>     /* SysV IPC functions */
#include <sys/msg.h>     /* SysV message queues */
#include <sys/sem.h>     /* SysV semaphores */
#include <sys/shm.h>     /* SysV shared memory */

#include "macros.h"      /* My macros */
/* Help printing routine */
void usage(void);

int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int i; 
    int n = 3;                                      /* default is 3 times */
    char type='q';                                  /* default is use queues */
    int id;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hqsmn:")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':   /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
	case 'q':   /* Message Queue */
	    debug("Message Queue\n");
	    type = i;
	    break;
	case 's':   /* Semaphore */
	    debug("Semaphore\n");
	    type = i;
	    break;
	case 'm':   /* Shared Memory */
	    debug("Shared Memory\n");
	    type = i;
	    break;
	case 'n':   /* Number of tryes */
	    debug("Number of tryes\n");
	    n = strtol(optarg, NULL, 10);
	    break;
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
    switch (type) {
    case 'q':   /* Message Queue */
	debug("Message Queue Try\n");
	for (i=0; i<n; i++) {
	    id = msgget(IPC_PRIVATE, IPC_CREAT|0666);
	    printf("Identifier Value %d \n", id);
	    msgctl(id, IPC_RMID, NULL);
	}
	break;
    case 's':   /* Semaphore */
	debug("Semaphore\n");
	for (i=0; i<n; i++) {
	    id = semget(IPC_PRIVATE, 1, IPC_CREAT|0666);
	    printf("Identifier Value %d \n", id);
	    semctl(id, 0, IPC_RMID);
	}
	break;
    case 'm':   /* Shared Memory */
	debug("Shared Memory\n");
	for (i=0; i<n; i++) {
	    id = shmget(IPC_PRIVATE, 1000, IPC_CREAT|0666);
	    printf("Identifier Value %d \n", id);
	    shmctl(id, IPC_RMID, NULL);
	}
	break;
    default:    /* should not reached */
	return -1;
    }
    return 0;
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program ipctestid : test IPC identifier number assignment\n");
    printf("Usage:\n");
    printf("  ipctestid [-h] [-qsm] [-n N] \n");
    printf("  -h     print this help\n");
    printf("  -q     try for message queue identifiers\n");
    printf("  -s     try for semaphore identifiers\n");
    printf("  -m     try for shared memory identifiers\n");
    printf("  -n XX  try XX times\n");
    
    exit(1);
}

