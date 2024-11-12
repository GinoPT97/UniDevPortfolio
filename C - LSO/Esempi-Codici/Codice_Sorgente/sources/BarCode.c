/* BarCode.c
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
#include <fcntl.h>	 /* file control functions */
#include <assert.h>	 /* C assertion functions */
#include <time.h>        /* date and time constants, types and functions */

#include"macros.h"

/* Program begin */
int main(int argc, char *argv[], char *envp[])
{
/*
 * Variables definition	 
 */
    FILE *pipe[4];
    FILE *pipein;
    char *cmd_string[4]={
	"pnmtopng",
	"pnmmargin -white 10",
	"pnmcrop",
	"gs -sDEVICE=ppmraw -sOutputFile=- -sNOPAUSE -q - -c showpage -c quit"
    };	
    char content[]="Content-type: image/png\n\n";
    int i;
    /* write mime-type to stdout */ 
    write(STDOUT_FILENO, content, strlen(content));
    /* execute chain of command */
    for (i=0; i<4; i++) {
	pipe[i] = popen(cmd_string[i], "w");
	dup2(fileno(pipe[i]), STDOUT_FILENO); 
    }
    /* create barcode (in PS) */
    pipein = popen("barcode", "w");
    /* send barcode string to barcode program */
    write(fileno(pipein), argv[1], strlen(argv[1]));
    pclose(pipein);
    /* close all pipes (in reverse order) */
    for (i=4; i==0; i--) {
	pclose((pipe[i]));
    }
    exit(0);
}
