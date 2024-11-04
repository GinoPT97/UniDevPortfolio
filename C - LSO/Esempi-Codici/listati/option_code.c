    opterr = 0;  /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hp:c:e:")) != -1) {
        switch (i) {
        /* 
         * Handling options 
         */ 
        case 'h':   /* help option */
            printf("Wrong -h option use\n");
            usage();
            return -1;
            break;
        case 'c':   /* take wait time for children */
            wait_child = strtol(optarg, NULL, 10);    /* convert input */
            break;
        case 'p':   /* take wait time for children */
            wait_parent = strtol(optarg, NULL, 10);   /* convert input */
            break;
        case 'e':   /* take wait before parent exit */
            wait_end = strtol(optarg, NULL, 10);      /* convert input */
            break;
        case '?':   /* unrecognized options */
            printf("Unrecognized options -%c\n",optopt);
            usage();
        default:    /* should not reached */
            usage();
        }
    }
    debug("Optind %d, argc %d\n",optind,argc);
