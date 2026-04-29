#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <libpq-fe.h>
#include <sys/time.h>
#include <errno.h>
#include <fcntl.h>
#include <pthread.h>
#include "Socket.h"
#include "DBOperation.h"
#include <json-c/json.h>

#define CONNINFO "dbname=LSO-24-25 user=postgres password=postgres host=db port=5432"
#define TIMEOUT 10
#define PORT 8080
#define UDP_PORT 5000

#define CHECK_AND_CLOSE(fd, msg) \
    do { \
        if ((fd) >= 0) close(fd); \
        perror(msg); \
    } while(0)

// Crea ed inizializza il socket del server
int CreateServerSocket() {
    printf("Creazione del socket del server...\n");
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd == -1) {
        perror("Creazione del socket fallita");
        exit(EXIT_FAILURE);
    }

    int opt = 1;
    if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt)) < 0) {
        CHECK_AND_CLOSE(server_fd, "Impostazione dell'opzione SO_REUSEADDR fallita");
        exit(EXIT_FAILURE);
    }

    struct sockaddr_in server_addr = {0};
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = INADDR_ANY;

    struct timeval timeout = { .tv_sec = TIMEOUT, .tv_usec = 0 };
    if (setsockopt(server_fd, SOL_SOCKET, SO_RCVTIMEO, &timeout, sizeof(timeout)) < 0) {
        CHECK_AND_CLOSE(server_fd, "Impostazione del timeout di ricezione del socket fallita");
        exit(EXIT_FAILURE);
    }

    if (bind(server_fd, (struct sockaddr *)&server_addr, sizeof(server_addr)) == -1) {
        CHECK_AND_CLOSE(server_fd, "Bind fallito");
        exit(EXIT_FAILURE);
    }
    printf("Bind effettuato con successo sulla porta %d\n", PORT);

    if (listen(server_fd, SOMAXCONN) == -1) {
        CHECK_AND_CLOSE(server_fd, "Ascolto fallito");
        exit(EXIT_FAILURE);
    }
    printf("Il server è in ascolto sulla porta %d\n", PORT);
    return server_fd;
}

// Accetta la connessione di un client
int AcceptClientConnection(int server_fd) {
    struct sockaddr_in client_addr;
    socklen_t addr_len = sizeof(client_addr);
    int client_fd = accept(server_fd, (struct sockaddr*)&client_addr, &addr_len);
    if (client_fd == -1) {
        if (errno != EAGAIN && errno != EWOULDBLOCK && errno != EINTR) {
            perror("Accettazione della connessione fallita");
        }
        return -1;
    }
    printf("Client connesso: %s:%d\n", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));

    int flags = fcntl(client_fd, F_GETFL, 0);
    if (flags < 0 || fcntl(client_fd, F_SETFL, flags | O_NONBLOCK) < 0) {
        CHECK_AND_CLOSE(client_fd, "Impostazione del socket client non bloccante fallita");
        return -1;
    }

    return client_fd;
}

// Riceve un messaggio dal client; in caso di assenza di dati restituisce 0 (senza chiudere la connessione)
ssize_t ReceiveMessage(int client_fd, char *buffer, size_t buffer_size) {
    if (!buffer || buffer_size == 0) return -1;
    ssize_t bytes_received = recv(client_fd, buffer, buffer_size - 1, 0);
    if (bytes_received < 0) {
        if (errno == EAGAIN || errno == EWOULDBLOCK) {
            return 0;
        } else {
            perror("Ricezione fallita");
            return -1;
        }
    }
    if (bytes_received == 0) {
        // Il client ha chiuso la connessione
        printf("Client disconnesso\n");
        return -1;
    }
    buffer[bytes_received] = '\0';
    printf("Messaggio ricevuto: '%s'\n", buffer);
    return bytes_received;
}

// Invia un messaggio al client
ssize_t SendMessage(int client_fd, const char *message) {
    if (!message) return -1;
    size_t message_len = strlen(message);
    char *messageWithNewline = malloc(message_len + 2);
    if (!messageWithNewline) {
        perror("Allocazione della memoria fallita");
        return -1;
    }
    strcpy(messageWithNewline, message);
    strcat(messageWithNewline, "\n");

    ssize_t bytes_sent = send(client_fd, messageWithNewline, strlen(messageWithNewline), 0);
    if (bytes_sent == -1) {
        perror("Invio fallito");
    }
    free(messageWithNewline);
    return bytes_sent;
}

// Funzione per gestire il comando ricevuto dal client
void HandleRequest(PGconn *conn, int client_fd, const char *request) {
    printf("Comando ricevuto: '%s'\n", request);
    char *response = NULL;
    int should_close = 0; // CORRETTO: variabile locale, non static
    static int register_handled = 0; // static per connessione/thread

    char request_copy[1024];
    strncpy(request_copy, request, sizeof(request_copy) - 1);
    request_copy[sizeof(request_copy) - 1] = '\0';

    char *token = strtok(request_copy, "\n");
    while (token != NULL) {
        if (strncmp(token, "LOGIN", 5) == 0) {
            char command[6], username[64], password[64];
            if (sscanf(token, "%5s %63s %63s", command, username, password) == 3) {
                int user_id;
                if (login_user(conn, username, password, &user_id)) {
                    char buf[128];
                    snprintf(buf, sizeof(buf), "SUCCESS %d", user_id);
                    SendMessage(client_fd, buf);
                } else {
                    SendMessage(client_fd, "ERROR login failed");
                }
            } else {
                SendMessage(client_fd, "ERROR invalid login format");
            }
        }
        else if (strncmp(token, "REGISTER", 8) == 0) {
            if (register_handled) {
                SendMessage(client_fd, "ERROR: registration already attempted");
            } else {
                char command[9], username[64], password[64];
                if (sscanf(token, "%8s %63s %63s", command, username, password) == 3) {
                    int reg_result = register_user(conn, username, password);
                    if (reg_result == 1) {
                        SendMessage(client_fd, "SUCCESS");
                    } else if (reg_result == 2) {
                        SendMessage(client_fd, "ERROR username already exists");
                    } else {
                        SendMessage(client_fd, "ERROR registration failed");
                    }
                } else {
                    SendMessage(client_fd, "ERROR invalid registration format");
                }
                register_handled = 1;
            }
        }
        else if (strcmp(token, "GET_FILMS") == 0) {
            if (get_film_response(conn, &response) == 0) {
                SendMessage(client_fd, response);
                free(response);
            } else {
                fprintf(stderr, "Errore nella query dei film: %s\n", PQerrorMessage(conn));
                SendMessage(client_fd, "Errore nel recupero dei film dal database.");
            }
        }
        else if (strncmp(token, "LIST_USERS", 10) == 0) {
            if (list_users(conn, &response) == 0) {
                SendMessage(client_fd, response);
                free(response);
            } else {
                SendMessage(client_fd, "Errore recupero utenti");
            }
        }
        else if (strncmp(token, "SEND_NOTIFICATION", 17) == 0) {
            int target_id;
            char message[1024];
            if (sscanf(token, "%*s %d %[^\n]", &target_id, message) == 2) {
                send_notification(conn, target_id, message);
                SendMessage(client_fd, "Notifica inviata");
            } else {
                SendMessage(client_fd, "ERROR invalid SEND_NOTIFICATION format");
            }
        }
        else if (strncmp(token, "GET_NOTIFICATIONS", 17) == 0) {
            int user_id;
            if (sscanf(token, "%*s %d", &user_id) == 1) {
                if (get_notifications(conn, user_id, &response) == 0) {
                    SendMessage(client_fd, response);
                    free(response);
                } else {
                    SendMessage(client_fd, "Errore recupero notifiche");
                }
            } else {
                SendMessage(client_fd, "ERROR invalid GET_NOTIFICATIONS format");
            }
        }
        else if (strncmp(token, "DELETE_NOTIFICATION", 19) == 0) {
            int notification_id;
            if (sscanf(token, "%*s %d", &notification_id) == 1) {
                delete_notification(conn, notification_id);
                SendMessage(client_fd, "Notifica eliminata");
            } else {
                SendMessage(client_fd, "ERROR invalid DELETE_NOTIFICATION format");
            }
        }
        else if (strncmp(token, "CHECKOUT", 8) == 0) {
            char noleggioData[2048];
            if (sscanf(token, "CHECKOUT %2047[^\n]", noleggioData) == 1) {
                if (checkout(conn, noleggioData)) {
                    SendMessage(client_fd, "SUCCESS checkout completed");
                } else {
                    SendMessage(client_fd, "ERROR checkout failed: Il numero totale di noleggi supera il limite massimo consentito.");
                }
            } else {
                SendMessage(client_fd, "ERROR invalid CHECKOUT format");
            }
        }
        else if (strncmp(token, "GET_LAST_5_RENTALS_BY_USER", 26) == 0) {
            int user_id;
            if (sscanf(token, "%*s %d", &user_id) == 1) {
                if (get_last_5_rentals_by_user(conn, user_id, &response) == 0) {
                    SendMessage(client_fd, response);
                    free(response);
                } else {
                    SendMessage(client_fd, "Errore nel recupero degli ultimi 5 noleggi.");
                }
            } else {
                SendMessage(client_fd, "ERROR invalid GET_LAST_5_RENTALS_BY_USER format");
            }
        }
        else if (strcmp(token, "GET_TOP_5_RENTED_FILMS") == 0) {
            if (get_top_5_rented_films(conn, &response) == 0) {
                SendMessage(client_fd, response);
                free(response);
            } else {
                SendMessage(client_fd, "Errore nel recupero dei 5 film più noleggiati.");
            }
        }
        else if (strcmp(token, "GET_ALL_RENTALS_OVERVIEW") == 0) {
            if (get_all_rentals_overview(conn, &response) == 0) {
                SendMessage(client_fd, response);
                free(response);
            } else {
                SendMessage(client_fd, "Errore nel recupero della panoramica dei noleggi.");
            }
        }
        else if (strncmp(token, "RETURN_RENTAL", 13) == 0) {
            int rental_id;
            if (sscanf(token, "RETURN_RENTAL %d", &rental_id) == 1) {
                if (return_rental(conn, rental_id)) {
                    SendMessage(client_fd, "SUCCESS rental returned");
                } else {
                    SendMessage(client_fd, "ERROR return rental failed");
                }
            } else {
                SendMessage(client_fd, "ERROR invalid RETURN_RENTAL format");
            }
        }
        else if (strncmp(token, "CHECK_AVAILABILITY", 18) == 0) {
            int film_id;
            if (sscanf(token, "%*s %d", &film_id) == 1) {
                if (check_film_availability(conn, film_id)) {
                    SendMessage(client_fd, "AVAILABLE");
                } else {
                    SendMessage(client_fd, "UNAVAILABLE");
                }
            } else {
                SendMessage(client_fd, "ERROR invalid CHECK_AVAILABILITY format");
            }
        }
        else if (strncmp(token, "SET_MAX_RENTALS", 15) == 0) {
            int max_rentals;
            if (sscanf(token, "%*s %d", &max_rentals) == 1) {
                set_max_rentals(conn, max_rentals);
                SendMessage(client_fd, "SUCCESS max rentals updated");
            } else {
                SendMessage(client_fd, "ERROR invalid SET_MAX_RENTALS format");
            }
        }
        else if (strcmp(token, "GET_MAX_RENTALS") == 0) {
            int max_rentals = get_max_rentals(conn);
            char response[64];
            snprintf(response, sizeof(response), "MAX_RENTALS %d", max_rentals);
            SendMessage(client_fd, response);
        }
        else if (strncmp(token, "GET /", 5) == 0) {
            response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\nWelcome to the server!";
            SendMessage(client_fd, response);
        }
        else if (strcmp(token, "PING") == 0) {
            SendMessage(client_fd, "PONG");
        }
        else if (strcmp(token, "CLOSE_CONNECTION") == 0) {
            response = "Server closed connection.";
            SendMessage(client_fd, response);
            should_close = 1;
        }
        else if (strncmp(token, "GET_ACTIVE_RENTALS_BY_USER", 26) == 0) {
            int user_id;
            if (sscanf(token, "%*s %d", &user_id) == 1) {
                if (get_active_rentals_by_user(conn, user_id, &response) == 0) {
                    SendMessage(client_fd, response);
                    free(response);
                } else {
                    SendMessage(client_fd, "Errore nel recupero dei noleggi attivi.");
                }
            } else {
                SendMessage(client_fd, "ERROR invalid GET_ACTIVE_RENTALS_BY_USER format");
            }
        }
        else {
            SendMessage(client_fd, "Unrecognized command!");
        }
        token = strtok(NULL, "\n");
    }

    // La connessione viene chiusa solo su richiesta esplicita (CLOSE_CONNECTION)
    // Non chiudere automaticamente per mantenere connessioni persistenti
}

// Funzione per gestire il client
void* HandleClient(void* arg) {
    int client_fd = *(int*)arg;
    free(arg);

    PGconn *conn = PQconnectdb(CONNINFO);
    if (PQstatus(conn) != CONNECTION_OK) {
        fprintf(stderr, "Errore di connessione al database: %s\n", PQerrorMessage(conn));
        close(client_fd);
        return NULL;
    }

    char buffer[1024];
    int disconnected = 0;
    int no_data_cycles = 0;
    const int MAX_NO_DATA_CYCLES = 600; // 60 secondi (600 * 100ms)
    
    while (1) {
        ssize_t r = ReceiveMessage(client_fd, buffer, sizeof(buffer));
        if (r < 0) {
            disconnected = 1;
            break;
        }
        if (r == 0) {
            no_data_cycles++;
            // Se non ci sono dati per troppo tempo, verifica se il client è ancora connesso
            if (no_data_cycles >= MAX_NO_DATA_CYCLES) {
                // Test di connettività: prova a inviare un byte di controllo
                char test_byte = '\0';
                if (send(client_fd, &test_byte, 1, MSG_NOSIGNAL) < 0) {
                    if (errno == EPIPE || errno == ECONNRESET) {
                        printf("Client disconnesso (test di connettività fallito)\n");
                        disconnected = 1;
                        break;
                    }
                }
                no_data_cycles = 0; // Reset del contatore
            }
            usleep(100000); // 100ms
            continue;
        }
        
        no_data_cycles = 0; // Reset del contatore quando riceviamo dati
        buffer[strcspn(buffer, "\r\n")] = '\0';
        if (strlen(buffer) > 0) {
            printf("Elaborazione richiesta: '%s'\n", buffer);
            HandleRequest(conn, client_fd, buffer);
            
            // Verifica se la richiesta era di chiusura esplicita
            if (strncmp(buffer, "CLOSE_CONNECTION", 16) == 0) {
                disconnected = 1;
                break;
            }
        }
    }

    if (disconnected) {
        printf("Client disconnesso\n");
        printf("Chiusura della connessione con il client\n");
    }
    close(client_fd);
    PQfinish(conn);
    return NULL;
}

void* HandleUDP(void* arg) {
    int udp_sock;
    struct sockaddr_in server_addr, client_addr;
    char buf[1024];
    socklen_t client_len = sizeof(client_addr);

    if ((udp_sock = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
        perror("Socket UDP");
        pthread_exit(NULL);
    }

    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(UDP_PORT);

    if (bind(udp_sock, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        perror("Bind UDP");
        close(udp_sock);
        pthread_exit(NULL);
    }

    printf("Server UDP in ascolto sulla porta %d per la discovery...\n", UDP_PORT);

    while (1) {
        int n = recvfrom(udp_sock, buf, sizeof(buf) - 1, 0, (struct sockaddr*)&client_addr, &client_len);
        if (n < 0) {
            if (errno == EINTR) continue;
            perror("recvfrom");
            continue;
        }
        buf[n] = '\0';
        printf("Messaggio UDP ricevuto: %s\n", buf);

        if (strcmp(buf, "DISCOVER_SERVER") == 0) {
            char reply[] = "SERVER_HERE:8080";
            if (sendto(udp_sock, reply, strlen(reply), 0, (struct sockaddr*)&client_addr, client_len) < 0) {
                perror("sendto");
            } else {
                printf("Risposta UDP inviata al client.\n");
            }
        }
    }

    close(udp_sock);
    return NULL;
}

void HandleServerConnections(int server_fd) {
    while (1) {
        fd_set read_fds;
        struct timeval timeout;

        FD_ZERO(&read_fds);
        FD_SET(server_fd, &read_fds);

        timeout.tv_sec = 5;
        timeout.tv_usec = 0;

        int activity = select(server_fd + 1, &read_fds, NULL, NULL, &timeout);

        if (activity < 0) {
            if (errno == EINTR) continue;
            perror("Errore nella select");
        } else if (activity > 0) {
            int client_fd = AcceptClientConnection(server_fd);
            if (client_fd < 0) {
                // AcceptClientConnection stampa già l'errore
                continue;
            } else {
                printf("Connessione accettata dal client\n");
                int *client_fd_ptr = malloc(sizeof(int));
                if (!client_fd_ptr) {
                    perror("malloc client_fd_ptr");
                    close(client_fd);
                    continue;
                }
                *client_fd_ptr = client_fd;

                pthread_t client_tid;
                if (pthread_create(&client_tid, NULL, HandleClient, client_fd_ptr) != 0) {
                    perror("Errore nella creazione del thread per il client");
                    close(client_fd);
                    free(client_fd_ptr);
                } else {
                    pthread_detach(client_tid);
                }
            }
        }
    }
}