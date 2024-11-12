struct sockaddr_ll {
    unsigned short  sll_family;    /* Always AF_PACKET */
    unsigned short  sll_protocol;  /* Physical layer protocol */
    int             sll_ifindex;   /* Interface number */
    unsigned short  sll_hatype;    /* Header type */
    unsigned char   sll_pkttype;   /* Packet type */
    unsigned char   sll_halen;     /* Length of address */
    unsigned char   sll_addr[8];   /* Physical layer address */
};
