int UnSetTermAttr(int fd, tcflag_t flag) 
{
    struct termios values;
    int res;
    if (res = tcgetattr(desc, &values)) {
        perror("Cannot get attributes");
        return res;
    }
    values.c_lflag &= (~flag);
    if (res = tcsetattr(desc, TCSANOW, &values)) {
        perror("Cannot set attributes");
        return res;
    }
    return 0;
}
