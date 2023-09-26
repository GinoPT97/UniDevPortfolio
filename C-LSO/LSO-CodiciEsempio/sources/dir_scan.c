/* dir_scan.c
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
 * File dir_scan.c: routine for directory scan 
 *
 * Author: S. Piccardi Jan. 2003
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <dirent.h>      /* directory operation constants and functions */
#include <stdlib.h>      /* C standard library */
#include <stdio.h>	 /* standard I/O library */
#include <unistd.h>      /* unix standard library */

/*
 * Function dir_scan: 
 * 
 * Scan all entries in a directory, executing the provided function
 * on each one.
 *
 * Input:  the directory name and a computation function
 * Return: 0 if OK, -1 on errors
 */
int dir_scan(char * dirname, int(*compute)(struct dirent *)) 
{
    DIR * dir;
    struct dirent *direntry;
    int fd;

    if ( (dir = opendir(dirname)) == NULL) {               /* oper directory */
	printf("Opening %s\n", dirname);          /* on error print messages */
	perror("Cannot open directory");                  /* and then return */
	return -1;
    }
    fd = dirfd(dir);                                  /* get file descriptor */
    fchdir(fd);                                          /* change directory */
    /* loop on directory entries */
    while ( (direntry = readdir(dir)) != NULL) {               /* read entry */
	if (compute(direntry)) {                   /* execute function on it */
	    return -1;                                    /* on error return */
	}
    }
    closedir(dir);
    return 0;
}
