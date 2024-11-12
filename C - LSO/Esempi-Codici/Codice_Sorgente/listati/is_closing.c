int is_closing(int sock) 
{
    struct tcp_info info;
    socklen_t len = sizeof(info);
    if (getsockopt(sock, SOL_TCP, TCP_INFO, &info, &len) != -1) {
	if (info.tcpi_state == TCP_CLOSE ||
	    info.tcpi_state == TCP_CLOSE_WAIT ||
	    info.tcpi_state == TCP_CLOSING) {
	    return 1;
	} else {
	    return 0;
	}
    } else {
	return errno;
    }
}
