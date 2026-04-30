# Cinebox React Native Client

Client React Native (Expo) compatibile con il backend C socket-based tramite bridge HTTP->TCP.

## Script disponibili

- `npm run start`: avvia Expo in locale
- `npm run bridge`: avvia solo il bridge HTTP->TCP
- `npm run start:docker`: avvia bridge + Expo insieme (usato in docker-compose)
- `npm run lint`: lint del progetto

## Variabili ambiente principali

- `EXPO_PUBLIC_BRIDGE_URL`: URL del bridge usato dall'app (default: `http://localhost:8090`)
- `BRIDGE_PORT`: porta del bridge Node (default: `8090`)
- `TCP_SERVER_HOST`: host del server C per il bridge (default: `server`)
- `TCP_SERVER_PORT`: porta del server C per il bridge (default: `8080`)

## Flussi supportati

- Login / registrazione
- Catalogo film con filtro ricerca, genere e ordinamento
- Carrello e checkout con controllo disponibilita
- Dashboard utente (ultimi noleggi, top film, noleggi attivi)
- Dashboard admin (panoramica noleggi e restituzione)
- Notifiche admin/utente
- Gestione admin del limite `max_rentals`
