int WriteVect(FILE *stream, double *vec, size_t nelem) 
{
    int size, nread;
    size = sizeof(*vec);
    if ( (nread = fwrite(vec, size, nelem, stream)) != nelem) {
        perror("Write error");
    }
    return nread;
}
