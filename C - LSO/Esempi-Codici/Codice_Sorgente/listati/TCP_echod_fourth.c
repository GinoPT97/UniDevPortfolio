int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int list_fd, conn_fd;
    int waiting = 0;
    int keepalive = 0;
    ...
    ...	

	if (pid == 0) {      /* child */
	    close(list_fd);          /* close listening socket */   
	    if (keepalive) {         /* enable keepalive ? */
		setsockopt(conn_fd, SOL_SOCKET, SO_KEEPALIVE, 
			   &keepalive, sizeof(keepalive));
	    }
	    ServEcho(conn_fd);       /* handle echo */
	    ...
}
