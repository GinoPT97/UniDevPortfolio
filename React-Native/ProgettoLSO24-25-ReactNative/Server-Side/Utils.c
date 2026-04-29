#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <openssl/evp.h>
#include <libpq-fe.h>
#include "Utils.h"

static void handle_db_error(PGconn *conn, PGresult *res, const char *message) {
    fprintf(stderr, "%s: %s\n", message, PQerrorMessage(conn));
    if (res) PQclear(res);
    PQfinish(conn);
    exit(EXIT_FAILURE);
}

void hash_password(const char *password, char *hashed_password) {
  unsigned char hash[EVP_MAX_MD_SIZE];
  unsigned int hash_len;
  EVP_MD_CTX *mdctx = EVP_MD_CTX_new();

  if (mdctx == NULL) {
      perror("Errore inizializzazione OpenSSL");
      exit(EXIT_FAILURE);
  }

  if (EVP_DigestInit_ex(mdctx, EVP_sha256(), NULL) != 1) {
      perror("Errore inizializzazione digest");
      exit(EXIT_FAILURE);
  }

  if (EVP_DigestUpdate(mdctx, password, strlen(password)) != 1) {
      perror("Errore aggiornamento digest");
      exit(EXIT_FAILURE);
  }

  if (EVP_DigestFinal_ex(mdctx, hash, &hash_len) != 1) {
      perror("Errore finale digest");
      exit(EXIT_FAILURE);
  }

  for (unsigned int i = 0; i < hash_len; i++) {
      sprintf(&hashed_password[i * 2], "%02x", hash[i]);
  }

  EVP_MD_CTX_free(mdctx);
}

void CheckConnection(PGconn *conn) {
    if (PQstatus(conn) != CONNECTION_OK) {
        handle_db_error(conn, NULL, "Connection to database failed");
    }
}

void CreateTable(PGconn *conn, const char *query, const char *table_name) {
    PGresult *res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        fprintf(stderr, "Errore nella creazione della tabella %s: %s\n", table_name, PQerrorMessage(conn));
    } else {
        printf("Tabella %s verificata o creata.\n", table_name);
    }
    PQclear(res);
}

void CreateTablesIfNotExist(PGconn *conn) {
    struct {
        const char *query;
        const char *name;
    } create_table_queries[] = {
        {"CREATE TABLE IF NOT EXISTS FILM ("
         "id_FILM SERIAL PRIMARY KEY,"
         "Titolo VARCHAR(100) NOT NULL,"
         "Genere VARCHAR(255),"
         "Descrizione TEXT,"
         "LinguaOriginale CHAR(2),"
         "DataRilascio DATE,"
         "Locandina VARCHAR(150),"
         "VotoMedio DECIMAL(3, 1) CHECK (VotoMedio >= 0 AND VotoMedio <= 10),"
         "NumeroVoti INT CHECK (NumeroVoti >= 0),"
         "Popularita DECIMAL(10, 2) CHECK (Popularita >= 0),"
         "Prezzo DECIMAL(10, 2) NOT NULL CHECK (Prezzo >= 0),"
         "NumeroCopieDisponibili SMALLINT NOT NULL CHECK (NumeroCopieDisponibili >= 0),"
         "NumeroCopieInPrestito SMALLINT NOT NULL DEFAULT 0 CHECK (NumeroCopieInPrestito >= 0),"
         "Stato VARCHAR(20) DEFAULT 'disponibile');", "FILM"},
        {"ALTER TABLE FILM ADD COLUMN IF NOT EXISTS Stato VARCHAR(20) DEFAULT 'disponibile';", "FILM.Stato"},
        {"CREATE INDEX IF NOT EXISTS idx_titolo ON FILM (Titolo);", "idx_titolo"},
        {"CREATE INDEX IF NOT EXISTS idx_genere ON FILM (Genere);", "idx_genere"},
        {"CREATE TABLE IF NOT EXISTS UTENTE ("
         "id_UTENTE SERIAL PRIMARY KEY,"
         "Username VARCHAR(20) UNIQUE NOT NULL,"
         "Password VARCHAR(100) NOT NULL,"
         "max_rentals INT DEFAULT 5);", "UTENTE"},
        {"CREATE TABLE IF NOT EXISTS NOLEGGIO ("
         "id_NOLEGGIO SERIAL PRIMARY KEY,"
         "id_UTENTE INT NOT NULL,"
         "id_FILM INT NOT NULL,"
         "NumeroCopieNoleggiate INT NOT NULL CHECK (NumeroCopieNoleggiate > 0),"
         "DataNoleggio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
         "DataRestituzione TIMESTAMP DEFAULT CURRENT_TIMESTAMP + INTERVAL '1 month',"
         "Restituito BOOLEAN DEFAULT FALSE,"
         "FOREIGN KEY (id_UTENTE) REFERENCES UTENTE(id_UTENTE) ON DELETE CASCADE,"
         "FOREIGN KEY (id_FILM) REFERENCES FILM(id_FILM) ON DELETE CASCADE);", "NOLEGGIO"},
        {"CREATE INDEX IF NOT EXISTS idx_id_film ON NOLEGGIO (id_FILM);", "idx_id_film"},
        {"CREATE INDEX IF NOT EXISTS idx_id_utente ON NOLEGGIO (id_UTENTE);", "idx_id_utente"},
        {"CREATE TABLE IF NOT EXISTS NOTIFICA ("
         "id_NOTIFICA SERIAL PRIMARY KEY,"
         "id_UTENTE INT NOT NULL,"
         "Messaggio TEXT NOT NULL,"
         "DataNotifica TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
         "Stato VARCHAR(20) DEFAULT 'inviata',"
         "FOREIGN KEY (id_UTENTE) REFERENCES UTENTE(id_UTENTE) ON DELETE CASCADE);", "NOTIFICA"}
    };

    for (int i = 0; i < sizeof(create_table_queries) / sizeof(create_table_queries[0]); i++) {
        CreateTable(conn, create_table_queries[i].query, create_table_queries[i].name);
    }
    printf("Tabelle del database verificate/certificate con successo.\n");

    char hashed_admin[65];
    hash_password("admin", hashed_admin);

    char query[256];
    snprintf(query, sizeof(query),
        "INSERT INTO UTENTE (id_UTENTE, Username, Password, max_rentals) "
        "VALUES (0, 'admin', '%s', 5) "
        "ON CONFLICT (id_UTENTE) DO NOTHING;", hashed_admin);

    PGresult *res = PQexec(conn, query);
    if (PQresultStatus(res) != PGRES_COMMAND_OK) {
        fprintf(stderr, "Errore nell'inserimento dell'utente admin: %s\n", PQerrorMessage(conn));
    } else {
        printf("Utente admin verificato o creato.\n");
    }
    PQclear(res);

        // Aggiunta di 5 utenti di default
        const char *usernames[5] = {"marco", "giulia", "luca", "sara", "andrea"};
        for (int i = 0; i < 5; i++) {
            char password[40];
            snprintf(password, sizeof(password), "%s97", usernames[i]);
            char hashed_pw[65];
            hash_password(password, hashed_pw);
            char user_query[256];
            snprintf(user_query, sizeof(user_query),
                "INSERT INTO UTENTE (Username, Password, max_rentals) "
                "VALUES ('%s', '%s', 5) "
                "ON CONFLICT (Username) DO NOTHING;", usernames[i], hashed_pw);
            PGresult *user_res = PQexec(conn, user_query);
            if (PQresultStatus(user_res) != PGRES_COMMAND_OK) {
                fprintf(stderr, "Errore nell'inserimento dell'utente %s: %s\n", usernames[i], PQerrorMessage(conn));
            } else {
                printf("Utente %s verificato o creato.\n", usernames[i]);
            }
            PQclear(user_res);
        }
}