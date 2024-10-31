/* tee.c
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
/****************************************************************
 *
 * Program tee.c: 
 * A sample program that duolicete stdin to stdout and a file
 *
 * Author: Simone Piccardi
 * Aug. 2007
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#define _GNU_SOURCE
#include <fcntl.h>       /* file control functions */
#include <unistd.h>      /* unix standard library */
#include <stdlib.h>      /* C standard library */
#include <errno.h>       /* error definitions and routines */
#include <stdio.h>       /* standard I/O library */
#include <string.h>      /* C strings library */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <sys/types.h>   /* primitive system data types */
#include <limits.h>      /* system limits constants, types and functions */

#include "macros.h"
/* 
 * Function and globals definitions
 */
void usage(void);  /* Help printing routine */
/*
 * Main program
 */
int main(int argc, char *argv[])
{
   /*
    * Variables definition
    */
    int i;
    size_t size = 4096;
    int fd;
    int len, nwrite;
    struct stat fdata;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;  /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hs:")) != -1) {
        switch (i) {
        /* 
         * Handling options 
         */ 
        case 'h':      /* help option */
            printf("Wrong -h option use\n");
            usage();
            return -1;
            break;
        case 's':      /* buffer size */
            size = strtol(optarg, NULL, 10);    /* convert input */
            break;
	case '?':      /* unrecognized options */
            printf("Unrecognized options -%c\n", optopt);
            usage();
        default:       /* should not reached */
            usage();
        }
    }
   /*
    * Main body
    */
    if ((argc - optind) != 1) { /* There must be one argument */
        printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    /* open destination file and check stdin and stdout */
    fd = open(argv[1], O_WRONLY|O_CREAT|O_TRUNC, 0644);
    if (fd == -1) {
        printf("cannot open destination file %s, %s", argv[1], 
	       strerror(errno));
        exit(EXIT_FAILURE);
    }
    if (fstat(STDIN_FILENO, &fdata) < 0) {
	perror("stat");
	exit(EXIT_FAILURE);
    }
    if (!S_ISFIFO(fdata.st_mode)) {
	fprintf(stderr, "stdin must be a pipe\n");
	exit(EXIT_FAILURE);
    }
    if (fstat(STDOUT_FILENO, &fdata) < 0) {
	perror("stat");
	exit(EXIT_FAILURE);
    }
    if (!S_ISFIFO(fdata.st_mode)) {
	fprintf(stderr, "stdout must be a pipe\n");
	exit(EXIT_FAILURE);
    }
    /* tee loop */
    while (1) {
        /* copy stdin to stdout */
        len = tee(STDIN_FILENO, STDOUT_FILENO, size, 0);
	fprintf(stderr, "Copied %d byte\n", len); /* debug (use stderr!) */
	if (len == 0) break;
        if (len < 0) {
            if (errno == EAGAIN) {
		continue;
	    } else {
		perror("error on tee stdin to stdout");
		exit(EXIT_FAILURE);
	    }
        }
        /* write data to the file using splice */
        while (len > 0) {
            nwrite = splice(STDIN_FILENO, NULL, fd, NULL, len, SPLICE_F_MOVE);
            if (nwrite < 0) {
                perror("error on splice stdin to file");
                break;
            }
            len -= nwrite;
        }
    }
    close(fd);
    exit(EXIT_SUCCESS);
}

/*
 * routine to print usage info and exit 
 */
void usage(void) {
    printf("Program tee: duplicate stdin to stdout and a file\n");
    printf("Usage:\n");
    printf("  tee [-h] [-s N] filename\n");
    printf("  -h           print this help\n");
    printf("  -s N         set a buffer size of N bytes \n");
    exit(1);
}

