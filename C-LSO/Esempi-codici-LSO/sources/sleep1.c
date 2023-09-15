/* sleep1.c
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
 * Program sleep1.c: 
 * Example of a broken implementation for sleep
 *
 * Author: Simone Piccardi
 * Mar. 2002
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#define _GNU_SOURCE
#include <unistd.h>      /* unix standard library */
#include <signal.h>      /* signal constants, types and functions */

void alarm_hand(int sig) {
    /* check if the signal is the right one */
    if (sig != SIGALRM) { /* if not exit with error */
	printf("Something wrong, handler for SIGALRM\n");
	exit(1);
    } else {    /* do nothing, just interrupt pause */
	return;
    }
}
unsigned int sleep(unsigned int seconds)
{
    sighandler_t prev_handler;
    /* install and check new handler */
    if ((prev_handler = signal(SIGALRM, alarm_hand)) == SIG_ERR) {
	printf("Cannot set handler for alarm\n"); 
	exit(-1);
    }
    /* set alarm and go to sleep */
    alarm(seconds); 
    pause(); 
    /* restore previous signal handler */
    signal(SIGALRM, prev_handler);
    /* return remaining time */
    return alarm(0);
}
