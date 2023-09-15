struct ip_mreq_source {
     struct in_addr imr_multiaddr;  /* IP multicast group address */
     struct in_addr imr_interface;  /* IP address of local interface */
     struct in_addr imr_sourceaddr; /* IP address of multicast source */
};
