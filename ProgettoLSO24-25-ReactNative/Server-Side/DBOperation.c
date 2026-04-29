#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <libpq-fe.h>
#include "DBOperation.h"
#include "Utils.h"
#include <json-c/json.h>

#define GET_FILMS_QUERY "SELECT id_FILM, Titolo, Genere, Descrizione, LinguaOriginale, DataRilascio, Locandina, NumeroVoti, Prezzo, NumeroCopieDisponibili, NumeroCopieInPrestito, Stato FROM FILM"
#define INSERT_FILM_QUERY "INSERT INTO FILM (Titolo, Genere, Descrizione, LinguaOriginale, DataRilascio, Locandina, VotoMedio, NumeroVoti, Popularita, Prezzo, NumeroCopieDisponibili, NumeroCopieInPrestito, Stato) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)"

static void handle_db_error(PGconn *conn, PGresult *res, const char *message) {
    fprintf(stderr, "%s: %s\n", message, PQerrorMessage(conn));
    if (res) PQclear(res);
    PQfinish(conn);
    exit(EXIT_FAILURE);
}

void ExecuteQuery(PGconn *conn, const char *query) {
    CheckConnection(conn);
    PGresult *res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        handle_db_error(conn, res, "Query execution failed");
    }
    PQclear(res);
}

void ExecuteQueryWithParams(PGconn *conn, const char *query, int nParams, const char **param_values) {
    CheckConnection(conn);
    PGresult *res = PQexecParams(conn, query, nParams, NULL, param_values, NULL, NULL, 0);
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        handle_db_error(conn, res, "Query execution with params failed");
    }
    PQclear(res);
}

char* GetFilms(PGconn *conn) {
    CheckConnection(conn);
    PGresult *res = PQexec(conn, GET_FILMS_QUERY);

    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        handle_db_error(conn, res, "SELECT query failed");
        PQclear(res);
        return NULL;
    }

    int rows = PQntuples(res);
    int cols = PQnfields(res);

    size_t max_size = rows * cols * 256;
    char *response = calloc(max_size, 1);
    if (!response) {
        fprintf(stderr, "Memory allocation failed\n");
        PQclear(res);
        return NULL;
    }

    char *ptr = response;
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            ptr += snprintf(ptr, max_size - (ptr - response), "%s", PQgetvalue(res, i, j));
            if (j < cols - 1) {
                ptr += snprintf(ptr, max_size - (ptr - response), ", ");
            }
        }
        ptr += snprintf(ptr, max_size - (ptr - response), "\n");
    }

    PQclear(res);
    return response;
}

int FilmExists(PGconn *conn, const char *titolo, const char *data_rilascio) {
    CheckConnection(conn);

    // Prepara la query per verificare se il film esiste
    const char *query = "SELECT 1 FROM FILM WHERE Titolo = $1 AND DataRilascio = $2";
    const char *params[2] = {titolo, data_rilascio};

    PGresult *res = PQexecParams(conn, query, 2, NULL, params, NULL, NULL, 0);
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        handle_db_error(conn, res, "Check if film exists query failed");
    }

    int exists = PQntuples(res) > 0;
    PQclear(res);
    return exists;
}

void InsertFilm(PGconn *conn, const Film *film) {
    if (FilmExists(conn, film->titolo, film->data_rilascio)) {
        return;
    }

    const char *param_values[13] = {
        film->titolo, film->genere, film->descrizione, film->lingua,
        film->data_rilascio, film->locandina, film->voto_medio_str,
        film->numero_voti_str, film->popularita_str, film->prezzo_str,
        film->numero_copie_disponibili_str, film->numero_copie_in_prestito_str,
        film->stato
    };

    ExecuteQueryWithParams(conn, INSERT_FILM_QUERY, 13, param_values);
}

int login_user(PGconn *conn, const char *username, const char *password, int *user_id) {
    char hashed_password[65];
    hash_password(password, hashed_password);

    char query[256];

    // Verifica le credenziali dell'utente
    snprintf(query, sizeof(query),
             "SELECT id_UTENTE FROM utente WHERE username = '%s' AND password = '%s'",
             username, hashed_password);

    PGresult *res = PQexec(conn, query);

    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        fprintf(stderr, "Errore query login: %s\n", PQerrorMessage(conn));
        PQclear(res);
        return 0;
    }

    int rows = PQntuples(res);

    if (rows > 0) {
        *user_id = atoi(PQgetvalue(res, 0, 0));
        PQclear(res);
        printf("Login riuscito per l'utente %s con ID %d\n", username, *user_id);
        return 1;
    }

    PQclear(res);
    printf("Login fallito per l'utente %s\n", username);
    return 0;
}

int register_user(PGconn *conn, const char *username, const char *password) {
    char hashed_password[65];
    hash_password(password, hashed_password);

    int admin_max_rentals = get_max_rentals(conn);

    char query[256];
    snprintf(query, sizeof(query),
             "INSERT INTO utente (username, password, max_rentals) VALUES ('%s', '%s', %d)",
             username, hashed_password, admin_max_rentals);

    PGresult *res = PQexec(conn, query);

    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        const char *errMsg = PQresultErrorMessage(res);
        if (errMsg && strstr(errMsg, "duplicate key value violates unique constraint")) {
            fprintf(stderr, "Errore registrazione utente: username già esistente\n");
            PQclear(res);
            return 2; // Username già esistente
        }
        fprintf(stderr, "Errore registrazione utente: %s\n", PQerrorMessage(conn));
        PQclear(res);
        return 0;
    }

    PQclear(res);
    printf("Registrazione completata per l'utente %s\n", username);
    return 1;
}

void send_notification(PGconn *conn, int user_id, const char *message) {
    char query[512];

    // Invia una notifica
    snprintf(query, sizeof(query),
             "INSERT INTO NOTIFICA (id_UTENTE, Messaggio) VALUES (%d, '%s')",
             user_id, message);

    PGresult *res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        fprintf(stderr, "Errore invio notifica: %s\n", PQerrorMessage(conn));
    }
    PQclear(res);
}

int get_notifications(PGconn *conn, int user_id, char **response) {
    char query[256];

    // Recupera le notifiche per l'utente
    snprintf(query, sizeof(query),
             "SELECT id_NOTIFICA, Messaggio, TO_CHAR(DataNotifica, 'DD-MM-YYYY HH24:MI:SS.FF2') "
             "FROM NOTIFICA WHERE id_UTENTE = %d", user_id);
    PGresult *res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        PQclear(res);
        return -1;
    }
    int rows = PQntuples(res);
    size_t buf_size = 1024;
    char *buf = malloc(buf_size);
    if (!buf) {
        PQclear(res);
        return -1;
    }
    buf[0] = '\0';
    for (int i = 0; i < rows; i++) {
        char line[512];
        snprintf(line, sizeof(line), "ID: %s | %s | Data: %s\n",
                 PQgetvalue(res, i, 0),
                 PQgetvalue(res, i, 1),
                 PQgetvalue(res, i, 2));
        if (strlen(buf) + strlen(line) + 1 > buf_size) {
            buf_size *= 2;
            buf = realloc(buf, buf_size);
            if (!buf) {
                PQclear(res);
                return -1;
            }
        }
        strcat(buf, line);
    }
    strcat(buf, "END\n");
    *response = buf;
    PQclear(res);
    return 0;
}

void delete_notification(PGconn *conn, int notification_id) {
    char query[256];

    // Elimina una notifica specifica
    snprintf(query, sizeof(query), "DELETE FROM NOTIFICA WHERE id_NOTIFICA = %d", notification_id);

    PGresult *res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        fprintf(stderr, "Errore eliminazione notifica: %s\n", PQerrorMessage(conn));
    } else {
        printf("Notifica con ID %d eliminata correttamente.\n", notification_id);
    }
    PQclear(res);
}

int list_users(PGconn *conn, char **response) {
    PGresult *res = PQexec(conn, "SELECT id_UTENTE, username FROM UTENTE");
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        PQclear(res);
        return -1;
    }
    int rows = PQntuples(res);
    size_t buf_size = 1024;
    char *buf = malloc(buf_size);
    if (!buf) {
        PQclear(res);
        return -1;
    }
    buf[0] = '\0';
    for (int i = 0; i < rows; i++) {
        char line[256];
        snprintf(line, sizeof(line), "%s - %s\n",
                 PQgetvalue(res, i, 0),
                 PQgetvalue(res, i, 1));
        if (strlen(buf) + strlen(line) + 1 > buf_size) {
            buf_size *= 2;
            buf = realloc(buf, buf_size);
            if (!buf) {
                PQclear(res);
                return -1;
            }
        }
        strcat(buf, line);
    }
    strcat(buf, "END\n");
    *response = buf;
    PQclear(res);
    return 0;
}

int get_film_response(PGconn *conn, char **response) {
    CheckConnection(conn);

    // Esegui la query per ottenere tutti i campi
    PGresult *res = PQexec(conn, "SELECT id_FILM, Titolo, Genere, Descrizione, LinguaOriginale, DataRilascio, Locandina, VotoMedio, NumeroVoti, Popularita, Prezzo, NumeroCopieDisponibili, NumeroCopieInPrestito, Stato FROM FILM");

    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        handle_db_error(conn, res, "SELECT query failed");
        PQclear(res);
        return -1;
    }

    int rows = PQntuples(res);
    int cols = PQnfields(res);

    size_t max_size = rows * cols * 256;
    *response = calloc(max_size, 1);
    if (!*response) {
        fprintf(stderr, "Memory allocation failed\n");
        PQclear(res);
        return -1;
    }

    char *ptr = *response;
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            ptr += snprintf(ptr, max_size - (ptr - *response), "%s", PQgetvalue(res, i, j));
            if (j < cols - 1) {
                ptr += snprintf(ptr, max_size - (ptr - *response), "\t");
            }
        }
        ptr += snprintf(ptr, max_size - (ptr - *response), "\n");
    }

    ptr += snprintf(ptr, max_size - (ptr - *response), "END\n");

    PQclear(res);
    return 0;
}

void UpdateNoleggioReferences(PGconn *conn) {
    PGresult *res = PQexec(conn,
        "WITH new_ids AS ("
        "  SELECT id_film, ROW_NUMBER() OVER (ORDER BY id_film) AS new_id "
        "  FROM film"
        ") "
        "UPDATE noleggio "
        "SET id_film = new_ids.new_id "
        "FROM new_ids "
        "WHERE noleggio.id_film = new_ids.id_film;"
    );
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        handle_db_error(conn, res, "Aggiornamento dei riferimenti nella tabella noleggio fallito");
        PQexec(conn, "ROLLBACK;");
        return;
    }
    PQclear(res);
}

void RemoveDuplicatesAndResetSequences(PGconn *conn) {
    PGresult *res = NULL;

    res = PQexec(conn, "BEGIN;");
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        handle_db_error(conn, res, "Errore nell'iniziare la transazione");
        return;
    }
    PQclear(res);

    UpdateNoleggioReferences(conn);

    res = PQexec(conn,
        "WITH new_ids AS ("
        "  SELECT id_film, ROW_NUMBER() OVER (ORDER BY id_film) AS new_id "
        "  FROM film"
        ") "
        "UPDATE film "
        "SET id_film = new_ids.new_id "
        "FROM new_ids "
        "WHERE film.id_film = new_ids.id_film;"
    );
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        handle_db_error(conn, res, "Reimpostazione degli id_film fallita");
        PQexec(conn, "ROLLBACK;");
        return;
    }
    PQclear(res);

    res = PQexec(conn,
        "WITH duplicates AS ("
        "  SELECT id_film, "
        "         ROW_NUMBER() OVER (PARTITION BY titolo, descrizione, datarilascio "
        "                            ORDER BY id_film) AS rn "
        "  FROM film"
        ") "
        "DELETE FROM film "
        "WHERE id_film IN (SELECT id_film FROM duplicates WHERE rn > 1);"
    );
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        handle_db_error(conn, res, "Eliminazione dei duplicati fallita");
        PQexec(conn, "ROLLBACK;");
        return;
    }
    PQclear(res);

    res = PQexec(conn,
        "SELECT setval(pg_get_serial_sequence('film', 'id_film'), "
        "COALESCE((SELECT MAX(id_film) FROM film), 0) + 1, false);"
    );
    if (PQresultStatus(res) != PGRES_TUPLES_OK && PQresultStatus(res) != PGRES_COMMAND_OK) {
        handle_db_error(conn, res, "Reimpostazione della sequenza di film fallita");
        PQexec(conn, "ROLLBACK;");
        return;
    }
    PQclear(res);

    const char *tables[] = {"utente", "noleggio", "notifica"};
    const char *columns[] = {"id_utente", "id_noleggio", "id_notifica"};

    for (int i = 0; i < sizeof(tables) / sizeof(tables[0]); i++) {
        char query[256];
        snprintf(query, sizeof(query),
                 "SELECT setval(pg_get_serial_sequence('%s', '%s'), "
                 "COALESCE((SELECT MAX(%s) FROM %s), 0) + 1, false);",
                 tables[i], columns[i], columns[i], tables[i]);

        res = PQexec(conn, query);
        if (PQresultStatus(res) != PGRES_TUPLES_OK && PQresultStatus(res) != PGRES_COMMAND_OK) {
            handle_db_error(conn, res, "Reimpostazione della sequenza fallita per la tabella");
            PQexec(conn, "ROLLBACK;");
            return;
        }
        PQclear(res);
    }

    res = PQexec(conn, "COMMIT;");
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        handle_db_error(conn, res, "Errore nel COMMIT della transazione");
        return;
    }
    PQclear(res);

    printf("Duplicati rimossi e seriali reimpostati correttamente.\n");
}

int checkout(PGconn *conn, const char *noleggioData) {
    CheckConnection(conn);

    PGresult *res = NULL;

    res = PQexec(conn, "BEGIN");
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        fprintf(stderr, "Errore nell'avvio della transazione: %s\n", PQerrorMessage(conn));
        PQclear(res);
        return 0;
    }
    PQclear(res);

    // Parsifica i dati del noleggio (ci aspettiamo una stringa separata da punti e virgola)
    char *data_copy = strdup(noleggioData);
    if (!data_copy) {
        fprintf(stderr, "Errore di allocazione della memoria.\n");
        PQexec(conn, "ROLLBACK");
        return 0;
    }

    // Calcola il numero totale di film nel checkout
    int total_checkout_quantity = 0;
    int userId = -1;
    char *token = strtok(data_copy, ";");
    while (token != NULL) {
        int filmId, quantity;
        if (sscanf(token, "%d,%d,%d", &userId, &filmId, &quantity) != 3) {
            fprintf(stderr, "Formato dei dati del noleggio non valido.\n");
            free(data_copy);
            PQexec(conn, "ROLLBACK");
            return 0;
        }
        total_checkout_quantity += quantity;
        token = strtok(NULL, ";");
    }

    // Controlla il numero di noleggi non restituiti dell'utente
    char query[256];
    snprintf(query, sizeof(query), "SELECT COUNT(*) FROM NOLEGGIO WHERE id_UTENTE = %d AND Restituito = FALSE", userId);
    res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_TUPLES_OK || PQntuples(res) != 1) {
        fprintf(stderr, "Errore nel controllo dei noleggi non restituiti: %s\n", PQerrorMessage(conn));
        PQclear(res);
        free(data_copy);
        PQexec(conn, "ROLLBACK");
        return 0;
    }
    int non_returned_rentals = atoi(PQgetvalue(res, 0, 0));
    PQclear(res);

    // Ottieni il valore di max_rentals per l'utente
    snprintf(query, sizeof(query), "SELECT max_rentals FROM UTENTE WHERE id_UTENTE = %d", userId);
    res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_TUPLES_OK || PQntuples(res) != 1) {
        fprintf(stderr, "Errore nel recupero del valore max_rentals: %s\n", PQerrorMessage(conn));
        PQclear(res);
        free(data_copy);
        PQexec(conn, "ROLLBACK");
        return 0;
    }
    int max_rentals = atoi(PQgetvalue(res, 0, 0));
    PQclear(res);

    // Verifica se il checkout supera il limite di max_rentals
    if (non_returned_rentals + total_checkout_quantity > max_rentals) {
        fprintf(stderr, "Il numero totale di noleggi supera il limite massimo consentito.\n");
        free(data_copy);
        PQexec(conn, "ROLLBACK");
        return 0;
    }

    // Ripeti la parsificazione dei dati del noleggio per eseguire il checkout
    free(data_copy);
    data_copy = strdup(noleggioData);
    token = strtok(data_copy, ";");
    while (token != NULL) {
        int filmId, quantity;
        if (sscanf(token, "%d,%d,%d", &userId, &filmId, &quantity) != 3) {
            fprintf(stderr, "Formato dei dati del noleggio non valido.\n");
            free(data_copy);
            PQexec(conn, "ROLLBACK");
            return 0;
        }

        snprintf(query, sizeof(query), "SELECT NumeroCopieDisponibili FROM FILM WHERE id_FILM = %d", filmId);
        res = PQexec(conn, query);
        if (PQresultStatus(res) != PGRES_TUPLES_OK || PQntuples(res) != 1) {
            fprintf(stderr, "Errore nel controllo disponibilità per film ID %d: %s\n", filmId, PQerrorMessage(conn));
            PQclear(res);
            free(data_copy);
            PQexec(conn, "ROLLBACK");
            return 0;
        }
        int available = atoi(PQgetvalue(res, 0, 0));
        PQclear(res);
        if (available < quantity) {
            fprintf(stderr, "Copie insufficienti per film ID %d (richieste: %d, disponibili: %d).\n", filmId, quantity, available);
            free(data_copy);
            PQexec(conn, "ROLLBACK");
            return 0;
        }

        snprintf(query, sizeof(query),
                 "UPDATE FILM SET NumeroCopieDisponibili = NumeroCopieDisponibili - %d, "
                 "NumeroCopieInPrestito = NumeroCopieInPrestito + %d WHERE id_FILM = %d",
                 quantity, quantity, filmId);
        res = PQexec(conn, query);
        if (PQresultStatus(res) != PGRES_COMMAND_OK) {
            fprintf(stderr, "Errore nell'aggiornamento dello stock per film ID %d: %s\n", filmId, PQerrorMessage(conn));
            PQclear(res);
            free(data_copy);
            PQexec(conn, "ROLLBACK");
            return 0;
        }
        PQclear(res);

        snprintf(query, sizeof(query),
                 "INSERT INTO NOLEGGIO (id_UTENTE, id_FILM, NumeroCopieNoleggiate) "
                 "VALUES (%d, %d, %d)",
                 userId, filmId, quantity);
        res = PQexec(conn, query);
        if (PQresultStatus(res) != PGRES_COMMAND_OK) {
            fprintf(stderr, "Errore nell'inserimento in NOLEGGIO per film ID %d: %s\n", filmId, PQerrorMessage(conn));
            PQclear(res);
            free(data_copy);
            PQexec(conn, "ROLLBACK");
            return 0;
        }
        PQclear(res);

        token = strtok(NULL, ";");
    }

    free(data_copy);

    res = PQexec(conn, "COMMIT");
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        fprintf(stderr, "Errore nel commit della transazione: %s\n", PQerrorMessage(conn));
        PQclear(res);
        return 0;
    }
    PQclear(res);

    return 1;
}

int get_last_5_rentals_by_user(PGconn *conn, int user_id, char **response) {
    char query[256];
    snprintf(query, sizeof(query),
             "SELECT f.Titolo FROM NOLEGGIO n "
             "JOIN FILM f ON n.id_FILM = f.id_FILM "
             "WHERE n.id_UTENTE = %d "
             "ORDER BY n.DataNoleggio DESC "
             "LIMIT 5", user_id);
    PGresult *res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        PQclear(res);
        return -1;
    }

    int rows = PQntuples(res);
    size_t buf_size = 1024;
    char *buf = malloc(buf_size);
    if (!buf) {
        PQclear(res);
        return -1;
    }

    buf[0] = '\0';
    for (int i = 0; i < rows; i++) {
        char line[256];
        snprintf(line, sizeof(line), "%s\n", PQgetvalue(res, i, 0));
        if (strlen(buf) + strlen(line) + 1 > buf_size) {
            buf_size *= 2;
            buf = realloc(buf, buf_size);
            if (!buf) {
                PQclear(res);
                return -1;
            }
        }
        strcat(buf, line);
    }
    strcat(buf, "END\n");
    *response = buf;
    PQclear(res);
    return 0;
}

int get_top_5_rented_films(PGconn *conn, char **response) {
    char query[256];
    snprintf(query, sizeof(query),
             "SELECT f.Titolo, COUNT(*) as RentCount FROM NOLEGGIO n "
             "JOIN FILM f ON n.id_FILM = f.id_FILM "
             "GROUP BY f.Titolo "
             "ORDER BY RentCount DESC "
             "LIMIT 5");
    PGresult *res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        PQclear(res);
        return -1;
    }
    int rows = PQntuples(res);
    size_t buf_size = 1024;
    char *buf = malloc(buf_size);
    if (!buf) {
        PQclear(res);
        return -1;
    }

    buf[0] = '\0';
    for (int i = 0; i < rows; i++) {
        char line[256];
        snprintf(line, sizeof(line), "%s\n", PQgetvalue(res, i, 0));
        if (strlen(buf) + strlen(line) + 1 > buf_size) {
            buf_size *= 2;
            buf = realloc(buf, buf_size);
            if (!buf) {
                PQclear(res);
                return -1;
            }
        }
        strcat(buf, line);
    }
    strcat(buf, "END\n");
    *response = buf;
    PQclear(res);
    return 0;
}

int get_all_rentals_overview(PGconn *conn, char **response) {
    char query[256];
    snprintf(query, sizeof(query),
             "SELECT n.id_NOLEGGIO, f.Titolo, TO_CHAR(n.DataRestituzione, 'DD-MM-YYYY HH24:MI:SS.FF2'), u.Username "
             "FROM NOLEGGIO n "
             "JOIN FILM f ON n.id_FILM = f.id_FILM "
             "JOIN UTENTE u ON n.id_UTENTE = u.id_UTENTE "
             "WHERE n.Restituito = FALSE "
             "ORDER BY n.DataRestituzione ASC");
    PGresult *res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        PQclear(res);
        return -1;
    }

    int rows = PQntuples(res);
    size_t buf_size = 1024;
    char *buf = malloc(buf_size);
    if (!buf) {
        PQclear(res);
        return -1;
    }

    buf[0] = '\0';
    for (int i = 0; i < rows; i++) {
        char line[256];
        snprintf(line, sizeof(line), "%s - %s - %s - %s\n",
                 PQgetvalue(res, i, 0), // id_NOLEGGIO
                 PQgetvalue(res, i, 1), // Titolo
                 PQgetvalue(res, i, 2), // DataRestituzione
                 PQgetvalue(res, i, 3)); // Username
        if (strlen(buf) + strlen(line) + 1 > buf_size) {
            buf_size *= 2;
            buf = realloc(buf, buf_size);
            if (!buf) {
                PQclear(res);
                return -1;
            }
        }
        strcat(buf, line);
    }
    strcat(buf, "END\n");
    *response = buf;
    PQclear(res);
    return 0;
}

int return_rental(PGconn *conn, int rental_id) {
    CheckConnection(conn);
    PGresult *res = NULL;

    res = PQexec(conn, "BEGIN");
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        fprintf(stderr, "Errore nell'avvio della transazione: %s\n", PQerrorMessage(conn));
        PQclear(res);
        return 0;
    }
    PQclear(res);

    // Ottieni id_FILM e NumeroCopieNoleggiate dal noleggio
    char query[256];
    snprintf(query, sizeof(query), "SELECT id_FILM, NumeroCopieNoleggiate FROM NOLEGGIO WHERE id_NOLEGGIO = %d", rental_id);
    res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        PQclear(res);
        PQexec(conn, "ROLLBACK");
        return 0;
    }
    int film_id = atoi(PQgetvalue(res, 0, 0));
    int num_copie_noleggiate = atoi(PQgetvalue(res, 0, 1));
    PQclear(res);

    // Aggiorna il noleggio come restituito
    snprintf(query, sizeof(query), "UPDATE NOLEGGIO SET Restituito = TRUE WHERE id_NOLEGGIO = %d", rental_id);
    res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        PQclear(res);
        PQexec(conn, "ROLLBACK");
        return 0;
    }
    PQclear(res);

    // Aggiorna il numero di copie disponibili e in prestito
    snprintf(query, sizeof(query),
             "UPDATE FILM SET NumeroCopieDisponibili = NumeroCopieDisponibili + %d, "
             "NumeroCopieInPrestito = NumeroCopieInPrestito - %d WHERE id_FILM = %d",
             num_copie_noleggiate, num_copie_noleggiate, film_id);
    res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        PQclear(res);
        PQexec(conn, "ROLLBACK");
        return 0;
    }
    PQclear(res);

    res = PQexec(conn, "COMMIT");
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        fprintf(stderr, "Errore nel commit della transazione: %s\n", PQerrorMessage(conn));
        PQclear(res);
        return 0;
    }
    PQclear(res);

    return 1;
}

int check_film_availability(PGconn *conn, int film_id) {
    CheckConnection(conn);
    char query[256];

    // Controlla se il film è disponibile
    snprintf(query, sizeof(query), "SELECT NumeroCopieDisponibili FROM FILM WHERE id_FILM = %d", film_id);
    PGresult *res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        PQclear(res);
        return 0;
    }
    int available = atoi(PQgetvalue(res, 0, 0));
    PQclear(res);
    return available > 0;
}

int get_max_rentals(PGconn *conn) {
    CheckConnection(conn);

    // Recupera il valore di max_rentals per l'utente amministratore
    PGresult *res = PQexec(conn, "SELECT max_rentals FROM UTENTE WHERE id_UTENTE = 0");
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        PQclear(res);
        return 5; // Default
    }
    int max_rentals = atoi(PQgetvalue(res, 0, 0));
    PQclear(res);
    return max_rentals;
}

void set_max_rentals(PGconn *conn, int max_rentals) {
    CheckConnection(conn);
    char query[256];

    // Aggiorna il valore di max_rentals
    snprintf(query, sizeof(query), "UPDATE UTENTE SET max_rentals = %d", max_rentals);
    PGresult *res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        fprintf(stderr, "Errore nell'aggiornamento del massimo noleggi: %s\n", PQerrorMessage(conn));
    }
    PQclear(res);
}

int get_active_rentals_by_user(PGconn *conn, int user_id, char **response) {
    char query[256];

    // Recupera i noleggi attivi dell'utente
    snprintf(query, sizeof(query),
             "SELECT f.Titolo, TO_CHAR(n.DataRestituzione, 'DD-MM-YYYY HH24:MI:SS.FF2') "
             "FROM NOLEGGIO n "
             "JOIN FILM f ON n.id_FILM = f.id_FILM "
             "WHERE n.id_UTENTE = %d AND n.Restituito = FALSE "
             "ORDER BY n.DataRestituzione ASC", user_id);
    PGresult *res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        PQclear(res);
        return -1;
    }

    int rows = PQntuples(res);
    size_t buf_size = 1024;
    char *buf = malloc(buf_size);
    if (!buf) {
        PQclear(res);
        return -1;
    }

    buf[0] = '\0';
    for (int i = 0; i < rows; i++) {
        char line[256];
        snprintf(line, sizeof(line), "%s - %s\n",
                 PQgetvalue(res, i, 0), // Titolo
                 PQgetvalue(res, i, 1)); // DataRestituzione
        if (strlen(buf) + strlen(line) + 1 > buf_size) {
            buf_size *= 2;
            buf = realloc(buf, buf_size);
            if (!buf) {
                PQclear(res);
                return -1;
            }
        }
        strcat(buf, line);
    }
    strcat(buf, "END\n");
    *response = buf;
    PQclear(res);
    return 0;
}