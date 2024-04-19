/* is_closing.c
 * 
 * Copyright (C) 2006 Simone Piccardi
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
 * Routine is_closing
 * Routine to check if a TCP socket is closing
 *
 * Return 1 if the socket is closing, 0 if not and a negative value in
 * case of errors
 *
 * Author: Simone Piccardi
 * Sep. 2006
 *
 ****************************************************************/
#include <sys/socket.h>  /* socket constants, types and functions */
#include <netinet/in.h>  /* IPv4 and IPv6 constants and types */
#include <netinet/tcp.h> /* TCP constants and types */
#include <errno.h>       /* error definitions and routines */
#
int is_closing(int sock) 
{
    struct tcp_info info;
    socklen_t len = sizeof(info);
    if (getsockopt(sock, SOL_TCP, TCP_INFO, &info, &len) != -1) {
	if (info.tcpi_state == TCP_CLOSE ||
	    info.tcpi_state == TCP_CLOSE_WAIT ||
	    info.tcpi_state == TCP_CLOSING) {
	    return 1;
	} else {
	    return 0;
	}
    } else {
	return errno;
    }
}
