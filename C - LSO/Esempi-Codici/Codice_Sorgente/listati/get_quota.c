PyObject * get_quota(int who, int id, const char *dev) 
{
  struct dqblk dq;
  
  if (!quotactl(QCMD(Q_GETQUOTA,who), dev, id, (caddr_t) &dq)) {
    return Py_BuildValue("({s:K,s:(KK),s:K},{s:K,s:(KK),s:K})",
                         "used", dq.dqb_curspace,
                         "quota", dq.dqb_bsoftlimit, dq.dqb_bhardlimit,
                         "grace", dq.dqb_btime,
                         "used", dq.dqb_curinodes,
                         "quota", dq.dqb_isoftlimit, dq.dqb_ihardlimit,
                         "grace", dq.dqb_itime );
  } else {
    PyErr_SetFromErrno(PyExc_OSError);
    return NULL;
  }
}
