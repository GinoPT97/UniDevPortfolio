-- Funzioni conformi alla traccia accademica del negozio di alimentari

-- FUNZIONE per ricercare clienti per categoria di prodotti acquistati
-- Richiesta dalla traccia: "permettere la ricerca dei clienti, differenziandoli sulla base delle categorie di prodotti acquistati"
CREATE OR REPLACE FUNCTION ricerca_clienti_per_categoria(categoria_ricerca TIPOLOGIA)
RETURNS TABLE(
    codcliente INTEGER,
    nome VARCHAR(255),
    cognome VARCHAR(255),
    punti_categoria NUMERIC,
    spesa_categoria NUMERIC,
    ordini_categoria BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        c.codcliente,
        c.nome,
        c.cognome,
        COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) as punti_categoria,
        COALESCE(SUM(ao.prezzo * ao.numeroarticoli), 0) as spesa_categoria,
        COUNT(DISTINCT o.codordine)::BIGINT as ordini_categoria
    FROM cliente c
    JOIN ordine o ON c.codcliente = o.codcliente
    JOIN articoliordine ao ON o.codordine = ao.codordine
    JOIN prodotto p ON ao.codprodotto = p.codprodotto
    WHERE p.categoria = categoria_ricerca
    GROUP BY c.codcliente, c.nome, c.cognome
    HAVING COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) > 0
    ORDER BY punti_categoria DESC;
END;
$$ LANGUAGE plpgsql;

-- FUNZIONE per trovare il dipendente con maggior numero di vendite in un periodo
-- Richiesta dalla traccia: "ricercare quale dipendente ha effettuato il maggior numero di vendite in un determinato periodo"
CREATE OR REPLACE FUNCTION dipendente_maggiori_vendite(data_inizio DATE, data_fine DATE)
RETURNS TABLE(
    coddipendente INTEGER,
    nome VARCHAR(255),
    cognome VARCHAR(255),
    numero_vendite BIGINT,
    introito_totale NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        d.coddipendente,
        d.nome,
        d.cognome,
        COUNT(o.codordine)::BIGINT as numero_vendite,
        COALESCE(SUM(o.prezzototale), 0) as introito_totale
    FROM dipendente d
    JOIN ordine o ON d.coddipendente = o.coddipendente
    WHERE o.dataacquisto BETWEEN data_inizio AND data_fine
    GROUP BY d.coddipendente, d.nome, d.cognome
    ORDER BY numero_vendite DESC, introito_totale DESC
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;

-- FUNZIONE per trovare il dipendente con maggior introito in un periodo
-- Richiesta dalla traccia: "differenziandolo dal dipendente che ha portato al fruttivendolo il maggior introito"
CREATE OR REPLACE FUNCTION dipendente_maggior_introito(data_inizio DATE, data_fine DATE)
RETURNS TABLE(
    coddipendente INTEGER,
    nome VARCHAR(255),
    cognome VARCHAR(255),
    numero_vendite BIGINT,
    introito_totale NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        d.coddipendente,
        d.nome,
        d.cognome,
        COUNT(o.codordine)::BIGINT as numero_vendite,
        COALESCE(SUM(o.prezzototale), 0) as introito_totale
    FROM dipendente d
    JOIN ordine o ON d.coddipendente = o.coddipendente
    WHERE o.dataacquisto BETWEEN data_inizio AND data_fine
    GROUP BY d.coddipendente, d.nome, d.cognome
    ORDER BY introito_totale DESC, numero_vendite DESC
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;

-- FUNZIONE per aggiornare automaticamente i punti nella tessera (10% del valore della spesa)
-- Richiesta dalla traccia: "Per ogni acquisto il cliente riceve il 10% del valore della spesa in punti fedeltà"
CREATE OR REPLACE FUNCTION aggiorna_punti_tessera()
RETURNS TRIGGER AS $$
DECLARE
    punti_da_aggiungere NUMERIC;
    cliente_id INTEGER;
BEGIN
    -- Calcola i punti da aggiungere (10% del valore)
    punti_da_aggiungere := NEW.prezzo * NEW.numeroarticoli * 0.10;
    
    -- Ottieni il codice cliente dall'ordine
    SELECT codcliente INTO cliente_id 
    FROM ordine 
    WHERE codordine = NEW.codordine;
    
    -- Aggiorna i punti nella tessera
    UPDATE tessera 
    SET numeropunti = numeropunti + punti_da_aggiungere 
    WHERE codcliente = cliente_id;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- FUNZIONE per rimuovere punti quando un articolo viene eliminato dall'ordine
CREATE OR REPLACE FUNCTION rimuovi_punti_tessera()
RETURNS TRIGGER AS $$
DECLARE
    punti_da_rimuovere NUMERIC;
    cliente_id INTEGER;
BEGIN
    -- Calcola i punti da rimuovere (10% del valore)
    punti_da_rimuovere := OLD.prezzo * OLD.numeroarticoli * 0.10;
    
    -- Ottieni il codice cliente dall'ordine
    SELECT codcliente INTO cliente_id 
    FROM ordine 
    WHERE codordine = OLD.codordine;
    
    -- Rimuovi i punti dalla tessera
    UPDATE tessera 
    SET numeropunti = GREATEST(numeropunti - punti_da_rimuovere, 0)
    WHERE codcliente = cliente_id;
    
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- FUNZIONE per aggiornare il prezzo totale dell'ordine
CREATE OR REPLACE FUNCTION aggiorna_prezzo_totale_ordine()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE ordine 
    SET prezzototale = (
        SELECT COALESCE(SUM(prezzo * numeroarticoli), 0)
        FROM articoliordine 
        WHERE codordine = COALESCE(NEW.codordine, OLD.codordine)
    )
    WHERE codordine = COALESCE(NEW.codordine, OLD.codordine);
    
    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- FUNZIONE per controllare la disponibilità del prodotto prima dell'inserimento
CREATE OR REPLACE FUNCTION controlla_disponibilita_prodotto()
RETURNS TRIGGER AS $$
BEGIN
    -- Controlla se c'è abbastanza scorta
    IF (SELECT scorta FROM prodotto WHERE codprodotto = NEW.codprodotto) < NEW.numeroarticoli THEN
        RAISE EXCEPTION 'Scorta insufficiente per il prodotto %. Disponibili: %, Richiesti: %', 
            NEW.codprodotto, 
            (SELECT scorta FROM prodotto WHERE codprodotto = NEW.codprodotto), 
            NEW.numeroarticoli;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- FUNZIONE per aggiornare la scorta del prodotto
CREATE OR REPLACE FUNCTION aggiorna_scorta_prodotto()
RETURNS TRIGGER AS $$
BEGIN
    -- Se è un inserimento, sottrai la quantità
    IF TG_OP = 'INSERT' THEN
        UPDATE prodotto 
        SET scorta = scorta - NEW.numeroarticoli 
        WHERE codprodotto = NEW.codprodotto;
        RETURN NEW;
    END IF;
    
    -- Se è una modifica, aggiusta la differenza
    IF TG_OP = 'UPDATE' THEN
        UPDATE prodotto 
        SET scorta = scorta + OLD.numeroarticoli - NEW.numeroarticoli 
        WHERE codprodotto = NEW.codprodotto;
        RETURN NEW;
    END IF;
    
    -- Se è una cancellazione, ripristina la quantità
    IF TG_OP = 'DELETE' THEN
        UPDATE prodotto 
        SET scorta = scorta + OLD.numeroarticoli 
        WHERE codprodotto = OLD.codprodotto;
        RETURN OLD;
    END IF;
    
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;
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