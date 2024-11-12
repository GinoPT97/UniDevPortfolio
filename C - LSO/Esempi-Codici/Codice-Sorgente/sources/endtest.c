/* endtest.c
 *
 * Copyright (C) 2003 Simone Piccardi
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
/***************************************************************
 *
 * program endtest: program to detect endianess
 *
 * Author: S. Piccardi
 * May. 2003
 *
 ***************************************************************/

#include<stdio.h>	 /* standard I/O library */

int main(int argc, char *argv[])
{
    int i, val = 0xABCDEF01;
    char * ptr;

    printf("Using value %X\n", val);
    ptr = (char *) &val;
    for (i=0; i<sizeof(int); i++) {
	printf("val[%d]=%2hhX\n", i, ptr[i]);
    }
    return 0;
}
