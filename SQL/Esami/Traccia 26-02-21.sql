-- Creazione delle tabelle

-- Tabella FOLDER
CREATE TABLE FOLDER (
    CodFO INT PRIMARY KEY,               -- Codice univoco del folder
    Nome VARCHAR(255) NOT NULL,           -- Nome del folder
    Path VARCHAR(255) NOT NULL,           -- Percorso completo del folder
    Dimensione INT NOT NULL,              -- Dimensione totale del folder
    FolderContenente INT,                 -- Codice del folder che contiene questo folder
    FOREIGN KEY (FolderContenente) REFERENCES FOLDER(CodFO)
);

-- Tabella FILE
CREATE TABLE FILE (
    CodFI INT PRIMARY KEY,                -- Codice univoco del file
    Nome VARCHAR(255) NOT NULL,            -- Nome del file
    CodFO INT,                            -- Codice del folder che contiene il file
    Path VARCHAR(255) NOT NULL,            -- Percorso completo del file (aggiornato automaticamente)
    DataC TIMESTAMP NOT NULL,             -- Data di creazione del file
    DataM TIMESTAMP NOT NULL,             -- Data dell'ultima modifica del file
    Dimensione INT NOT NULL,              -- Dimensione del file
    FOREIGN KEY (CodFO) REFERENCES FOLDER(CodFO)
);

-- Tabella UTENTE
CREATE TABLE UTENTE (
    CodU INT PRIMARY KEY,                 -- Codice univoco dell'utente
    Nome VARCHAR(255) NOT NULL,            -- Nome dell'utente
    Cognome VARCHAR(255) NOT NULL,         -- Cognome dell'utente
    DataN DATE NOT NULL                   -- Data di nascita dell'utente
);

-- Tabella DIRITTI
CREATE TABLE DIRITTI (
    CodFO INT,                             -- Codice del folder
    CodU INT,                              -- Codice dell'utente
    Operazione CHAR(1) CHECK (Operazione IN ('R', 'W', 'D', 'I')), -- Operazione che l'utente può eseguire
    PRIMARY KEY (CodFO, CodU, Operazione), -- Chiave primaria composta
    FOREIGN KEY (CodFO) REFERENCES FOLDER(CodFO),
    FOREIGN KEY (CodU) REFERENCES UTENTE(CodU)
);

-- Tabella LOG
CREATE TABLE LOG (
    CodOp INT PRIMARY KEY,                -- Codice univoco dell'operazione
    CodU INT,                              -- Codice dell'utente che ha effettuato l'operazione
    Operazione CHAR(1) CHECK (Operazione IN ('R', 'W', 'D', 'I')), -- Tipo di operazione
    CodFI INT,                             -- Codice del file su cui è stata effettuata l'operazione
    Time TIMESTAMP NOT NULL,               -- Timestamp dell'operazione
    FOREIGN KEY (CodU) REFERENCES UTENTE(CodU),
    FOREIGN KEY (CodFI) REFERENCES FILE(CodFI)
);

-- Tabella CONTENUTO
CREATE TABLE CONTENUTO (
    CodFO INT,                             -- Codice del folder
    CodFOContenuto INT,                    -- Codice del folder contenuto
    Profondita INT,                        -- Profondità del folder nell'albero
    PRIMARY KEY (CodFO, CodFOContenuto),
    FOREIGN KEY (CodFO) REFERENCES FOLDER(CodFO),
    FOREIGN KEY (CodFOContenuto) REFERENCES FOLDER(CodFO)
);

-- Creazione dei vincoli

-- 1. File con nomi diversi nello stesso folder
ALTER TABLE FILE
ADD CONSTRAINT UniquenessFileNamePerFolder UNIQUE (CodFO, Nome);

-- Creazione dei trigger

-- Trigger per aggiornare la dimensione del folder
CREATE OR REPLACE FUNCTION update_folder_size() 
RETURNS TRIGGER AS $$
BEGIN
    UPDATE FOLDER
    SET Dimensione = (SELECT SUM(Dimensione) FROM FILE WHERE CodFO = NEW.CodFO) + 
                     (SELECT SUM(Dimensione) FROM FOLDER WHERE FolderContenente = NEW.CodFO)
    WHERE CodFO = NEW.CodFO;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_folder_size_trigger
AFTER INSERT OR UPDATE ON FILE
FOR EACH ROW
EXECUTE FUNCTION update_folder_size();

-- Trigger per aggiornare il campo Path del file
CREATE OR REPLACE FUNCTION update_file_path()
RETURNS TRIGGER AS $$
BEGIN
    NEW.Path := (SELECT Path FROM FOLDER WHERE CodFO = NEW.CodFO) || '/' || NEW.Nome;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_file_path_trigger
BEFORE INSERT ON FILE
FOR EACH ROW
EXECUTE FUNCTION update_file_path();

-- Creazione delle procedure

-- Procedura per inserire contenuti nella tabella CONTENUTO
CREATE OR REPLACE PROCEDURE insert_content(CodFO integer)
AS $$
DECLARE
    rec RECORD;
    depth integer := 0;
BEGIN
    FOR rec IN
        SELECT CodFO, CodFOContenuto FROM CONTENUTO WHERE CodFO = CodFO
    LOOP
        INSERT INTO CONTENUTO (CodFO, CodFOContenuto, Profondita)
        VALUES (rec.CodFO, rec.CodFOContenuto, depth);
        depth := depth + 1;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Procedura per ottenere i codici degli utenti che hanno modificato i file indicati
CREATE OR REPLACE PROCEDURE get_users_by_files(file_codes text)
AS $$
DECLARE
    file_list text[];
    file_code text;
    query text;
BEGIN
    -- Convert the file_codes string into an array of file codes
    file_list := string_to_array(file_codes, ';');

    -- Build the dynamic query
    query := 'SELECT DISTINCT CodU FROM LOG WHERE CodFI IN (' || 
             array_to_string(file_list, ',') || ')';

    -- Execute the dynamic query
    EXECUTE query;
END;
$$ LANGUAGE plpgsql;

