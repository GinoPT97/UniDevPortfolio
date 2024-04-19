/* SetTermAttr.c
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
/****************************************************************
 *
 * Routine SetTermAttr
 * Routine to set a local attribute for a terminal
 *
 * Author: Simone Piccardi
 * Jun. 2001
 *
 ****************************************************************/
#include <unistd.h>      /* unix standard library */
#include <termios.h>     /* terminals constants, types and functions */
#include <errno.h>       /* error definitions and routines */
#include <stdio.h>	 /* standard I/O library */

int SetTermAttr(int fd, tcflag_t flag) 
{
    struct termios values;
    int res;

    res = tcgetattr (fd, &values);
    if (res) {
	perror("Cannot get attributes");
	return res;
    }
    values.c_lflag |= flag;
    res = tcsetattr (fd, TCSANOW, &values);
    if (res) {
	perror("Cannot set attributes");
	return res;
    }
    return 0;
}
int UnSetTermAttr(int fd, tcflag_t flag) 
{
    struct termios values;
    int res;

    res = tcgetattr (fd, &values);
    if (res) {
	perror("Cannot get attributes");
	return res;
    }
    values.c_lflag &= (~flag);
    res = tcsetattr (fd, TCSANOW, &values);
    if (res) {
	perror("Cannot set attributes");
	return res;
    }
    return 0;
}

