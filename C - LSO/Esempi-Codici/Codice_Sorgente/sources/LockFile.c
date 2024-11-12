/* LockFile.c
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
/*****************************************************************************
 *
 * File LockFile.h: 
 * Function to manipulate lock files.
 *
 * Author: S. Piccardi, Dec 2002
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <unistd.h>      /* unix standard library */
#include <fcntl.h>       /* file control functions */
/*
 * Function LockFile:
 *
 * Create a lockfile of the given pathname.  Fail and exit in case of
 * error or existence of the same lock file, using unlink do not need
 * to remove the file.
 *
 * Author: Simone Piccardi, Dec. 2002
 */
int LockFile(const char* path_name)
{
    return open(path_name, O_EXCL|O_CREAT);
}
/*
 * Function UnlockFile:
 * Remove a lockfile of the given pathname.
 *
 * Author: Simone Piccardi, Dec. 2002
 */
int UnlockFile(const char* path_name) 
{
    return unlink(path_name);
}
