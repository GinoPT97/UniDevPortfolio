# Progetto LSO 2024-25

Progetto didattico per il corso di Sistemi Operativi. Questa cartella conserva la variante con server C, database PostgreSQL e client Android/Kotlin.

## Architettura

- **Server:** applicazione C con gestione di connessioni e accesso al database
- **Database:** PostgreSQL 16, inizializzato dal server
- **Client:** applicazione Android/Kotlin
- **Ambiente:** Docker Compose; il client e l'emulatore Android sono inclusi nel profilo `android`

## Prerequisiti

- Docker Engine con Docker Compose v2
- Per il profilo Android: supporto KVM (`/dev/kvm`) e risorse adeguate
- Una chiave API TMDB per importare i dati dei film

## Configurazione TMDB

Copia il template e inserisci la tua chiave API personale:

```bash
cp .env.example .env
```

Apri `.env` e imposta `TMDB_API_KEY`. Il file `.env` è escluso da Git: non pubblicare né committare chiavi personali. Senza la chiave il server può avviarsi, ma il popolamento del database da TMDB non viene eseguito.

## Avvio

Avvia database e server:

```bash
docker compose up --build
```

Per avviare anche emulatore e client Android:

```bash
docker compose --profile android up --build
```

Il primo avvio può richiedere tempo, soprattutto per il download dell'immagine dell'emulatore Android. Per fermare e rimuovere i container:

```bash
docker compose --profile android down
```

I dati PostgreSQL sono conservati nel volume Docker `db-data`; per istruzioni sui servizi, porte e profili, consulta [`docker-compose.yml`](./docker-compose.yml).
