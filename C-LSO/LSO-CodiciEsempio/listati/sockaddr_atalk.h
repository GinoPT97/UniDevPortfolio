struct sockaddr_atalk {
    sa_family_t     sat_family; /* address family */
    uint8_t         sat_port;   /* port */
    struct at_addr  sat_addr;   /* net/node */
};
struct at_addr {
    uint16_t        s_net;
    uint8_t         s_node;
};
