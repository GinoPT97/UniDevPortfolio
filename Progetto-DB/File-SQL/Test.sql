-- =========================   
-- TEST VINCOLI CHECK E FK =
-- =========================

-- Deve fallire: FRUTTA non può avere datascadenza
INSERT INTO prodotto (nome, prezzo, scorta, categoria, datascadenza)
VALUES ('Mela Verde', 0.5, 100, 'FRUTTA', '2025-12-31');

-- Deve fallire: scorta insufficiente (trigger TrgVerificaScortaProdotto)
INSERT INTO articoliordine (codordine, codprodotto, numeroarticoli, prezzo)
VALUES (1, 1, 9999, 10.00);

-- ==========================================================
-- TEST TRIGGER: AggiornaScortaEPunti, AggiornaPrezzoOrdine =
-- ==========================================================

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

-- ====================================================================
-- TEST TRIGGER: RipristinaScortaProdotto, AggiornaPrezzoOrdineDelete =
-- ====================================================================

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

-- ====================================
-- TEST TRIGGER: AggiornaStatoTessera =
-- ====================================

-- Mostra stato tessera prima
SELECT codtessera, datascadenza, stato FROM tessera WHERE codtessera = 1;

-- Forza una tessera scaduta (datascadenza < oggi)
UPDATE tessera
SET datascadenza = CURRENT_DATE - INTERVAL '1 day'
WHERE codtessera = 1;

-- Verifica aggiornamento automatico stato tessera
SELECT codtessera, stato FROM tessera WHERE codtessera = 1;

-- ================================
-- TEST COMPLEMENTARI DI COERENZA =
-- ================================

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


-- ==========================
-- TEST FUNZIONI DI RICERCA =
-- ==========================

-- VIEW 1: Riepilogo punti e stato tessera dei clienti
CREATE OR REPLACE VIEW VwClientiTessere AS
SELECT c.codcliente, c.nome, c.cognome, t.numeropunti, t.stato, t.datascadenza
FROM cliente c
JOIN tessera t ON c.codcliente = t.codcliente;

-- VIEW 2: Prodotti con scorta bassa (< 20)
CREATE OR REPLACE VIEW VwProdottiScortaBassa AS
SELECT codprodotto, nome, categoria, scorta
FROM prodotto
WHERE scorta < 20;

-- VIEW 3: Ordini dettagliati
CREATE OR REPLACE VIEW VwOrdiniDettagliati AS
SELECT o.codordine, o.dataacquisto, o.prezzototale, c.nome AS nome_cliente, c.cognome AS cognome_cliente,
       d.nome AS nome_dipendente, d.cognome AS cognome_dipendente,
       p.nome AS nome_prodotto, p.categoria, ao.numeroarticoli, ao.prezzo
FROM ordine o
JOIN cliente c ON o.codcliente = c.codcliente
JOIN dipendente d ON o.coddipendente = d.coddipendente
JOIN articoliordine ao ON o.codordine = ao.codordine
JOIN prodotto p ON ao.codprodotto = p.codprodotto;

-- Esempi: dipendente con più ordini e con più introiti in diversi intervalli temporali
SELECT * FROM DipendenteStatistiche((CURRENT_DATE - INTERVAL '3 months')::date, CURRENT_DATE);
SELECT * FROM DipendenteStatistiche((CURRENT_DATE - INTERVAL '6 months')::date, CURRENT_DATE);
SELECT * FROM DipendenteStatistiche((CURRENT_DATE - INTERVAL '9 months')::date, CURRENT_DATE);
SELECT * FROM DipendenteStatistiche((CURRENT_DATE - INTERVAL '12 months')::date, CURRENT_DATE);
SELECT * FROM DipendenteStatistiche((SELECT MIN(dataacquisto) FROM ordine), (SELECT MAX(dataacquisto) FROM ordine));

-- Esempio: clienti e punti per TUTTE le categorie acquistate
SELECT *
FROM RicercaClientiPerCategoria()
ORDER BY codcliente, categoria;

SELECT *
FROM RicercaClientiPerCategoria('FRUTTA')
WHERE punti_categoria BETWEEN 0 AND 100;

-- Esempi di query sulle VIEW
-- Riepilogo punti e stato tessera di tutti i clienti
SELECT * FROM VwClientiTessere ORDER BY codcliente;

-- Prodotti con scorta bassa
SELECT * FROM VwProdottiScortaBassa ORDER BY scorta ASC;

-- Ordini dettagliati (tutti)
SELECT * FROM VwOrdiniDettagliati ORDER BY codordine, nome_prodotto;

-- Ordini dettagliati per un cliente specifico (es. cliente 1)
SELECT * FROM VwOrdiniDettagliati WHERE nome_cliente = 'Aldo' AND cognome_cliente = 'Marzante';

-- Ordini dettagliati per un dipendente specifico (es. dipendente 'Dario Forte')
SELECT * FROM VwOrdiniDettagliati WHERE nome_dipendente = 'Dario' AND cognome_dipendente = 'Forte';