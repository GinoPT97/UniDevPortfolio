struct termios {
    tcflag_t c_iflag;      /* input mode flagss */
    tcflag_t c_oflag;      /* output modes flags */
    tcflag_t c_cflag;      /* control modes flags */
    tcflag_t c_lflag;      /* local modes flags */
    cc_t c_line;           /* line discipline */
    cc_t c_cc[NCCS];       /* control characters */
    speed_t c_ispeed;      /* input speed */
    speed_t c_ospeed;      /* output speed */
};
