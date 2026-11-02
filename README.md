# UniDevPortfolio

Portfolio tecnico e archivio di progetti sviluppati durante il percorso di studi in Informatica all'Università degli Studi di Napoli Federico II.

La repository raccoglie esercizi, soluzioni, librerie e progetti completi: dalle basi della programmazione alle strutture dati, dai sistemi operativi all'analisi dei dati. È pensata per mostrare un percorso di apprendimento concreto e offrire una mappa pratica dei materiali.

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](./LICENSE)
[![Languages](https://img.shields.io/badge/Languages-C%20%7C%20C%2B%2B%20%7C%20Java%20%7C%20Python%20%7C%20Rust-informational)](#panoramica-tecnica)
[![University](https://img.shields.io/badge/University-Federico%20II-0066a1)](https://www.unina.it/)

## Progetti in evidenza

### CineBox — progetto di Sistemi Operativi

Applicazione didattica multi-servizio con server concorrente in C, database PostgreSQL e client Android/Kotlin. Docker Compose orchestra database e server; un profilo opzionale avvia anche l'emulatore Android e il client.

- **Tecnologie:** C, socket, PostgreSQL, Docker Compose, Android, Kotlin, Gradle
- **Avvio e note sullo stato:** [README del progetto](./ProgettoLSO24-25/README.md)
- **Configurazione:** per importare i film da TMDB serve una chiave API; parti dal [template `.env.example`](./ProgettoLSO24-25/.env.example)
- **Codice:** [server](./ProgettoLSO24-25/Server-Side/) · [client Android](./ProgettoLSO24-25/Client/)

### Risk Analysis — analisi di eventi sismici

Progetto Python che genera un catalogo sismico sintetico con parametri ispirati a modelli realistici e lo analizza con strumenti statistici e di machine learning. Il progetto include anche un'analisi del rischio di mercato.

- **Tecnologie:** Python, NumPy, pandas, SciPy, scikit-learn, Matplotlib
- **Codice e dipendenze:** [Python/Risk-Analisis](./Python/Risk-Analisis/)

### Librerie e strutture dati in C

Raccolta di implementazioni riutilizzabili e programmi di esercizio per liste, stack, code, heap, alberi di ricerca e grafi.

- **Tecnologie:** C, Make, CMake
- **Codice:** [Librerie-C](./Librerie-C/) · [C - LASD](./C%20-%20LASD/)

### Calcolatrice interattiva in Rust

Piccolo progetto Cargo per esplorare la struttura di un'applicazione Rust e la gestione dell'input in una CLI.

- **Tecnologie:** Rust, Cargo
- **Codice:** [Rust/CalcolatriceInterattiva](./Rust/CalcolatriceInterattiva/)

---

## Panoramica tecnica

| Area | Linguaggi e tecnologie |
| --- | --- |
| Programmazione e sistemi | C, C++, Java, Kotlin, Rust, Bash |
| Scripting e analisi | Python, NumPy, pandas, SciPy, scikit-learn |
| Paradigmi dichiarativi e funzionali | Prolog, Standard ML |
| Basso livello | Assembly Motorola 68000 |
| Database e strumenti | SQL, PostgreSQL, Docker, Gradle, CMake, Make |

### Temi

Algoritmi e strutture dati · processi e thread · concorrenza · pipe e socket · database relazionali · applicazioni Android · analisi dei dati · logica computazionale.

## Esplora la repository

| Cartella | Contenuti |
| --- | --- |
| [C - Generico](./C%20-%20Generico/) | Esercizi e mini-progetti: ricorsione, vettori, matrici, file e programmi multi-file |
| [C - LAB](./C%20-%20LAB/) | Tracce di laboratorio ed esercizi d'esame in C |
| [C - LASD](./C%20-%20LASD/) | Algoritmi e strutture dati, con esercizi e soluzioni |
| [Librerie-C](./Librerie-C/) | Implementazioni di liste, stack, code, heap, BST e grafi |
| [C - LSO](./C%20-%20LSO/) | Processi, thread, pipe, socket ed esercizi di sistemi operativi |
| [C++](./C%2B%2B/) | Esercizi su funzioni e strutture dati |
| [Java - Personale](./Java%20-%20Personale/) | Esercitazioni Java e piccoli progetti, inclusi esempi MVC |
| [Assembly](./Assembly/) | Esempi e tracce in Assembly Motorola 68000 |
| [ProgettoLSO24-25](./ProgettoLSO24-25/) | Progetto Android e server/database per il corso di Sistemi Operativi |
| [Python](./Python/) | Esercizi, analisi dati, progetti di ricerca ed esempi di rete |
| [Rust](./Rust/) | Progetti Cargo introduttivi e applicativi |
| [ML](./ML/) | Esercizi in Standard ML |
| [Prolog](./Prolog/) | Programmazione logica: liste, grafi, puzzle e giochi |
| [SQL](./SQL/) | Query, join, normalizzazione e tracce d'esame |
| [Ubuntu-Utility](./Ubuntu-Utility/) | Script e appunti per ambienti Ubuntu/Linux |

### Percorsi di studio

- **Primi passi in C:** [C - Generico](./C%20-%20Generico/) → [C - LAB](./C%20-%20LAB/)
- **Strutture dati:** [C - LASD](./C%20-%20LASD/) → [Librerie-C](./Librerie-C/)
- **Sistemi operativi e networking:** [C - LSO/Socket-Esempi](./C%20-%20LSO/Socket-Esempi/) → [C - LSO/Esami-Svolti](./C%20-%20LSO/Esami-Svolti/)
- **Analisi dati:** [Python](./Python/) → [Risk-Analisis](./Python/Risk-Analisis/)
- **Programmazione dichiarativa:** [Prolog](./Prolog/) e [ML](./ML/)

## Avvio rapido

Non esiste un'unica build: le cartelle sono progetti indipendenti e possono richiedere toolchain diverse. Installa solo gli strumenti necessari al progetto che vuoi eseguire.

### Clona la repository

```bash
git clone https://github.com/Kenobi1797/UniDevPortfolio.git
cd UniDevPortfolio
```

### Esempi

**Backend del progetto LSO** (Docker e Docker Compose):

```bash
cd ProgettoLSO24-25
cp .env.example .env
# Inserisci la tua chiave TMDB_API_KEY nel file .env
docker compose up --build
```

**Stack completo con emulatore Android** (richiede supporto KVM e risorse adeguate):

```bash
cd ProgettoLSO24-25
docker compose --profile android up --build
```

**Analisi Python:**

```bash
cd Python/Risk-Analisis
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
python EarthquakeRiskAnalysis.py
```

**Calcolatrice Rust:**

```bash
cd Rust/CalcolatriceInterattiva
cargo run
```

Per i dettagli e i prerequisiti specifici, consulta il README della singola cartella quando disponibile.

## Come orientarsi

- Parti dai progetti in evidenza per una panoramica delle applicazioni più articolate.
- Per esercizi e soluzioni, usa la mappa delle cartelle e i percorsi di studio.
- Verifica gli eventuali README locali prima di installare dipendenze o avviare un progetto.
- I materiali sono principalmente didattici; alcune implementazioni e configurazioni possono riflettere il contesto in cui sono state realizzate.

## Informazioni

- **Ateneo:** Università degli Studi di Napoli Federico II
- **GitHub:** [@Kenobi1797](https://github.com/Kenobi1797)
- **Licenza:** [MIT](./LICENSE)

---

Repository didattica e personale: i progetti non sono necessariamente mantenuti come prodotti di produzione. Le dipendenze di terze parti restano soggette alle rispettive licenze.
