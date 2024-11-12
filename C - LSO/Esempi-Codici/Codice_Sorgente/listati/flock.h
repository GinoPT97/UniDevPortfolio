struct flock {
    short int l_type;   /* Type of lock: F_RDLCK, F_WRLCK, or F_UNLCK.  */
    short int l_whence; /* Where `l_start' is relative to (like `lseek').*/
    off_t l_start;      /* Offset where the lock begins.  */
    off_t l_len;        /* Size of the locked area; zero means until EOF.*/
    pid_t l_pid;        /* Process holding the lock.  */
};
