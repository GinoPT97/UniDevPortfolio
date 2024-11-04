...
#include <sys/epoll.h>    /* Linux epoll interface */
#include <sys/signalfd.h> /* Linux signalfd interface */

void die(char *);         /* print error and exit function */
#define MAX_EPOLL_EV 10
int main(int argc, char *argv[])
{
/* Variables definition */
    int i, n, nread, t = 10;
    char buffer[4096];
    int fifofd, epfd, sigfd;
    sigset_t sigmask;
    char *fifoname = "/tmp/reporter.fifo";
    struct epoll_event epev, events[MAX_EPOLL_EV];
    struct signalfd_siginfo siginf;
    ...
    /* Initial setup */
    if ((epfd=epoll_create(5)) < 0)                        // epoll init
	die("Failing on epoll_create");
    /* Signal setup for signalfd and epoll use */
    sigemptyset(&sigmask);
    sigaddset(&sigmask, SIGINT);
    sigaddset(&sigmask, SIGQUIT);
    sigaddset(&sigmask, SIGTERM);
    if (sigprocmask(SIG_BLOCK, &sigmask, NULL) == -1)      // block signals
	die("Failing in sigprocmask");
    if ((sigfd=signalfd(-1, &sigmask, SFD_NONBLOCK)) == -1) // take a signalfd
	die("Failing in signalfd");
    epev.data.fd = sigfd;                      // add fd to epoll 
    epev.events = EPOLLIN;
    if (epoll_ctl(epfd, EPOLL_CTL_ADD, sigfd, &epev)) 
	die("Failing in signal epoll_ctl");
    /* Fifo setup for epoll use */
    if (mkfifo(fifoname, 0622)) {  // create well known fifo if does't exist 
	if (errno!=EEXIST)
	    die("Cannot create well known fifo");
    }
    if ((fifofd = open(fifoname, O_RDWR|O_NONBLOCK)) < 0)  // open fifo
	die("Cannot open read only well known fifo");
    epev.data.fd = fifofd;                              // add fd to epoll 
    epev.events = EPOLLIN;
    if (epoll_ctl(epfd, EPOLL_CTL_ADD, fifofd, &epev)) 
	die("Failing in fifo epoll_ctl");
    /* Main body: wait something to report */
    ...
}
