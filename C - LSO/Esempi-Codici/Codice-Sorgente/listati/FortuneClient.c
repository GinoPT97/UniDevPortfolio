int main(int argc, char *argv[])
{
/* Variables definition */
    int n = 0;
    char *fortunefilename = "/tmp/fortune.fifo";
    char line[80];
    int fifo_server, fifo_client;
    char fifoname[80];
    int nread;
    char buffer[PIPE_BUF];
    ...
    snprintf(fifoname, 80, "/tmp/fortune.%d", getpid()); /* compose name */
    if (mkfifo(fifoname, 0622)) { /* open client fifo */
        if (errno!=EEXIST) {
            perror("Cannot create well known fifo");
            exit(-1);
        }
    }
    fifo_server = open(fortunefilename, O_WRONLY); /* open server fifo */
    if (fifo_server < 0) {
        perror("Cannot open well known fifo");
        exit(-1);
    }
    nread = write(fifo_server, fifoname, strlen(fifoname)+1); /* write name */
    close(fifo_server);                     /* close server fifo */
    fifo_client = open(fifoname, O_RDONLY); /* open client fifo */
    if (fifo_client < 0) {
        perror("Cannot open well known fifo");
        exit(-1);
    }
    nread = read(fifo_client, buffer, sizeof(buffer)); /* read answer */
    printf("%s", buffer);   /* print fortune */
    close(fifo_client);     /* close client */
    close(fifo_server);     /* close server */
    unlink(fifoname);       /* remove client fifo */
}
