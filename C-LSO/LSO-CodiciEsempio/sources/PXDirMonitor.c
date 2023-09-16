/* PXDirMonitor.c
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
/*****************************************************************************
 *
 * File PXDirMonitor: 
 *
 * An example for shared memory use: monitor a directory status,
 * saving data in a shared memory segment. This version use POSIX
 * shared memory.
 *
 * Author: S. Piccardi Jan. 2003
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <dirent.h>      /* directory operation constants and functions */
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <string.h>	 /* C strings library */

#include "Gapil.h"
#include "macros.h"

/* Help printing routine */
void usage(void);
/* computation function for dir_scan */
int ComputeValues(struct dirent * direntry);
void HandSIGTERM(int signo);

/* global variables for shared memory segment */
struct DirProp {
    int tot_size;    
    int tot_files;   
    int tot_regular; 
    int tot_fifo;    
    int tot_link;    
    int tot_dir;     
    int tot_block;   
    int tot_char;    
    int tot_sock;
} *shmptr;
int shmid; 
int mutex;

int main(int argc, char *argv[]) 
{
    int i, pause = 10;
    key_t key;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hp:")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'p':                                     /* set pause (in sec.) */
	    pause = strtol(optarg, NULL, 10);
	    break;
	case 'h':                                             /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
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
    if (chdir(argv[1])) {                      /* chdir to be sure dir exist */
	perror("Cannot find directory to monitor");
	exit(1);
    }
    Signal(SIGTERM, HandSIGTERM);            /* set handlers for termination */
    Signal(SIGINT, HandSIGTERM);
    Signal(SIGQUIT, HandSIGTERM);
    key = ftok("~/gapil/sources/DirMonitor.c", 1);  /* define a key, use dir */
    shmid = shmget(key, 4096, IPC_CREAT|0666);        /* get a shared memory */
    if (shmid < 0) {
	perror("Cannot create shared memory");
	exit(1);
    }
    if ( (shmptr = shmat(shmid, NULL, 0)) == NULL ) {   /* attach to process */
	perror("Cannot attach segment");
	exit(1);
    }
    if ((mutex = MutexCreate(key)) == -1) {                   /* get a Mutex */
	perror("Cannot create mutex");
	exit(1);
    }
    /* main loop, monitor directory properties each 10 sec */
    daemon(1, 0);              /* demonize process, staying in monitored dir */
    while (1) {
	MutexLock(mutex);                              /* lock shared memory */
	memset(shmptr, 0, sizeof(struct DirProp));    /* erase previous data */
	dir_scan(argv[1], ComputeValues);                    /* execute scan */
	MutexUnlock(mutex);                          /* unlock shared memory */
	sleep(pause);                              /* sleep until next watch */
    }
}
/*
 * Routine to compute directory properties inside dir_scan
 */
int ComputeValues(struct dirent * direntry) 
{
    struct stat data;
    stat(direntry->d_name, &data);                          /* get stat data */
    shmptr->tot_size += data.st_size;
    shmptr->tot_files++;
    if (S_ISREG(data.st_mode)) shmptr->tot_regular++;
    if (S_ISFIFO(data.st_mode)) shmptr->tot_fifo++;
    if (S_ISLNK(data.st_mode)) shmptr->tot_link++;
    if (S_ISDIR(data.st_mode)) shmptr->tot_dir++;
    if (S_ISBLK(data.st_mode)) shmptr->tot_block++;
    if (S_ISCHR(data.st_mode)) shmptr->tot_char++;
    if (S_ISSOCK(data.st_mode)) shmptr->tot_sock++;
    return 0;
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program myls: list file in a directory \n");
    printf("Usage:\n");
    printf("  DirMonitor [-h] [-p sec] dirname \n");
    printf("  -h	   print this help\n");
    printf("  -p sec	   set watch interval to sec seconds \n");
    exit(1);
}
/*
 * Signal Handler to manage termination
 */
void HandSIGTERM(int signo) {
    MutexLock(mutex);
    debug("Terminated by %s\n", strsignal(signo));
    if (shmdt(shmptr)) {
	perror("Error detaching shared memory");
	exit(1);
    }
    if (shmctl(shmid, IPC_RMID, NULL)) {
	perror("Cannot remove shared memory segment");
	exit(1);
    }
    MutexRemove(mutex);
    exit(0);
}
