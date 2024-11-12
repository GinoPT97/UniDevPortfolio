/* Function CreateShm: Create a shared memory segment mapping it */
void * CreateShm(char * shm_name, off_t shm_size, mode_t perm, int fill) 
{
    void * shm_ptr;
    int fd;
    int flag;
    /* first open the object, creating it if not existent */
    flag = O_CREAT|O_EXCL|O_RDWR;
    fd = shm_open(shm_name, flag, perm); /* get object file descriptor */
    if (fd < 0) { 
        return NULL;
    }
    /* set the object size */
    if (ftruncate(fd, shm_size)) {
        return NULL;
    }
    /* map it in the process address space */
    shm_ptr = mmap(NULL, shm_size, PROT_WRITE|PROT_READ, MAP_SHARED, fd, 0);
    if (shm_ptr == MAP_FAILED) {
        return NULL;
    }
    memset((void *) shm_ptr, fill, shm_size); /* fill segment */
    return shm_ptr;
}
/* Function FindShm: Find a POSIX shared memory segment  */
void * FindShm(char * shm_name, off_t shm_size) 
{
    void * shm_ptr;
    int fd;           /* ID of the IPC shared memory segment */
    /* find shared memory ID */
    if ((fd = shm_open(shm_name, O_RDWR|O_EXCL, 0)) < 0) {
        return NULL;
    }
    /* take the pointer to it */
    shm_ptr = mmap(NULL, shm_size, PROT_WRITE|PROT_READ, MAP_SHARED, fd, 0);
    if (shm_ptr == MAP_FAILED) {
        return NULL;
    }
    return shm_ptr;
}
/* Function RemoveShm: Remove a POSIX shared memory segment */
int RemoveShm(char * shm_name)
{
    return shm_unlink(shm_name);
}
