int main(int argc, char *argv[]) 
{
    key_t key;
    ...
    /* create needed IPC objects */
    key = ftok("~/gapil/sources/DirMonitor.c", 1); /* define a key */
    if (!(shmptr = ShmFind(key, 4096))) { /* get a shared memory segment */
        perror("Cannot find shared memory");
        exit(1);
    }
    if ((mutex = MutexFind(key)) == -1) { /* get the Mutex */
        perror("Cannot find mutex");
        exit(1);
    }
    /* main loop */
    MutexLock(mutex);                     /* lock shared memory */
    printf("Ci sono %d file dati\n", shmptr->tot_regular);
    printf("Ci sono %d directory\n", shmptr->tot_dir);
    printf("Ci sono %d link\n", shmptr->tot_link);
    printf("Ci sono %d fifo\n", shmptr->tot_fifo);
    printf("Ci sono %d socket\n", shmptr->tot_sock);
    printf("Ci sono %d device a caratteri\n", shmptr->tot_char);
    printf("Ci sono %d device a blocchi\n", shmptr->tot_block);
    printf("Totale  %d file, per %d byte\n",
           shmptr->tot_files, shmptr->tot_size);
    MutexUnlock(mutex);                   /* unlock shared memory */
}
