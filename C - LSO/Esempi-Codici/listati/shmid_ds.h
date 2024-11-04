struct shmid_ds {
     struct    ipc_perm shm_perm;  /* operation perms */
     int  shm_segsz;               /* size of segment (bytes) */
     time_t    shm_atime;          /* last attach time */
     time_t    shm_dtime;          /* last detach time */
     time_t    shm_ctime;          /* last change time */
     unsigned short shm_cpid;      /* pid of creator */
     unsigned short shm_lpid;      /* pid of last operator */
     short     shm_nattch;         /* no. of current attaches */
};
