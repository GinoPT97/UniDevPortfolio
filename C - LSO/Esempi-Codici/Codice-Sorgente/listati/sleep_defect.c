static jmp_buff alarm_return;
unsigned int sleep(unsigned int seconds)
{
    signandler_t prev_handler;
    if ((prev_handler = signal(SIGALRM, alarm_hand)) == SIG_ERR) {
        printf("Cannot set handler for alarm\n");
        exit(1);
    }
    if (setjmp(alarm_return) == 0) { /* if not returning from handler */
        alarm(second);      /* call alarm */
        pause();            /* then wait */
    }
    /* restore previous signal handler */
    signal(SIGALRM, prev_handler);
    /* remove alarm, return remaining time */
    return alarm(0);
}
void alarm_hand(int sig) 
{
    /* check if the signal is the right one */
    if (sig != SIGALRM) { /* if not exit with error */
        printf("Something wrong, handler for SIGALRM\n");
        exit(1);
    } else {    /* return in main after the call to pause */
        longjump(alarm_return, 1);
    }
}      
