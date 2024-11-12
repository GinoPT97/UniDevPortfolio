struct msgbuf {
     long mtype;          /* message type, must be > 0 */
     char mtext[LENGTH];  /* message data */
};
