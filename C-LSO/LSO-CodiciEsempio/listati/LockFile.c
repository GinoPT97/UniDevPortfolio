#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>    /* Unix standard functions */
/*
 * Function LockFile:
 */
int LockFile(const char* path_name)
{
    return open(path_name, O_EXCL|O_CREAT);
}
/*
 * Function UnlockFile:
 */
int UnlockFile(const char* path_name) 
{
    return unlink(path_name);
}
