void alarm_hand(int);
unsigned int sleep(unsigned int seconds)
{
    struct sigaction new_action, old_action;
    sigset_t old_mask, stop_mask, sleep_mask;
    /* set the signal handler */
    sigemptyset(&new_action.sa_mask);              /* no signal blocked */
    new_action.sa_handler = alarm_hand;            /* set handler */
    new_action.sa_flags = 0;                       /* no flags */
    sigaction(SIGALRM, &new_action, &old_action);  /* install action */
    /* block SIGALRM to avoid race conditions */
    sigemptyset(&stop_mask);                       /* init mask to empty */
    sigaddset(&stop_mask, SIGALRM);                /* add SIGALRM */
    sigprocmask(SIG_BLOCK, &stop_mask, &old_mask); /* add SIGALRM to blocked */
    /* send the alarm */
    alarm(seconds); 
    /* going to sleep enabling SIGALRM */
    sleep_mask = old_mask;                         /* take mask */
    sigdelset(&sleep_mask, SIGALRM);               /* remove SIGALRM */
    sigsuspend(&sleep_mask);                       /* go to sleep */
    /* restore previous settings */
    sigprocmask(SIG_SETMASK, &old_mask, NULL);     /* reset signal mask */    
    sigaction(SIGALRM, &old_action, NULL);         /* reset signal action */
    /* return remaining time */
    return alarm(0);
}
void alarm_hand(int sig) 
{
    return;     /* just return to interrupt sigsuspend */
}
