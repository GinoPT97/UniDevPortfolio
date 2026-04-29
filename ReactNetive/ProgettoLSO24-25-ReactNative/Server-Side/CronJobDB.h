#ifndef CRON_JOB_DB_H
#define CRON_JOB_DB_H

#include <libpq-fe.h>

void PopulateDatabaseWithTmdbData(PGconn *conn);
void* CronjobThread(void* arg);
void RemoveDuplicatesAndResetSequences(PGconn *conn);

#endif
