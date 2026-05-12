# UniDevPortfolio

Raccolta strutturata di progetti, esercizi e materiali sviluppati durante il percorso universitario e in attività personali di approfondimento.

La repository non rappresenta un singolo prodotto, ma un archivio tecnico organizzato per corso, linguaggio e area tematica: algoritmi, sistemi operativi, sviluppo web, basi di dati, programmazione mobile, linguaggi logici e funzionali.

Nel portfolio i progetti piu forti da usare come vetrina sono soprattutto quelli completi e multi-componente; alcune cartelle, come Tech-Web, restano invece utili soprattutto come archivio didattico e tracciamento del percorso formativo.

## Panoramica

- Ambiti coperti: programmazione in C e C++, strutture dati, sistemi operativi, socket TCP/UDP, sviluppo web, SQL, mobile, scripting e linguaggi dichiarativi.
- Linguaggi presenti: C, C++, Java, Kotlin, JavaScript, TypeScript, Python, Rust, SQL, Prolog, Standard ML, Assembly Motorola 68000, Bash.
- Tecnologie presenti nella repo: Docker, PostgreSQL, Node.js/Express, Angular, Vite, Expo/React Native, Gradle, CMake, Makefile.
- Organizzazione: quasi ogni cartella è autonoma e ha prerequisiti propri; non esiste un build unico per tutta la repo.

## Struttura del Repository

| Percorso | Contenuto |
| --- | --- |
| `Assembly/` | Esercizi e tracce su Assembly Motorola 68000: operazioni, stringhe, indirizzamenti, strutture dati ed esami. |
| `C - Generico/` | Fondamenti del C: file, ricorsione, vettori, matrici, ordinamenti, stack e progetti multi-file. |
| `C - LAB/` | Soluzioni a tracce di laboratorio ed esercizi d'esame in C. |
| `C - LASD/` | Materiale per algoritmi e strutture dati, con sorgenti C, CMake e una soluzione d'esame dedicata. |
| `C - LSO/` | Esercizi ed esami su sistemi operativi: fork, pipe, thread, socket e client/server concorrenti. |
| `Librerie-C/` | Implementazioni riutilizzabili di stack, code, heap, liste, BST, grafi e utilità di input. |
| `C++/` | Esercizi organizzati per funzioni e strutture dati. |
| `Java - Personale/` | Materiale Java di studio, prove MVC, esercizi elementari e piccoli progetti locali. |
| `Python/` | Script, esercizi numerici, plotting, piccoli esempi TCP e progetti di analisi. |
| `Rust/` | Progetti Cargo indipendenti per l'apprendimento del linguaggio. |
| `ML/` | Esercizi in Standard ML su funzioni, alberi, map/reduce e tracce d'esame. |
| `Prolog/` | Esercizi di logica su liste, grafi, puzzle, sudoku e giochi. |
| `SQL/` | Script SQL e tracce d'esame con query e operazioni su database. |
| `Tech-Web/` | Esercitazioni e mini-progetti web del corso di Tecnologie Web, mantenuti come materiale didattico e storico. |
| `ProgettoLSO24-25/` | Progetto completo con server C, database PostgreSQL e client Android Kotlin. |
| `ProgettoLSO24-25-ReactNative/` | Variante del progetto con client Expo/React Native e bridge HTTP verso il server C. |
| `Ubuntu-Utility/` | Script shell e appunti pratici per ambienti Ubuntu/Linux. |
| `FileProgrammi/` | File di configurazione e materiale di supporto non centrale rispetto ai progetti principali. |

## Aree Principali

### Programmazione di Sistema e Algoritmi

- `C - Generico/`, `C - LAB/`, `C - LASD/` e `Librerie-C/` raccolgono il nucleo del materiale su C, strutture dati, algoritmi e preparazione a prove pratiche.
- `C - LSO/` contiene la parte più orientata a processi, thread, pipe e networking, con esempi client/server immediatamente leggibili.
- `Assembly/` completa il materiale più vicino all'hardware con esercizi su architettura M68000.

### Progetti Completi

- `ProgettoLSO24-25/` include backend C, schema database e client Android. È utile come esempio di architettura multi-componente con Docker.
- `ProgettoLSO24-25-ReactNative/` porta lo stesso dominio applicativo su stack Expo/React Native, mantenendo il backend C e introducendo un bridge applicativo.

### Materiale Web Di Supporto

- `Tech-Web/` raccoglie esercitazioni, assignment e mini-progetti utili a documentare il percorso sul web, ma non è la sezione più rappresentativa della repo se l'obiettivo è mostrare i progetti migliori.
- Per una presentazione portfolio conviene considerarla complementare ai progetti più maturi, non il punto di accesso principale.

### Linguaggi e Paradigmi

- `Python/` e `Rust/` raccolgono esercizi e sperimentazioni più moderne o orientate all'analisi.
- `ML/` e `Prolog/` coprono paradigmi funzionali e logici.
- `Java - Personale/` documenta materiale di studio e pattern classici come MVC.

## Da Dove Iniziare

Se vuoi esplorare la repo in modo mirato, questi sono i punti di ingresso più utili:

| Obiettivo | Cartella consigliata |
| --- | --- |
| Fondamenti di C | `C - Generico/` |
| Tracce pratiche in C | `C - LAB/` |
| Strutture dati e algoritmi | `C - LASD/` oppure `Librerie-C/` |
| Socket, processi e thread | `C - LSO/Socket-Esempi/` oppure `C - LSO/Esami-Svolti/` |
| Web e frontend applicativo | `ProgettoLSO24-25-ReactNative/` |
| Mobile e backend con Docker | `ProgettoLSO24-25/` oppure `ProgettoLSO24-25-ReactNative/` |
| Analisi e scripting Python | `Python/` |
| Esercizi Rust | `Rust/CalcolatriceInterattiva/` oppure `Rust/basic/` |
| Logica e programmazione dichiarativa | `Prolog/` e `ML/` |

## Avvio Rapido

### Clonazione

```bash
git clone https://github.com/GinoPT97/UniDevPortfolio.git
cd UniDevPortfolio
```

### Prerequisiti Generali

Per lavorare con la maggior parte del materiale è utile avere disponibili:

```bash
sudo apt update
sudo apt install -y build-essential gcc g++ make cmake default-jdk \
  nodejs npm python3 python3-pip postgresql-client docker-compose
```

Per Rust:

```bash
rustup default stable
```

### Esempi di Esecuzione

Progetto LSO con Android e server C:

```bash
cd ProgettoLSO24-25
docker compose up --build
```

Versione React Native:

```bash
cd ProgettoLSO24-25-ReactNative
docker compose down --remove-orphans
docker compose up --build
```

Esempio Express dal materiale didattico:

```bash
cd "Tech-Web/Esercitazioni/express-hello-world"
npm install
npm start
```

Esempio Python con dipendenze:

```bash
cd Python/Risk-Analisis
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
python3 EarthquakeRiskAnalysis.py
```

Esempio Rust:

```bash
cd Rust/CalcolatriceInterattiva
cargo run
```

Esempio C su socket:

```bash
cd "C - LSO/Socket-Esempi"
gcc -o serverTCP ServerTCP.c
gcc -o clientTCP ClientTCP.c
```

## README Specifici

Le cartelle più articolate hanno documentazione dedicata. In particolare:

- `ProgettoLSO24-25/README.md`
- `ProgettoLSO24-25-ReactNative/README.md`
- `Tech-Web/Esercitazioni/Assignment-01-solution/README.md`

Per questi progetti è preferibile seguire prima la documentazione locale e usare il README principale come mappa di orientamento.

## Note Sulla Repository

- La repo è pensata come portfolio tecnico e archivio di studio, quindi convivono esercizi piccoli, librerie didattiche e progetti più completi.
- Alcune cartelle sono storiche o dipendono dall'ambiente usato durante il corso; i progetti complessi hanno setup propri.
- Alcuni materiali non sono codice eseguibile in senso stretto, ma configurazioni, documenti o appunti di supporto.
- `Tech-Web/` è mantenuta perché utile a mostrare continuità e varietà del percorso, ma nel README resta volutamente in secondo piano rispetto ai progetti completi.

## Contatti

- GitHub: [@GinoPT97](https://github.com/GinoPT97)
- Ateneo: Università degli Studi di Napoli Federico II

## Licenza

Il repository è distribuito sotto licenza MIT. Vedi `LICENSE` per i dettagli.
