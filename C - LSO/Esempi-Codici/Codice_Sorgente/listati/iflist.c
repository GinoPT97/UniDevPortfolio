int i, num, ret, sock;
struct ifconf iflist;
char buffer[4096];
struct sockaddr_in * address;
...
/* create a socket for the operation */
sock = socket(PF_INET, SOCK_STREAM, 0);
if (sock < 0) {
    perror("Socket creation error");
    return 1;
}
/* init values for the ifcon structure and do SIOCGIFCONF */
iflist.ifc_len = sizeof(buffer);
iflist.ifc_buf = buffer;
ret = ioctl(sock, SIOCGIFCONF, &iflist);
if (ret < 0) {
    perror("ioctl failed");
    return 1;
}
/* check that we have all data */
if (iflist.ifc_len == sizeof(buffer)) {
    printf("Probable overflow, too many interfaces, cannot read\n");
    return 1;
} else {
    num = iflist.ifc_len/sizeof(struct ifreq);
    printf("Found %i interfaces \n", num);
}
/* loop on interface to write data */
for (i=0; i < num; i++) {
    address = (struct sockaddr_in *) &iflist.ifc_req[i].ifr_addr;
    printf("Interface %s, address %s\n", iflist.ifc_req[i].ifr_name, 
	   inet_ntoa(address->sin_addr));
}
return 0;
