char *fifoname = "/tmp/fortune.fifo";
int main(int argc, char *argv[])
{
/* Variables definition */
    int i, n = 0;
    char *fortunefilename = "/usr/share/games/fortunes/linux";
    char **fortune;
    char line[80];
    int fifo_server, fifo_client;
    int nread;
    ...
    if (n==0) usage();  /* if no pool depth exit printing usage info */
    Signal(SIGTERM, HandSIGTERM); /* set handlers for termination */
    Signal(SIGINT, HandSIGTERM);
    Signal(SIGQUIT, HandSIGTERM);
    i = FortuneParse(fortunefilename, fortune, n); /* parse phrases */
    if (mkfifo(fifoname, 0622)) { /* create well known fifo if does't exist */
        if (errno!=EEXIST) {
            perror("Cannot create well known fifo");
            exit(1);
        }
    }
    daemon(0, 0);
    /* open fifo two times to avoid EOF */
    fifo_server = open(fifoname, O_RDONLY);
    if (fifo_server < 0) {
        perror("Cannot open read only well known fifo");
        exit(1);
    }
    if (open(fifoname, O_WRONLY) < 0) {                        
        perror("Cannot open write only well known fifo");
        exit(1);
    }
    /* Main body: loop over requests */
    while (1) {
        nread = read(fifo_server, line, 79); /* read request */
        if (nread < 0) {
            perror("Read Error");
            exit(1);
        }
        line[nread] = 0;                     /* terminate fifo name string */
        n = random() % i;                    /* select random value */
        fifo_client = open(line, O_WRONLY);  /* open client fifo */
        if (fifo_client < 0) {
            perror("Cannot open");
            exit(1);
        }
        nread = write(fifo_client,           /* write phrase */
                      fortune[n], strlen(fortune[n])+1);
        close(fifo_client);                  /* close client fifo */
    }
}
