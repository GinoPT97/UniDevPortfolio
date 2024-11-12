/* pause.c
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
 * Program pause.c: 
 * A simple program that call pause, and wait indefinitely
 *
 * Author: Simone Piccardi
 * Mar. 2002
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#include <unistd.h>      /* unix standard library */

int main(int argc, char *argv[])
{
/*
 * Variables definition
 */    
    return pause();
}
