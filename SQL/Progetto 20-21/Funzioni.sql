-- Funzioni conformi alla struttura del database secondo la traccia accademica

-- Funzione per aggiornare la scorta quando viene inserito un articolo nell'ordine
CREATE OR REPLACE FUNCTION aggiorna_scorta_prodotto()
RETURNS TRIGGER AS $$
BEGIN
    -- Diminuisce la scorta quando viene aggiunto un articolo all'ordine
    IF TG_OP = 'INSERT' THEN
        UPDATE prodotto
        SET scorta = scorta - NEW.numeroarticoli
        WHERE codprodotto = NEW.codprodotto;
        
        -- Controlla se la scorta diventa negativa
        IF (SELECT scorta FROM prodotto WHERE codprodotto = NEW.codprodotto) < 0 THEN
            RAISE EXCEPTION 'Scorta insufficiente per il prodotto con codice %', NEW.codprodotto;
        END IF;
        
        RETURN NEW;
    END IF;
    
    -- Ripristina la scorta quando viene rimosso un articolo dall'ordine
    IF TG_OP = 'DELETE' THEN
        UPDATE prodotto
        SET scorta = scorta + OLD.numeroarticoli
        WHERE codprodotto = OLD.codprodotto;
        
        RETURN OLD;
    END IF;
    
    -- Gestisce l'aggiornamento della quantità
    IF TG_OP = 'UPDATE' THEN
        UPDATE prodotto
        SET scorta = scorta + OLD.numeroarticoli - NEW.numeroarticoli
        WHERE codprodotto = NEW.codprodotto;
        
        -- Controlla se la scorta diventa negativa
        IF (SELECT scorta FROM prodotto WHERE codprodotto = NEW.codprodotto) < 0 THEN
            RAISE EXCEPTION 'Scorta insufficiente per il prodotto con codice %', NEW.codprodotto;
        END IF;
        
        RETURN NEW;
    END IF;
    
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Funzione per calcolare il totale di un ordine
CREATE OR REPLACE FUNCTION calcola_totale_ordine(p_codordine INTEGER)
RETURNS NUMERIC AS $$
DECLARE
    totale NUMERIC := 0;
BEGIN
    SELECT COALESCE(SUM(prezzo * numeroarticoli), 0)
    INTO totale
    FROM articoliordine
    WHERE codordine = p_codordine;
    
    RETURN totale;
END;
$$ LANGUAGE plpgsql;

-- Funzione per aggiornare automaticamente il prezzo totale dell'ordine
CREATE OR REPLACE FUNCTION aggiorna_prezzo_totale_ordine()
RETURNS TRIGGER AS $$
BEGIN
    -- Aggiorna il prezzo totale nell'ordine
    UPDATE ordine 
    SET prezzototale = calcola_totale_ordine(
        CASE 
            WHEN TG_OP = 'DELETE' THEN OLD.codordine
            ELSE NEW.codordine
        END
    )
    WHERE codordine = 
        CASE 
            WHEN TG_OP = 'DELETE' THEN OLD.codordine
            ELSE NEW.codordine
        END;
    
    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Funzione per aggiornare i punti fedeltà del cliente (10% del valore della spesa)
CREATE OR REPLACE FUNCTION aggiorna_punti_fedelta()
RETURNS TRIGGER AS $$
DECLARE
    punti_da_aggiungere NUMERIC;
    cod_cliente INTEGER;
BEGIN
    -- Ottiene il codice cliente dall'ordine
    SELECT codcliente INTO cod_cliente
    FROM ordine
    WHERE codordine = NEW.codordine;
    
    -- Calcola i punti (10% del valore della spesa per questo articolo)
    punti_da_aggiungere := (NEW.prezzo * NEW.numeroarticoli) * 0.10;
    
    -- Aggiorna i punti nella tessera del cliente
    UPDATE tessera
    SET numeropunti = numeropunti + punti_da_aggiungere
    WHERE codcliente = cod_cliente;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Funzione per rimuovere punti fedeltà quando un articolo viene rimosso dall'ordine
CREATE OR REPLACE FUNCTION rimuovi_punti_fedelta()
RETURNS TRIGGER AS $$
DECLARE
    punti_da_rimuovere NUMERIC;
    cod_cliente INTEGER;
BEGIN
    -- Ottiene il codice cliente dall'ordine
    SELECT codcliente INTO cod_cliente
    FROM ordine
    WHERE codordine = OLD.codordine;
    
    -- Calcola i punti da rimuovere (10% del valore della spesa per questo articolo)
    punti_da_rimuovere := (OLD.prezzo * OLD.numeroarticoli) * 0.10;
    
    -- Rimuove i punti dalla tessera del cliente
    UPDATE tessera
    SET numeropunti = GREATEST(0, numeropunti - punti_da_rimuovere)
    WHERE codcliente = cod_cliente;
    
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Funzione per controllare la disponibilità del prodotto prima dell'inserimento
CREATE OR REPLACE FUNCTION controlla_disponibilita_prodotto()
RETURNS TRIGGER AS $$
BEGIN
    -- Controlla se la scorta è sufficiente
    IF NEW.numeroarticoli > (SELECT scorta FROM prodotto WHERE codprodotto = NEW.codprodotto) THEN
        RAISE EXCEPTION 'Scorta insufficiente per il prodotto %. Disponibili: %, Richiesti: %', 
            NEW.codprodotto, 
            (SELECT scorta FROM prodotto WHERE codprodotto = NEW.codprodotto),
            NEW.numeroarticoli;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;