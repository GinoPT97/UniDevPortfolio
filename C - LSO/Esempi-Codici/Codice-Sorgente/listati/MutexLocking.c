/* Function CreateMutex: Create a mutex using file locking. */
int CreateMutex(const char *path_name)
{
    return open(path_name, O_EXCL|O_CREAT);
}
/* Function UnlockMutex: unlock a file. */
int FindMutex(const char *path_name)
{
    return open(path_name, O_RDWR);
}
/* Function LockMutex: lock mutex using file locking. */
int LockMutex(int fd)
{
    struct flock lock;        /* file lock structure */
    lock.l_type = F_WRLCK;    /* set type: read or write */
    lock.l_whence = SEEK_SET; /* start from the beginning of the file */
    lock.l_start = 0;         /* set the start of the locked region */
    lock.l_len = 0;           /* set the length of the locked region */
    /* do locking */
    return fcntl(fd, F_SETLKW, &lock);
}
/* Function UnlockMutex: unlock a file. */
int UnlockMutex(int fd)
{
    struct flock lock;        /* file lock structure */
    lock.l_type = F_UNLCK;    /* set type: unlock */
    lock.l_whence = SEEK_SET; /* start from the beginning of the file */
    lock.l_start = 0;         /* set the start of the locked region */
    lock.l_len = 0;           /* set the length of the locked region */
    /* do locking */
    return fcntl(fd, F_SETLK, &lock);
}
/* Function RemoveMutex: remove a mutex (unlinking the lock file). */
int RemoveMutex(const char *path_name)
{
    return unlink(path_name);
}
/* Function ReadMutex: read a mutex status. */
int ReadMutex(int fd)
{
    int res;
    struct flock lock;        /* file lock structure */
    lock.l_type = F_WRLCK;    /* set type: unlock */
    lock.l_whence = SEEK_SET; /* start from the beginning of the file */
    lock.l_start = 0;         /* set the start of the locked region */
    lock.l_len = 0;           /* set the length of the locked region */
    /* do locking */
    if ( (res = fcntl(fd, F_GETLK, &lock)) ) {
        return res;
    }
    return lock.l_type;
}
