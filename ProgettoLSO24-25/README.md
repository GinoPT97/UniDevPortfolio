# ProgettoLSO24-25

Questa cartella contiene la versione iniziale del progetto con client Android, che e in fase di ultimazione.

La versione completa e ultimata del progetto e disponibile nella repository originale, dove il client è stato evoluto in versione web.

## Docker

L'avvio completo con emulatore Android e client mobile richiede tempo al primo utilizzo, perche l'immagine `budtmo/docker-android:emulator_14.0` e molto pesante da scaricare.

Per un avvio piu rapido della parte base del progetto usa:

```bash
docker compose up --build
```

Questo comando avvia solo i servizi base come database e server.

Per avviare anche l'emulatore Android e il client mobile usa invece:

```bash
docker compose --profile android up --build
```

Dopo il primo download, i successivi avvii saranno piu veloci finche le immagini restano nella cache locale di Docker.