struct dirent {
    ino_t d_ino;                    /* inode number */
    off_t d_off;                    /* offset to the next dirent */
    unsigned short int d_reclen;    /* length of this record */
    unsigned char d_type;           /* type of file;
				       by all file system types */
    char d_name[256];               /* filename */
};
