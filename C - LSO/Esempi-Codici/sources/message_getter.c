/* message_getter.c
 * 
 * Copyright (C) 2010 Simone Piccardi
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
 * File message_setter: Get amd print a message in a shared memory segment,
 * protecting it with a semaphore. 
 *
 * Author: S. Piccardi Jan. 2010
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <dirent.h>      /* directory operation constants and functions */  
#include <unistd.h>      /* unix standard library */
#include <stdlib.h>      /* C standard library */
#include <string.h>	 /* C strings library */
#include <time.h>

#include <semaphore.h>
#include "Gapil.h"
/*
 * Program message_setter
 *
 * List files and their size inside a given directory
 */
/* Help printing routine */
void usage(void);
void HandSigInt(int sig);

#define MSGMAXSIZE 256
char *shmname = "messages";
char *semname = "messages";

int main(int argc, char *argv[]) 
{
    int i;
    sem_t * sem;
    void * shm_ptr;
    time_t t;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hf:s:")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':                                            /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
        case 'f':
            shmname = optarg;
            break;
        case 's':
            semname = optarg;
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
    if ((argc - optind) != 1) {          /* There must be remaing parameters */
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    Signal(SIGINT, HandSigInt);
    // get a shared memory segment
    if ((shm_ptr = CreateShm(shmname, MSGMAXSIZE, 0666, 0)) == NULL) {
	perror("Cannot find shared memory");
	exit(1);
    }
    // get a locked semaphore
    if ((sem = sem_open(semname, O_CREAT|O_EXCL, 0666, 0)) == SEM_FAILED) {
	perror("Cannot open semaphore");
	exit(1);
    }
    // set initial string
    strncpy((char *) shm_ptr, argv[optind], MSGMAXSIZE);
    // do initial release
    if (sem_post(sem) != 0) {
	perror("cannot do semaphore initial release");
	exit(1);
    }
    // main loop
    while(1) {
	if (sem_getvalue(sem, &i) !=0) {             // get sem values
	    perror("cannot get semaphore value");
	    exit(1);
	}
	printf("sem=%i, ", i);                       // print sem values
	t = time(NULL);                              // get time
	printf("%s", ctime(&t));                     // print time
	if (sem_wait(sem) != 0) {                    // acquire semaphore
	    perror("cannot use semaphore");
	    exit(1);
	}
	printf("message: %s\n", (char *) shm_ptr );  // print message
	if (sem_post(sem) != 0) {                    // release semaphore
	    perror("cannot release semaphore");
	    exit(1);
	}
	sleep(1);
   }
exit(0);    
}

/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program message_setter: put a message in shared memory segment \n");
    printf("Usage:\n");
    printf("  message_setter [-h] [-f shmname]  message \n");
    printf("  -h	   print this help\n");
    printf("  -f shmname   use shmname memory segment\n");
    printf("  -s semname   use semname semaphore\n");
    exit(1);
}

void HandSigInt(int sig)
{
    if (RemoveShm(shmname) != 0) perror("Cannot remove shared memory");
    if (sem_unlink(semname)!= 0) perror("Cannot remove semaphore") ;
    exit(0);
}
