/* Sleep.c
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
 * Program Sleep.c: 
 * Example implementation for sleep
 *
 * Author: Simone Piccardi
 * Apr. 2002
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <unistd.h>      /* unix standard library */
#include <signal.h>      /* signal constants, types and functions */

void alarm_hand(int);

unsigned int sleep(unsigned int seconds)
{
/* 
 * Variables definition  
 */
    struct sigaction new_action, old_action;
    sigset_t old_mask, stop_mask, sleep_mask;
    /* set the signal handler */
    sigemptyset(&new_action.sa_mask);              /* no signal blocked */
    new_action.sa_handler = alarm_hand;            /* set handler */
    new_action.sa_flags = 0;                       /* no flags */
    sigaction(SIGALRM, &new_action, &old_action);  /* install action */
    /* block SIGALRM to avoid race conditions */
    sigemptyset(&stop_mask);                       /* init mask to empty */
    sigaddset(&stop_mask, SIGALRM);                /* add SIGALRM */
    sigprocmask(SIG_BLOCK, &stop_mask, &old_mask); /* add SIGALRM to blocked */
    /* send the alarm */
    alarm(seconds); 
    /* going to sleep enabling SIGALRM */
    sleep_mask = old_mask;                         /* take mask */
    sigdelset(&sleep_mask, SIGALRM);               /* remove SIGALRM */
    sigsuspend(&sleep_mask);                       /* go to sleep */
    /* restore previous settings */
    sigprocmask(SIG_SETMASK, &old_mask, NULL);     /* reset signal mask */    
    sigaction(SIGALRM, &old_action, NULL);         /* reset signal action */
    /* return remaining time */
    return alarm(0);
}
/*
 * Signal Handler for SIGALRM
 */
void alarm_hand(int sig) {
    /* just return to interrupt sigsuspend */
    return;
}
