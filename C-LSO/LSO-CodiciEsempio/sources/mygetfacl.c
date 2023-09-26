/* mygetfacl.c
 * 
 * Copyright (C) 2012 Simone Piccardi
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
 * Program mygetfacl.c: 
 * A simple program that get a file acl
 *
 * Author: Simone Piccardi
 * Jan 2012
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <sys/types.h>   /* primitive system data types */
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>       /* standard I/O library */
#include <sys/acl.h>     /* acl library (use -l acl) */
#include <errno.h>     /* acl library (use -l acl) */


/* pass -D DEBUG to gcc to enable debug printing */
#ifdef DEBUG
#define debug printf
#else
#define debug(fmt, arg...)
#endif /* DEBUG */

/*
 * Program mygetfacl
 */
/* Help printing routine */
void usage(void);

int main(int argc, char *argv[])
{
/*
 * Variables definition
 */    
    acl_t acl;
    ssize_t size;
    char * buffer;
    int i;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "h")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':                                  /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
	case '?':                                  /* unrecognized options */
	    printf("Unrecognized options -%c\n",optopt);
	    usage();
	default:                                   /* should not reached */
	    usage();
	}
    }
    /* must have an argument */
    if ((argc - optind) != 1) {
        printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    /* main body */
    errno = 0;
    acl = acl_get_file(argv[1], ACL_TYPE_ACCESS);
    if (acl == NULL) {
	printf("error on getting ACL for file %s\n", argv[1]);
	perror(argv[1]);
	return 1;
    }
    buffer = acl_to_text(acl, &size);
    if (buffer == NULL) {
	perror("cannot convert acl");
	return 1;
    }
    printf("ACL for file '%s':\n%s\n", argv[1], buffer);
    acl_free(acl);
    acl_free(buffer);
    return 0;
}


/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program mymount:  \n");
    printf("Usage:\n");
    printf("mygetfacl [-h] file\n");
    printf("  -h          print this help\n");
    exit(1);
}
