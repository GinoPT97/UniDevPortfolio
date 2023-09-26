#include <sys/types.h>
#include <sys/stat.h>
#include <dirent.h>        /* directory */
#include <stdlib.h>        /* C standard library */
#include <unistd.h>

/*
 * Function dir_scan: 
 * 
 * Input:  the directory name and a computation function
 * Return: 0 if OK, -1 on errors
 */
int dir_scan(char * dirname, int(*compute)(struct dirent *)) 
{
    DIR * dir;
    struct dirent *direntry;

    if ( (dir = opendir(dirname)) == NULL) { /* open directory */
        printf("Opening %s\n", dirname);     /* on error print messages */
        perror("Cannot open directory");     /* and then return */
        return -1;
    }
    fd = dirfd(dir);                        /* get file descriptor */
    fchdir(fd);                             /* change directory */
    /* loop on directory entries */
    while ( (direntry = readdir(dir)) != NULL) { /* read entry */
        if (compute(direntry)) {            /* execute function on it */
            return -1;                      /* on error return */
        }
    }
    closedir(dir);
    return 0;
}
