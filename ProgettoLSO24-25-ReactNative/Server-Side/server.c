#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <string.h>
#include <sys/select.h>
#include <signal.h>
#include "DBOperation.h"
#include "CronJobDB.h"
#include "Socket.h"
#include "Utils.h"

PGconn *conn;

void handle_signal(int sig) {
    printf("Terminazione del server...\n");
    fflush(stdout);
    PQfinish(conn);
    exit(0);
}

void* start_cronjob(void* arg) {
    CronjobThread(arg);
    return NULL;
}

void* populate_db_thread(void* arg) {
    PGconn *conn = (PGconn*)arg;
    printf("Popolamento del database con dati da TMDB...\n");
    fflush(stdout);
    PopulateDatabaseWithTmdbData(conn);
    printf("Database popolato con successo.\n");
    fflush(stdout);
    return NULL;
}

#define PORT SERVER_PORT

int main() {
    printf("Avvio del server...\n");
    fflush(stdout);

    signal(SIGINT, handle_signal);

    const char *conninfo = "host=db port=5432 dbname=LSO-24-25 user=postgres password=postgres";
    conn = PQconnectdb(conninfo);
    if (PQstatus(conn) != CONNECTION_OK) {
        fprintf(stderr, "Errore di connessione al database: %s\n", PQerrorMessage(conn));
        PQfinish(conn);
        exit(EXIT_FAILURE);
    }

    printf("Connessione al database riuscita.\n");
    fflush(stdout);

    CheckConnection(conn);

    printf("Creazione delle tabelle se non esistono...\n");
    fflush(stdout);
    CreateTablesIfNotExist(conn);
    printf("Tabelle create/verificate con successo.\n");
    fflush(stdout);

    pthread_t cronjob_tid;
    if (pthread_create(&cronjob_tid, NULL, start_cronjob, NULL) != 0) {
        perror("Errore nella creazione del thread del cronjob");
        PQfinish(conn);
        exit(EXIT_FAILURE);
    }

    pthread_t udp_thread;
    if (pthread_create(&udp_thread, NULL, HandleUDP, NULL) != 0) {
        perror("pthread_create UDP");
        exit(EXIT_FAILURE);
    }

    pthread_t populate_tid;
    if (pthread_create(&populate_tid, NULL, populate_db_thread, conn) != 0) {
        perror("Errore nella creazione del thread di popolamento DB");
        PQfinish(conn);
        exit(EXIT_FAILURE);
    }

    int server_fd = CreateServerSocket();
    if (server_fd == -1) {
        fprintf(stderr, "Errore nella creazione del socket del server\n");
        PQfinish(conn);
        exit(EXIT_FAILURE);
    }

    printf("Server avviato correttamente e in ascolto sulla porta %d\n", PORT);
    fflush(stdout);

    HandleServerConnections(server_fd);

    printf("Chiusura del server...\n");
    fflush(stdout);
    close(server_fd);
    PQfinish(conn);
    pthread_join(cronjob_tid, NULL);
    pthread_join(udp_thread, NULL);
    pthread_join(populate_tid, NULL);
    return 0;
}
