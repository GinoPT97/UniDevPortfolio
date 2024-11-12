void HandSigInt(int sig)
{
    if (RemoveShm(shmname) != 0) perror("Cannot remove shared memory");
    if (sem_unlink(semname)!= 0) perror("Cannot remove semaphore") ;
    exit(0);
}
