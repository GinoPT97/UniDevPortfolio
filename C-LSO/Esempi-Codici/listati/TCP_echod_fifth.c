int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int list_fd, conn_fd;
    int keepalive = 0; 
    int reuse = 0;
    ... 
    /* create and bind socket */
    if ( (list_fd = sockbindopt(argv[optind], "echo", 6, 
				SOCK_STREAM, reuse)) < 0) {
	return 1;
    }   
    ...
    /* normal exit, never reached */
    exit(0);
}
