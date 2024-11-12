void HandSigCHLD(int sig)
{
    int errno_save;
    int status;
    pid_t pid;
    /* save errno current value */
    errno_save = errno;
    /* loop until no */
    do {
        errno = 0;
        pid = waitpid(WAIT_ANY, &status, WNOHANG);
    } while (pid > 0);
    /* restore errno value */
    errno = errno_save;
    /* return */
    return;
}
