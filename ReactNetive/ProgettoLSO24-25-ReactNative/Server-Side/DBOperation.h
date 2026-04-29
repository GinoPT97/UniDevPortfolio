#ifndef DB_OPERATION_H
#define DB_OPERATION_H

#include <libpq-fe.h>

#define BUFFER_SIZE 1024
#define CONNINFO "dbname=LSO-24-25 user=postgres password=postgres host=db port=5432"

typedef struct {
    const char *titolo;
    char genere[256];
    const char *descrizione;
    const char *lingua;
    const char *data_rilascio;
    const char *locandina;
    char voto_medio_str[10];
    char numero_voti_str[10];
    char popularita_str[10];
    char prezzo_str[10];
    char numero_copie_disponibili_str[10];
    char numero_copie_in_prestito_str[10];
    const char *stato;
} Film;

void ExecuteQuery(PGconn *conn, const char *query);
char* GetFilms(PGconn *conn);
void InsertFilm(PGconn *conn, const Film *film);
int register_user(PGconn *conn, const char *username, const char *password);
int login_user(PGconn *conn, const char *username, const char *password, int *user_id);
void send_notification(PGconn *conn, int user_id, const char *message);
int get_notifications(PGconn *conn, int user_id, char **response);
void delete_notification(PGconn *conn, int notification_id);
int list_users(PGconn *conn, char **response);
int get_film_response(PGconn *conn, char **response);
int checkout(PGconn *conn, const char *noleggioData);
int get_last_5_rentals_by_user(PGconn *conn, int user_id, char **response);
int get_top_5_rented_films(PGconn *conn, char **response);
int get_all_rentals_overview(PGconn *conn, char **response);
int return_rental(PGconn *conn, int rental_id);
int check_film_availability(PGconn *conn, int film_id);
int get_max_rentals(PGconn *conn);
void set_max_rentals(PGconn *conn, int max_rentals);
int get_active_rentals_by_user(PGconn *conn, int user_id, char **response);

#endif
