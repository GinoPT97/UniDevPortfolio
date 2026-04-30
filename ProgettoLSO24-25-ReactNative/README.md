# React Native Docker Stack

Questa cartella contiene la versione React Native del progetto Cinebox con stack Docker completo.

## Servizi inclusi

- `db`: PostgreSQL 16 con bootstrap da `database.sql`
- `server`: backend C su socket TCP (`8080`)
- `react-native-client`: Expo + bridge HTTP->TCP (`8090`) per collegare il client React Native al server C

## Avvio

Eseguire dalla cartella `ProgettoLSO24-25-ReactNative`:

```bash
docker compose down --remove-orphans
docker compose up --build
```

## Endpoint utili

- Server C: `localhost:8080`
- Bridge API (client -> server C): `localhost:8090`
- Expo Metro: `localhost:8081`
- Expo dev tools: `19000`, `19001`, `19002`
- PostgreSQL: `localhost:5433`

## Come usa la rete il client

- Il frontend React Native invia comandi a `POST /command` sul bridge.
- Il bridge apre una connessione TCP verso `server:8080` nel network Docker.
- In questo modo il client replica i comandi dell'app Android (LOGIN, GET_FILMS, CHECKOUT, notifiche, dashboard, admin).

## Nota manutenzione

Il backend C presente qui e allineato alla versione in `ProgettoLSO24-25/Server-Side`.
