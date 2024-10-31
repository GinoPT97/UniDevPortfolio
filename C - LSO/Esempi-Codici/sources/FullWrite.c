/* FullWrite.c
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
/****************************************************************
 *
 * Routine FullWrite
 * Routine to write an exact number of bytes into a socket
 *
 * Author: Simone Piccardi
 * Jun. 2001
 *
 ****************************************************************/
#include <unistd.h>      /* unix standard library */
#include <errno.h>       /* error definitions and routines */

ssize_t FullWrite(int fd, const void *buf, size_t count) 
{
    size_t nleft;
    ssize_t nwritten;

    nleft = count;
    while (nleft > 0) {             /* repeat until no left */
	if ( (nwritten = write(fd, buf, nleft)) < 0) {
	    if (errno == EINTR) {   /* if interrupted by system call */
		continue;           /* repeat the loop */
	    } else {
		return(nwritten);   /* otherwise exit with error */
	    }
	}
	nleft -= nwritten;          /* set left to write */
	buf +=nwritten;             /* set pointer */
    }
    return (nleft);
}

