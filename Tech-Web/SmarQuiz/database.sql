-- Script migliorato per creare la struttura del database per SMARTQUIZ

-- 1. Tabella degli Utenti
CREATE TABLE IF NOT EXISTS Utenti (
  Id SERIAL PRIMARY KEY,
  Nome VARCHAR(100) NOT NULL,
  Email VARCHAR(150) UNIQUE NOT NULL,
  Password VARCHAR(255) NOT NULL,
  CreatoIl TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabella dei Quiz
CREATE TABLE IF NOT EXISTS Quiz (
  Id SERIAL PRIMARY KEY,
  Titolo VARCHAR(255) NOT NULL,
  Descrizione TEXT,
  UtenteId INTEGER NOT NULL,
  ErroriMassimi INTEGER NOT NULL DEFAULT 0,
  CreatoIl TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (UtenteId) REFERENCES Utenti (Id) ON DELETE CASCADE
);

-- 3. Tabella delle Domande
CREATE TABLE IF NOT EXISTS Domande (
  Id SERIAL PRIMARY KEY,
  QuizId INTEGER NOT NULL,
  TestoDomanda TEXT NOT NULL,
  Tipo VARCHAR(50) NOT NULL CHECK (Tipo IN ('multiple', 'aperta')),  -- 'multiple' per domande a risposta multipla o 'aperta' per domande aperte
  Opzioni TEXT[],            -- Array di opzioni per le domande a risposta multipla, NULL per le domande aperte
  RispostaCorretta TEXT,
  CreatoIl TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (QuizId) REFERENCES Quiz (Id) ON DELETE CASCADE
);

-- 4. Tabella dei Tentativi
CREATE TABLE IF NOT EXISTS Tentativi (
  Id SERIAL PRIMARY KEY,
  QuizId INTEGER NOT NULL,
  NomeUtente VARCHAR(100) DEFAULT 'Anonimo',
  Punteggio INTEGER,          -- Calcolato alla fine del quiz
  Superato BOOLEAN,           -- TRUE se il quiz è stato superato, FALSE altrimenti
  IniziatoIl TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CompletatoIl TIMESTAMP,
  FOREIGN KEY (QuizId) REFERENCES Quiz (Id) ON DELETE CASCADE
);

-- 5. Tabella delle Risposte
CREATE TABLE IF NOT EXISTS Risposte (
  Id SERIAL PRIMARY KEY,
  TentativoId INTEGER NOT NULL,
  DomandaId INTEGER NOT NULL,
  Risposta TEXT NOT NULL,
  RispostoIl TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (TentativoId) REFERENCES Tentativi (Id) ON DELETE CASCADE,
  FOREIGN KEY (DomandaId) REFERENCES Domande (Id) ON DELETE CASCADE
);
