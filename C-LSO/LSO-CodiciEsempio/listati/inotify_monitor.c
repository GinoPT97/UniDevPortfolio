#include <sys/inotify.h> /* Linux inotify interface */
...
int main(int argc, char *argv[]) 
{
    int i, narg, nread;
    int fd, wd;
    char buffer[512 * (sizeof(struct inotify_event) + 16)];
    unsigned int mask=0;
    struct inotify_event * event;
    ...
    narg = argc - optind; 
    if (narg < 1) { /* There must be at least one argument */
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    fd = inotify_init();       /* initialize inotify */
    if (fd < 0) {
	perror("Failing on inotify_init");
	exit(-1);
    }
    i = 0;
    while (i < narg) {
	wd = inotify_add_watch(fd, argv[optind+i], mask);  /* add watch */
	if (wd <= 0) {
	    printf("Failing to add watched file %s, mask %i; %s\n", 
		   argv[optind+i], mask, strerror(errno));
	    exit(-1);
	}
	i++;
    }
    /* Main Loop: read events and print them */
    while (1) {
	nread = read(fd, buffer, sizeof(buffer));
	if (nread < 0) {
	    if (errno == EINTR) {
		continue;
	    } else {
		perror("error reading inotify data");
		exit(1);
	    }
	} else {
	    i = 0;
	    while (i < nread) { 
		event = (struct inotify_event *) buffer + i;
		printf("Watch descriptor %i\n", event->wd);
		printf("Observed event on %s\n", argv[optind-1+event->wd]);
		if (event->len) {
		    printf("On file %s\n", event->name);
		}
		printevent(event->mask);
		i += sizeof(struct inotify_event) + event->len;
	    }
	}
    }
    return 0;
}
