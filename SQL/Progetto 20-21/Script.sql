-- Creare il nuovo tipo ENUM con le categorie corrette e migliorato
CREATE TYPE CATEGORIA_PRODOTTO AS ENUM (
    'FRUTTA',
    'VERDURA', 
    'FARINACEI',
    'LATTICINI',
    'UOVA',
    'CONFEZIONATI'
);

-- Creare tipo ENUM per lo stato della tessera
CREATE TYPE STATO_TESSERA AS ENUM (
    'ATTIVA',
    'SOSPESA',
    'SCADUTA'
);

-- Tabella cliente (modificata per riferimento alla tessera)
CREATE TABLE IF NOT EXISTS cliente (
    codcliente SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL CHECK (nome ~* '^[A-Za-z ]+$'),
    cognome VARCHAR(255) NOT NULL CHECK (cognome ~* '^[A-Za-z ]+$'),
    codicefiscale CHAR(16) NOT NULL UNIQUE CHECK (codicefiscale ~* '^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$'),
    indirizzo VARCHAR(255) NOT NULL,
    telefono VARCHAR(20) CHECK (telefono ~* '^[0-9+ ]+$'),
    email VARCHAR(255) NOT NULL UNIQUE CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]+$')
);

-- Tabella tessera (separata come richiesto)
CREATE TABLE IF NOT EXISTS tessera (
    codtessera SERIAL PRIMARY KEY,
    codcliente INTEGER NOT NULL UNIQUE,
    numeropunti NUMERIC NOT NULL DEFAULT 0.00 CHECK (numeropunti >= 0.00),
    dataemissione DATE NOT NULL DEFAULT CURRENT_DATE,
    datascadenza DATE NOT NULL DEFAULT (CURRENT_DATE + INTERVAL '2 years'),
    stato STATO_TESSERA NOT NULL DEFAULT 'ATTIVA',
    CONSTRAINT tessera_cliente_fk FOREIGN KEY (codcliente) REFERENCES cliente (codcliente)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- Tabella dipendente
CREATE TABLE IF NOT EXISTS dipendente (
    coddipendente SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL CHECK (nome ~* '^[A-Za-z ]+$'),
    cognome VARCHAR(255) NOT NULL CHECK (cognome ~* '^[A-Za-z ]+$'),
    codicefiscale CHAR(16) NOT NULL UNIQUE CHECK (codicefiscale ~* '^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$'),
    indirizzo VARCHAR(255) NOT NULL,
    telefono VARCHAR(20) CHECK (telefono ~* '^[0-9+ ]+$'),
    email VARCHAR(255) NOT NULL UNIQUE CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]+$')
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
    categoria CATEGORIA_PRODOTTO,
    scorta INT CHECK (scorta >= 0)
);

-- Vincolo CHECK per validare i campi specifici di ogni categoria secondo la traccia accademica
ALTER TABLE prodotto ADD CONSTRAINT checkCategoria CHECK (
  -- FRUTTA: deve avere data di raccolta
  (categoria = 'FRUTTA' AND dataraccolta IS NOT NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NULL AND glutine IS NULL) OR
  
  -- VERDURA: deve avere data di raccolta  
  (categoria = 'VERDURA' AND dataraccolta IS NOT NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NULL AND glutine IS NULL) OR
  
  -- LATTICINI: devono avere data di mungitura del latte e data di produzione
  (categoria = 'LATTICINI' AND dataraccolta IS NULL AND datamungitura IS NOT NULL AND dataproduzione IS NOT NULL AND datascadenza IS NOT NULL AND glutine IS NULL) OR
  
  -- FARINACEI: informazioni sul glutine
  (categoria = 'FARINACEI' AND dataraccolta IS NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NULL AND glutine IS NOT NULL) OR
  
  -- UOVA: solo data di scadenza
  (categoria = 'UOVA' AND dataraccolta IS NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NOT NULL AND glutine IS NULL) OR
  
  -- CONFEZIONATI: solo data di scadenza
  (categoria = 'CONFEZIONATI' AND dataraccolta IS NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NOT NULL AND glutine IS NULL)
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
    prezzo NUMERIC NOT NULL DEFAULT 0.00 CHECK (prezzo >= 0.00),
    numeroarticoli INT NOT NULL CHECK (numeroarticoli > 0),
    CONSTRAINT PK_ArticoliOrdine PRIMARY KEY (codordine, codprodotto),
    CONSTRAINT articoliordineprodottofk FOREIGN KEY (codprodotto) REFERENCES prodotto (codprodotto)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT articoliordineordinek FOREIGN KEY (codordine) REFERENCES ordine (codordine)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);