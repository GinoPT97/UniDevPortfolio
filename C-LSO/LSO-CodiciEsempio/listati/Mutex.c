/* Function MutexCreate: create a mutex/semaphore */
int MutexCreate(key_t ipc_key) 
{
    const union semun semunion={1}; /* semaphore union structure */
    int sem_id, ret;
    sem_id = semget(ipc_key, 1, IPC_CREAT|0666); /* get semaphore ID */
    if (sem_id == -1) {             /* if error return code */
        return sem_id;
    }
    ret = semctl(sem_id, 0, SETVAL, semunion);   /* init semaphore */
    if (ret == -1) {
        return ret;
    }
    return sem_id;
}
/* Function MutexFind: get the semaphore/mutex Id given the IPC key value */
int MutexFind(key_t ipc_key) 
{
    return semget(ipc_key,1,0);
}
/* Function MutexRead: read the current value of the mutex/semaphore */
int MutexRead(int sem_id) 
{
    return semctl(sem_id, 0, GETVAL);
}
/* Define sembuf structures to lock and unlock the semaphore  */
struct sembuf sem_lock={  /* to lock semaphore */
    0,                    /* semaphore number (only one so 0) */
    -1,                   /* operation (-1 to use resource) */
    SEM_UNDO};            /* flag (set for undo at exit) */
struct sembuf sem_ulock={ /* to unlock semaphore */
    0,                    /* semaphore number (only one so 0) */
    1,                    /* operation (1 to release resource) */
    SEM_UNDO};            /* flag (in this case 0) */
/* Function MutexLock: to lock a mutex/semaphore */
int MutexLock(int sem_id) 
{
    return semop(sem_id, &sem_lock, 1);
}
/* Function MutexUnlock: to unlock a mutex/semaphore */
int MutexUnlock(int sem_id) 
{
    return semop(sem_id, &sem_ulock, 1);
}
/* Function MutexRemove: remove a mutex/semaphore */
int MutexRemove(int sem_id) 
{
    return semctl(sem_id, 0, IPC_RMID);
}
