/* SharedMem.c
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
/***************************************************************
 *
 * File SharedMem.c 
 * Routines for Shared Memory use. 
 *
 * Define two interfaces, the first one use SysV shared memory, the
 * second POSIX shared memory.
 *
 * Author: S. Piccardi
 *
 ***************************************************************/
#include <sys/shm.h>     /* SysV shared memory */
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <stdio.h>       /* standard I/O library */
#include <fcntl.h>       /* file control functions */
#include <signal.h>      /* signal constants, types and functions */
#include <unistd.h>      /* unix standard library */
#include <sys/mman.h>
#include <string.h>      /* C strings library */
#include <errno.h>       /* error definitions and routines */

#include "macros.h"
/* *************************************************************************
 *
 *  Functions for SysV shared memory
 *
 * ************************************************************************* */
/*
 * Function ShmCreate:
 * Create and attach a SysV shared memory segment to the current process.
 *
 * First call get a shared memory segment with KEY key access and size SIZE,
 * by creating it with R/W privilege for the user (this is the meaning of
 * the ored flags). The function return an identifier shmid used for any 
 * further reference to the shared memory segment. 
 * Second call attach the shared memory segment to this process and return a
 * pointer to it (of void * type). 
 * Then initialize shared memory to the given value
 *
 * Input:  an IPC key value
 *         the shared memory segment size
 *         the permissions
 *         the fill value
 * Return: the address of the shared memory segment (NULL on error)
 */
void * ShmCreate(key_t ipc_key, int shm_size, int perm, int fill) 
{
    void * shm_ptr;
    int shm_id;                       /* ID of the IPC shared memory segment */
    shm_id = shmget(ipc_key, shm_size, IPC_CREAT|perm);        /* get shm ID */
    if (shm_id < 0) { 
	return NULL;
    }
    shm_ptr = shmat(shm_id, NULL, 0);                  /* map it into memory */
    if (shm_ptr < 0) {
	return NULL;
    }
    memset((void *)shm_ptr, fill, shm_size);                 /* fill segment */
    return shm_ptr;
}
/*
 * Function ShmFind:
 * Find a SysV shared memory segment 
 * Input:  an IPC key value
 *         the shared memory segment size
 * Return: the address of the segment (NULL on error)
 */
void * ShmFind(key_t ipc_key, int shm_size) 
{
    void * shm_ptr;
    int shm_id;                      /* ID of the SysV shared memory segment */
    shm_id = shmget(ipc_key, shm_size, 0);          /* find shared memory ID */
    if (shm_id < 0) {
	return NULL;
    }
    shm_ptr = shmat(shm_id, NULL, 0);                  /* map it into memory */
    if (shm_ptr < 0) {
	return NULL;
    }
    return shm_ptr;
}
/*
 * Function ShmRemove:
 * Schedule removal for a SysV shared memory segment 
 * Input:  an IPC key value
 *         the shared memory segment size
 * Return: 0 on success, -1 on error
 */
int ShmRemove(key_t ipc_key, void * shm_ptr) 
{
    int shm_id;                      /* ID of the SysV shared memory segment */
    /* first detach segment */
    if (shmdt(shm_ptr) < 0) {
	return -1;
    }
    /* schedule segment removal */
    shm_id = shmget(ipc_key, 0, 0);                 /* find shared memory ID */
    if (shm_id < 0) {
	if (errno == EIDRM) return 0;
	return -1;
    }
    if (shmctl(shm_id, IPC_RMID, NULL) < 0) {             /* ask for removal */
	if (errno == EIDRM) return 0;
	return -1;
    }
    return 0;
}
/* *************************************************************************
 *
 *  Functions for POSIX shared memory
 *
 * ************************************************************************* */
/*
 * Function CreateShm:
 * Create a POSIX shared memory segment and map it to the current process.
 *
 *
 * Input:  a pathname
 *         the shared memory segment size
 *         the permissions
 *         the fill value
 * Return: the address of the shared memory segment (NULL on error)
 */
void * CreateShm(char * shm_name, off_t shm_size, mode_t perm, int fill) 
{
    void * shm_ptr;
    int fd;
    int flag;
    /* first open the object, creating it if not existent */
    flag = O_CREAT|O_EXCL|O_RDWR;
    fd = shm_open(shm_name, flag, perm);    /* get object file descriptor */
    if (fd < 0) { 
	perror("errore in shm_open");
	return NULL;
    }
    /* set the object size */
    if (ftruncate(fd, shm_size)) {
	perror("errore in ftruncate");
	return NULL;
    }
    /* map it in the process address space */
    shm_ptr = mmap(NULL, shm_size, PROT_WRITE|PROT_READ, MAP_SHARED, fd, 0);
    if (shm_ptr == MAP_FAILED) {
	perror("errore in mmap");
	return NULL;
    }
    memset((void *) shm_ptr, fill, shm_size);                /* fill segment */
    return shm_ptr;
}
/*
 * Function FindShm:
 * Find a POSIX shared memory segment 
 * Input:  a name
 *         the shared memory segment size
 * Return: the address of the segment (NULL on error)
 */
void * FindShm(char * shm_name, off_t shm_size) 
{
    void * shm_ptr;
    int fd;                           /* ID of the IPC shared memory segment */
    /* find shared memory ID */
    if ((fd = shm_open(shm_name, O_RDWR|O_EXCL, 0)) < 0) {
	debug("Cannot open %s\n", shm_name);
	return NULL;
    }
    /* take the pointer to it */
    shm_ptr = mmap(NULL, shm_size, PROT_WRITE|PROT_READ, MAP_SHARED, fd, 0);
    if (shm_ptr == MAP_FAILED) {
	return NULL;
    }
    return shm_ptr;
}
/*
 * Function RemoveShm:
 * Remove a POSIX shared memory segment 
 * Input:  the object name
 * Return: 0 on success, -1 on error
 */
int RemoveShm(char * shm_name)
{
    return shm_unlink(shm_name);
}
