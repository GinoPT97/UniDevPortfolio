-- Viste e query conformi alla traccia accademica

-- Vista per la ricerca di dipendenti in base al numero di ordini
CREATE OR REPLACE VIEW dipendenti_per_numero_ordini AS
SELECT 
    d.coddipendente,
    d.nome, 
    d.cognome, 
    COUNT(o.codordine) AS numero_ordini
FROM dipendente d
LEFT JOIN ordine o ON d.coddipendente = o.coddipendente
GROUP BY d.coddipendente, d.nome, d.cognome
ORDER BY numero_ordini DESC;

-- Vista per la ricerca di dipendenti in base agli introiti generati
CREATE OR REPLACE VIEW dipendenti_per_introiti AS
SELECT 
    d.coddipendente,
    d.nome, 
    d.cognome, 
    COALESCE(SUM(o.prezzototale), 0) AS introiti_totali
FROM dipendente d
LEFT JOIN ordine o ON d.coddipendente = o.coddipendente
GROUP BY d.coddipendente, d.nome, d.cognome
ORDER BY introiti_totali DESC;

-- Vista per la ricerca di clienti con i loro punti fedeltà per categoria
CREATE OR REPLACE VIEW clienti_punti_per_categoria AS
SELECT 
    c.codcliente,
    c.nome,
    c.cognome,
    p.categoria,
    COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) as punti_categoria
FROM cliente c
LEFT JOIN ordine o ON c.codcliente = o.codcliente
LEFT JOIN articoliordine ao ON o.codordine = ao.codordine
LEFT JOIN prodotto p ON ao.codprodotto = p.codprodotto
GROUP BY c.codcliente, c.nome, c.cognome, p.categoria
ORDER BY c.cognome, c.nome, p.categoria;

-- Vista per statistiche vendite per dipendente
CREATE OR REPLACE VIEW statistiche_vendite_dipendente AS
SELECT 
    d.coddipendente,
    d.nome,
    d.cognome,
    COUNT(DISTINCT o.codordine) as numero_vendite,
    COALESCE(SUM(o.prezzototale), 0) as totale_incasso,
    COALESCE(AVG(o.prezzototale), 0) as media_vendita
FROM dipendente d
LEFT JOIN ordine o ON d.coddipendente = o.coddipendente
GROUP BY d.coddipendente, d.nome, d.cognome
ORDER BY numero_vendite DESC, totale_incasso DESC;

-- Vista per prodotti con informazioni complete
CREATE OR REPLACE VIEW prodotti_completi AS
SELECT 
    p.*,
    CASE 
        WHEN p.scorta <= 5 THEN 'SCARSO'
        WHEN p.scorta <= 20 THEN 'MEDIO'
        ELSE 'BUONO'
    END as livello_scorta
FROM prodotto p
ORDER BY p.categoria, p.nome;

-- Query per trovare il dipendente con il maggior numero di vendite in un periodo specifico
-- Esempio per il mese di dicembre 2023
SELECT 
    d.nome, 
    d.cognome, 
    COUNT(o.codordine) AS numero_ordini
FROM dipendente d
JOIN ordine o ON d.coddipendente = o.coddipendente
WHERE o.dataacquisto >= '2023-12-01' AND o.dataacquisto <= '2023-12-31'
GROUP BY d.coddipendente, d.nome, d.cognome
ORDER BY numero_ordini DESC
LIMIT 1;

-- Query per trovare il dipendente con il maggior introito in un periodo specifico
-- Esempio per il mese di dicembre 2023
SELECT 
    d.nome, 
    d.cognome, 
    SUM(o.prezzototale) AS introiti_totali
FROM dipendente d
JOIN ordine o ON d.coddipendente = o.coddipendente
WHERE o.dataacquisto >= '2023-12-01' AND o.dataacquisto <= '2023-12-31'
GROUP BY d.coddipendente, d.nome, d.cognome
ORDER BY introiti_totali DESC
LIMIT 1;

-- Query per la ricerca dei clienti in base alla categoria di prodotti acquistati
-- Esempio per la categoria FRUTTA
SELECT 
    c.codcliente,
    c.nome, 
    c.cognome, 
    p.categoria,
    SUM(ao.numeroarticoli) AS articoli_acquistati,
    SUM(ao.prezzo * ao.numeroarticoli) AS spesa_totale
FROM cliente c
JOIN ordine o ON c.codcliente = o.codcliente
JOIN articoliordine ao ON o.codordine = ao.codordine
JOIN prodotto p ON ao.codprodotto = p.codprodotto
WHERE p.categoria = 'FRUTTA'
GROUP BY c.codcliente, c.nome, c.cognome, p.categoria
ORDER BY spesa_totale DESC;

-- Query per la ricerca dei clienti in base ai punti acquisiti per ogni categoria
SELECT 
    c.codcliente,
    c.nome, 
    c.cognome, 
    p.categoria,
    SUM(ao.prezzo * ao.numeroarticoli * 0.10) AS punti_acquisiti
FROM cliente c
JOIN ordine o ON c.codcliente = o.codcliente
JOIN articoliordine ao ON o.codordine = ao.codordine
JOIN prodotto p ON ao.codprodotto = p.codprodotto
GROUP BY c.codcliente, c.nome, c.cognome, p.categoria
ORDER BY c.cognome, c.nome, punti_acquisiti DESC;

-- Query per visualizzare i punti totali di ogni cliente
SELECT 
    c.codcliente,
    c.nome,
    c.cognome,
    t.numeropunti AS punti_attuali,
    t.dataemissione,
    t.datascadenza,
    t.stato
FROM cliente c
JOIN tessera t ON c.codcliente = t.codcliente
ORDER BY t.numeropunti DESC;
