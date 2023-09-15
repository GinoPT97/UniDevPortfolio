/* test_linkat.c
 * 
 * Copyright (C) 2019 Simone Piccardi
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
 * Program test_linkat.c: 
 * Program to test use of linkat and O_TMPFILE and other combinations 
 *
 * Author: Simone Piccardi
 * Oct. 2018
 *
 * Usage: testlinkat -h give all info's
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#define _GNU_SOURCE
#include <errno.h>       /* error definitions and routines */ 
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>	 /* standard I/O library */
#include <string.h>      /* C strings library */
#include <limits.h>
#include <libgen.h>      /* needed for dirname e basename */
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>

/* Help printing routine */
void usage(void);

int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int i;
    int wait=0;
    char *srcfile = NULL;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hw:f:")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':   /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
        case 'w':      /* time to wait in s */
            wait = strtol(optarg, NULL, 10);    /* convert input */
            break;
        case 'f':      /* using a source file */
            srcfile = optarg;
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
    /* There must be 1 remaing parameters */
    if ( (argc-optind) != 1 )  {
	printf("From %d arguments, removed %d options\n", argc, optind);
	usage();
    }
    char *path, *dir, *file;
    char pattern[] = "prova prova prova\n";
    path = strdup(argv[optind]);
    file = basename(argv[optind]);
    dir = dirname(argv[optind]);
    printf("Working on dir %s file: %s\nwith path: %s\n",
	   dir, file, path);
    int fd, newfd;
    if ( ! srcfile ) {
	fd = open(dir, O_TMPFILE|O_RDWR,S_IRUSR|S_IWUSR);
	if ( fd <= 0 )
	    perror("cannot open TMPFILE");
	if ( (i = write(fd, pattern, sizeof(pattern))) < 0 )   
	    perror("cannot write on TMPFILE");
	else
	    printf("saved %d chars on fd %d\n", i, fd);
    } else {
	printf("source is %s\n", srcfile);
	if ( (fd = open(srcfile, O_RDONLY)) < 0 ) {
	    perror("cannot open source");
	    exit(1);
	}
    }
    newfd = open(dir, O_PATH|O_RDWR);
    int err;
    err = linkat(fd, "", newfd, file, AT_EMPTY_PATH);
    if ( err < 0  ) {
	perror("error on creating TMPFILE");
	printf("cannot link fd %d on fd %d, %s with AT_EMPTY_PATH\n",
	       fd, newfd, file);
	char fdpath[PATH_MAX];
	snprintf(fdpath, PATH_MAX, "/proc/self/fd/%d", fd);
	printf("fd path %s\n", fdpath);
	err = linkat(AT_FDCWD, fdpath, newfd, file, AT_SYMLINK_FOLLOW);
	if ( err < 0  ) {
            perror("still error creating TMPFILE");
	    sleep(wait);
	    exit(1);
        }
	if ( fchmodat(newfd, file,S_IRUSR|S_IWUSR,0) < 0 )
	    perror("Cannot change permission to new file");
    }
    free(path);
    return 0;
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program testlinkat : test linkat for a file  \n");
    printf("Usage:\n");
    printf("  testlinkat [-h] file mode \n");
    printf("  -h	   print this help\n");
    printf("  -w [N]	   wait N seconds\n");
    printf("  -f [file]	   use file as source\n");
    
    exit(1);
}

