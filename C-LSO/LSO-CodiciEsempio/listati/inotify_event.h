struct inotify_event {
    int      wd;       /* Watch descriptor */
    uint32_t mask;     /* Mask of events */
    uint32_t cookie;   /* Unique cookie associating related
                          events (for rename(2)) */
    uint32_t len;      /* Size of 'name' field */
    char     name[];   /* Optional null-terminated name */
};
