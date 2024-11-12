struct inode {
    ...
    unsigned long           i_ino;
    atomic_t                i_count;
    unsigned int            i_nlink;
    uid_t                   i_uid;
    gid_t                   i_gid;
    dev_t                   i_rdev;
    unsigned int            i_blkbits;
    u64                     i_version;
    loff_t                  i_size;
    struct timespec         i_atime;
    struct timespec         i_mtime;
    struct timespec         i_ctime;
    ...
    const struct inode_operations   *i_op;
    const struct file_operations    *i_fop;     
    ...
};

