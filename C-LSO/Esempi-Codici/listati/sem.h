struct sem {
  short   sempid;         /* pid of last operation */
  ushort  semval;         /* current value */
  ushort  semncnt;        /* num procs awaiting increase in semval */
  ushort  semzcnt;        /* num procs awaiting semval = 0 */
};
