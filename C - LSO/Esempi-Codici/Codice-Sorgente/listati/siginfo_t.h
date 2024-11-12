siginfo_t {
    int      si_signo;   /* Signal number */
    int      si_errno;   /* An errno value */
    int      si_code;    /* Signal code */
    int      si_trapno;  /* Trap number that caused hardware-generated 
			    signal (unused on most architectures) */
    pid_t    si_pid;     /* Sending process ID */
    uid_t    si_uid;     /* Real user ID of sending process */
    int      si_status;  /* Exit value or signal */
    clock_t  si_utime;   /* User time consumed */
    clock_t  si_stime;   /* System time consumed */
    sigval_t si_value;   /* Signal value */
    int      si_int;     /* POSIX.1b signal */
    void *   si_ptr;     /* POSIX.1b signal */
    int      si_overrun; /* Timer overrun count; POSIX.1b timers */
    int      si_timerid; /* Timer ID; POSIX.1b timers */
    void *   si_addr;    /* Memory location which caused fault */
    long     si_band;    /* Band event (was int before glibc 2.3.2) */
    int      si_fd;      /* File descriptor */
}
