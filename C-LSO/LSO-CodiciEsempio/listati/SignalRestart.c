inline SigFunc * SignalRestart(int signo, SigFunc *func) 
{
    struct sigaction new_handl, old_handl;
    new_handl.sa_handler = func;             /* set signal handler */
    new_handl.sa_flags = SA_RESTART;         /* restart system call */
    /* clear signal mask: no signal blocked during execution of func */
    if (sigemptyset(&new_handl.sa_mask)!=0){ /* initialize signal set */
        return SIG_ERR;
    }
    /* change action for signo signal */
    if (sigaction(signo, &new_handl, &old_handl)){ 
        return SIG_ERR;
    }
    return (old_handl.sa_handler);
}
