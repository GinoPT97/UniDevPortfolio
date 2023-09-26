struct sock_extended_err {
    u_int32_t       ee_errno;   /* error number */
    u_int8_t        ee_origin;  /* where the error originated */
    u_int8_t        ee_type;    /* type */
    u_int8_t        ee_code;    /* code */
    u_int8_t        ee_pad;
    u_int32_t       ee_info;    /* additional information */
    u_int32_t       ee_data;    /* other data */
    /* More data may follow */
};
