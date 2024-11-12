#include <errno.h>       /* error definitions and routines */ 
#include <stdlib.h>      /* C standard library */
#include <unistd.h>      /* unix standard library */
#include <stdio.h>       /* standard I/O library */
#include <string.h>      /* string functions */

/* Help printing routine */
void usage(void);

int main(int argc, char *argv[])
{
/* 
 * Variables definition  
 */
    int nchild, i;
    pid_t pid;
    int wait_child  = 0;
    int wait_parent = 0;
    int wait_end    = 0;
    ...        /* handling options */
    nchild = atoi(argv[optind]);
    printf("Test for forking %d child\n", nchild);
    /* loop to fork children */
    for (i=0; i<nchild; i++) {
        if ( (pid = fork()) < 0) { 
            /* on error exit */ 
            printf("Error on %d child creation, %s\n", i+1, strerror(errno));
            exit(-1); 
        }
        if (pid == 0) {   /* child */
            printf("Child %d successfully executing\n", ++i);
            if (wait_child) sleep(wait_child);
            printf("Child %d, parent %d, exiting\n", i, getppid());
            exit(0);
        } else {          /* parent */
            printf("Spawned %d child, pid %d \n", i+1, pid);
            if (wait_parent) sleep(wait_parent);
            printf("Go to next child \n");
        }
    }
    /* normal exit */
    if (wait_end) sleep(wait_end);
    return 0;
}
