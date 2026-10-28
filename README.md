# UniDevPortfolio

Portfolio tecnico universitario e archivio didattico di esercizi, soluzioni, librerie e progetti sviluppati durante il percorso accademico presso l'Università degli Studi di Napoli Federico II.

Questa repository non è un singolo prodotto software: è una raccolta organizzata per linguaggio, corso e area tematica, pensata come riferimento pratico per ripasso, studio e sviluppo personale.

## Obiettivo

- raccogliere esercizi e soluzioni di base avanzata
- documentare progetti universitari e mini-applicazioni
- offrire una mappa chiara di apprendimento per linguaggi e temi
- mantenere un archivio navigabile, anche fuori dal contesto universitario

---

## Panoramica

### Linguaggi e tecnologie trattati

- Linguaggi imperativi e di sistema: C, C++, Java, Kotlin, Rust, Bash
- Linguaggi dichiarativi e funzionali: Prolog, Standard ML
- Scripting e analisi: Python, SQL
- Basso livello: Assembly Motorola 68000
- Database e infrastruttura: PostgreSQL, Docker, Gradle, CMake, Makefile

### Aree di studio

- algoritmi e strutture dati
- sistemi operativi e networking
- programmazione concorrente
- database e SQL
- programmazione mobile Android
- logica computazionale e paradigma dichiarativo
- data science e analisi dati

---

## Mappa della repository

### Fondamenti e programmazione procedurale

| Cartella | Descrizione |
| --- | --- |
| `C - Generico/` | esercizi e mini-progetti in C: ricorsione, vettori, matrici, file, strutture dati, programmi multi-file |
| `C - LAB/` | tracce di laboratorio, esercizi d'esame e soluzioni in C |
| `C - LASD/` | algoritmi e strutture dati in C con soluzioni e template d'esame |
| `Librerie-C/` | librerie riutilizzabili: liste, stack, code, heap, BST, grafi, input helpers |
| `C++/` | esercizi e implementazioni in C++ orientate a funzioni e strutture dati |
| `Java - Personale/` | esercitazioni Java, pattern MVC, semplici progetti didattici |

### Sistemi operativi, networking e basso livello

| Cartella | Descrizione |
| --- | --- |
| `C - LSO/` | esercizi e soluzioni su processi, thread, pipe, socket e sistemi operativi |
| `Assembly/` | programmi in Assembly Motorola 68000 con operazioni, stringhe e indirizzamenti |

### Progetti completi e applicazioni

| Cartella | Descrizione |
| --- | --- |
| `ProgettoLSO24-25/` | progetto capstone universitario: backend concorrente in C, database PostgreSQL, client Android/Kotlin e Docker |
| `Python/` | script, analisi numerica, progetti di ricerca, data analysis e piccoli esempi di rete |
| `Rust/` | progetti introduttivi e applicativi in Rust: calcolatrice, fibonacci, numeri primi, hello world |

### Paradigmi dichiarativi e funzionali

| Cartella | Descrizione |
| --- | --- |
| `ML/` | esercizi in Standard ML: funzioni, alberi, pattern matching, map/reduce |
| `Prolog/` | logica computazionale, puzzle, grafi, sudoku e giochi |
| `SQL/` | script SQL e tracce d'esame su query, join, normalizzazione e schema relazionale |

### Utility e supporto

| Cartella | Descrizione |
| --- | --- |
| `Ubuntu-Utility/` | script shell, setup Linux e strumenti utili per ambienti Ubuntu |
| `FileProgrammi/` | configurazioni, file di supporto e tool secondari |
| `bin/` | file eseguibili o artefatti vari |

---

## Percorsi consigliati

Scegli la cartella in base al tuo obiettivo di studio:

| Obiettivo | Punto di partenza |
| --- | --- |
| imparare C da zero | `C - Generico/` |
| strutture dati e algoritmi | `C - LASD/`, `Librerie-C/` |
| sistemi operativi e socket | `C - LSO/Socket-Esempi/`, `C - LSO/Esami-Svolti/` |
| architettura multi-tier | `ProgettoLSO24-25/` |
| analisi dati e scripting | `Python/` |
| Rust | `Rust/CalcolatriceInterattiva/`, `Rust/basic/` |
| logica e programmazione dichiarativa | `Prolog/`, `ML/` |
| SQL | `SQL/` |

---

## Quick start

### Clonazione

```bash
git clone git@github.com:Kenobi1797/UniDevPortfolio.git
cd UniDevPortfolio
```

### Prerequisiti generali

Per lavorare con la maggior parte del materiale, in un ambiente Ubuntu/Debian:

```bash
sudo apt update
sudo apt install -y build-essential gcc g++ make cmake default-jdk \
  nodejs npm python3 python3-pip postgresql-client docker-compose
```

Per Rust:

```bash
rustup default stable
```

### Esempi di esecuzione

#### Progetto LSO: backend solo database + server

```bash
cd ProgettoLSO24-25
docker compose up --build
```

#### Progetto LSO: stack completo con emulatore Android

```bash
cd ProgettoLSO24-25
docker compose --profile android up --build
```

#### Script Python con virtual environment

```bash
cd Python/Risk-Analisis
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
python3 EarthquakeRiskAnalysis.py
```

#### Progetto Rust

```bash
cd Rust/CalcolatriceInterattiva
cargo run
```

#### Esempio C con socket TCP

```bash
cd "C - LSO/Socket-Esempi"
gcc -o serverTCP ServerTCP.c
gcc -o clientTCP ClientTCP.c
./serverTCP &
./clientTCP
```

---

## Documentazione specifica

Per i progetti più complessi, la documentazione locale è la fonte più affidabile:

- [`ProgettoLSO24-25/README.md`](ProgettoLSO24-25/README.md) — setup Docker, architettura del sistema e profilo Android

Questa root README funge da indice generale e guida di orientamento.

---

## Nota sulla struttura

- la repository è composta da cartelle quasi autonome
- non esiste un'unica build centrale o un solo comando di esecuzione condiviso
- ogni modulo può richiedere dipendenze, toolchain o configurazioni diverse
- alcuni contenuti riflettono ambienti o tecnologie usate nel percorso universitario e sono mantenuti come riferimento storico didattico

---

## Informazioni utili

- dimensione approssimativa: circa 535 MB (working tree, escluso `.git`)
- anno di inizio: settembre 2023
- licenza: MIT ([`LICENSE`](LICENSE))
- ateneo: Università degli Studi di Napoli Federico II

---

## Contatti

- GitHub: [@Kenobi1797](https://github.com/Kenobi1797)
- Università: Università degli Studi di Napoli Federico II

---

## Licenza

Questo repository è distribuito sotto licenza MIT. Consulta [`LICENSE`](LICENSE) per i dettagli completi.

---

Questo portfolio riflette un percorso accademico ampio e variegato, pensato come materiale di studio, riferimento tecnico e archivio personale di esperienze pratiche in informatica.
