/* Function ShmCreate  Create a SysV shared memory segment */
void * ShmCreate(key_t ipc_key, int shm_size, int perm, int fill) 
{
    void * shm_ptr;
    int shm_id;     /* ID of the IPC shared memory segment */
    shm_id = shmget(ipc_key, shm_size, IPC_CREAT|perm); /* get shm ID */
    if (shm_id < 0) { 
        return NULL;
    }
    shm_ptr = shmat(shm_id, NULL, 0);         /* map it into memory */
    if (shm_ptr < 0) {
        return NULL;
    }
    memset((void *)shm_ptr, fill, shm_size);  /* fill segment */
    return shm_ptr;
}
/* Function ShmFind: Find a SysV shared memory segment  */
void * ShmFind(key_t ipc_key, int shm_size) 
{
    void * shm_ptr;
    int shm_id;     /* ID of the SysV shared memory segment */
    shm_id = shmget(ipc_key, shm_size, 0);    /* find shared memory ID */
    if (shm_id < 0) {
        return NULL;
    }
    shm_ptr = shmat(shm_id, NULL, 0);         /* map it into memory */
    if (shm_ptr < 0) {
        return NULL;
    }
    return shm_ptr;
}
/* Function ShmRemove: Schedule removal for a SysV shared memory segment  */
int ShmRemove(key_t ipc_key, void * shm_ptr) 
{
    int shm_id;     /* ID of the SysV shared memory segment */
    /* first detach segment */
    if (shmdt(shm_ptr) < 0) {
        return -1;
    }
    /* schedule segment removal */
    shm_id = shmget(ipc_key, 0, 0);           /* find shared memory ID */
    if (shm_id < 0) {
        if (errno == EIDRM) return 0;
        return -1;
    }
    if (shmctl(shm_id, IPC_RMID, NULL) < 0) { /* ask for removal */
        if (errno == EIDRM) return 0;
        return -1;
    }
    return 0;
}
