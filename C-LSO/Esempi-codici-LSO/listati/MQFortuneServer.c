int msgid;          /* Message queue identifier */
int main(int argc, char *argv[])
{
/* Variables definition */
    int i, n = 0;
    char **fortune;       /* array of fortune message string */
    char *fortunefilename = "/usr/share/games/fortunes/linux"; /* file name */
    struct msgbuf_read {  /* message struct to read request from clients */
        long mtype;       /* message type, must be 1 */
        long pid;         /* message data, must be the pid of the client */
    } msg_read;
    struct msgbuf_write { /* message struct to write result to clients */
        long mtype;       /* message type, will be the pid of the client*/
        char mtext[MSGMAX]; /* message data, will be the fortune */
    } msg_write;
    key_t key;            /* Message queue key */
    int size;             /* message size */
    ...
    Signal(SIGTERM, HandSIGTERM); /* set handlers for termination */
    Signal(SIGINT, HandSIGTERM);
    Signal(SIGQUIT, HandSIGTERM);
    if (n==0) usage();    /* if no pool depth exit printing usage info */
    i = FortuneParse(fortunefilename, fortune, n); /* parse phrases */
    /* Create the queue */
    key = ftok("./MQFortuneServer.c", 1); 
    msgid = msgget(key, IPC_CREAT|0666);
    if (msgid < 0) {
        perror("Cannot create message queue");
        exit(1);
    }
    /* Main body: loop over requests */
    daemon(0, 0);
    while (1) {
        msgrcv(msgid, &msg_read, sizeof(int), 1, MSG_NOERROR);
        n = random() % i;             /* select random value */
        strncpy(msg_write.mtext, fortune[n], MSGMAX);
        size = min(strlen(fortune[n])+1, MSGMAX);  
        msg_write.mtype=msg_read.pid; /* use request pid as type */
        msgsnd(msgid, &msg_write, size, 0);
    }
}
/*
 * Signal Handler to manage termination
 */
void HandSIGTERM(int signo) {
    msgctl(msgid, IPC_RMID, NULL);    /* remove message queue */
    exit(0);
}
