struct dqblk
  {
    u_int64_t dqb_bhardlimit; /* absolute limit on disk quota blocks alloc */
    u_int64_t dqb_bsoftlimit; /* preferred limit on disk quota blocks */
    u_int64_t dqb_curspace;   /* current quota block count */
    u_int64_t dqb_ihardlimit; /* maximum # allocated inodes */
    u_int64_t dqb_isoftlimit; /* preferred inode limit */
    u_int64_t dqb_curinodes;  /* current # allocated inodes */
    u_int64_t dqb_btime;      /* time limit for excessive disk use */
    u_int64_t dqb_itime;      /* time limit for excessive files */
    u_int32_t dqb_valid;      /* bitmask of QIF_* constants */
  };
