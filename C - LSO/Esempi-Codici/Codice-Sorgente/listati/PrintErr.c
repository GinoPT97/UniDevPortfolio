void PrintErr(char * error) {
    if (demonize) {                       /* daemon mode */
	syslog(LOG_ERR, "%s: %m", error); /* log string and error message */
    } else {
	perror(error);
    }
    return;
}
