-- Viste e query conformi alla traccia accademica del negozio di alimentari

-- VIEW per calcolare i punti fedeltà per categoria (10% del valore della spesa)
-- Richiesta dalla traccia: "differenziare i clienti sulla base delle categorie di prodotti acquistati e sulla quantità di punti"
CREATE OR REPLACE VIEW PuntiPerCategoria AS
SELECT 
    c.codcliente AS CodCliente,
    c.nome AS Nome,
    c.cognome AS Cognome,
    p.categoria AS Categoria,
    COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) AS PuntiCategoria,
    COALESCE(SUM(ao.prezzo * ao.numeroarticoli), 0) AS SpesaTotaleCategoria,
    COUNT(DISTINCT o.codordine) AS OrdiniCategoria
FROM cliente c
LEFT JOIN ordine o ON c.codcliente = o.codcliente
LEFT JOIN articoliordine ao ON o.codordine = ao.codordine
LEFT JOIN prodotto p ON ao.codprodotto = p.codprodotto
GROUP BY c.codcliente, c.nome, c.cognome, p.categoria
ORDER BY c.codcliente, p.categoria;

-- VIEW per calcolare i punti totali dei clienti (aggiornamento automatico della tessera)
CREATE OR REPLACE VIEW PuntiTotaliClienti AS
SELECT 
    c.codcliente AS CodCliente,
    c.nome AS Nome,
    c.cognome AS Cognome,
    COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) AS PuntiTotaliCalcolati,
    t.numeropunti AS PuntiTessera,
    t.stato AS StatoTessera
FROM cliente c
LEFT JOIN tessera t ON c.codcliente = t.codcliente
LEFT JOIN ordine o ON c.codcliente = o.codcliente
LEFT JOIN articoliordine ao ON o.codordine = ao.codordine
GROUP BY c.codcliente, c.nome, c.cognome, t.numeropunti, t.stato;

-- VIEW per le statistiche dei dipendenti 
CREATE OR REPLACE VIEW StatisticheDipendenti AS
SELECT 
    d.coddipendente AS CodDipendente,
    d.nome AS Nome,
    d.cognome AS Cognome,
    COUNT(DISTINCT o.codordine) AS NumeroVendite,
    COALESCE(SUM(o.prezzototale), 0) AS IntroitoTotale,
    COALESCE(AVG(o.prezzototale), 0) AS IntroitoMedio,
    MIN(o.dataacquisto) AS PrimaVendita,
    MAX(o.dataacquisto) AS UltimaVendita
FROM dipendente d
LEFT JOIN ordine o ON d.coddipendente = o.coddipendente
GROUP BY d.coddipendente, d.nome, d.cognome
ORDER BY NumeroVendite DESC, IntroitoTotale DESC;

CREATE OR REPLACE VIEW ProdottiCompleti AS
SELECT 
    p.*,
    CASE 
        WHEN p.scorta <= 5 THEN 'SCARSO'
        WHEN p.scorta <= 20 THEN 'MEDIO'
        ELSE 'BUONO'
    END AS LivelloScorta
FROM prodotto p
ORDER BY p.categoria, p.nome;

