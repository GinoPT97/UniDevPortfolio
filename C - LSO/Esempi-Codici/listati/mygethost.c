int main(int argc, char *argv[]) 
{
/* 
 * Variables definition
 */
    int i;
    struct hostent *data;
    char **alias;
    char *addr;
    char buffer[INET6_ADDRSTRLEN];
    ...
    /*
     * Main Body
     */
    if ((argc - optind) != 1) {
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    /* get resolution */
    data = gethostbyname(argv[1]);
    if (data == NULL) {
	herror("Errore di risoluzione");
	exit(1);
    }
    printf("Canonical name %s\n", data->h_name);
    alias = data->h_aliases;
    while (*alias != NULL) {
	printf("Alias %s\n", *alias);
	alias++;
    }
    if (data->h_addrtype == AF_INET) {
	printf("Address are IPv4\n");
    } else if (data->h_addrtype == AF_INET6) {
	printf("Address are IPv6\n");
    } else {
	printf("Tipo di indirizzo non valido\n");
	exit(1);
    }
    alias = data->h_addr_list;
    while (*alias != NULL) {
	addr = inet_ntop(data->h_addrtype, *alias, buffer, sizeof(buffer));
	printf("Indirizzo %s\n", addr);
	alias++;
    }    
    exit(0);
}
