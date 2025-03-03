-- (Opzionale) Definizione del tipo TIPOLOGIA se non esiste già:
CREATE TYPE TIPOLOGIA AS ENUM ('Ortofrutticoli', 'Latticini', 'Inscatolati', 'Farinacei');

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
    categoria TIPOLOGIA,
    scorta INT CHECK (scorta >= 0),
    CONSTRAINT categoria_check CHECK (
        (categoria = 'Ortofrutticoli' AND dataraccolta IS NOT NULL AND datamungitura IS NULL AND datascadenza IS NULL AND glutine IS NULL) OR
        (categoria = 'Latticini' AND dataraccolta IS NULL AND datamungitura IS NOT NULL AND datascadenza IS NULL AND glutine IS NULL) OR
        (categoria = 'Inscatolati' AND dataraccolta IS NULL AND datamungitura IS NULL AND datascadenza IS NOT NULL AND glutine IS NULL) OR
        (categoria = 'Farinacei' AND dataraccolta IS NULL AND datamungitura IS NULL AND datascadenza IS NULL AND glutine IS NOT NULL)
    )
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
    codcliente SERIAL PRIMARY KEY,  
    prezzo NUMERIC NOT NULL DEFAULT 0.00 CHECK (prezzo >= 0.00),
    numeropunti NUMERIC NOT NULL DEFAULT 0.00 CHECK (numeropunti >= 0.00),
    numeroarticoli INT NOT NULL,
    categoria TIPOLOGIA,
    CONSTRAINT articoliordineclientefk FOREIGN KEY (codcliente) REFERENCES cliente (codcliente),
    CONSTRAINT articoliordineprodottofk FOREIGN KEY (codprodotto) REFERENCES prodotto (codprodotto),
    CONSTRAINT articoliordineordinek FOREIGN KEY (codordine) REFERENCES ordine (codordine)
);

