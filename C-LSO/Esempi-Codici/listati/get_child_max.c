get_child_max(void)
{
#ifdef CHILD_MAX
    return CHILD_MAX;
#else
    int val = sysconf(_SC_CHILD_MAX);
    if (val < 0) {
        perror("fatal error");
        exit(-1);
    }
    return val;
#endif
}
