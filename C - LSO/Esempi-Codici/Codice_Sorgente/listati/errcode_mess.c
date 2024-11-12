    /* convert string to number */
    err = strtol(argv[optind], NULL, 10);
    /* testing error condition on conversion */
    if (err==LONG_MIN) {
        perror("Underflow on error code");
        return 1;
    } else if (err==LONG_MIN) {
        perror("Overflow on error code");
        return 1;
    }
    /* conversion is fine */
    if (message) {
        printf("Error message for %d is %s\n", err, strerror(err));
    }
    if (label) {
        printf("Error label for %d is %s\n", err, err_code[err]);
    }
