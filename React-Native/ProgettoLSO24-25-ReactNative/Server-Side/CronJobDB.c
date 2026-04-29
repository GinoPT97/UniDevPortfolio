#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <curl/curl.h>
#include <json-c/json.h>
#include <libpq-fe.h>
#include <unistd.h>
#include <time.h> // aggiunto per srand/rand
#include "DBOperation.h"
#include "CronJobDB.h"
#include "Utils.h"

#define TMDB_API_URL "https://api.themoviedb.org/3/discover/movie?api_key="
#define MAX_PAGES 5
#define REQUEST_DELAY 2
#define MAX_RETRIES 3
#define CONNINFO "dbname=LSO-24-25 user=postgres password=postgres host=db port=5432"

typedef struct {
    char *memory;
    size_t size;
} MemoryStruct;

size_t write_callback(void *contents, size_t size, size_t nmemb, void *userp) {
    size_t total_size = size * nmemb;
    MemoryStruct *mem = (MemoryStruct *)userp;

    char *ptr = realloc(mem->memory, mem->size + total_size + 1);
    if (ptr == NULL) {
        fprintf(stderr, "Not enough memory for curl response\n");
        return 0;
    }

    mem->memory = ptr;
    memcpy(&(mem->memory[mem->size]), contents, total_size);
    mem->size += total_size;
    mem->memory[mem->size] = 0;

    return total_size;
}

const char* get_genre_name(int genre_id) {
    switch (genre_id) {
        case 28: return "Azione";
        case 12: return "Avventura";
        case 16: return "Animazione";
        case 35: return "Commedia";
        case 80: return "Crimine";
        case 99: return "Documentario";
        case 18: return "Dramma";
        case 10751: return "Famiglia";
        case 14: return "Fantasy";
        case 36: return "Storico";
        case 27: return "Horror";
        case 10402: return "Musica";
        case 9648: return "Mistero";
        case 10749: return "Romantico";
        case 878: return "Fantascienza";
        case 10770: return "Film TV";
        case 53: return "Thriller";
        case 10752: return "Guerra";
        case 37: return "Western";
        default: return "Sconosciuto";
    }
}

void SetupCurl(CURL *curl, MemoryStruct *chunk, const char *url) {
    curl_easy_setopt(curl, CURLOPT_URL, url);
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_callback);
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *)chunk);
}

void ParseAndInsertFilms(PGconn *conn, struct json_object *results) {
    size_t num_films = json_object_array_length(results);
    for (size_t i = 0; i < num_films; i++) {
        struct json_object *film_json = json_object_array_get_idx(results, i);
        const char *title = json_object_get_string(json_object_object_get(film_json, "title"));
        struct json_object *genres_array = json_object_object_get(film_json, "genre_ids");

        char genres[256] = "";
        size_t num_genres = json_object_array_length(genres_array);
        for (size_t j = 0; j < num_genres; j++) {
            int genre_id = json_object_get_int(json_object_array_get_idx(genres_array, j));
            const char *genre_name = get_genre_name(genre_id);
            strncat(genres, genre_name, sizeof(genres) - strlen(genres) - 1);
            if (j < num_genres - 1) {
                strncat(genres, ", ", sizeof(genres) - strlen(genres) - 1);
            }
        }

        const char *overview = json_object_get_string(json_object_object_get(film_json, "overview"));
        const char *language = json_object_get_string(json_object_object_get(film_json, "original_language"));
        const char *release_date = json_object_get_string(json_object_object_get(film_json, "release_date"));
        const char *poster_path = json_object_get_string(json_object_object_get(film_json, "poster_path"));
        float vote_average = json_object_get_double(json_object_object_get(film_json, "vote_average"));
        int vote_count = json_object_get_int(json_object_object_get(film_json, "vote_count"));
        float popularity = json_object_get_double(json_object_object_get(film_json, "popularity"));

        // --- Generazione valori casuali ---
        float price = ((rand() % 1500) + 500) / 100.0f; // tra 5.00 e 20.00 euro
        int available_copies = (rand() % 6) + 5; // tra 5 e 10 copie
        int borrowed_copies = 0; // puoi randomizzare se vuoi: (rand() % (available_copies/2 + 1))
        const char *status = "disponibile";
        // ----------------------------------

        Film film = {
            .titolo = title,
            .descrizione = overview,
            .lingua = language,
            .data_rilascio = release_date,
            .locandina = poster_path,
            .stato = status
        };
        strncpy(film.genere, genres, sizeof(film.genere) - 1);
        film.genere[sizeof(film.genere) - 1] = '\0';
        snprintf(film.voto_medio_str, sizeof(film.voto_medio_str), "%.1f", vote_average);
        snprintf(film.numero_voti_str, sizeof(film.numero_voti_str), "%d", vote_count);
        snprintf(film.popularita_str, sizeof(film.popularita_str), "%.2f", popularity);
        snprintf(film.prezzo_str, sizeof(film.prezzo_str), "%.2f", price);
        snprintf(film.numero_copie_disponibili_str, sizeof(film.numero_copie_disponibili_str), "%d", available_copies);
        snprintf(film.numero_copie_in_prestito_str, sizeof(film.numero_copie_in_prestito_str), "%d", borrowed_copies);

        if (title && overview && language && release_date && poster_path) {
            InsertFilm(conn, &film);
        } else {
            fprintf(stderr, "Dati incompleti o corrotti per il film con indice %zu\n", i);
        }
    }
}

void PopulateDatabaseWithTmdbData(PGconn *conn) {
    CURL *curl = curl_easy_init();
    if (!curl) {
        fprintf(stderr, "Errore nell'inizializzazione di cURL\n");
        return;
    }

    // Inizializza il seed del random solo una volta
    srand(time(NULL));

    const char *tmdb_api_key = "771b37a5e226fd843d9460fe33c41412";

    if (!tmdb_api_key) {
        fprintf(stderr, "API Key non trovata\n");
        return;
    }

    MemoryStruct chunk;
    for (int page = 1; page <= MAX_PAGES; page++) {
        char url[256];
        snprintf(url, sizeof(url), "%s%s&language=it-IT&page=%d", TMDB_API_URL, tmdb_api_key, page);

        chunk.memory = calloc(1, 1);
        chunk.size = 0;

        SetupCurl(curl, &chunk, url);
        CURLcode res;
        int retries = 0;
        do {
            res = curl_easy_perform(curl);
            if (res != CURLE_OK) {
                fprintf(stderr, "cURL failed: %s\n", curl_easy_strerror(res));
                retries++;
                sleep(REQUEST_DELAY * retries);
            }
        } while (res != CURLE_OK && retries < MAX_RETRIES);

        if (res != CURLE_OK) {
            free(chunk.memory);
            continue;
        }

        struct json_object *parsed_json, *results;
        parsed_json = json_tokener_parse(chunk.memory);
        if (parsed_json && json_object_object_get_ex(parsed_json, "results", &results)) {
            ParseAndInsertFilms(conn, results);
        } else {
            fprintf(stderr, "Errore nel parsing JSON per la pagina %d\n", page);
        }

        free(chunk.memory);
        json_object_put(parsed_json);
        sleep(REQUEST_DELAY);
    }

    curl_easy_cleanup(curl);
}

void* CronjobThread(void* arg) {
    PGconn *conn = PQconnectdb(CONNINFO);
    if (PQstatus(conn) != CONNECTION_OK) {
        fprintf(stderr, "Connection to database failed: %s", PQerrorMessage(conn));
        PQfinish(conn);
        return NULL;
    }

    while (1) {
        printf("Esecuzione del cronjob per aggiornare il database...\n");

        RemoveDuplicatesAndResetSequences(conn);

        PopulateDatabaseWithTmdbData(conn);
        printf("Aggiornamento del database completato.\n");

        sleep(450);
    }

    PQfinish(conn);
    return NULL;
}