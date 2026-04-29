#ifndef UTILS_H
#define UTILS_H

#include <libpq-fe.h> 

void hash_password(const char *password, char *hashed_password);
void CheckConnection(PGconn *conn);
void CreateTablesIfNotExist(PGconn *conn);

#endif