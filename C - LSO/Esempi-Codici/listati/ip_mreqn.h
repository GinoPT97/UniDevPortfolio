struct ip_mreqn {
    struct in_addr imr_multiaddr; /* IP multicast group address */
    struct in_addr imr_address;   /* IP address of local interface */
    int            imr_ifindex;   /* interface index */
};
