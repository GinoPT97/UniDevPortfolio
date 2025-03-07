-- Funzione che aggiorna la scorta in base al numero di articoli selezionati
CREATE OR REPLACE FUNCTION updateScorta()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE PRODOTTO SET Scorta = Scorta - NEW.NumeroArticoli
    WHERE CodProdotto = NEW.CodProdotto;
    RETURN NEW;
END;
$$;

-- Funzione che calcola automaticamente il prezzo dell'articolo in base alla quantità selezionata dall'utente
CREATE OR REPLACE FUNCTION selectPrezzo()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
DECLARE
    var REAL := 0.0;
BEGIN
    FOR counter IN 1..NEW.NumeroArticoli LOOP
        var := var + (SELECT P.Prezzo
                      FROM PRODOTTO AS P
                      WHERE P.CodProdotto = NEW.CodProdotto);
    END LOOP;
    UPDATE ARTICOLIORDINE SET Prezzo = var
    WHERE CodProdotto = NEW.CodProdotto;
    RETURN NEW;
END;
$$;

-- Funzione che (ri)calcola automaticamente il totale dell'ordine effettuato dal cliente ad ogni inserimento di un nuovo pezzo nella spesa
CREATE OR REPLACE FUNCTION updatePrezzo()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE ORDINE SET PrezzoTotale = PrezzoTotale + (SELECT AO.Prezzo
                                                     FROM ARTICOLIORDINE AS AO
                                                     WHERE AO.CodProdotto = NEW.CodProdotto);
    RETURN NEW;
END;
$$;

-- Funzione che (ri)calcola automaticamente il totale dell'ordine effettuato dal cliente ad ogni rimozione di un pezzo nella spesa
CREATE OR REPLACE FUNCTION updateDeletePrezzo()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE ORDINE SET PrezzoTotale = PrezzoTotale - OLD.Prezzo;
    RETURN NEW;
END;
$$;

-- Funzione che controlla la disponibilità del prodotto;
-- se la scorta è sufficiente a soddisfare la domanda, permette di lanciare il trigger updateScorta.
-- Qualora la scorta non fosse sufficiente, lancia un eccezione e notifica all'utente l'esaurimento delle scorte
CREATE OR REPLACE FUNCTION checkScorta()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    IF (NEW.NumeroArticoli <= (SELECT P.Scorta
                               FROM PRODOTTO AS P
                               WHERE P.CodProdotto = NEW.CodProdotto)) THEN
        RETURN NEW;
    ELSE
        RAISE EXCEPTION 'La scorta dell articolo selezionato è inferiore rispetto alla quantità richiesta; il prodotto potrebbe essere esaurito o disponibile in quantità ridotte!';
    END IF;
END;
$$;

-- Funzione che aggiorna il saldo punti dei clienti in base al prezzo totale dell'ordine
CREATE OR REPLACE FUNCTION updatePunti()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
DECLARE
    temp NUMERIC;
BEGIN
    SELECT P.Prezzo * NumeroArticoli INTO temp
    FROM PRODOTTO AS P
    WHERE P.CodProdotto = NEW.CodProdotto;
    UPDATE TESSERA
    SET NumeroPunti = NumeroPunti + (temp * 10) / 100
    WHERE CodCliente = (SELECT O.CodCliente
                        FROM ORDINE AS O
                        WHERE CodOrdine = NEW.CodOrdine);
    RETURN NEW;
END;
$$;

-- Funzione che ripristina la scorta di un articolo rimosso dall'ordine
CREATE OR REPLACE FUNCTION restoreScorta()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE PRODOTTO SET Scorta = Scorta + OLD.NumeroArticoli
    WHERE CodProdotto = OLD.CodProdotto;
    RETURN NEW;
END;
$$;

-- Funzione che elimina dalla tessera i punti di un articolo rimosso da un ordine
CREATE OR REPLACE FUNCTION updateDeletePunti()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
DECLARE
    temp NUMERIC;
BEGIN
    SELECT P.Prezzo * NumeroArticoli INTO temp
    FROM PRODOTTO AS P
    WHERE P.CodProdotto = OLD.CodProdotto;
    UPDATE TESSERA
    SET NumeroPunti = NumeroPunti - (temp * 10) / 100
    WHERE CodCliente = (SELECT O.CodCliente
                        FROM ORDINE AS O
                        WHERE CodOrdine = OLD.CodOrdine);
    RETURN NEW;
END;
$$;

-- Funzione che aggiorna i punti del cliente dopo l'inserimento di un articolo
CREATE OR REPLACE FUNCTION updatePuntiInserimento()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE TESSERA
    SET NumeroPunti = NumeroPunti + (NEW.prezzo * 0.10)
    WHERE CodCliente = (SELECT O.CodCliente
                        FROM ORDINE AS O
                        WHERE O.CodOrdine = NEW.CodOrdine);
    RETURN NEW;
END;
$$;