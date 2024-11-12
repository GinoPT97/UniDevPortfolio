/* global variables for shared memory segment */
struct DirProp {
    int tot_size;    
    int tot_files;   
    int tot_regular; 
    int tot_fifo;    
    int tot_link;    
    int tot_dir;     
    int tot_block;   
    int tot_char;    
    int tot_sock;
} *shmptr;
key_t key;
int mutex;
/* main body */
int main(int argc, char *argv[]) 
{
    int i, pause = 10;
    ...
    if ((argc - optind) != 1) {   /* There must be remaing parameters */
        printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    if (chdir(argv[1])) {         /* chdir to be sure dir exist */
        perror("Cannot find directory to monitor");
    }
    Signal(SIGTERM, HandSIGTERM); /* set handlers for termination */
    Signal(SIGINT, HandSIGTERM);
    Signal(SIGQUIT, HandSIGTERM);
    key = ftok("~/gapil/sources/DirMonitor.c", 1); /* define a key */
    shmptr = ShmCreate(key, 4096, 0666, 0); /* get a shared memory segment */
    if (!shmptr) {
        perror("Cannot create shared memory");
        exit(1);
    }
    if ((mutex = MutexCreate(key)) == -1) {        /* get a Mutex */
        perror("Cannot create mutex");
        exit(1);
    }
    /* main loop, monitor directory properties each 10 sec */
    daemon(1, 0);           /* demonize process, staying in monitored dir */
    while (1) {
        MutexLock(mutex);   /* lock shared memory */
        memset(shmptr, 0, sizeof(struct DirProp)); /* erase previous data */
        dir_scan(argv[1], ComputeValues);          /* execute scan */
        MutexUnlock(mutex); /* unlock shared memory */
        sleep(pause);       /* sleep until next watch */
    }
}
