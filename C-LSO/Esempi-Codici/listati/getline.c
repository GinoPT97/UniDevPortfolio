    size_t n = 0; 
    char *ptr = NULL;
    int nread;
    FILE * file;
    ...    
    nread = getline(&ptr, &n, file);
