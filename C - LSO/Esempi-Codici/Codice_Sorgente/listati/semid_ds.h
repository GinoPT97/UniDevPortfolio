struct semid_ds
{
    struct ipc_perm sem_perm;       /* operation permission struct */
    time_t sem_otime;               /* last semop time */
    time_t sem_ctime;               /* last time changed by semctl */
    unsigned long int sem_nsems;    /* number of semaphores in set */
};
