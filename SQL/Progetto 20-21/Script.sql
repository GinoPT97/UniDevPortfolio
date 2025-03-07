-- Creare il nuovo tipo ENUM con le categorie corrette
CREATE TYPE TIPOLOGIA AS ENUM ('Frutta', 'Verdura', 'Farinacei', 'Latticini', 'Uova', 'Confezionati');

-- Tabella cliente
CREATE TABLE IF NOT EXISTS cliente (
    codcliente SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL CHECK (nome ~* '^[A-Za-z ]+$'),
    cognome VARCHAR(255) NOT NULL CHECK (cognome ~* '^[A-Za-z ]+$'),
    codicefiscale CHAR(16) NOT NULL CHECK (codicefiscale ~* '^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$'),
    indirizzo VARCHAR(255) NOT NULL,
    telefono VARCHAR(20) CHECK (telefono ~* '^[0-9+ ]+$'),
    email VARCHAR(255) NOT NULL CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]+$')
);

-- Tabella dipendente
CREATE TABLE IF NOT EXISTS dipendente (
    coddipendente SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL CHECK (nome ~* '^[A-Za-z ]+$'),
    cognome VARCHAR(255) NOT NULL CHECK (cognome ~* '^[A-Za-z ]+$'),
    codicefiscale CHAR(16) NOT NULL CHECK (codicefiscale ~* '^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$'),
    indirizzo VARCHAR(255) NOT NULL,
    telefono VARCHAR(20) CHECK (telefono ~* '^[0-9+ ]+$'),
    email VARCHAR(255) NOT NULL CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]+$')
);

-- Tabella tessera
CREATE TABLE IF NOT EXISTS tessera (
    codtessera SERIAL PRIMARY KEY,
    numeropunti REAL NOT NULL DEFAULT 0.00,
    codcliente INTEGER NOT NULL UNIQUE,
    CONSTRAINT TesseraFK FOREIGN KEY (codcliente) REFERENCES cliente (codcliente)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- Tabella prodotto
CREATE TABLE IF NOT EXISTS prodotto (
    codprodotto SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL CHECK (nome ~* '^[A-Za-z ]+$'),
    descrizione VARCHAR(500),
    prezzo NUMERIC DEFAULT 0.00 CHECK (prezzo >= 0.00),
    luogoprovenienza VARCHAR(255),
    dataraccolta DATE,
    datamungitura DATE,
    glutine BOOLEAN,
    datascadenza DATE,
    dataproduzione DATE,
    categoria TIPOLOGIA,
    scorta INT CHECK (scorta >= 0)
);

-- Rimuovere il vincolo CHECK unificato
ALTER TABLE prodotto DROP CONSTRAINT IF EXISTS categoria_check;

-- Aggiungere i vincoli CHECK separati per ogni categoria

-- Frutta
ALTER TABLE prodotto ADD CONSTRAINT check_frutta CHECK (
  categoria <> 'Frutta' OR
  (categoria = 'Frutta' AND dataraccolta IS NOT NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NULL AND glutine IS NULL)
);

-- Verdura
ALTER TABLE prodotto ADD CONSTRAINT check_verdura CHECK (
  categoria <> 'Verdura' OR
  (categoria = 'Verdura' AND dataraccolta IS NOT NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NULL AND glutine IS NULL)
);

-- Latticini
ALTER TABLE prodotto ADD CONSTRAINT check_latticini CHECK (
  categoria <> 'Latticini' OR
  (categoria = 'Latticini' AND dataraccolta IS NULL AND datamungitura IS NOT NULL AND dataproduzione IS NOT NULL AND datascadenza IS NULL AND glutine IS NULL)
);

-- Farinacei
ALTER TABLE prodotto ADD CONSTRAINT check_farinacei CHECK (
  categoria <> 'Farinacei' OR
  (categoria = 'Farinacei' AND dataraccolta IS NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NULL AND glutine IS NOT NULL)
);

-- Uova
ALTER TABLE prodotto ADD CONSTRAINT check_uova CHECK (
  categoria <> 'Uova' OR
  (categoria = 'Uova' AND dataraccolta IS NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NOT NULL AND glutine IS NULL)
);

-- Confezionati
ALTER TABLE prodotto ADD CONSTRAINT check_confezionati CHECK (
  categoria <> 'Confezionati' OR
  (categoria = 'Confezionati' AND dataraccolta IS NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NOT NULL AND glutine IS NULL)
);

-- Tabella ordine
CREATE TABLE IF NOT EXISTS ordine (
    codordine SERIAL PRIMARY KEY,
    prezzototale REAL NOT NULL DEFAULT 0.00 CHECK (prezzototale >= 0),
    dataacquisto DATE NOT NULL,
    codcliente INTEGER NOT NULL,
    coddipendente INTEGER NOT NULL,
    CONSTRAINT ordineclientefk FOREIGN KEY (codcliente) REFERENCES cliente (codcliente)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT ordinedipendentefk FOREIGN KEY (coddipendente) REFERENCES dipendente (coddipendente)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

-- Tabella articoliordine
CREATE TABLE IF NOT EXISTS articoliordine (
    codordine INTEGER NOT NULL,
    codprodotto INTEGER NOT NULL,
    codcliente INTEGER NOT NULL,
    prezzo NUMERIC NOT NULL DEFAULT 0.00 CHECK (prezzo >= 0.00),
    numeropunti NUMERIC NOT NULL DEFAULT 0.00 CHECK (numeropunti >= 0.00),
    numeroarticoli INT NOT NULL,
    CONSTRAINT PK_ArticoliOrdine PRIMARY KEY (codordine, codprodotto),
    CONSTRAINT articoliordineclientefk FOREIGN KEY (codcliente) REFERENCES cliente (codcliente),
    CONSTRAINT articoliordineprodottofk FOREIGN KEY (codprodotto) REFERENCES prodotto (codprodotto),
    CONSTRAINT articoliordineordinek FOREIGN KEY (codordine) REFERENCES ordine (codordine)
);