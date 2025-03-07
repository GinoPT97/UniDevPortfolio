-- Vista per la ricerca di dipendenti in base al numero di ordini --

CREATE OR REPLACE VIEW numordini AS
SELECT D.nome, D.cognome, COUNT(O.codordine) AS Tordini
FROM dipendente AS D
JOIN ordine AS O ON O.coddipendente = D.coddipendente
GROUP BY D.nome, D.cognome
ORDER BY Tordini DESC
LIMIT 1;

-- Vista per la ricerca di dipendenti in base agli introiti generati --

CREATE OR REPLACE VIEW introiti AS
SELECT D.nome, D.cognome, SUM(O.prezzototale) AS Sordine
FROM dipendente AS D
JOIN ordine AS O ON O.coddipendente = D.coddipendente
GROUP BY D.nome, D.cognome
ORDER BY Sordine DESC
LIMIT 1;

-- Vista per la ricerca di dipendenti con il maggior numero di vendite in un determinato periodo
CREATE OR REPLACE VIEW vendite_periodo AS
SELECT D.nome, D.cognome, COUNT(O.codordine) AS Tordini
FROM dipendente AS D
JOIN ordine AS O ON O.coddipendente = D.coddipendente
WHERE O.dataacquisto >= $1 AND O.dataacquisto <= $2
GROUP BY D.nome, D.cognome
ORDER BY Tordini DESC;

-- Vista per la ricerca di dipendenti con il maggior introito in un determinato periodo
CREATE OR REPLACE VIEW introiti_periodo AS
SELECT D.nome, D.cognome, SUM(O.prezzototale) AS Sordine
FROM dipendente AS D
JOIN ordine AS O ON O.coddipendente = D.coddipendente
WHERE O.dataacquisto >= $1 AND O.dataacquisto <= $2
GROUP BY D.nome, D.cognome
ORDER BY Sordine DESC;

-- Query per la ricerca di dipendenti in base al numero di ordini in un determinato periodo --

SELECT D.nome, D.cognome, COUNT(O.codordine) AS Tordini
FROM dipendente AS D
JOIN ordine AS O ON O.coddipendente = D.coddipendente
WHERE O.dataacquisto >= '2025-01-01' AND O.dataacquisto <= '2025-03-01'
GROUP BY D.nome, D.cognome
ORDER BY Tordini DESC
LIMIT 1;

-- Query per la ricerca di dipendenti in base agli introiti generati in un determinato periodo --

SELECT D.nome, D.cognome, SUM(O.prezzototale) AS Sordine
FROM dipendente AS D
JOIN ordine AS O ON O.coddipendente = D.coddipendente
WHERE O.dataacquisto >= '2025-01-01' AND O.dataacquisto <= '2025-03-01'
GROUP BY D.nome, D.cognome
ORDER BY Sordine DESC
LIMIT 1;

-- Query per la ricerca dei clienti

SELECT C.codcliente, C.nome, C.cognome, AO.categoria, SUM(AO.numeropunti) AS PuntiTotali
FROM cliente AS C
JOIN articoliordine AS AO ON C.codcliente = AO.codcliente
GROUP BY C.codcliente, C.nome, C.cognome, AO.categoria
ORDER BY PuntiTotali DESC;

-- Query per la ricerca dei clienti in base alla categoria di prodotti acquistati

SELECT C.nome, C.cognome, SUM(AO.numeroarticoli) AS NumeroArticoliAcquistati
FROM cliente AS C
JOIN articoliordine AS AO ON C.codcliente = AO.codcliente
JOIN prodotto AS P ON AO.codprodotto = P.codprodotto
WHERE P.categoria = '<categoriaInteressata>'
GROUP BY C.nome, C.cognome
ORDER BY NumeroArticoliAcquistati DESC;

-- Query per la ricerca dei clienti in base ai punti acquisiti per ogni categoria

SELECT C.nome, C.cognome, CAST(SUM(AO.prezzo) * 0.10 AS DECIMAL(10,2)) AS PuntiTot
FROM cliente AS C
JOIN articoliordine AS AO ON C.codcliente = AO.codcliente
JOIN prodotto AS P ON AO.codprodotto = P.codprodotto
GROUP BY C.nome, C.cognome
ORDER BY PuntiTot DESC;
