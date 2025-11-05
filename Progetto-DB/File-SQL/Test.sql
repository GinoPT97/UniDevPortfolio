-- ============================================================
-- TEST VINCOLI CHECK E FK
-- ============================================================

-- Deve fallire: FRUTTA non può avere datascadenza
INSERT INTO prodotto (nome, prezzo, scorta, categoria, datascadenza)
VALUES ('Mela Verde', 0.5, 100, 'FRUTTA', '2025-12-31');

-- Deve fallire: scorta insufficiente (trigger TrgVerificaScortaProdotto)
INSERT INTO articoliordine (codordine, codprodotto, numeroarticoli, prezzo)
VALUES (1, 1, 9999, 10.00);

-- ============================================================
-- TEST TRIGGER: AggiornaScortaEPunti, AggiornaPrezzoOrdine
-- ============================================================

-- Mostra stato iniziale
SELECT codprodotto, nome, scorta FROM prodotto WHERE codprodotto = 1;
SELECT prezzototale FROM ordine WHERE codordine = 1;
SELECT * FROM tessera WHERE codcliente = 1;

-- Inserisci un nuovo articolo ordine (scorte diminuiranno, punti aumenteranno, prezzo totale aggiornato)
INSERT INTO articoliordine (codordine, codprodotto, numeroarticoli, prezzo)
VALUES (1, 1, 2, 5.00);

-- Verifica decremento scorte (AggiornaScortaEPunti)
SELECT codprodotto, scorta FROM prodotto WHERE codprodotto = 1;

-- Verifica incremento punti tessera (AggiornaScortaEPunti)
SELECT codcliente, numeropunti FROM tessera WHERE codcliente = 1;

-- Verifica aggiornamento prezzo totale (AggiornaPrezzoOrdine)
SELECT prezzototale FROM ordine WHERE codordine = 1;

-- ============================================================
-- TEST TRIGGER: RipristinaScortaProdotto, AggiornaPrezzoOrdineDelete
-- ============================================================

-- STATO INIZIALE

-- Scorta prodotto prima dell'eliminazione
SELECT codprodotto, nome, scorta FROM prodotto WHERE codprodotto = 1;

-- Prezzo totale ordine prima dell'eliminazione
SELECT codordine, prezzototale FROM ordine WHERE codordine = 1;
WHERE codordine = 1;

-- Punti tessera prima dell'eliminazione
SELECT codcliente, numeropunti FROM tesseraWHERE codcliente = 1;

-- OPERAZIONE CHE ATTIVA I TRIGGER (DELETE)
DELETE FROM articoliordine
WHERE codordine = 1 AND codprodotto = 1;

-- STATO FINALE (verifica effetto dei trigger)

-- Scorta prodotto dopo l'eliminazione (RipristinaScortaProdotto)
SELECT codprodotto, nome, scorta FROM prodotto WHERE codprodotto = 1;

-- Prezzo totale ordine dopo l'eliminazione (AggiornaPrezzoOrdineDelete)
SELECT codordine, prezzototale FROM ordine WHERE codordine = 1;

-- Punti tessera dopo l'eliminazione (RipristinaScortaProdotto)
SELECT codcliente, numeropunti FROM tessera WHERE codcliente = 1;

-- ============================================================
-- TEST TRIGGER: AggiornaStatoTessera
-- ============================================================

-- Mostra stato tessera prima
SELECT codtessera, datascadenza, stato FROM tessera WHERE codtessera = 1;

-- Forza una tessera scaduta (datascadenza < oggi)
UPDATE tessera
SET datascadenza = CURRENT_DATE - INTERVAL '1 day'
WHERE codtessera = 1;

-- Verifica aggiornamento automatico stato tessera
SELECT codtessera, stato FROM tessera WHERE codtessera = 1;

-- ============================================================
-- TEST COMPLEMENTARI DI COERENZA
-- ============================================================

-- Prezzo ordine deve essere coerente con articoliordine
SELECT o.codordine, o.prezzototale AS totale_registrato,
       COALESCE(SUM(a.numeroarticoli * a.prezzo), 0) AS totale_calcolato
FROM ordine o
LEFT JOIN articoliordine a ON o.codordine = a.codordine
GROUP BY o.codordine;

-- Scorte non devono mai diventare negative
SELECT * FROM prodotto WHERE scorta < 0;

-- Tessere attive non devono essere scadute
SELECT * FROM tessera WHERE stato = 'ATTIVA' AND datascadenza < CURRENT_DATE;

-- Ordini senza articoli (eventuale anomalia)
SELECT o.codordine FROM ordine o
LEFT JOIN articoliordine a ON o.codordine = a.codordine
WHERE a.codordine IS NULL;
