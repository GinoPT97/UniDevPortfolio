/* FortuneParse.c
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
 * Routine FortuneParse
 * parse fortunes from a fortune file
 *
 * Author: Simone Piccardi
 * Aug. 2002
 *
 * Usage: int FortuneParse(char *file, char ** fortune, int n);
 * Read n fortunes from fortune file file, and put it into the
 * string array fortune
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>       /* standard I/O library */
#include <stdlib.h>	 /* C standard library */
#include <string.h>	 /* C strings library */
#include <errno.h>	 /* error definitions and routines */
#include <fcntl.h>	 /* file control functions */

#include "macros.h"

/* Subroutines declaration */
extern void usage(void);

int FortuneParse(char *file, char **fortune, int n)
{
/* Variables definition */
    FILE *fortunefile;
    char line[80];
    int i, len;
    /* 
     *  fortune file scanning, read string in memory 
     */
    fortunefile = fopen(file,"r");
    if (fortunefile == NULL) {                       /* on open error exit */
	printf("On file %s\n", file);
	perror("Cannot open file");
	exit(-1);
    }
    i = 0;
    do {
	if (!fgets(line, 80, fortunefile)) {
	    if (feof(fortunefile)) break;
	    perror("Read error");
	    exit(-1);
	}
	debug("i=%d, line=%s", i, line);
	if (line[0]=='%') {
	    if (fortune[i]!=NULL) i++;
	    continue;
	}
	len = strlen(line) + 1;
	if (fortune[i]==NULL) {
	    fortune[i] = malloc(len);
	    strncpy(fortune[i], line, len);
	} else {
	    fortune[i] = realloc(fortune[i], strlen(fortune[i])+len+1);
	    strncat(fortune[i], line, len);
	}
    } while (i<n);
    return i;
}
