/* Routine  to compute directory properties inside dir_scan */
int ComputeValues(struct dirent * direntry) 
{
    struct stat data;
    stat(direntry->d_name, &data);      /* get stat data */
    shmptr->tot_size += data.st_size;
    shmptr->tot_files++;
    if (S_ISREG(data.st_mode)) shmptr->tot_regular++;
    if (S_ISFIFO(data.st_mode)) shmptr->tot_fifo++;
    if (S_ISLNK(data.st_mode)) shmptr->tot_link++;
    if (S_ISDIR(data.st_mode)) shmptr->tot_dir++;
    if (S_ISBLK(data.st_mode)) shmptr->tot_block++;
    if (S_ISCHR(data.st_mode)) shmptr->tot_char++;
    if (S_ISSOCK(data.st_mode)) shmptr->tot_sock++;
    return 0;
}
/* Signal Handler to manage termination */
void HandSIGTERM(int signo) {
    MutexLock(mutex);
    ShmRemove(key, shmptr);
    MutexRemove(mutex);
    exit(0);
}
