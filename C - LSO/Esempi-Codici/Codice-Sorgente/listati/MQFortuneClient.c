int main(int argc, char *argv[])
{
    ...
    key = ftok("./MQFortuneServer.c", 1); 
    msgid = msgget(key, 0); 
    if (msgid < 0) {
        perror("Cannot find message queue");
        exit(1);
    }
    /* Main body: do request and write result */
    msg_read.mtype = 1;                /* type for request is always 1 */
    msg_read.pid = getpid();           /* use pid for communications */
    size = sizeof(msg_read.pid);  
    msgsnd(msgid, &msg_read, size, 0); /* send request message */
    msgrcv(msgid, &msg_write, MSGMAX, msg_read.pid, MSG_NOERROR);
    printf("%s", msg_write.mtext);
}
