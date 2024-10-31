struct sigevent {
    int          sigev_notify;    /* Notification method */
    int          sigev_signo;     /* Timer expiration signal */
    union sigval sigev_value;     /* Value accompanying signal or
                                     passed to thread function */
    /* Function used for thread notifications (SIGEV_THREAD) */
    void         (*sigev_notify_function) (union sigval);                   
    /* Attributes for notification thread (SIGEV_THREAD) */
    void         *sigev_notify_attributes;
    /* ID of thread to signal (SIGEV_THREAD_ID) */
    pid_t        sigev_notify_thread_id;
};
