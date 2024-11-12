/* Flock.c
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
/*****************************************************************************
 *
 * Program Flock.c: 
 * Program to test file locking
 *
 * Author: Simone Piccardi
 * Nov. 2002
 *
 * Usage: flock -h give all info's
 *
 *****************************************************************************/
/*
 * Include needed headers
 */
#include <errno.h>       /* error definitions and routines */ 
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>	 /* standard I/O library */
#include <string.h>      /* C strings library */
#include <fcntl.h>       /* file control functions */
#include <sys/file.h>

/* user defined header */
#include "macros.h"      /* some useful macros */

/* Help printing routine */
void usage(void);

int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int type = F_UNLCK;  /* lock type: default to unlock (invalid) */
    off_t start = 0;     /* start of the locked region: default to 0 */
    off_t len = 0;       /* length of the locked region: default to 0 */
    int fd, res, i;      /* internal variables */
    int bsd = 0;         /* semantic type: default to POSIX */
    int cmd = F_SETLK;   /* lock command: default to non-blocking */
    struct flock lock;   /* file lock structure */
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
	case 'h':   /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
	case 's':   /* take start point of the lock */
	    start = strtol(optarg, NULL, 10);  /* convert input */
	    break;
	case 'l':   /* take length of the lock */
	    len = strtol(optarg, NULL, 10);    /* convert input */
	    break;
	case 'w':   /* set type to write lock */
	    type = F_WRLCK;
	    break;
	case 'r':   /* set type to read lock */
	    type = F_RDLCK;
	    break;
	case 'b':  /* set lock to blocking */
	    cmd = F_SETLKW;
	    break;
	case 'f':  /* enable BSD semantic */
	    bsd = 1;
	    break;
	case '?':  /* unrecognized options */
	    printf("Unrecognized options -%c\n",optopt);
	    usage();
	default:   /* should not reached */
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
    if (type == F_UNLCK) {       /* There must be a -w or -r option set */
	printf("You should set a read or a write lock\n");
	usage();
    }
    fd = open(argv[optind], O_RDONLY);  /* open the file to be locked */
    if (fd < 0) {                       /* on error exit */
	perror("Wrong filename");
	exit(1);
    }
    /* do lock */
    if (bsd) {  /* BSD locking */
	/* rewrite cmd for suitables flock operation values */ 
	if (cmd == F_SETLK) {  /* if no-blocking set for flock operation */
	    cmd = LOCK_NB;
	} else {               /* else default is null */
	    cmd = 0;
	}
	if (type == F_RDLCK) cmd |= LOCK_SH;   /* set for shared lock */
	if (type == F_WRLCK) cmd |= LOCK_EX;   /* set for exclusive lock */
	res = flock(fd, cmd);                  /* esecute lock */
    } else {  /* POSIX locking */
	/* setting flock structure */
	lock.l_type = type;          /* set type: read or write */
	lock.l_whence = SEEK_SET;    /* start from the beginning of the file */
	lock.l_start = start;        /* set the start of the locked region */
	lock.l_len = len;            /* set the length of the locked region */
	res = fcntl(fd, cmd, &lock); /* do lock */
    }
    /* check lock results */
    if (res) {   /* on error exit */
	perror("Failed lock");
	exit(1);
    } else {     /* else write message */
	printf("Lock acquired\n");
    }
    pause();     /* stop the process, use a signal to exit */
    return 0;
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program flock: lock a region of a file \n");
    printf("Usage:\n");
    printf("  forktest [-h] [-s start] [-l len] [-w|-r] filename \n");
    printf("  -h	   print this help\n");
    printf("  -s start	   region starting byte\n");
    printf("  -l len       region length (0 means all file)\n");
    printf("  -w           write lock\n");
    printf("  -r           read lock\n");
    printf("  -b           block when locking impossible\n");
    printf("  -f           enable BSD semantic\n");
    exit(1);
}
