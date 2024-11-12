struct sockaddr {
    sa_family_t  sa_family;     /* address family: AF_xxx */
    char         sa_data[14];   /* address (protocol-specific) */
};
