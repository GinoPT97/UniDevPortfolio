int main(int argc, char *argv[])
{
    int list_fd, conn_fd; 
    pid_t pid;
    struct sockaddr_in serv_add;
    ...
    /* create and init socket */
    if ( (list_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
	perror("Socket creation error");
	exit(1);
    }
    memset((void *)&serv_add, 0, sizeof(serv_add)); /* clear server address */
    serv_add.sin_family = AF_INET;                  /* address type is INET */
    serv_add.sin_port = htons(7);                   /* echo port is 7 */
    serv_add.sin_addr.s_addr = htonl(INADDR_ANY);   /* connect from anywhere */
    if (bind(list_fd, (struct sockaddr *)&serv_add, sizeof(serv_add)) < 0) {
	perror("bind error");
	exit(1);
    }
    /* give away privileges and go daemon */
    if (setgid(65534) !=0) { /* first give away group privileges */
	perror("cannot give away group privileges");
	exit(1);
    }
    if (setuid(65534) !=0) { /* and only after user ... */
	perror("cannot give away user privileges");
	exit(1);
    }
    if (demonize) {          /* go daemon */
        openlog(argv[0], 0, LOG_DAEMON); /* open logging */
 	if (daemon(0, 0) != 0) {
	    perror("cannot start as daemon");
	    exit(1);
	}
    }
    ...
