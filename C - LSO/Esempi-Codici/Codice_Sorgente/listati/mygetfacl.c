#include <sys/types.h>   /* primitive system data types */
#include <stdlib.h>      /* C standard library */
#include <stdio.h>       /* standard I/O library */
#include <unistd.h>      /* unix standard library */
#include <sys/acl.h>     /* acl library (use -l acl) */

int main(int argc, char *argv[])
{
/*
 * Variables definition
 */    
    acl_t acl;
    ssize_t size;
    char * buffer;
    ...
    /* must have an argument */
    if ((argc - optind) != 1) {
        printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    /* main body */
    acl = acl_get_file(argv[1], ACL_TYPE_ACCESS);
    if (acl == NULL) {
	perror("cannot get acl for file");
	return 1;
    }
    buffer = acl_to_text(acl, &size);
    if (buffer == NULL) {
	perror("cannot convert acl");
	return 1;
    }
    printf("ACL for file '%s':\n%s\n", argv[1], buffer);
    acl_free(acl);
    acl_free(buffer);
    return 0;
}
