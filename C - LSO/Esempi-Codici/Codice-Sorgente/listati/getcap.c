    if (!pid) {
	capab = cap_get_proc();
	if (capab == NULL) {
	    perror("cannot get current process capabilities");
	    return 1;
	}
    } else {
	capab = cap_get_pid(pid);
	if (capab == NULL) {
	    perror("cannot get process capabilities");
	    return 1;
	}
    }

    string = cap_to_text(capab, NULL);
    printf("Capability: %s\n", string);

    cap_free(capab);
    cap_free(string);
    return 0;
