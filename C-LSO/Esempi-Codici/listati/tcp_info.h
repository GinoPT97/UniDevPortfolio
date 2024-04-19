struct tcp_info
{
  u_int8_t      tcpi_state;
  u_int8_t      tcpi_ca_state;
  u_int8_t      tcpi_retransmits;
  u_int8_t      tcpi_probes;
  u_int8_t      tcpi_backoff;
  u_int8_t      tcpi_options;
  u_int8_t      tcpi_snd_wscale : 4, tcpi_rcv_wscale : 4;
  u_int32_t     tcpi_rto;
  u_int32_t     tcpi_ato;
  u_int32_t     tcpi_snd_mss;
  u_int32_t     tcpi_rcv_mss;
  u_int32_t     tcpi_unacked;
  u_int32_t     tcpi_sacked;
  u_int32_t     tcpi_lost;
  u_int32_t     tcpi_retrans;
  u_int32_t     tcpi_fackets;
  /* Times. */
  u_int32_t     tcpi_last_data_sent;
  u_int32_t     tcpi_last_ack_sent;     /* Not remembered, sorry.  */
  u_int32_t     tcpi_last_data_recv;
  u_int32_t     tcpi_last_ack_recv;
  /* Metrics. */
  u_int32_t     tcpi_pmtu;
  u_int32_t     tcpi_rcv_ssthresh;
  u_int32_t     tcpi_rtt;
  u_int32_t     tcpi_rttvar;
  u_int32_t     tcpi_snd_ssthresh;
  u_int32_t     tcpi_snd_cwnd;
  u_int32_t     tcpi_advmss;
  u_int32_t     tcpi_reordering;
};
