FROM debian:bookworm-slim AS builder

RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc libcurl4-openssl-dev libjson-c-dev libpq-dev && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY *.c *.h ./
RUN gcc -O2 -o server server.c Socket.c DBOperation.c CronJobDB.c Utils.c \
    -I/usr/include/postgresql -lpq -lpthread -ljson-c -lcrypto -lcurl

FROM debian:bookworm-slim

RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates libcurl4 libjson-c5 libpq5 libssl3 tini && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=builder /app/server .

EXPOSE 8080
ENTRYPOINT ["/usr/bin/tini", "--"]
CMD ["./server"]
