struct utsname {
    char sysname[];      /* Operating system name (e.g., "Linux") */
    char nodename[];     /* Name within "some implementation-defined
                                     network" */
    char release[];      /* OS release (e.g., "2.6.28") */
    char version[];      /* OS version */
    char machine[];      /* Hardware identifier */
#ifdef _GNU_SOURCE
    char domainname[];   /* NIS or YP domain name */
#endif
};
