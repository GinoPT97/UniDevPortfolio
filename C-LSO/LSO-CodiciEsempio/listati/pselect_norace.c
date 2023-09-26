while (1) {
    sigprocmask(SIG_BLOCK, &newmask, &oldmask);
    if (receive_signal != 0) handle_signal();
    n = pselect(nfd, rset, wset, eset, NULL, &oldmask);
    sigprocmask(SIG_SETMASK, &oldmask, NULL);
    if (n < 0) {
	if (errno == EINTR) {
	    continue;
	}
    } else {
	handle_filedata();
    }
}
