/* full_fwrite.c
 * 
 * Copyright (C) 2005 Simone Piccardi
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
 * Routine full_fwrite
 * Routine to write an exact number of bytes into a file
 *
 * Author: Simone Piccardi
 * Mar. 2005
 *
 ****************************************************************/
#include <unistd.h>      /* unix standard library */
#include <errno.h>       /* error definitions and routines */
#include <stdio.h>	 /* standard I/O library */

size_t full_fwrite(FILE *file, const void *buf, size_t count) 
{
    size_t nleft;
    size_t nwritten;

    nleft = count;
    while (nleft > 0) {             /* repeat until no left */
	if ( (nwritten = fwrite(buf, 1, nleft, file)) < nleft) {
	    if (ferror(file)) {     /* if error */
		return(-1);         /* return negative value as error */
	    }
	}
	nleft -= nwritten;          /* set left to write */
	buf +=nwritten;             /* set pointer */
    }
    return (nleft);
}

