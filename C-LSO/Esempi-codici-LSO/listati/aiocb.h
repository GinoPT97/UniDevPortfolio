struct aiocb
{
    int aio_fildes;               /* File descriptor.  */
    off_t aio_offset;             /* File offset */
    volatile void *aio_buf;       /* Location of buffer.  */
    size_t aio_nbytes;            /* Length of transfer.  */
    int aio_reqprio;              /* Request priority offset.  */
    struct sigevent aio_sigevent; /* Signal number and value.  */
    int aio_lio_opcode;           /* Operation to be performed.  */
};
