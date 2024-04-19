struct msqid_ds {
    struct ipc_perm msg_perm;    /* structure for operation permission */
    time_t msg_stime;            /* time of last msgsnd command */
    time_t msg_rtime;            /* time of last msgrcv command */
    time_t msg_ctime;            /* time of last change */
    unsigned long __msg_cbytes;  /* current number of bytes on queue */
    msgqnum_t msg_qnum;          /* number of messages currently on queue */
    msglen_t msg_qbytes;         /* max number of bytes allowed on queue */
    pid_t msg_lspid;             /* pid of last msgsnd() */
    pid_t msg_lrpid;             /* pid of last msgrcv() */
};
