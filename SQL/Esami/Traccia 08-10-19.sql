-- Creazione delle tabelle

-- Tabella FILM
CREATE TABLE FILM (
    CodFilm INT PRIMARY KEY,               -- Codice univoco del film
    Titolo VARCHAR(255) NOT NULL,           -- Titolo del film
    Regista VARCHAR(255) NOT NULL,          -- Nome del regista
    Anno INT NOT NULL,                      -- Anno di uscita del film
    Durata INT NOT NULL                     -- Durata del film in minuti
);

-- Tabella SALA
CREATE TABLE SALA (
    CodSala INT PRIMARY KEY,               -- Codice univoco della sala
    Nome VARCHAR(255) NOT NULL,             -- Nome della sala
    NumPosti INT NOT NULL                  -- Numero di posti nella sala
);

-- Tabella PROIEZF
CREATE TABLE PROIEZF (
    CodProiez INT PRIMARY KEY,              -- Codice della proiezione
    CodSala INT NOT NULL,                   -- Codice della sala
    CodFilm INT NOT NULL,                   -- Codice del film
    Data DATE NOT NULL,                     -- Data della proiezione
    Ora TIME NOT NULL,                      -- Ora della proiezione
    MaxSpot INT NOT NULL,                   -- Tempo massimo per gli spot
    MaxTrailer INT NOT NULL,                -- Tempo massimo per i trailer
    FOREIGN KEY (CodSala) REFERENCES SALA(CodSala),
    FOREIGN KEY (CodFilm) REFERENCES FILM(CodFilm)
);

-- Tabella SPOT
CREATE TABLE SPOT (
    CodS INT PRIMARY KEY,                   -- Codice dello spot
    Titolo VARCHAR(255) NOT NULL,           -- Titolo dello spot
    Prodotto VARCHAR(255) NOT NULL,         -- Nome del prodotto
    Durata INT NOT NULL                     -- Durata dello spot
);

-- Tabella PROIEZST
CREATE TABLE PROIEZST (
    CodST INT PRIMARY KEY,                  -- Codice dello spot o trailer
    CodProiez INT NOT NULL,                  -- Codice della proiezione
    Ordine INT NOT NULL,                    -- Ordine dell'inserimento nella proiezione
    Durata INT NOT NULL,                    -- Durata dello spot/trailer
    FOREIGN KEY (CodProiez) REFERENCES PROIEZF(CodProiez)
);

-- Tabella TRAILER
CREATE TABLE TRAILER (
    CodT INT PRIMARY KEY,                   -- Codice del trailer
    CodFilm INT NOT NULL,                   -- Codice del film
    Durata INT NOT NULL                     -- Durata del trailer
);

-- Esercizio 01: Algebra Relazionale per trovare i film senza trailer
-- In algebra relazionale, l'espressione per trovare i film senza trailer:
-- Pi(Titolo)(FILM) - Pi(Titolo)(FILM ⨝ CodFilm = CodFilm(PROIEZF ⨝ CodProiez = CodProiez(PROIEZST ⨝ CodST = CodT TRAILER)))

-- Esercizio 02: Vista SQL per film con dettagli sulle proiezioni
CREATE VIEW FilmProiezioni AS
SELECT 
    f.Titolo,
    COUNT(DISTINCT p.CodProiez) AS NumProiezioni,
    COUNT(DISTINCT ps.CodST) AS NumTrailer,
    COUNT(DISTINCT ps.CodST) AS NumSpot,
    SUM(ps.Durata) AS DurataTotaleSpotTrailer
FROM FILM f
JOIN PROIEZF p ON f.CodFilm = p.CodFilm
LEFT JOIN PROIEZST ps ON p.CodProiez = ps.CodProiez
GROUP BY f.Titolo;

-- Esercizio 03: Vincoli

-- 1. Il tempo complessivo degli spot non deve eccedere MaxSpot
CREATE FUNCTION check_max_spot() RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT SUM(Durata) FROM PROIEZST WHERE CodProiez = NEW.CodProiez) + NEW.Durata > 
       (SELECT MaxSpot FROM PROIEZF WHERE CodProiez = NEW.CodProiez) THEN
        RAISE EXCEPTION 'Il tempo totale degli spot supera MaxSpot';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER max_spot_trigger
BEFORE INSERT OR UPDATE ON PROIEZST
FOR EACH ROW
EXECUTE FUNCTION check_max_spot();

-- 2. Lo stesso spot non può essere proiettato più di una volta in una proiezione
CREATE FUNCTION check_duplicate_spot() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM PROIEZST WHERE CodProiez = NEW.CodProiez AND CodST = NEW.CodST) THEN
        RAISE EXCEPTION 'Lo spot è già stato proiettato in questa proiezione';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER duplicate_spot_trigger
BEFORE INSERT ON PROIEZST
FOR EACH ROW
EXECUTE FUNCTION check_duplicate_spot();

-- 3. La durata dello spot deve essere uguale alla durata dello spot nel record di PROIEZST
CREATE FUNCTION check_spot_duration() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.Durata != (SELECT Durata FROM SPOT WHERE CodS = NEW.CodST) THEN
        RAISE EXCEPTION 'La durata dello spot non corrisponde alla durata specificata';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER spot_duration_trigger
BEFORE INSERT ON PROIEZST
FOR EACH ROW
EXECUTE FUNCTION check_spot_duration();

-- Esercizio 04: Procedura per associare uno spot a proiezioni future
CREATE OR REPLACE PROCEDURE associare_spot_alle_proiezioni(CodS INT, k INT)
AS $$
DECLARE
    proiezioni RECORD;
    count INT := 0;
BEGIN
    FOR proiezioni IN
        SELECT CodProiez, MaxSpot FROM PROIEZF WHERE Data > CURRENT_DATE
    LOOP
        IF count < k THEN
            -- Controlla se lo spot può essere aggiunto alla proiezione
            IF (SELECT COALESCE(SUM(Durata), 0) FROM PROIEZST WHERE CodProiez = proiezioni.CodProiez) + 
               (SELECT Durata FROM SPOT WHERE CodS = CodS) <= proiezioni.MaxSpot THEN
                -- Associa lo spot alla proiezione
                INSERT INTO PROIEZST (CodST, CodProiez, Ordine, Durata)
                VALUES (CodS, proiezioni.CodProiez, count + 1, (SELECT Durata FROM SPOT WHERE CodS = CodS));
                count := count + 1;
            END IF;
        END IF;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Esercizio 11: Ristrutturazione del Class Diagram per la traduzione relazionale (Teorico)
-- Non è possibile generare direttamente il codice SQL per questo esercizio senza il diagramma specifico.

-- Esercizio 12: Schema del frammento del database per lo stradario e l'anagrafe della città

-- Creazione delle tabelle per l'anagrafe

-- Tabella QUARTIERE
CREATE TABLE QUARTIERE (
    CodQ INT PRIMARY KEY,                   -- Codice del quartiere
    Nome VARCHAR(255) NOT NULL,             -- Nome del quartiere
    Descrizione TEXT NOT NULL               -- Descrizione del quartiere
);

-- Tabella ISOLATO
CREATE TABLE ISOLATO (
    CodI INT PRIMARY KEY,                   -- Codice dell'isolato
    CodQ INT NOT NULL,                      -- Codice del quartiere
    Nome VARCHAR(255) NOT NULL,             -- Nome dell'isolato
    FOREIGN KEY (CodQ) REFERENCES QUARTIERE(CodQ)
);

-- Tabella VIA
CREATE TABLE VIA (
    CodV INT PRIMARY KEY,                   -- Codice della via
    Nome VARCHAR(255) NOT NULL              -- Nome della via
);

-- Tabella SEGMENTO
CREATE TABLE SEGMENTO (
    CodS INT PRIMARY KEY,                   -- Codice del segmento
    CodV INT NOT NULL,                      -- Codice della via
    CodI INT NOT NULL,                      -- Codice dell'isolato
    Pari BOOLEAN NOT NULL,                  -- Se il segmento ha numeri civici pari o dispari
    NMin INT NOT NULL,                      -- Numero civico minimo
    NMax INT NOT NULL,                      -- Numero civico massimo
    FOREIGN KEY (CodV) REFERENCES VIA(CodV),
    FOREIGN KEY (CodI) REFERENCES ISOLATO(CodI)
);

-- Tabella RESIDENTI
CREATE TABLE RESIDENTI (
    CF VARCHAR(16) PRIMARY KEY,             -- Codice fiscale del residente
    Nome VARCHAR(255) NOT NULL,             -- Nome del residente
    Cognome VARCHAR(255) NOT NULL,          -- Cognome del residente
    CodV INT NOT NULL,                      -- Codice della via
    Numero INT NOT NULL,                    -- Numero civico
    FOREIGN KEY (CodV) REFERENCES VIA(CodV)
);

