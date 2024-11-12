#define _GNU_SOURCE
#include <fcntl.h>       /* file control functions */
...

int main(int argc, char *argv[])
{
    int size = 4096;
    int pipefd[2];
    int in_fd, out_fd;
    int nread, nwrite;
    ...
    /* copy loop */
    while (1) {
	nread = splice(in_fd, NULL, pipefd[1], NULL, size, 
		       SPLICE_F_MOVE|SPLICE_F_MORE);
	if (nread == 0)	break;
	if (nread < 0) {
	    if (errno == EINTR) {
		continue;
	    } else {
		perror("read error");
		exit(EXIT_FAILURE);
	    } 
	}
	while (nread > 0) {
	    nwrite = splice(pipefd[0], NULL, out_fd, NULL, nread, 
			    SPLICE_F_MOVE|SPLICE_F_MORE);
	    if (nwrite < 0) {
		if (errno == EINTR)
		    continue;
		else {
		    perror("write error");
		    exit(EXIT_FAILURE);
		}
	    }
	    nread -= nwrite;
	}
    }
    return EXIT_SUCCESS;
}
