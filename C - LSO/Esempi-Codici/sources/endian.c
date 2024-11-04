/* endian.c
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
 * routine endian: routine to detect endianess
 *
 * Author: S. Piccardi
 * May. 2003
 *
 ***************************************************************/

int endian(void)
{
/*
 * Variables definition
 */
    short magic, test;
    char * ptr;
   
    magic = 0xABCD;                     /* endianess magic number */
    ptr = (char *) &magic;              
    test = (ptr[1]<<8) + (ptr[0]&0xFF); /* build value byte by byte */
    return (magic == test);             /* if the same is little endian */ 
}
