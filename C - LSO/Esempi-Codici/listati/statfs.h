struct statfs {
   long    f_type;     /* tipo di filesystem */
   long    f_bsize;    /* dimensione ottimale dei blocchi di I/O */
   long    f_blocks;   /* blocchi totali nel filesystem */
   long    f_bfree;    /* blocchi liberi nel filesystem */
   long    f_bavail;   /* blocchi liberi agli utenti normali */
   long    f_files;    /* inode totali nel filesystem */
   long    f_ffree;    /* inode liberi nel filesystem */
   fsid_t  f_fsid;     /* filesystem id */
   long    f_namelen;  /* lunghezza massima dei nomi dei file */
   long    f_spare[6]; /* riservati per uso futuro */
};
