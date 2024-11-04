/* WriteShm.c
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
 * File WriteShm.c: 
 *
 * An example of POSIX shared memory use: write some content to a
 * shared memory segment
 *
 * Author: S. Piccardi Jan. 2003
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <dirent.h>      /* directory operation constants and functions */
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>       /* standard I/O library */

#include "Gapil.h"
#include "macros.h"

/* Help printing routine */
void usage(void);

int main(int argc, char *argv[]) 
{
    int i;
    int size = 4096;
    char * file_name = NULL;
    int fd = 0;
    int mutex;
    char * mutex_file;
    void * shm_ptr;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hs:f:")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 's':   /* set pause (in sec.) */
	    size = strtol(optarg, NULL, 10);
	    break;
	case 'f':   /* set pause (in sec.) */
	    file_name = optarg;
	    break;
	case 'h':   /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
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
    if ((argc - optind) != 1) {  /* There must be remaing parameters */
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    /* get a Mutex */
    mutex_file = tempnam("/tmp","mutex");
    if ((mutex = CreateMutex(mutex_file)) == -1) { 
	perror("Cannot create mutex");
	goto errmutex;
    }
    /* main loop */
    shm_ptr = CreateShm(argv[1], size, 0644, 0);
    if (shm_ptr == NULL) {
	perror("Error creating shared memory");
	goto errshared;
    }
    if (file_name) {
	fd = open(file_name, O_RDONLY);
    }
    FullRead(fd, shm_ptr, size);
//    RemoveShm(argv[1]);
    RemoveMutex(mutex_file);
    exit(0);
 errshared:
    RemoveShm(argv[1]);
    RemoveMutex(mutex_file);
 errmutex:
    exit(1);
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program myls: list file in a directory \n");
    printf("Usage:\n");
    printf("  WriteShm [-h] [-s size] [-f file] name \n");
    printf("  -h	   print this help\n");
    printf("  -s size	   size of segment \n");
    printf("  -f file	   filename to read contents \n");
    exit(1);
}
