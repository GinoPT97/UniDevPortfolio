struct mq_attr {
        long    mq_flags;       /* message queue flags                  */
        long    mq_maxmsg;      /* maximum number of messages           */
        long    mq_msgsize;     /* maximum message size                 */
        long    mq_curmsgs;     /* number of messages currently queued  */
};
