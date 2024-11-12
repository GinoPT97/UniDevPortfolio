struct ipc_perm
{
    key_t key;                        /* Key.  */
    uid_t uid;                        /* Owner's user ID.  */
    gid_t gid;                        /* Owner's group ID.  */
    uid_t cuid;                       /* Creator's user ID.  */
    gid_t cgid;                       /* Creator's group ID.  */
    unsigned short int mode;          /* Read/write permission.  */
    unsigned short int seq;           /* Sequence number.  */
};
