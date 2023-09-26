/* testsignalcgi.c
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
 * Program testsignalcgi
 * CGI to test timout signals sent by apache
 *
 * Author: Simone Piccardi
 * May 2011
 *
 * Usage: cgi-bin for apache.
 * Called by downloading something like:
 * http://localhost/cgi-bin/testsinglacgi
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>	 /* standard I/O library */
#include <stdlib.h>	 /* C standard library */
#include <string.h>	 /* C strings library */
#include <signal.h>      /* signal constants, types and functions */
#include <time.h>

void sig_hand(int, siginfo_t *, void *);
typedef void SigFunc(int, siginfo_t *, void *);
/* Function Signal: Initialize a signal handler. See SigHand.c */
SigFunc * Signal(int signo, SigFunc *func);

FILE * file;

/* Program begin */
int main(int argc, char *argv[], char *envp[])
{
/*
 * Variables definition	 
 */
    char content[]="Content-type: text/html\n\n";
    time_t t;
    int i;
    struct sigaction new_handl, old_handl;
    /* write mime-type to stdout */ 
    write(STDOUT_FILENO, content, strlen(content));
    file = fopen("/tmp/testsignalcgi.out","w+");
    Signal(SIGPIPE,sig_hand);
    Signal(SIGQUIT,sig_hand);
    Signal(SIGTERM,sig_hand);
    Signal(SIGINT,sig_hand);
    Signal(SIGHUP,sig_hand);
    Signal(SIGUSR1,sig_hand);
    Signal(SIGUSR2,sig_hand);
    Signal(SIGIO,sig_hand);
    Signal(SIGURG,sig_hand);
    Signal(SIGABRT,sig_hand);
    pause();
    t = time(NULL);
    fprintf(file, "Exit time: %s", ctime(&t));
    exit(0);
}

/*
 * Signal Handler to print signal
 */
void sig_hand(int sig, siginfo_t * siginfo, void *ptr) {

    /* just return to interrupt sigsuspend */
    pid_t pid;
    time_t t;
    t = time(NULL);
    fprintf(file, "Received time: %s", ctime(&t));
    pid=getpid();
    fprintf(file, "My pid is: %i, sending pid %i, uid %i\n", 
	    pid,siginfo->si_pid,siginfo->si_uid);
    fprintf(file, "Signal is: %i,%i, err %i, code %i\n", 
	    sig,siginfo->si_signo,siginfo->si_errno,siginfo->si_code);
    sleep(1);
    t = time(NULL);
    fprintf(file, "Printed time: %s", ctime(&t));
}

inline SigFunc * Signal(int signo, SigFunc *func) 
{
    struct sigaction new_handl, old_handl;
    new_handl.sa_sigaction = func;             /* set signal handler */
    /* clear signal mask: no signal blocked during execution of func */
    sigemptyset(&new_handl.sa_mask);
    new_handl.sa_flags=SA_SIGINFO; 
    /* change action for signo signal */
    sigaction(signo, &new_handl, &old_handl);
    return (old_handl.sa_sigaction);
}
