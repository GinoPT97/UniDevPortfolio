/* ReadMonitor.c
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
 * File ReadMonitor: 
 *
 * An example for shared memory use: read data saved in a shared
 * memory segment from the DirMonitor program
 *
 * Author: S. Piccardi Jan. 2003
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <dirent.h>      /* directory operation constants and functions */
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */

#include "Gapil.h"
#include "macros.h"

/* Help printing routine */
void usage(void);

/* variables for shared memory segment */
int shmid; 
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
};
struct DirProp *shmptr;
int mutex;

int main(int argc, char *argv[]) 
{
    int i;
    key_t key;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hs:l:wrbf")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':                                            /* help option */
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
    /* create needed IPC objects */
    key = ftok("~/gapil/sources/DirMonitor.c", 1);           /* define a key */
    if (!(shmptr = ShmFind(key, 4096))) {     /* get a shared memory segment */
	perror("Cannot find shared memory");
	exit(1);
    }
    if ((mutex = MutexFind(key)) == -1) {                   /* get the Mutex */
	perror("Cannot find mutex");
    }
    /* main loop */
    MutexLock(mutex);                                  /* lock shared memory */
    printf("Ci sono %d file dati\n", shmptr->tot_regular);
    printf("Ci sono %d directory\n", shmptr->tot_dir);
    printf("Ci sono %d link\n", shmptr->tot_link);
    printf("Ci sono %d fifo\n", shmptr->tot_fifo);
    printf("Ci sono %d socket\n", shmptr->tot_sock);
    printf("Ci sono %d device a caratteri\n", shmptr->tot_char);
    printf("Ci sono %d device a blocchi\n", shmptr->tot_block);
    printf("Totale  %d file, per %d byte\n",
           shmptr->tot_files, shmptr->tot_size);
    MutexUnlock(mutex);                              /* unlock shared memory */
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program myls: list file in a directory \n");
    printf("Usage:\n");
    printf("  myls [-h] dirname \n");
    printf("  -h	   print this help\n");
    exit(1);
}
