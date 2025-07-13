-- Viste e query conformi alla traccia accademica del negozio di alimentari

-- VIEW per calcolare i punti fedeltà per categoria (10% del valore della spesa)
-- Richiesta dalla traccia: "differenziare i clienti sulla base delle categorie di prodotti acquistati e sulla quantità di punti"
CREATE OR REPLACE VIEW puntiPerCategoria AS
SELECT 
    c.codcliente,
    c.nome,
    c.cognome,
    p.categoria,
    COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) as puntiCategoria,
    COALESCE(SUM(ao.prezzo * ao.numeroarticoli), 0) as spesaTotaleCategoria,
    COUNT(DISTINCT o.codordine) as ordiniCategoria
FROM cliente c
LEFT JOIN ordine o ON c.codcliente = o.codcliente
LEFT JOIN articoliordine ao ON o.codordine = ao.codordine
LEFT JOIN prodotto p ON ao.codprodotto = p.codprodotto
GROUP BY c.codcliente, c.nome, c.cognome, p.categoria
ORDER BY c.codcliente, p.categoria;

-- VIEW per calcolare i punti totali dei clienti (aggiornamento automatico della tessera)
CREATE OR REPLACE VIEW puntiTotaliClienti AS
SELECT 
    c.codcliente,
    c.nome,
    c.cognome,
    COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) as puntiTotaliCalcolati,
    t.numeropunti as puntiTessera,
    t.stato as statoTessera
FROM cliente c
LEFT JOIN tessera t ON c.codcliente = t.codcliente
LEFT JOIN ordine o ON c.codcliente = o.codcliente
LEFT JOIN articoliordine ao ON o.codordine = ao.codordine
GROUP BY c.codcliente, c.nome, c.cognome, t.numeropunti, t.stato;

-- VIEW per le statistiche dei dipendenti (richiesto dalla traccia per gruppi da 3)
CREATE OR REPLACE VIEW statisticheDipendenti AS
SELECT 
    d.coddipendente,
    d.nome,
    d.cognome,
    COUNT(DISTINCT o.codordine) as numeroVendite,
    COALESCE(SUM(o.prezzototale), 0) as introitoTotale,
    COALESCE(AVG(o.prezzototale), 0) as introitoMedio,
    MIN(o.dataacquisto) as primaVendita,
    MAX(o.dataacquisto) as ultimaVendita
FROM dipendente d
LEFT JOIN ordine o ON d.coddipendente = o.coddipendente
GROUP BY d.coddipendente, d.nome, d.cognome
ORDER BY numeroVendite DESC, introitoTotale DESC;

CREATE OR REPLACE VIEW prodottiCompleti AS
SELECT 
    p.*,
    CASE 
        WHEN p.scorta <= 5 THEN 'SCARSO'
        WHEN p.scorta <= 20 THEN 'MEDIO'
        ELSE 'BUONO'
    END as livelloScorta
FROM prodotto p
ORDER BY p.categoria, p.nome;

