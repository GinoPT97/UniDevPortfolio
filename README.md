# UniDevPortfolio

Portfolio tecnico universitario: raccolta strutturata di progetti, esercizi e soluzioni sviluppate durante il percorso accademico e attività di approfondimento personale presso l'Università degli Studi di Napoli Federico II.

La repository funge da **archivio didattico organizzato** per corso, linguaggio e area tematica — non è un singolo prodotto, ma un'ampia collezione di esperienze di apprendimento che spaziano da algoritmi e strutture dati a sistemi operativi, programmazione mobile e paradigmi dichiarativi.

---

## 📚 Contenuti Principali

### Linguaggi e Tecnologie

| Categoria | Contenuto |
| --- | --- |
| **Linguaggi imperativi** | C, C++, Java, Kotlin, JavaScript, TypeScript, Python, Rust, Bash |
| **Linguaggi dichiarativi** | Prolog, Standard ML |
| **Basso livello** | Assembly Motorola 68000 |
| **Database** | SQL, PostgreSQL |
| **Tecnologie e framework** | Docker, Gradle, CMake, Makefile, React Native/Expo, Android |

### Aree Tematiche Coperte

- **Programmazione di sistema**: processi, thread, socket TCP/UDP, pipe, concorrenza
- **Algoritmi e strutture dati**: liste, stack, code, heap, BST, grafi, ricerca e ordinamento
- **Sistemi operativi**: fork, gestione memoria, sincronizzazione
- **Programmazione mobile**: Android Kotlin, React Native
- **Database**: SQL, query, schema relazionali
- **Scripting e automation**: Shell script, Python per analisi dati
- **Logica computazionale**: Prolog, programmazione funzionale (ML)

---

## 📁 Struttura della Repository

### Fondamenti e Programmazione Procedurale

| Percorso | Descrizione |
| --- | --- |
| `C - Generico/` | Fondamenti del C: ricorsione, vettori, matrici, ordinamenti, file, stack e progetti multi-file |
| `C - LAB/` | Tracce di laboratorio ed esercizi d'esame in C con soluzioni complete |
| `C - LASD/` | Algoritmi e strutture dati in C: implementazioni con CMake e soluzioni d'esame |
| `Librerie-C/` | Librerie riutilizzabili: stack, code, heap, liste, BST, grafi e funzioni di input |
| `C++/` | Esercizi organizzati per funzioni e strutture dati in C++ |
| `Java - Personale/` | Studio Java: pattern MVC, esercizi elementari, piccoli progetti didattici |

### Sistemi Operativi e Networking

| Percorso | Descrizione |
| --- | --- |
| `C - LSO/` | Esercizi e soluzioni d'esame su sistemi operativi: fork, pipe, thread, socket, client/server concorrenti |
| `Assembly/` | Programmi su Assembly Motorola 68000: operazioni, stringhe, indirizzamenti, strutture dati |

### Progetti Completi e Applicazioni

| Percorso | Descrizione |
| --- | --- |
| `ProgettoLSO24-25/` | **Progetto capstone**: server C concorrente + database PostgreSQL + client Android Kotlin. Include Docker compose per ambiente completo. |
| `ProgettoLSO24-25-ReactNative/` | **Variante moderna**: stessa architettura backend C + PostgreSQL, ma con client Expo/React Native. Bridge HTTP verso il server. |
| `Python/` | Script, analisi numerica, data science, plotting, piccoli esempi di rete TCP e progetti di analisi dati |
| `Rust/` | Progetti Cargo per apprendimento del linguaggio: calcolatrici, esercizi di base, esperimenti di sicurezza memoria |

### Paradigmi Dichiarativi e Funzionali

| Percorso | Descrizione |
| --- | --- |
| `ML/` | Esercizi in Standard ML: funzioni, alberi, map/reduce, pattern matching, tracce d'esame |
| `Prolog/` | Logica e programmazione dichiarativa: liste, grafi, puzzle, sudoku, giochi |
| `SQL/` | Script SQL, tracce d'esame con query complesse, operazioni di normalizzazione e join |

### Utility e Supporto

| Percorso | Descrizione |
| --- | --- |
| `Ubuntu-Utility/` | Script shell e appunti pratici per ambienti Ubuntu/Linux |
| `FileProgrammi/` | Configurazioni e materiale di supporto secondario |

---

## 🚀 Punti di Ingresso Consigliati

Scegli in base ai tuoi obiettivi di apprendimento:

| Obiettivo | Dove iniziare |
| --- | --- |
| **Imparare C da zero** | `C - Generico/` |
| **Strutture dati e algoritmi** | `C - LASD/` o `Librerie-C/` |
| **Sistemi operativi: processi, thread, socket** | `C - LSO/Socket-Esempi/` o `C - LSO/Esami-Svolti/` |
| **Architettura multi-tier completa** | `ProgettoLSO24-25/` o `ProgettoLSO24-25-ReactNative/` |
| **Mobile e frontend web** | `ProgettoLSO24-25-ReactNative/` |
| **Backend con Docker** | `ProgettoLSO24-25/` |
| **Analisi dati e scripting** | `Python/` |
| **Imparare Rust** | `Rust/CalcolatriceInterattiva/` o `Rust/basic/` |
| **Logica e programmazione dichiarativa** | `Prolog/` e `ML/` |

---

## ⚡ Quick Start

### Clonazione

```bash
git clone https://github.com/GinoPT97/UniDevPortfolio.git
cd UniDevPortfolio
```

### Prerequisiti Generali

Per lavorare con la maggior parte del materiale:

```bash
sudo apt update
sudo apt install -y build-essential gcc g++ make cmake default-jdk \
  nodejs npm python3 python3-pip postgresql-client docker-compose
```

Per **Rust**:
```bash
rustup default stable
```

### Esempi di Esecuzione

**Progetto LSO (Android + server C + PostgreSQL):**
```bash
cd ProgettoLSO24-25
docker compose up --build
```

**Progetto LSO (React Native + server C):**
```bash
cd ProgettoLSO24-25-ReactNative
docker compose down --remove-orphans
docker compose up --build
```

**Script Python con virtual environment:**
```bash
cd Python/Risk-Analisis
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
python3 EarthquakeRiskAnalysis.py
```

**Progetto Rust:**
```bash
cd Rust/CalcolatriceInterattiva
cargo run
```

**Esempio C con socket TCP:**
```bash
cd "C - LSO/Socket-Esempi"
gcc -o serverTCP ServerTCP.c
gcc -o clientTCP ClientTCP.c
./serverTCP &
./clientTCP
```

---

## 📖 Documentazione Specifica

Le cartelle più complesse hanno **README dedicati**:

- [`ProgettoLSO24-25/README.md`](ProgettoLSO24-25/README.md) — Setup Docker, architettura Android
- [`ProgettoLSO24-25-ReactNative/README.md`](ProgettoLSO24-25-ReactNative/README.md) — Setup React Native, bridge HTTP

**Consiglio**: per i progetti completi, leggi prima il README locale, poi usa questo file come mappa di orientamento generale.

---

## 📋 Informazioni Utili

### Organizzazione della Repository

- **Quasi ogni cartella è autonoma**: non esiste una build unica. Ogni progetto ha prerequisiti e setup propri.
- **Ampia varietà didattica**: convivono esercizi piccoli, librerie riutilizzabili e progetti enterprise-like.
- **Materiale eterogeneo**: oltre a codice eseguibile, contiene configurazioni, script e appunti di supporto.
- **Storicità**: alcune cartelle riflettono le piattaforme o ambienti usati durante il corso; alcuni progetti sono evoluti nel tempo.

### Dimensione e Natura

- Repository size: ~500 MB
- Creato: Settembre 2023
- Licenza: **MIT** (vedi [`LICENSE`](LICENSE))
- Ateneo: Università degli Studi di Napoli Federico II

---

## 👤 Contatti

- **GitHub**: [@GinoPT97](https://github.com/GinoPT97)
- **Università**: Università degli Studi di Napoli Federico II

---

## 📄 Licenza

Questo repository è distribuito sotto licenza **MIT**. Consulta [`LICENSE`](LICENSE) per i dettagli completi.

---

**Nota**: Questo portfolio riflette un percorso accademico ricco e variegato. È pensato sia come riferimento tecnico per chi apprende che come documentazione dell'evoluzione didattica in informatica.
