# React Native Docker Stack

Questa sottocartella contiene una versione Docker dedicata per il progetto React Native.

Servizi inclusi:
- `db`: PostgreSQL 16 con inizializzazione da `database.sql`
- `server`: backend C copiato da `ProgettoLSO24-25/Server-Side`
- `react-native-client`: avvio Expo del client React Native

## Avvio

Eseguire i comandi dalla cartella `docker-rn-stack`:

```bash
docker compose down --remove-orphans
docker compose up --build
```

## Endpoint utili

- Backend: `http://localhost:8080`
- Expo Metro: `http://localhost:8081`
- Expo dev ports: `19000`, `19001`, `19002`
- PostgreSQL: `localhost:5433`

## Note

- Il backend usato qui e una copia del codice presente in `ProgettoLSO24-25/Server-Side`.
- Se aggiorni il backend principale, ricopia i file in questa cartella per mantenere allineata la versione React Native.
