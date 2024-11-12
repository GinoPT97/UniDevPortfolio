/* BarCodePage.c
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
 * Program barcode 
 * CGI for barcode generation
 *
 * Author: Simone Piccardi
 * Jun. 2002
 *
 * Usage: cgi-bin for apache.
 * Called by downloading something like:
 * http://localhost/cgi-bin/barcode?string
 * where string is the code to be converted
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>	 /* standard I/O library */
#include <stdlib.h>	 /* C standard library */
#include <string.h>	 /* C strings library */
#include <wait.h> 	 /* process termination constants and functions */
#include <fcntl.h>       /* file control functions */
#include <assert.h>	 /* C assertion functions */
#include <time.h>        /* date and time constants, types and functions */


#include"macros.h"
void WriteMess(char *mess);

/* Program begin */
int main(int argc, char *argv[], char *envp[])
{
/*
 * Variables definition	 
 */
    pid_t pid;
    int retval;
    int pipein[2];
    int pipeout[2];
    char content[]="Content-type: image/jpeg\n\n";
    char size[]="-pA9";
    /* 
     * Begin
     */
    /* create two pipes, pipein and pipeout, to handle communication */
    if ( (retval = pipe(pipein)) ) {
	WriteMess("input pipe creation error");
	exit(0);	
    }
    if ( (retval = pipe(pipeout)) ) {
	WriteMess("output pipe creation error");
	exit(0);	
    }	 
    /* First fork: use child to run barcode program */
    if ( (pid = fork()) == -1 ) {
	WriteMess("child creation error");
	exit(0);
    }
    /* if child */
    if (pid == 0) {
	/*
	 * Child exec barcode program, that take input (string to encode)
	 * from pipein, remapped to stdin, and write the output (a PS
	 * image) to stdout, remapped to pipeout 
	 */
	close(pipein[1]);	         /* close output side of input pipe */
  	dup2(pipein[0], STDIN_FILENO);   /* remap stdin in pipe input */
	close(pipeout[0]);
	dup2(pipeout[1], STDOUT_FILENO); /* remap stdout in pipe output */
	execlp("barcode", "barcode", size, NULL); 
    } 
    /*
     * Parent write string to pipe input and close it, 
     * then wait child execution and results form pipeout, 
     * then fork to convert PS to JPEG using gs
     */
    close(pipein[0]);        /* close input side of input pipe */
    write(pipein[1], argv[1], strlen(argv[1]));
    close(pipein[1]);
    waitpid(pid, NULL, 0);
    /* Second fork: use child to run ghostscript*/
    if ( (pid = fork()) == -1) {
	WriteMess("child creation error");
	exit(0);
    }
    /* second child, convert PS to JPEG */
    if (pid == 0) {
	close(pipeout[1]);              /* close write end */
	dup2(pipeout[0], STDIN_FILENO); /* remap read end to stdin */
	/* send mime type */
	write(STDOUT_FILENO, content, strlen(content));
	execlp("gs", "gs", "-q", "-sDEVICE=jpeg", "-sOutputFile=-", "-", NULL);
    }
    /* still parent */
    close(pipeout[1]); 
    waitpid(pid, NULL, 0);
    exit(0);
}
/*
 * Routine to produce an HTML error message on output 
 */
void WriteMess(char *mess)
{
    printf("Content-type: text/html\n\n");
    perror(mess);
    printf("<br>\n");
}
