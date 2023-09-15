int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int list_fd, conn_fd;
    ...
    /* Main code begin here */
    if (compat) {                             /* install signal handler */
	Signal(SIGCHLD, HandSigCHLD);         /* non restarting handler */
    } else {
	SignalRestart(SIGCHLD, HandSigCHLD);  /* restarting handler */
    }
    /* create and bind socket */
    if ( (list_fd = sockbind(argv[optind], "echo", 6, SOCK_STREAM)) < 0) {
	return 1;
    }   
    ...
}
