void alarm_hand(int sig) {
    /* check if the signal is the right one */
    if (sig != SIGALRM) { /* if not exit with error */
        printf("Something wrong, handler for SIGALRM\n");
        exit(1);
    } else {    /* do nothing, just interrupt pause */
        return;
    }
}
unsigned int sleep(unsigned int seconds)
{
    sighandler_t prev_handler;
    /* install and check new handler */
    if ((prev_handler = signal(SIGALRM, alarm_hand)) == SIG_ERR) {
        printf("Cannot set handler for alarm\n"); 
        exit(-1);
    }
    /* set alarm and go to sleep */
    alarm(seconds); 
    pause(); 
    /* restore previous signal handler */
    signal(SIGALRM, prev_handler);
    /* return remaining time */
    return alarm(0);
}
