-- Funzioni conformi alla traccia accademica del negozio di alimentari

-- FUNZIONE per ricercare clienti per categoria di prodotti acquistati
-- Richiesta dalla traccia: "permettere la ricerca dei clienti, differenziandoli sulla base delle categorie di prodotti acquistati"
CREATE OR REPLACE FUNCTION RicercaClientiPerCategoria(categoriaRicerca TIPOLOGIA)
RETURNS TABLE(
    codcliente INTEGER,
    nome VARCHAR(255),
    cognome VARCHAR(255),
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
        COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) as punti_categoria,
        COALESCE(SUM(ao.prezzo * ao.numeroarticoli), 0) as spesa_categoria,
        COUNT(DISTINCT o.codordine)::BIGINT as ordini_categoria
    FROM cliente c
    JOIN ordine o ON c.codcliente = o.codcliente
    JOIN articoliordine ao ON o.codordine = ao.codordine
    JOIN prodotto p ON ao.codprodotto = p.codprodotto
    WHERE p.categoria = categoriaRicerca
    GROUP BY c.codcliente, c.nome, c.cognome
    HAVING COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) > 0
    ORDER BY punti_categoria DESC;
END;
$$ LANGUAGE plpgsql;
