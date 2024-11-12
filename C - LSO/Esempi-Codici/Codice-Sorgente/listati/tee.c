#define _GNU_SOURCE
#include <fcntl.h>       /* file control functions */
...
int main(int argc, char *argv[])
{
    size_t size = 4096;
    int fd, len, nwrite;
    struct stat fdata;
    ...
    /* tee loop */
    while (1) {
        /* copy stdin to stdout */
        len = tee(STDIN_FILENO, STDOUT_FILENO, size, 0);
	if (len == 0) break;
        if (len < 0) {
            if (errno == EAGAIN) {
		continue;
	    } else {
		perror("error on tee stdin to stdout");
		exit(EXIT_FAILURE);
	    }
	}
        /* write data to the file using splice */
        while (len > 0) {
            nwrite = splice(STDIN_FILENO, NULL, fd, NULL, len, SPLICE_F_MOVE);
            if (nwrite < 0) {
                perror("error on splice stdin to file");
                break;
            }
            len -= nwrite;
        }
    }
    exit(EXIT_SUCCESS);
}
