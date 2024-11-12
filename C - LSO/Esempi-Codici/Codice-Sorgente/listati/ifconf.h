struct ifconf {
    int                ifc_len; /* size of buffer */
    union {
        char *         ifc_buf; /* buffer address */
        struct ifreq * ifc_req; /* array of structures */
    };
};
