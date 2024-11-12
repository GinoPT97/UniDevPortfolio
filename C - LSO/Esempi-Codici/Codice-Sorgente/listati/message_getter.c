void HandSigInt(int sig);
#define MSGMAXSIZE 256
char *shmname = "messages";
char *semname = "messages";

int main(int argc, char *argv[]) 
{
    sem_t * sem, void * shm_ptr, time_t t;
    ...
    Signal(SIGINT, HandSigInt);
    // get a shared memory segment
    if ((shm_ptr = CreateShm(shmname, MSGMAXSIZE, 0666, 0)) == NULL) {
	perror("Cannot find shared memory");
	exit(1);
    }
    // get a locked semaphore
    if ((sem = sem_open(semname, O_CREAT|O_EXCL, 0666, 0)) == SEM_FAILED) {
	perror("Cannot open semaphore");
	exit(1);
    }
    // set initial string
    strncpy((char *) shm_ptr, argv[optind], MSGMAXSIZE);
    // do initial release
    if (sem_post(sem) != 0) {
	perror("cannot do semaphore initial release");
	exit(1);
    }
    // main loop
    while(1) {
	if (sem_getvalue(sem, &i) !=0) {             // get sem values
	    perror("cannot get semaphore value");
	    exit(1);
	}
	printf("sem=%i, ", i);                       // print sem values
	t = time(NULL);                              // get time
	printf("%s", ctime(&t));                     // print time
	if (sem_wait(sem) != 0) {                    // acquire semaphore
	    perror("cannot use semaphore");
	    exit(1);
	}
	printf("message: %s\n", (char *) shm_ptr );  // print message
	if (sem_post(sem) != 0) {                    // release semaphore
	    perror("cannot release semaphore");
	    exit(1);
	}
	sleep(1);
   }
exit(0);    
}
