struct ip_msfilter {
    struct in_addr imsf_multiaddr; /* IP multicast group address */
    struct in_addr imsf_interface; /* IP address of local interface */
    uint32_t       imsf_fmode;     /* Filter-mode */
    uint32_t       imsf_numsrc;    /* Number of sources in the 
				      following array */
    struct in_addr imsf_slist[1];  /* Array of source addresses */
};
