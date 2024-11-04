struct sockaddr_storage {
    sa_family_t  ss_family;                  /* address family: AF_xxx */
    __ss_aligntype __ss_align;
    char         __ss_padding[_SS_PADSIZWE]; /* address (protocol-specific) */
};
