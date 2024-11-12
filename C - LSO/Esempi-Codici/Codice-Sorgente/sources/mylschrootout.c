/* mylschrootout.c
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
/*****************************************************************************
 *
 * File myls.c: An example ls done after a chroot
 *
 * Author: S. Piccardi Apr. 2007
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <dirent.h>      /* directory operation constants and functions */
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */

#include "Gapil.h"
/*
 * Program mylschrootout
 *
 * List files and their size inside exiting from a chroot
 */
/* Help printing routine */
void usage(void);
/* computation function for dir_scan */
int do_ls(struct dirent * direntry);

int main(int argc, char *argv[]) 
{
    int i;
    char * arg[]={"ls","-l", NULL};
    struct stat data;
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
    if ((argc - optind) != 1) {          /* There must be remaing parameters */
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }

    printf("Chrooting to %s\n", argv[1]);
    if (chroot(argv[1])) {
	perror("Chroot fail");
    }

    if (execve("/ls", arg, NULL)) {
	perror("errore in execve");
    }
    if (stat("/ls", &data)) {
	perror("errore in stat");
    } else {
	printf("inode: %d\n", data.st_ino);
    }
    mkdir("escape", 0777);
    chroot("escape");
    dir_scan("../", do_ls);

    exit(0);
}
/*
 * Routine to print file name and size inside dir_scan
 */
int do_ls(struct dirent * direntry) 
{
    struct stat data;
    stat(direntry->d_name, &data);                          /* get stat data */
    printf("File: %s \t inode: %d\n", direntry->d_name, data.st_ino);
    return 0;
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program mylschrootout: chroot in a directory and list files \n");
    printf("Usage:\n");
    printf("  myls [-h] dirname \n");
    printf("  -h	   print this help\n");
    exit(1);
}
