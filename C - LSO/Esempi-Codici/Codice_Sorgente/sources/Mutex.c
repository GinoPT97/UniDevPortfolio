/* Mutex.c
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
 * File Mutex.c: define a set of functions for mutex manipulation 
 *
 * Author: S. Piccardi Dec. 2002
 *
 *****************************************************************************/
#include <sys/sem.h>     /* SysV semaphores */
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <fcntl.h>       /* file control functions */
#include <signal.h>      /* signal constants, types and functions */

#include "Gapil.h"
/*
 * Function MutexCreate: create a mutex/semaphore
 *
 * First call create a semaphore, using the given key. 
 * We want only one semaphore so we set second argument to 1; third 
 * parameter is the flag argument, and is set to create a semaphore 
 * with R/W privilege for the user.
 * Second call initialize the semaphore to 1 (unlocked)
 *
 * Input: an IPC key value (to create an unique semaphore)
 * Return: the semaphore id# or -1 on error
 */
int MutexCreate(key_t ipc_key) 
{
    const union semun semunion={1};             /* semaphore union structure */
    int sem_id, ret;
    sem_id = semget(ipc_key, 1, IPC_CREAT|0666);         /* get semaphore ID */
    if (sem_id == -1) {                              /* if error return code */
	return sem_id;
    }
    ret = semctl(sem_id, 0, SETVAL, semunion);             /* init semaphore */
    if (ret == -1) {
	return ret;
    }
    return sem_id;
}
/*
 * Function MutexFind: get the semaphore/mutex Id given the IPC key value
 *
 * Input: an IPC key value
 */
int MutexFind(key_t ipc_key) 
{
    return semget(ipc_key, 1, 0);
}
/*
 * Function MutexRead: read the current value of the mutex/semaphore
 *
 * Input:  a semaphore id #
 * Return: the semaphore value
 */
int MutexRead(int sem_id) 
{
    return semctl(sem_id, 0, GETVAL);
}
/*
 * Define sembuf structures to lock and unlock the semaphore 
 * (used to implement a mutex)
 */
struct sembuf sem_lock={                                /* to lock semaphore */
    0,                                   /* semaphore number (only one so 0) */
    -1,                                    /* operation (-1 to use resource) */
    SEM_UNDO};                                /* flag (set for undo at exit) */
struct sembuf sem_ulock={                             /* to unlock semaphore */
    0,                                   /* semaphore number (only one so 0) */
    1,                                  /* operation (1 to release resource) */
    SEM_UNDO};                                      /* flag (in this case 0) */
/*
 * Function MutexLock: to lock a mutex/semaphore
 *
 * Input:  a semaphore id #
 * Output: semop return code  (0 OK, -1 KO)
 */
int MutexLock(int sem_id) 
{
    return semop(sem_id, &sem_lock, 1);
}
/*
 * Function MutexUnlock: to unlock a mutex/semaphore
 *
 * Input:  a semaphore id #
 * Return: semop return code (0 OK, -1 KO)
 */
int MutexUnlock(int sem_id) 
{
    return semop(sem_id, &sem_ulock, 1);
}
/*
 * Function MutexRemove: remove a mutex/semaphore
 *
 * Input:  a semaphore id #
 * Return: return code of semctl
 */
int MutexRemove(int sem_id) 
{
    return semctl(sem_id, 0, IPC_RMID);
}
/***************************************************************************** 
 *
 * File locking mutex 
 *
 * Create a mutex usinf file locking. Use file locking to lock a file
 * as a mutex request, and unlock it as a mutex release.
 *
 * Author: S. Piccardi Dec. 2002
 *
 *****************************************************************************/
/*
 * Function CreateMutex: Create a mutex using file locking.  
 *
 * Open a new lock file (creating it if not existent, and giving error
 * otherwise). Is a simple wrapper for open.
 *
 * Input:  a filename
 * Output: a file descriptor  (>0 OK, -1 KO)
 */
int CreateMutex(const char *path_name)
{
    return open(path_name, O_EXCL|O_CREAT);
}
/*
 * Function UnlockMutex: unlock a file.  
 * 
 * Open a lock file (failing if not existent). Is a simple wrapper for
 * open.
 *
 * Input:  a filename
 * Output: a return code  (>0 OK, -1 KO)
 */
int FindMutex(const char *path_name)
{
    return open(path_name, O_RDWR);
}
/*
 * Function LockMutex: lock mutex using file locking.
 *
 * Input:  a mutex (i.e. a file descriptor)
 * Output: a return code  (0 OK, -1 KO)
 */
int LockMutex(int fd)
{
    struct flock lock;                                /* file lock structure */
    /* first open the file (creating it if not existent) */
    /* set flock structure */
    lock.l_type = F_WRLCK;                        /* set type: read or write */
    lock.l_whence = SEEK_SET;        /* start from the beginning of the file */
    lock.l_start = 0;                  /* set the start of the locked region */
    lock.l_len = 0;                   /* set the length of the locked region */
    /* do locking */
    return fcntl(fd, F_SETLKW, &lock);
}
/*
 * Function UnlockMutex: unlock a file.  
 *
 * Input:  a mutex (i.e. a file descriptor)
 * Output: a return code  (0 OK, -1 KO)
 */
int UnlockMutex(int fd)
{
    struct flock lock;                                /* file lock structure */
    /* set flock structure */
    lock.l_type = F_UNLCK;                               /* set type: unlock */
    lock.l_whence = SEEK_SET;        /* start from the beginning of the file */
    lock.l_start = 0;                  /* set the start of the locked region */
    lock.l_len = 0;                   /* set the length of the locked region */
    /* do locking */
    return fcntl(fd, F_SETLK, &lock);
}
/*
 * Function RemoveMutex: remove a mutex (unlinking the lock file).
 * 
 * Just remove the lock file.
 *
 * Input:  a filename
 * Output: a return code (0 OK, -1 KO)
 */
int RemoveMutex(const char *path_name)
{
    return unlink(path_name);
}
/*
 * Function ReadMutex: read a mutex status.
 * 
 * Read the status for a mutex.
 *
 * Input:  a mutex (i.e. a file descriptor)
 * Output: the lock type (F_UNLCK,  F_RDLCK or  F_WRLCK, or -1 if KO)
 */
int ReadMutex(int fd)
{
    int res;
    struct flock lock;                                /* file lock structure */
    /* set flock structure */
    lock.l_type = F_WRLCK;                               /* set type: unlock */
    lock.l_whence = SEEK_SET;        /* start from the beginning of the file */
    lock.l_start = 0;                  /* set the start of the locked region */
    lock.l_len = 0;                   /* set the length of the locked region */
    /* do locking */
    if ( (res = fcntl(fd, F_GETLK, &lock)) ) {
	return res;
    }
    return lock.l_type;
}
