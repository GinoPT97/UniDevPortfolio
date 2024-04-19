// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
// SERVER TCP concorrente multi-task
// Sintassi: ServerTCP [port]
//			 port: numero di port da utilizzare
//
// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

// Librerie
#include <sys/socket.h>
#include <netinet/in.h>

#include <stdio.h>
#include <string.h>
#include <stdlib.h> 	// atoi()
#include <unistd.h> 	// close()

// Costanti
#define PORT 5194
#define QLEN 6

int main(int argc, char *argv[])
{
	struct sockaddr_in sad;		// SERVER
	int    sd;			// descrittore della socket
	unsigned short port;		// numero di port del servizio
	char   buf[1000];		// buffer in cui memorizzare la stringa

	// Lettura delle opzioni a riga di comando
	if (argc > 1) {
		port = atoi(argv[1]);
		if (port == 0) {
			fprintf(stderr, "numero di port non valido\n");
			return 1;
		}
	} else {
		port = PORT;
	}

	// Inzializzazione della struttura sad
	memset((char *)&sad, 0, sizeof(sad));
	sad.sin_family = AF_INET;
	sad.sin_addr.s_addr = INADDR_ANY;
	sad.sin_port = htons(port);		// conversione di port da "host byte order" a "TCP/IP network order"
	
	// Creazione di una socket di tipo SOCK_STREAM - TCP
	if ((sd = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0) {
		fprintf(stderr, "creazione della socket fallita\n");
		return 1;
	}

	// Configurazione dell'endpoint locale della socket utilizzando 
	// i parametri della struttura sockaddr_in sad
	if (bind(sd, (struct sockaddr *)&sad, sizeof(sad)) < 0) {
		fprintf(stderr, "bind fallito\n");
		return 1;
	}

	// Avvio dell'ascolto sulla socket sd
	if (listen(sd, QLEN) < 0) {
		fprintf(stderr, "listen fallito\n");
		exit(1);
	}
	printf("Server in ascolto sul port TCP %d\n", port);
	
	// Ciclo infinito
	while (1) {
		int nsd;	// nuova socket relativa all'ultima connessione
		char src[1000]; // buffer per la stringa modificata
		int num_proc;	// numero di processi creati
		int i;
		
		// Attesa di una connessione TCP
		if ((nsd = accept(sd, NULL, NULL)) < 0) {
			fprintf(stderr, "accept fallito\n");
			return 1;
		}
		
		num_proc++;
		if (fork() == 0) {
			// Processo figlio
			close(sd);	// Chiusura della socket in ascolto (processo figlio)

			// Ciclo infinito
			while (1) {
				// Ricezione della stringa inviata dal client
				memset(buf, 0, sizeof(buf));
				read(nsd, &buf, sizeof(buf));
				printf("Client %d: %s\n", num_proc, buf);

				// Chiusura della connessione da parte del client con la stringa "quit"
				if (!strncmp(buf, "quit", 4)) {
					close(nsd);	// Chiusura della socket relativa alla connessione (processo figlio)
					printf("socket %d chiuso\n", num_proc);
					return 0;
				} 

				// Operazione di conversione sulla stringa ricevuta
				printf("Elaborazione in corso...\n");
				for (i = 0; buf[i] != 0; i++)
					src[i] = (buf[i] >= 97 && buf[i] <= 122) ? (buf[i] - 32) : buf[i];
				src[i] = '\0';

				// Simulazione del tempo di elaborazione
				sleep(3);

				// Invio della stringa convertita al client
				write(nsd, &src, strlen(src));
				printf("Client %d: %s\n", num_proc, src);
			}
		}

		// Chiusura della socket relativa alla connessione (processo padre)
		close(nsd);
	}

	// Chiusura della socket in ascolto (processo padre)
 	close(sd);
}

