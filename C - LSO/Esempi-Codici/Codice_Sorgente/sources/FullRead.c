/* FullRead.c
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
 * Routine FullRead
 * Routine to read an exact number of bytes from a socket
 *
 * Author: Simone Piccardi
 * Jun. 2001
 *
 ****************************************************************/
#include <unistd.h>      /* unix standard library */
#include <errno.h>       /* error definitions and routines */

ssize_t FullRead(int fd, void *buf, size_t count) 
{
    size_t nleft;
    ssize_t nread;
 
    nleft = count;
    while (nleft > 0) {             /* repeat until no left */
	if ( (nread = read(fd, buf, nleft)) < 0) {
	    if (errno == EINTR) {   /* if interrupted by system call */
		continue;           /* repeat the loop */
	    } else {
		return(nread);      /* otherwise exit */
	    }
	} else if (nread == 0) {    /* EOF */
	    break;                  /* break loop here */ 
	}
	nleft -= nread;             /* set left to read */
	buf +=nread;                /* set pointer */
    }
    return (nleft);
}

