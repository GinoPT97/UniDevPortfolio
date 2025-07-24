-- Viste e query conformi alla traccia accademica del negozio di alimentari

-- VIEW per calcolare i punti fedeltà per categoria 
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

-- VIEW per prodotti con livello scorta
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

-- Funzione parametrica per intervallo temporale
CREATE OR REPLACE FUNCTION DipendenteStatistichePeriodo(data_inizio DATE, data_fine DATE)
RETURNS TABLE(nome TEXT, cognome TEXT, NumeroOrdini INTEGER, Introiti NUMERIC)
AS $$
    SELECT nome, cognome, NumeroOrdini, NULL AS Introiti
    FROM (
        SELECT D.nome, D.cognome, COUNT(O.*) AS NumeroOrdini
        FROM dipendente AS D
        JOIN ordine AS O ON O.coddipendente = D.coddipendente
        WHERE O.dataacquisto BETWEEN data_inizio AND data_fine
        GROUP BY D.nome, D.cognome
        ORDER BY NumeroOrdini DESC
        LIMIT 1
    ) AS TopVendite
    UNION ALL
    SELECT nome, cognome, NULL AS NumeroOrdini, Introiti
    FROM (
        SELECT D.nome, D.cognome, SUM(O.prezzototale) AS Introiti
        FROM dipendente AS D
        JOIN ordine AS O ON O.coddipendente = D.coddipendente
        WHERE O.dataacquisto BETWEEN data_inizio AND data_fine
        GROUP BY D.nome, D.cognome
        ORDER BY Introiti DESC
        LIMIT 1
    ) AS TopIntroito;
$$ LANGUAGE sql;