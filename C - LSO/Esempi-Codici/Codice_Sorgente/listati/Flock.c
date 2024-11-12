int main(int argc, char *argv[])
{
    int type = F_UNLCK; /* lock type: default to unlock (invalid) */
    off_t start = 0;    /* start of the locked region: default to 0 */
    off_t len = 0;      /* length of the locked region: default to 0 */
    int fd, res, i;     /* internal variables */
    int bsd = 0;        /* semantic type: default to POSIX */
    int cmd = F_SETLK;  /* lock command: default to non-blocking */
    struct flock lock;  /* file lock structure */
    ...
    if ((argc - optind) != 1) {      /* There must be remaing parameters */
        printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    if (type == F_UNLCK) {           /* There must be a -w or -r option set */
        printf("You should set a read or a write lock\n");
        usage();
    }
    fd = open(argv[optind], O_RDWR); /* open the file to be locked */
    if (fd < 0) {                    /* on error exit */
        perror("Wrong filename");
        exit(1);
    }
    /* do lock */
    if (bsd) {   /* if BSD locking */
        /* rewrite cmd for suitables flock operation values */ 
        if (cmd == F_SETLKW) {       /* if no-blocking */
            cmd = LOCK_NB;           /* set the value for flock operation */
        } else { /* else */
            cmd = 0;                 /* default is null */
        }
        if (type == F_RDLCK) cmd |= LOCK_SH; /* set for shared lock */
        if (type == F_WRLCK) cmd |= LOCK_EX; /* set for exclusive lock */
        res = flock(fd, cmd);                /* esecute lock */
    } else {     /* if POSIX locking */
        /* setting flock structure */
        lock.l_type = type;          /* set type: read or write */
        lock.l_whence = SEEK_SET;    /* start from the beginning of the file */
        lock.l_start = start;        /* set the start of the locked region */
        lock.l_len = len;            /* set the length of the locked region */
        res = fcntl(fd, cmd, &lock); /* do lock */
    }
    /* check lock results */
    if (res) {   /* on error exit */
        perror("Failed lock");
        exit(1);
    } else {     /* else write message */
        printf("Lock acquired\n");
    }
    pause();     /* stop the process, use a signal to exit */
    return 0;
}
