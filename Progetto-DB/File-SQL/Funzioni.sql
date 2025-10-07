-- Funzione per aggiornare scorta e punti
CREATE OR REPLACE FUNCTION AggiornaScortaEPunti()
RETURNS TRIGGER AS $$
DECLARE
    punti_da_aggiungere NUMERIC;
    cod_cliente INTEGER;
BEGIN
    -- Aggiorna la scorta del prodotto
    UPDATE prodotto
    SET scorta = scorta - NEW.numeroarticoli
    WHERE codprodotto = NEW.codprodotto;

    -- Calcola i punti da aggiungere (10% del valore speso)
    punti_da_aggiungere := ROUND(NEW.prezzo * NEW.numeroarticoli * 0.10, 2);

    -- Recupera il cliente associato all'ordine
    SELECT codcliente INTO cod_cliente
    FROM ordine
    WHERE codordine = NEW.codordine;

    -- Aggiorna i punti della tessera del cliente
    UPDATE tessera
    SET numeropunti = numeropunti + punti_da_aggiungere
    WHERE codcliente = cod_cliente;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Funzione trigger per aggiornare il prezzo totale dell'ordine dopo inserimento articolo
CREATE OR REPLACE FUNCTION AggiornaPrezzoOrdine()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE ordine
    SET prezzototale = prezzototale + (NEW.prezzo * NEW.numeroarticoli)
    WHERE codordine = NEW.codordine;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Funzione trigger per verificare la scorta del prodotto prima dell'inserimento dell'articolo
CREATE OR REPLACE FUNCTION VerificaScortaProdotto()
RETURNS TRIGGER AS $$
DECLARE
    scorta_corrente INT;
BEGIN
    SELECT scorta INTO scorta_corrente FROM prodotto WHERE codprodotto = NEW.codprodotto;
    IF scorta_corrente < NEW.numeroarticoli THEN
        RAISE EXCEPTION 'Scorta insufficiente per il prodotto %: richiesta % - disponibile %', NEW.codprodotto, NEW.numeroarticoli, scorta_corrente;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Funzione trigger per aggiornare lo stato della tessera in base alla data di scadenza
CREATE OR REPLACE FUNCTION AggiornaStatoTessera()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.datascadenza < CURRENT_DATE THEN
        NEW.stato := 'SCADUTA';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Funzione trigger per ripristinare la scorta prodotto dopo DELETE da articoliordine
CREATE OR REPLACE FUNCTION RipristinaScortaProdotto()
RETURNS TRIGGER AS $$
DECLARE
    cod_cliente INTEGER;
    punti_da_togliere NUMERIC;
BEGIN
    -- Ripristina la scorta del prodotto
    UPDATE prodotto
    SET scorta = scorta + OLD.numeroarticoli
    WHERE codprodotto = OLD.codprodotto;

    -- Recupera il cliente associato all'ordine
    SELECT codcliente INTO cod_cliente
    FROM ordine
    WHERE codordine = OLD.codordine;

    -- Calcola i punti da togliere (10% del valore speso)
    punti_da_togliere := ROUND(OLD.prezzo * OLD.numeroarticoli * 0.10, 2);

    -- Aggiorna i punti della tessera del cliente
    -- Non toglie più punti di quelli disponibili
    UPDATE tessera
    SET numeropunti = GREATEST(numeropunti - punti_da_togliere, 0)
    WHERE codcliente = cod_cliente;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Funzione trigger per aggiornare il prezzo totale dell'ordine dopo DELETE da articoliordine
CREATE OR REPLACE FUNCTION AggiornaPrezzoOrdineDelete()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE ordine
    SET prezzototale = GREATEST(prezzototale - (OLD.prezzo * OLD.numeroarticoli), 0)
    WHERE codordine = OLD.codordine;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;