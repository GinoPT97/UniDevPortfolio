struct histogram {
    int nbins; 
    double max, min;
    double *bin;
} histo; 

int WriteStruct(FILE *stream, struct histogram *histo) 
{
    if ( fwrite(histo, sizeof(*histo), 1, stream) !=1) {
        perror("Write error");
    }
    return nread;
}
