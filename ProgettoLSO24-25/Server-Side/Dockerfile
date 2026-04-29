FROM gcc:12.2.0 AS builder

RUN apt-get update && apt-get install -y --no-install-recommends \
    libcurl4-openssl-dev libjson-c-dev libpq-dev && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY *.c *.h ./

RUN test -f server.c && test -f Socket.c && test -f DBOperation.c && \
    test -f CronJobDB.c && test -f Utils.c || \
    { echo "File sorgenti mancanti!"; exit 1; } && \
    gcc -O2 -o server server.c Socket.c DBOperation.c CronJobDB.c Utils.c \
    -I/usr/include/postgresql -lpq -lpthread -ljson-c -lcrypto -lcurl || \
    { echo "Errore durante la compilazione"; exit 1; }

FROM debian:bookworm-slim

ENV DEBIAN_FRONTEND=noninteractive \
    LANG=en_US.UTF-8 \
    LC_ALL=en_US.UTF-8 \
    LOG_DB_QUERIES=true \
    TINI_SUBREAPER=1

RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates libcurl4 libjson-c5 libpq5 locales tini && \
    locale-gen en_US.UTF-8 && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=builder /app/server /app/server

EXPOSE 8080

ENTRYPOINT ["/usr/bin/tini", "--"]
CMD ["./server"]
