-- Definizione della tabella cliente

CREATE TABLE cliente(
    codcliente VARCHAR(5) PRIMARY KEY,CHECK(CodCliente ~* '^[0-9]+$'),
    nome VARCHAR(255) NOT NULL, CHECK(Nome ~* '^[A-Za-z ]+$'),
    cognome VARCHAR(255) NOT NULL,CHECK(Cognome ~* '^[A-Za-z ]+$'),
    codicefiscale CHAR(16) NOT NULL, CHECK(CodiceFiscale ~* '^[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]$'),
    indirizzo VARCHAR(255) NOT NULL,
    telefono VARCHAR(20) NOT NULL , CHECK(Telefono ~* '^[0-9+ ]+$'),
    email VARCHAR(255) NOT NULL, CHECK(Email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-za-z]+$')
);

-- Definizione della tabella dipendente

CREATE TABLE dipendente(
    coddipendente VARCHAR(5) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL, CHECK(Nome ~* '^[A-Za-z ]+$'),
    cognome VARCHAR(255) NOT NULL,CHECK(Cognome ~* '^[A-Za-z ]+$'),
    codicefiscale CHAR(16) NOT NULL, CHECK(CodiceFiscale ~* '^[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]$'),
    indirizzo VARCHAR(255) NOT NULL,
    telefono VARCHAR(20) NOT NULL , CHECK(Telefono ~* '^[0-9+ ]+$'),
    email VARCHAR(255) NOT NULL, CHECK(Email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-za-z]+$')
);

-- Definizione della tabella tessera

CREATE TABLE tessera(
        CodTessera VARCHAR(5) PRIMARY KEY, CHECK(CodTessera ~* '^[0-9]+$'),
	NumeroPunti NUMERIC NOT NULL DEFAULT 0,
	CodCliente VARCHAR(5) NOT NULL UNIQUE CHECK(CodCliente ~* '^[0-9]+$'),
	CONSTRAINT TesseraFK FOREIGN KEY(CodCliente) REFERENCES CLIENTE(CodCliente)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- Definizione della tabella prodotto

CREATE TABLE prodotto(
    codprodotto VARCHAR(5) PRIMARY KEY, CHECK(CodProdotto ~* '^[0-9]+$'),
    nome VARCHAR(255) NOT NULL, CHECK(Nome ~* '^[A-Za-z ]+$'),
    descrizione VARCHAR(500),
    prezzo NUMERIC NOT NULL DEFAULT 0.0,
    luogoprovenienza VARCHAR(255),
    dataraccolta DATE,
    datamungitura DATE,
    glutine BOOLEAN,
    datascadenza DATE,
    categoria VARCHAR(255),
    scorta INT CHECK (scorta >= 0),
    CONSTRAINT categoria CHECK (
        (categoria = 'Ortofrutticoli' AND dataraccolta IS NOT NULL AND datamungitura IS NULL AND datascadenza IS NULL AND glutine IS NULL) OR
        (categoria = 'Latticini' AND dataraccolta IS NULL AND datamungitura IS NOT NULL AND datascadenza IS NULL AND glutine IS NULL) OR
        (categoria = 'Inscatolati' AND dataraccolta IS NULL AND datamungitura IS NULL AND datascadenza IS NOT NULL AND glutine IS NULL) OR
        (categoria = 'Farinacei' AND dataraccolta IS NULL AND datamungitura IS NULL AND datascadenza IS NULL AND glutine IS NOT NULL)
    )
);

-- Definizione della tabella ordine

CREATE TABLE ordine(
    CodOrdine VARCHAR(5) PRIMARY KEY, CHECK(CodOrdine ~* '^[0-9]+$'),
    PrezzoTotale NUMERIC NOT NULL DEFAULT 0.0, CHECK(PrezzoTotale >= 0.0),
    DataAcquisto DATE NOT NULL,
    CodCliente VARCHAR(5) NOT NULL,
    CodDipendente VARCHAR(5) NOT NULL,
    CONSTRAINT OrdineClienteFK FOREIGN KEY(CodCliente) REFERENCES CLIENTE(CodCliente),
    CONSTRAINT OrdineDipendenteFK FOREIGN KEY(CodDipendente) REFERENCES DIPENDENTE(CodDipendente)
);

-- Definizione della tabella articoliordine

CREATE TABLE articoliordine(
    CodOrdine VARCHAR(5) NOT NULL,
    CodProdotto VARCHAR(5) NOT NULL,
    CodCliente VARCHAR(5) NOT NULL,
    prezzo NUMERIC NOT NULL DEFAULT 0.00,
    numeropunti NUMERIC NOT NULL DEFAULT 0.00,
    numeroarticoli INT NOT NULL,
    categoria VARCHAR(255),
    CONSTRAINT articoliordinecliente FOREIGN KEY (codcliente) REFERENCES cliente (codcliente),
    CONSTRAINT articoliordineprodotto FOREIGN KEY (codprodotto) REFERENCES prodotto (codprodotto),
    CONSTRAINT articoliordineordine FOREIGN KEY (codordine) REFERENCES ordine (codordine)
);

