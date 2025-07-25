-- Query conformi alla traccia accademica del negozio di alimentari

CREATE OR REPLACE FUNCTION RicercaClientiPerCategoria(categoriaRicerca TIPOLOGIA DEFAULT NULL)
RETURNS TABLE(
    codcliente INTEGER,
    nome VARCHAR(255),
    cognome VARCHAR(255),
    categoria TIPOLOGIA,
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
        p.categoria,
        ROUND(COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0), 2) as punti_categoria,
        ROUND(COALESCE(SUM(ao.prezzo * ao.numeroarticoli), 0), 2) as spesa_categoria,
        COUNT(DISTINCT o.codordine)::BIGINT as ordini_categoria
    FROM cliente c
    JOIN ordine o ON c.codcliente = o.codcliente
    JOIN articoliordine ao ON o.codordine = ao.codordine
    JOIN prodotto p ON ao.codprodotto = p.codprodotto
    WHERE (categoriaRicerca IS NULL OR p.categoria = categoriaRicerca)
    GROUP BY c.codcliente, c.nome, c.cognome, p.categoria
    HAVING COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) > 0
    ORDER BY c.codcliente, punti_categoria DESC;
END;
$$ LANGUAGE plpgsql;

-- Funzione parametrica per intervallo temporale
CREATE OR REPLACE FUNCTION DipendenteStatistiche(data_inizio DATE, data_fine DATE)
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

SELECT *
FROM ProdottiCompleti
WHERE LivelloScorta = 'SCARSO';

SELECT *
FROM PuntiTotaliClienti
WHERE StatoTessera = 'ATTIVA';

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