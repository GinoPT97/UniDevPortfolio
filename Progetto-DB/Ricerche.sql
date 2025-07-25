-- Viste e query conformi alla traccia accademica del negozio di alimentari

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
        COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) as punti_categoria,
        COALESCE(SUM(ao.prezzo * ao.numeroarticoli), 0) as spesa_categoria,
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

-- ESEMPI DI QUERY SU COME USARE LE VIEW E FUNZIONI DI RICERCA DEL PROGETTO --


-- Esempio: clienti e punti per TUTTE le categorie acquistate
SELECT *
FROM RicercaClientiPerCategoria()
ORDER BY codcliente, categoria;

-- Esempio: clienti e punti SOLO per la categoria 'FRUTTA'
SELECT *
FROM RicercaClientiPerCategoria('FRUTTA')
WHERE punti_categoria BETWEEN 0 AND 100;

SELECT *
FROM ProdottiCompleti
WHERE LivelloScorta = 'SCARSO';

SELECT *
FROM PuntiTotaliClienti
WHERE StatoTessera = 'ATTIVA';