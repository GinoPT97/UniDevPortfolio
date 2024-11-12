#include <sys/types.h>
#include <sys/stat.h>
#include <dirent.h>        /* directory */
#include <stdlib.h>        /* C standard library */
#include <unistd.h>
/* computation function for dir_scan */
int do_ls(struct dirent * direntry);
/* main body */
int main(int argc, char *argv[]) 
{
    ...
    if ((argc - optind) != 1) {          /* There must be remaing parameters */
        printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    dir_scan(argv[1], do_ls);
    exit(0);
}
/*
 * Routine to print file name and size inside dir_scan
 */
int do_ls(struct dirent * direntry) 
{
    struct stat data;

    stat(direntry->d_name, &data);       /* get stat data */
    printf("File: %s \t size: %d\n", direntry->d_name, data.st_size);
    return 0;
}
