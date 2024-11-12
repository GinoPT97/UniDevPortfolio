int sig_handler();            /* handler function */
int main()
{
    ...
    signal(SIGINT, sig_handler);  /* establish handler */
    ...
}

int sig_handler() 
{
    signal(SIGINT, sig_handler);  /* restablish handler */
    ...                           /* process signal */
}
