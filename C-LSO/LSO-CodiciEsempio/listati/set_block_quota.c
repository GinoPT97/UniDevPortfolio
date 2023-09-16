PyObject *set_block_quota(int who, int id, const char *dev, int soft, int hard) 
{
  struct dqblk dq;
  
  dq.dqb_bsoftlimit = soft;
  dq.dqb_bhardlimit = hard;
  dq.dqb_valid = QIF_BLIMITS;
  
  if (!quotactl(QCMD(Q_SETQUOTA,who), dev, id, (caddr_t) &dq)) {
        Py_RETURN_NONE;
  } else {
        PyErr_SetFromErrno(PyExc_OSError);
        return NULL;
  }    
}
