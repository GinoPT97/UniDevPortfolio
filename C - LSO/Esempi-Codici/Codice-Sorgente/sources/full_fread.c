/* full_fread.c
 * 
 * Copyright (C) 2005-2006 Simone Piccardi
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
 * Routine full_fread
 * Routine to read an exact number of bytes from a file
 *
 * Author: Simone Piccardi
 * Mar. 2005
 *
 ****************************************************************/
#include <unistd.h>      /* unix standard library */
#include <errno.h>       /* error definitions and routines */
#include <stdio.h>	 /* standard I/O library */

size_t full_fread(FILE *file, void *buf, size_t count) 
{
    size_t nleft;
    size_t nread;
 
    nleft = count;
    while (nleft > 0) {             /* repeat until no left */
	if ( (nread = fread(buf, 1, nleft, file)) < nleft) {
	    if (ferror(file)) {     /* if error */
		return(-1);         /* return negative value as error */
	    } else if (feof(file)) {    /* EOF */
		nleft -= nread;         /* set left */
		break;                  /* break loop here */ 
	    }
	}
	nleft -= nread;             /* set left to read */
	buf +=nread;                /* set pointer */
    }
    return (nleft);
}
