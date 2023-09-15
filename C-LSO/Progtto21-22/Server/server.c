#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <pthread.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>

#include "./GestioneFile.h"
#include "./user.h"
#include "./room.h"
#include "./vector.h"
#include "Finder/finder.h"


#define PORT 8125
#define BUFF_LEN 500

enum COMMAND{
    CHANGE_NICKNAME = 'c', // usage: c [new_nick]
    ENTER_IN_ROOM = 'r',
    NEW_ROOM = 'a', // msg pattern: r.g.b icon r.g.b t [roomname]
    ROOM_LIST = 'l', // input: l start end [name]

    // ---- per il Controller ----
    NEXT_USER = 'n',
    SEND_MSG = 'm',

    TIME_EXPIRED = 't',
    EXIT_FROM_ROOM = 'x',
    USERS_IN_ROOM = 'u',

    EXIT = 'e',
};

// ---- DISPATCHER ----

int dispatch(User* usr, RoomVector* vec, int command, char* msg); // 1 -> continue; 0 -> exit

// ---- FUNCTIONS ----

int changeNickname(User*, char*); // 1 valid, else 0
void enterInRoom(User*, unsigned int id, RoomVector*, char* buff);
void addRoom(char*, RoomVector*, User*);
void sendRooms(User*, RoomVector*, char*);



int startChatting(User*, User*, Connection*, char* buff, Room* room); // Read data from user1 and send to user2 until exit or next
// 0 -> exit
// 1 -> next user


void* clientHandler(void*);

RoomVector*  roomVector;
int main() {
// ----- Load rooms from file -----
    roomVector = newVector();
    int sortById(Room*, Room*);
    sortBy(roomVector, sortById);

    loadFromFile(roomVector, "rooms.rc");

    startAutoSave(roomVector, "rooms.rc", 60 * 5);

// ----- Starting server -----
    int server, client;

    struct sockaddr_in address;
    address.sin_family = AF_INET;
    address.sin_port = htons(PORT);
    address.sin_addr.s_addr = htonl(INADDR_ANY);

    printf("Creating server socket\n");
    server = socket(PF_INET, SOCK_STREAM, 0);
    if(server < 0) {
        perror("Error Creating server socket.\n"); perror(errno);
        exit(EXIT_FAILURE);
    }


    printf("Binding server socket\n");
    if(bind(server, (struct sockaddr*) &address, sizeof(address)) < 0){
        perror("Error Binding.\n"); perror(errno);
        exit(EXIT_FAILURE);
    }

    printf("Set server to listening mode.\n");
    if(listen(server, 10) < 0){
        perror("Error Listen.\n"); perror(errno);
        exit(EXIT_FAILURE);
    }


// ----- Wait for client -----
    unsigned long long connectionCount = 1;
    while (1){
        printf("Await for client...\n");

        client = accept(server, NULL, NULL);
        if (client == -1){
            perror("Error client accept.\n"); perror(errno);
            exit(EXIT_FAILURE);
        }

        printf("New connection (n %llu) starting thread...\n", connectionCount);
        pthread_t tid;

        User* user = malloc(sizeof(User));
        user->socketfd = client;
        user->prev = 0;
        user->connectionCount = connectionCount++;
        if(connectionCount == 0) connectionCount++;

        pthread_create(&tid, NULL, clientHandler, (void*) user);
        pthread_detach(tid);
        printf("Thread started.\n\n");
    }

    close(server);
    deleteVector(roomVector);
    return 0;
}



void* clientHandler(void* arg){
    char buff[BUFF_LEN];
    ssize_t msglen;

    User* user = (User*) arg;

    printf("[%llu] Thread started\n", user->connectionCount);

    // Setting nickname

    msglen = recv(user->socketfd, buff, BUFF_LEN, 0);
    if(msglen <= 0){
        printf("[%llu] Connection closed by client (nickname)\n", user->connectionCount);
        printf("[%llu] Closing connection\n", user->connectionCount);
        if(close(user->socketfd) < 0){
            perror("Error in Closing connection.\n");
        }
        free(user);
        pthread_exit(NULL);
    }
    buff[msglen] = '\0';

    if(!changeNickname(user, buff)){
        printf("[%llu] Invalid nickname, closing connection.\n", user->connectionCount);
        if(close(user->socketfd) < 0){
            printf("[%llu] Error in Closing connection.\n", user->connectionCount);
        }
        free(user);
        pthread_exit(NULL);
    }


    do{
        msglen = recv(user->socketfd, buff, BUFF_LEN, MSG_NOSIGNAL);
        if(msglen <= 0){
            if(errno == EINTR) continue;
            printf("[%llu] Connection closed by client\n", user->connectionCount);
            break;
        }
        buff[msglen] = '\0';
        printf("[%llu] Data recived from client: %s\n", user->connectionCount, buff);
    } while(dispatch(user, roomVector, buff[0], buff+1));

    printf("[%llu] Closing connection\n", user->connectionCount);
    if(close(user->socketfd) < 0){
        printf("[%llu] Error in Closing connection.\n", user->connectionCount);
    }
    free(user);
    pthread_exit(NULL);
}

int sortById(Room* a, Room* b){
    return a->id < b->id;
}

int dispatch(User* usr, RoomVector* vec, int command, char* msg){
    switch (command) {
        case CHANGE_NICKNAME:
            changeNickname(usr, msg);
            break;
        case ENTER_IN_ROOM:
            enterInRoom(usr, atoi(msg), vec, msg);
            break;
        case NEW_ROOM:
            addRoom(msg, vec, usr);
            break;
        case ROOM_LIST:
            sendRooms(usr, vec, msg);
            break;
        case EXIT:
            return 0;
        default:
            printf("Unknown cmd: %d %s\n", command, msg);
            return 0;
    }
    return 1;
}

int changeNickname(User* user, char* msg){
    char newNick[NICK_LEN];
    int len = stringInside(msg, '[', ']', newNick, NICK_LEN);

    for (int i = 0; i < len; ++i) {
        if(newNick[i] != ' '){
            strcpy(user->nickname, newNick);
            printf("[%llu] Nickname is set to [%s].\n", user->connectionCount, user->nickname);
            return 1;
        }
    }
    return 0;
}

void enterInRoom(User* user , unsigned int id, RoomVector* vec, char* buff){

    printf("[%llu] Try to enter in room #%d.\n", user->connectionCount, id);

    Room* room = getbyId(vec, id);
    if(room == NULL) {
        printf("[%llu] Error entering in room #%d. Id not valid\n", user->connectionCount, id);
        strcpy(buff, "e\n");
        send(user->socketfd, buff, 2, MSG_NOSIGNAL);
        return;
    }

    pthread_mutex_lock(&room->mutex);
    room->usersCount++;
    pthread_mutex_unlock(&room->mutex);

    int next;
    do{
        printf("[%llu] Searching for user.\n", user->connectionCount);
        Connection* conn = find(user, room, buff-1, BUFF_LEN);
        if(conn == NULL) break;

        User* user2 = (conn->user1 == user)? conn->user2: conn->user1;
        user->prev = user2->connectionCount;

        printf("[%llu] User found, nick: [%s]\n", user->connectionCount, user2->nickname);
        next = startChatting(user, user2, conn, buff-1, room);
    } while (next);

    printf("[%llu] Exit from room #%d.\n", user->connectionCount, id);
    strcpy(buff, "x\n");
    send(user->socketfd, buff, 2, MSG_NOSIGNAL);

    pthread_mutex_lock(&room->mutex);
    room->usersCount--;
    pthread_mutex_unlock(&room->mutex);
}

void addRoom(char* msg, RoomVector* vec, User* user){
    char name[ROOM_NAME_LEN];

    unsigned long long roomColor;
    int t;

    sscanf(msg, "%llu %d",
           &roomColor,
           &t
   );

    stringInside(msg, '[', ']', name, ROOM_NAME_LEN);


    pthread_mutex_lock(&vec->mutex);
    unsigned int id = add(vec, newRoom(name, roomColor, t), 1);
    pthread_mutex_unlock(&vec->mutex);

    printf("[%llu] Room Added {%u %llu %d [%s]}", user->connectionCount, id, roomColor, t, name);

    int len = sprintf(msg, "%c %d\n", NEW_ROOM, id);
    send(user->socketfd, msg, len+1,MSG_NOSIGNAL);
}

void sendRooms(User* user, RoomVector* roomVector, char* buff){

    unsigned int from = 0, to = 0;
    sscanf(buff, "%d %d", &from, &to);

    ssize_t len;
    char name[ROOM_NAME_LEN];
    int nameLen = stringInside(buff, '[', ']', name, ROOM_NAME_LEN);

    RoomVector* source = (nameLen > 0)? searchByName(roomVector, name): RoomVectorCopy(roomVector);

    int sortByUsercount(Room*, Room*);
    sortBy(source, sortByUsercount);

    unsigned int sendingRooms;
    if(from == 0 && to == 0) {
        sendingRooms = source->size;
        to = source->size;
    }else if(to > source->size) sendingRooms = source->size - from;
    else sendingRooms = to - from;

    len = sprintf(buff, "%c %u\n", ROOM_LIST, sendingRooms);
    send(user->socketfd, buff, len, MSG_NOSIGNAL);

    if(source->size != 0){
        printf("[%llu] Sending rooms to client: {\n", user->connectionCount);
        for (; from < to && from < source->size; ++from) {
            Room* r = source->rooms[from];
            len = sprintf(buff, "%c %d %ld %llu %d [%s]\n", ROOM_LIST,
                          r->id,
                          r->usersCount,
                          r->roomColor,
                          r->time,
                          r->name
            );
            len = send(user->socketfd, buff, len, MSG_NOSIGNAL);
            if(len < 0 && errno != EINTR) break;
            printf("[%llu] %s", user->connectionCount, buff);
        }
        printf("[%llu] }\n", user->connectionCount);
    }
    deleteVector(source);
}

int sortByUsercount(Room* a, Room* b){
    return a->usersCount > b->usersCount;
}

int startChatting(User* userRecv, User* userSend, Connection* conn, char* buff, Room* room){ // 0 -> exit; 1 -> next user
    ssize_t len;

    sprintf(buff, "r [%s]\n", userSend->nickname);
    send(userRecv->socketfd, buff, strlen(buff), MSG_NOSIGNAL);


    int timeExpired = 0;
    while (1) {
        fd_set rfdSet, errfdSet;
        FD_SET(userRecv->socketfd, &rfdSet); FD_SET(userRecv->socketfd, &errfdSet);

        if(!isOpen(conn))
            len = select(userRecv->socketfd + 1, &rfdSet, NULL, &errfdSet, NULL);
        else {
            FD_SET(conn->pipefd[0], &rfdSet);
            len = select(((userRecv->socketfd > conn->pipefd[0])? userRecv->socketfd: conn->pipefd[0]) + 1, &rfdSet, NULL, &errfdSet, NULL);
        }
        if(len >= 0){

            if(conn->timer != NULL && !timeExpired && FD_ISSET(conn->pipefd[0], &rfdSet)){ // timer expired
                printf("[%llu] Time expired\n", userRecv->connectionCount);
                buff[0] = TIME_EXPIRED;
                buff[1] = '\n';
                send(userRecv->socketfd, buff, 2, MSG_NOSIGNAL);
                timeExpired = 1;
            }

            if(FD_ISSET(userRecv->socketfd, &rfdSet) || FD_ISSET(userRecv->socketfd, &errfdSet)){
                len = recv(userRecv->socketfd, buff, BUFF_LEN, MSG_NOSIGNAL);
                if(len <= 0) { // user close socket === recv EXIT
                    if(errno == EINTR) continue;
                    if(isOpen(conn)){
                        closeConnection(conn);
                        buff[0] = EXIT;
                        buff[1] = '\n';
                        send(userSend->socketfd, buff, 2, MSG_NOSIGNAL);
                    } else closeConnection(conn);
                    return 0;
                }

                switch (buff[0]) {
                    case SEND_MSG:
                        if(isOpen(conn)){
                            send(userSend->socketfd, buff, len, MSG_NOSIGNAL);
                        }
                        break;
                    case NEXT_USER:
                        if(isOpen(conn)){
                            closeConnection(conn);
                            send(userSend->socketfd, buff, len, MSG_NOSIGNAL);
                        } else closeConnection(conn);
                        return 1;

                    case USERS_IN_ROOM:
                        len = sprintf(buff, "%c %lu\n", USERS_IN_ROOM, room->usersCount);
                        send(userRecv->socketfd, buff, len, MSG_NOSIGNAL);
                        break;
                    case EXIT:
                        if(isOpen(conn)){
                            closeConnection(conn);
                            send(userSend->socketfd, buff, len, MSG_NOSIGNAL);
                        } else closeConnection(conn);
                        return 0;
                }
            }
        }

    }
}
