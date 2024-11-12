/* checkmcheck.c
 * 
 * Copyright (C) 2009 Simone Piccardi
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
 * Program checkmcheck: 
 * Some sample tests on mcheck
 *
 * Author: Simone Piccardi
 * Jun. 2009
 *
 * Usage: errcode -h give all info's
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <errno.h>       /* error definitions and routines */ 
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>	 /* standard I/O library */
#include <string.h>      /* C strings library */
#include <mcheck.h>      /* Malloc checking library */


void myabort(enum mcheck_status status);
void printcode(enum mcheck_status status);

int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int ret;
    void * ptr;
    /*
     */
    ret = mcheck(myabort); 
    ptr = malloc(1000);
    printcode(mprobe(ptr));
    free(ptr);
    printcode(mprobe(ptr));
    free(ptr);
    printcode(mprobe(ptr));
	
    /* normal exit */
    return 0;
}

void printcode(status)
{
    switch (status) {
    case MCHECK_DISABLED:
	printf("Status code = MCHECK_DISABLED\n");
	return;
    case MCHECK_OK:
	printf("Status code = MCHECK_OK\n");
	return;
    case MCHECK_HEAD:
	printf("Status code = MCHECK_HEAD\n");
	return;
    case MCHECK_TAIL:
	printf("Status code = MCHECK_TAIL\n");
	return;
    case MCHECK_FREE:
	printf("Status code = MCHECK_FREE\n");
	return;
    default:
	return;
    }
}
/*
 * routine to print code and abort
 */
void myabort(enum mcheck_status status)
{
    printcode(status);
    abort();
}
