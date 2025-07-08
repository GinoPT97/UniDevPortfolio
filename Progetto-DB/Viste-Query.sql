-- Viste e query conformi alla traccia accademica del negozio di alimentari

-- VIEW per calcolare i punti fedeltà per categoria (10% del valore della spesa)
-- Richiesta dalla traccia: "differenziare i clienti sulla base delle categorie di prodotti acquistati e sulla quantità di punti"
CREATE OR REPLACE VIEW punti_per_categoria AS
SELECT 
    c.codcliente,
    c.nome,
    c.cognome,
    p.categoria,
    COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) as punti_categoria,
    COALESCE(SUM(ao.prezzo * ao.numeroarticoli), 0) as spesa_totale_categoria,
    COUNT(DISTINCT o.codordine) as ordini_nella_categoria
FROM cliente c
LEFT JOIN ordine o ON c.codcliente = o.codcliente
LEFT JOIN articoliordine ao ON o.codordine = ao.codordine
LEFT JOIN prodotto p ON ao.codprodotto = p.codprodotto
GROUP BY c.codcliente, c.nome, c.cognome, p.categoria
ORDER BY c.codcliente, p.categoria;

-- VIEW per calcolare i punti totali dei clienti (aggiornamento automatico della tessera)
CREATE OR REPLACE VIEW punti_totali_clienti AS
SELECT 
    c.codcliente,
    c.nome,
    c.cognome,
    COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) as punti_totali_calcolati,
    t.numeropunti as punti_tessera,
    t.stato as stato_tessera
FROM cliente c
LEFT JOIN tessera t ON c.codcliente = t.codcliente
LEFT JOIN ordine o ON c.codcliente = o.codcliente
LEFT JOIN articoliordine ao ON o.codordine = ao.codordine
GROUP BY c.codcliente, c.nome, c.cognome, t.numeropunti, t.stato;

-- VIEW per le statistiche dei dipendenti (richiesto dalla traccia per gruppi da 3)
CREATE OR REPLACE VIEW statistiche_dipendenti AS
SELECT 
    d.coddipendente,
    d.nome,
    d.cognome,
    COUNT(DISTINCT o.codordine) as numero_vendite,
    COALESCE(SUM(o.prezzototale), 0) as introito_totale,
    COALESCE(AVG(o.prezzototale), 0) as introito_medio,
    MIN(o.dataacquisto) as prima_vendita,
    MAX(o.dataacquisto) as ultima_vendita
FROM dipendente d
LEFT JOIN ordine o ON d.coddipendente = o.coddipendente
GROUP BY d.coddipendente, d.nome, d.cognome
ORDER BY numero_vendite DESC, introito_totale DESC;

-- Vista per prodotti con informazioni complete sulla scorta
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

-- QUERY ESEMPI per rispettare i requisiti della traccia:

-- 1. Ricerca clienti per categoria FRUTTA (esempio d'uso)
SELECT 
    c.codcliente,
    c.nome, 
    c.cognome, 
    p.categoria,
    SUM(ao.numeroarticoli) AS articoli_acquistati,
    SUM(ao.prezzo * ao.numeroarticoli) AS spesa_totale,
    SUM(ao.prezzo * ao.numeroarticoli * 0.10) AS punti_guadagnati
FROM cliente c
JOIN ordine o ON c.codcliente = o.codcliente
JOIN articoliordine ao ON o.codordine = ao.codordine
JOIN prodotto p ON ao.codprodotto = p.codprodotto
WHERE p.categoria = 'FRUTTA'
GROUP BY c.codcliente, c.nome, c.cognome, p.categoria
ORDER BY spesa_totale DESC;

-- 2. Dipendente con maggior numero di vendite in periodo (esempio dicembre 2024)
SELECT 
    d.nome, 
    d.cognome, 
    COUNT(o.codordine) AS numero_vendite,
    SUM(o.prezzototale) AS introito_totale
FROM dipendente d
JOIN ordine o ON d.coddipendente = o.coddipendente
WHERE o.dataacquisto >= '2024-12-01' AND o.dataacquisto <= '2024-12-31'
GROUP BY d.coddipendente, d.nome, d.cognome
ORDER BY numero_vendite DESC, introito_totale DESC
LIMIT 1;

-- 3. Dipendente con maggior introito in periodo (esempio dicembre 2024)
SELECT 
    d.nome, 
    d.cognome, 
    COUNT(o.codordine) AS numero_vendite,
    SUM(o.prezzototale) AS introito_totale
FROM dipendente d
JOIN ordine o ON d.coddipendente = o.coddipendente
WHERE o.dataacquisto >= '2024-12-01' AND o.dataacquisto <= '2024-12-31'
GROUP BY d.coddipendente, d.nome, d.cognome
ORDER BY introito_totale DESC, numero_vendite DESC
LIMIT 1;

-- 4. Clienti con i loro punti totali per categoria
SELECT 
    c.codcliente,
    c.nome, 
    c.cognome, 
    p.categoria,
    SUM(ao.prezzo * ao.numeroarticoli * 0.10) AS punti_per_categoria
FROM cliente c
JOIN ordine o ON c.codcliente = o.codcliente
JOIN articoliordine ao ON o.codordine = ao.codordine
JOIN prodotto p ON ao.codprodotto = p.codprodotto
GROUP BY c.codcliente, c.nome, c.cognome, p.categoria
ORDER BY c.cognome, c.nome, punti_per_categoria DESC;

-- 5. Punti totali di ogni cliente (da tessera)
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
