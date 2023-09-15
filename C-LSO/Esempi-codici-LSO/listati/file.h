struct file {
    ...
    struct path             f_path;
    const struct file_operations    *f_op;
    ...
    atomic_long_t           f_count;
    unsigned int            f_flags;
    fmode_t                 f_mode;
    loff_t                  f_pos;
    ...
};
