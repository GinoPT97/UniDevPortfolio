-- **Esercizio 02: SQL**
-- Interrogazione SQL per trovare nome e cognome delle persone che hanno avuto la residenza 
-- solo in città dove anche il loro padre ha avuto la residenza.

SELECT p.Nome, p.Cognome
FROM PERSONA p
JOIN RESIDENZA r1 ON p.CF = r1.CF
JOIN GENITORI g ON p.CF = g.CF
JOIN RESIDENZA r2 ON g.CFPadre = r2.CF
WHERE r1.Citta = r2.Citta;

-- **Esercizio 03: PL/SQL Procedure**
-- Procedura che riceve il CF di una persona e restituisce il nome e cognome di tutti gli antenati 
-- in linea maschile (padre, nonno paterno, ecc.)

CREATE OR REPLACE PROCEDURE get_antenati_maschili(p_CF IN VARCHAR2, p_result OUT VARCHAR2) IS
    v_CF VARCHAR2(16);
    v_nome VARCHAR2(255);
    v_cognome VARCHAR2(255);
BEGIN
    p_result := '';
    v_CF := p_CF;

    -- Ciclo attraverso i padri fino a quando non troviamo un padre nullo
    LOOP
        SELECT Nome, Cognome, CFPadre
        INTO v_nome, v_cognome, v_CF
        FROM GENITORI
        WHERE CF = v_CF;

        p_result := p_result || v_nome || ' ' || v_cognome || ' -> ';

        EXIT WHEN v_CF IS NULL;
    END LOOP;
END;

-- **Esercizio 04: Trigger**
-- Trigger attivato quando viene fissata la data di morte di una persona. Se la persona muore, 
-- la sua residenza viene chiusa, e se la persona è il capofamiglia, viene aggiornato il capofamiglia.

CREATE OR REPLACE TRIGGER tr_dato_morte
AFTER UPDATE OF DataMorte ON PERSONA
FOR EACH ROW
BEGIN
    -- Chiudi la residenza corrente
    UPDATE RESIDENZA
    SET DataF = SYSDATE
    WHERE CF = :NEW.CF AND DataF IS NULL;

    -- Se la persona è capofamiglia, aggiorna il capofamiglia
    DECLARE
        v_new_capofamiglia VARCHAR2(16);
    BEGIN
        SELECT CF
        INTO v_new_capofamiglia
        FROM FAMIGLIA f
        JOIN RESIDENTI r ON f.CodF = r.CodF
        WHERE f.CF_Capo = :NEW.CF
        ORDER BY r.DataN DESC
        FETCH FIRST 1 ROWS ONLY;

        UPDATE FAMIGLIA
        SET CF_Capo = v_new_capofamiglia
        WHERE CF_Capo = :NEW.CF;
    END;
END;

-- **Esercizio 12: Stradario e Anagrafe**

-- Creazione delle tabelle per lo stradario e l'anagrafe della città

CREATE TABLE Quartiere (
    CodQ INT PRIMARY KEY,
    Nome VARCHAR(255),
    Descrizione VARCHAR(255)
);

CREATE TABLE Isolato (
    CodI INT PRIMARY KEY,
    CodQ INT,
    Nome VARCHAR(255),
    FOREIGN KEY (CodQ) REFERENCES Quartiere(CodQ)
);

CREATE TABLE Via (
    CodV INT PRIMARY KEY,
    Nome VARCHAR(255)
);

CREATE TABLE Segmento (
    CodS INT PRIMARY KEY,
    CodV INT,
    CodI INT,
    Parita BOOLEAN,
    NMmin INT,
    NMmax INT,
    FOREIGN KEY (CodV) REFERENCES Via(CodV),
    FOREIGN KEY (CodI) REFERENCES Isolato(CodI)
);

CREATE TABLE Residenti (
    CF VARCHAR(16) PRIMARY KEY,
    Nome VARCHAR(255),
    Cognome VARCHAR(255),
    CodV INT,
    Numero INT,
    FOREIGN KEY (CodV) REFERENCES Via(CodV)
);

-- **Estensione della gestione delle residenze e delle famiglie:**
-- Aggiunta di un campo DataInizio e DataFine nella residenza, e gestione della composizione familiare

CREATE TABLE Residenza (
    CF VARCHAR(16),
    DataInizio DATE,
    DataFine DATE,
    Via VARCHAR(255),
    NumeroCivico INT,
    PRIMARY KEY (CF, DataInizio),
    FOREIGN KEY (CF) REFERENCES PERSONA(CF)
);

CREATE TABLE Famiglia (
    CodF INT PRIMARY KEY,
    CF_Capo VARCHAR(16),
    FOREIGN KEY (CF_Capo) REFERENCES PERSONA(CF)
);

CREATE TABLE ComponentiFamiglia (
    CodF INT,
    CF VARCHAR(16),
    PRIMARY KEY (CodF, CF),
    FOREIGN KEY (CodF) REFERENCES Famiglia(CodF),
    FOREIGN KEY (CF) REFERENCES PERSONA(CF)
);

-- Creazione del vincolo sui trigger
CREATE TRIGGER TriggerFamiglia
AFTER INSERT ON Famiglia
FOR EACH ROW
BEGIN
    -- Azioni relative all'aggiornamento dei capofamiglia
END;

-- Creazione di vincoli per le residenze
CREATE TABLE NucleoFamiliare (
    CodF INT,
    CF VARCHAR(16),
    PRIMARY KEY (CodF, CF),
    FOREIGN KEY (CF) REFERENCES PERSONA(CF),
    FOREIGN KEY (CodF) REFERENCES Famiglia(CodF)
);

