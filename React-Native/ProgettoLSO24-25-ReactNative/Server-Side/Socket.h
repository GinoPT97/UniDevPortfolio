#ifndef SOCKET_H
#define SOCKET_H

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <libpq-fe.h>

#define SERVER_ADDRESS "127.0.0.1"
#define SERVER_PORT 8080
#define UDP_PORT 5000

int CreateServerSocket();
int AcceptClientConnection(int server_fd);
ssize_t ReceiveMessage(int client_fd, char *buffer, size_t buffer_size);
ssize_t SendMessage(int client_fd, const char *message);
void HandleRequest(PGconn *conn, int client_fd, const char *request);
void* HandleClient(void* arg);
void* HandleUDP(void* arg);
int get_film_response(PGconn *conn, char **response);
void HandleServerConnections(int server_fd);
void RemoveClient(int client_fd);
int get_max_rentals(PGconn *conn);
void set_max_rentals(PGconn *conn, int max_rentals);

#endif
