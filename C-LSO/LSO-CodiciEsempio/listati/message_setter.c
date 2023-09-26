#define MSGMAXSIZE 256

int main(int argc, char *argv[]) 
{
    int t = 0, sem_t * sem, void *shm_ptr;
    char *shmname = "messages";
    char *semname = "messages";
    ...
    // get shared memory segment
    shm_ptr = FindShm(shmname, MSGMAXSIZE);
    if ( shm_ptr == NULL) {
	perror("Cannot find shared memory");
	exit(1);
    }
    // open semaphore
    if ( (sem = sem_open(semname, 0)) == SEM_FAILED ) {
	perror("Cannot open semaphore");
	exit(1);
    }
    // get semaphore
    if ( sem_wait(sem) != 0) {
	perror("cannot use semaphore");
	exit(1);
    }
    strncpy((char *) shm_ptr, argv[optind],  MSGMAXSIZE); // modify message 
    printf("Sleeping for %i seconds\n", t);               // print wait time
    sleep(t);                                             // sleep
    // release semaphore
    if ( sem_post(sem) != 0) {
	perror("cannot release semaphore");
	exit(1);
    }
    exit(0);
}
