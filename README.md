# 🎓 UniDevPortfolio

Benvenuto nel mio **UniDevPortfolio**, una raccolta completa e strutturata di progetti accademici e personali sviluppati durante il mio percorso di studi in **Informatica** presso l'**Università degli Studi di Napoli Federico II**.

Questo repository comprende soluzioni e implementazioni realizzate per i corsi di **Laboratorio di Programmazione**, **Laboratorio di Algoritmi e Strutture Dati (LASD)**, **Laboratorio di Sistemi Operativi (LSO)**, **Basi di Dati**, **Tecnologie Web**, **Architettura degli Elaboratori**, **Machine Learning** e progetti personali di ricerca.

## 🛠️ Stack Tecnologico

**Linguaggi di Programmazione:** C, C++, Java, Kotlin, JavaScript, TypeScript, Python, Rust, SQL, Prolog, Standard ML, Assembly Motorola 68000, Bash  
**Framework e Librerie:** React Native (Expo), Angular, Vite.js, Node.js/Express, JCalendar, PostgreSQL JDBC  
**Strumenti di Sviluppo:** Android Studio, VS Code, Git, Docker, CMake, Makefile, Gradle  
**Database:** PostgreSQL  
**Architetture:** Client-Server (TCP socket), MVC, REST API, Single Page Applications (SPA), Mobile (Android/React Native)

## 📂 Struttura del Repository

### 🔧 Programmazione Sistemistica e Algoritmi

#### **C - Generico**
Implementazioni fondamentali in linguaggio C organizzate per argomento:
- **File / File e liste**: Manipolazione di file e liste su file (multi-file)
- **Ricorsione Basilare / Ricorsione su stringhe**: Algoritmi ricorsivi
- **Sort Multi-File**: Algoritmi di ordinamento (bubble, merge, quick sort)
- **Vettori e matrici**: Operazioni su array e matrici dinamiche
- **Primo Progetto Stack**: Implementazione di stack in C multi-file

#### **C - LAB (Laboratorio di Programmazione)**
Soluzioni a tracce d'esame universitarie:
- **Tracce d'Esame**: soluzioni complete (esami 2019–2021)
- **Cubo Magico 1 & 2**: generazione e verifica di cubi magici
- **Studente con Matrice**: gestione anagrafica con matrici dinamiche
- **Traccia partite / targa / giugno**: elaborazione dati su domini specifici

#### **C - LASD (Laboratorio di Algoritmi e Strutture Dati)**
Libreria e tracce d'esame per il corso LASD:
- Implementazione in `funzioni.c` / `libreria.h` / `main.c`
- `Soluzione-06-23/`: tre esercizi d'esame (giugno 2023)

#### **C - LSO (Laboratorio di Sistemi Operativi)**
Programmazione di sistema Linux:
- **Esami-Svolti**: 12 tracce d'esame risolte (2020–2026) con socket, fork, pipe, thread
- **Esercizi LSO 21-22**: pipe, fork/exec, bash scripting, thread POSIX
- **Socket-Esempi**: implementazioni complete di client/server TCP e UDP (iterativo, concorrente, multi-thread)

#### **Librerie-C**
Librerie dati custom, ciascuna con header + sorgente + main di test:
- **BST**: albero binario di ricerca
- **Grafi / MatrixGraph**: grafi con lista di adiacenza e matrice di adiacenza
- **Heap**: heap binario
- **Stack / Code**: stack e coda
- **List / Double-List / Circle-List**: liste semplici, doppie e circolari
- **Input**: lettore di input generico

### 🚀 Progetti LSO — Applicazioni Complete

#### **ProgettoLSO24-25**
Applicazione completa con architettura **Client Android (Kotlin/Gradle) + Server C + PostgreSQL**:
- `Server-Side/`: server C con socket TCP, operazioni su DB, cron job e utilità (`server.c`, `Socket`, `DBOperation`, `CronJobDB`, `Utils`)
- `Client/`: app Android nativa in Kotlin con Gradle
- `database.sql`: schema PostgreSQL
- `docker-compose.yml`: orchestrazione server + database

#### **ProgettoLSO24-25-ReactNative**
Versione del progetto con client **React Native (Expo, TypeScript)**:
- `Server-Side/`: stesso server C del progetto precedente
- `client/`: app Expo con routing (`app/`), componenti, hooks, bridge HTTP→TCP, costanti, asset
- `database.sql` e `docker-compose.yml` aggiornati per il nuovo client

### ☕ Programmazione Java

#### **Java - Personale**
Progetto Eclipse con materiale di studio e sperimentazione:
- **Java-Varie**: 22 capitoli di studio progressivo + Termostato MVC
- **Prove MVC**: implementazione pattern Model-View-Controller
- **ProveElementari**: esercizi elementari
- **Padding**: esercizi sul padding/formattazione

### 🌐 Sviluppo Web

#### **Tech-Web**
Esercitazioni e progetti per il corso Tecnologie Web:
- **Esercitazioni/**: 6 assignment + esercizi con Node.js/Express, Angular, Vite.js
  - REST API con Express (`controllers/`, `models/`, `routes/`, `middleware/`)
  - Todo List SPA con Angular
  - Hello World con Vite.js
  - Applicazioni to-do con gestione cookies e sessioni
- **todo-list-spa/**: SPA frontend + backend Express separati

### 🗄️ Database

#### **SQL**
Script e tracce d'esame SQL:
- `Eliminazione-Righe.sql`: script di utilità
- `Esami/`: 3 tracce d'esame (2019–2021) con query, join, funzioni

### 🔬 Linguaggi Funzionali e Logici

#### **ML (Standard ML)**
Programmazione funzionale:
- `FunGeneric.sml`, `FunMap.sml`, `FunExam.sml`: funzioni higher-order, map/reduce
- `Alberi.sml`: algoritmi su alberi
- `reduce.sml`, `prova.sml`: esercizi vari

#### **Prolog**
Programmazione logica con 30+ file `.pl`:
- Algoritmi su liste, grafi, puzzle constraint
- `TicTacToe.pl` / `tic-tac-toe/`: gioco completo
- `sudoku.pl`: risolutore Sudoku
- `birds.pl`, `Grafo.pl`, `tipi-java.pl`

### ⚙️ Programmazione di Sistema

#### **Assembly (Motorola 68000)**
Programmazione a basso livello per architettura M68000 con file `.a68`:
- **Operazioni**: MIN/MAX, fattoriale, prodotto scalare, potenza, somme
- **Stringhe**: ricerca, tokenizzazione, conversione maiuscole
- **Strutture Dati**: stack, array, operazioni bit
- **Indirizzamenti**: modalità di indirizzamento M68000
- **Esami**: tracce d'esame 2014–2015

#### **Ubuntu-Utility**
Script e configurazioni per ambienti Ubuntu/Linux:
- `Aggiornamenti.sh`, `InstallApp.sh`, `Ottimizzazioni.sh`: manutenzione sistema
- `dumpdir.sh`, `dumphere.sh`: dump del filesystem
- `autoinstall.yaml`: configurazione autoinstall
- `Utils-SH/ImageToPDF.sh`: conversione immagini in PDF
- `Utils-TXT/`: note su configurazione, alias e retroilluminazione
- `Virtual-Machine/`: script di setup e verifica VM

### 🦀 Rust

#### **Rust**
6 progetti Cargo indipendenti per l'apprendimento del linguaggio:
- `basic/`: esercizi base
- `helloworld/`: primo programma
- `fattoriale/`, `fibonacci/`, `NumeriPrimi/`: algoritmi matematici
- `CalcolatriceInterattiva/`: calcolatrice da terminale con input interattivo

### 🐍 Python

#### **Python**
Progetti di ricerca, analisi dati e scripting:
- **Esercizi-Vari**: 25+ file con plotting (matplotlib/numpy), FFT, statistiche, client/server TCP
- **ASD**: implementazioni di algoritmi di ricerca (`RIC-IT.py`)
- **Progetto-Ricerca**: `CodeToImage.py` (conversione codice→immagine), analisi numeri primi
- **Risk-Analisis**: `EarthquakeRiskAnalysis.py` e `MarketRiskAnalysis.py` con ambiente virtuale dedicato
- `Ripristino-Google.py`, `Sommatoria.py`: script di utilità

### ⚡ C++

#### **C++**
Esercizi C++ organizzati per argomento:
- **funzioni/**: 12 file (`codice01.cpp`…`codice12.cpp`) con header libreria
- **strutture dati/**: 42 file (`codice01.cpp`…`codice42.cpp`) su strutture dati avanzate con STL

## ⚙️ Setup e Installazione

### **Clone Repository**
```bash
git clone https://github.com/GinoPT97/UniDevPortfolio.git
cd UniDevPortfolio
```

### **Prerequisiti Sistema**
```bash
# Ubuntu/Debian
sudo apt update && sudo apt install -y \
  build-essential gcc g++ gdb valgrind \
  default-jdk \
  postgresql-client \
  nodejs npm \
  python3 python3-pip \
  rustup \
  git cmake make
```

### **Compilazione C / C - LSO**
```bash
# Esempio: compilare un esercizio LSO
cd "C - LSO/Socket-Esempi"
gcc -o serverTCP ServerTCP.c
gcc -o clientTCP ClientTCP.c
```

### **Progetti LSO con Docker**
```bash
# Avvio completo server C + PostgreSQL
cd ProgettoLSO24-25
docker compose up --build

# Versione React Native
cd ProgettoLSO24-25-ReactNative
docker compose up --build
```

### **Progetti Web (Node.js/Express)**
```bash
cd "Tech-Web/Esercitazioni/express-todo-list-rest"
npm install
node index.js
```

### **Rust**
```bash
cd Rust/CalcolatriceInterattiva
cargo run
```

### **Python (Risk Analysis)**
```bash
cd Python/Risk-Analisis
python3 -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt
python3 EarthquakeRiskAnalysis.py
```

## 🔍 Navigazione Consigliata

```
📁 Punti di ingresso suggeriti:
├── C - Generico/              # Fondamenti C
├── C - LASD/                  # Algoritmi e strutture dati
├── C - LSO/                   # Sistemi operativi e socket
├── ProgettoLSO24-25/          # Progetto completo Android + C + PostgreSQL
├── ProgettoLSO24-25-ReactNative/  # Progetto React Native + C + PostgreSQL
├── Tech-Web/                  # Sviluppo web moderno
├── Librerie-C/                # Librerie dati custom
├── Rust/                      # Apprendimento Rust
└── Python/Risk-Analisis/      # Analisi dati e ricerca
```

## 📞 Contatti

- **GitHub**: [@GinoPT97](https://github.com/GinoPT97)
- **Università**: Università degli Studi di Napoli Federico II — Dipartimento di Ingegneria Elettrica e delle Tecnologie dell'Informazione

---

## 📄 Licenza

Questo progetto è rilasciato sotto **MIT License**. Consulta il file [LICENSE](LICENSE) per i dettagli completi.

---

<div align="center">

**⭐ Se questo repository ti è stato utile, considera di lasciare una stella!**

**🔄 Fork it, improve it, share it - contribuisci alla community!**

*Sviluppato con ❤️ per la comunità developer e academic*

</div>
