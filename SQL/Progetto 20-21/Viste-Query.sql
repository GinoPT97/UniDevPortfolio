-- Vista per la ricerca di dipendenti in base al numero di ordini --

CREATE VIEW numordini AS
SELECT DISTINCT D.nome, D.cognome, COUNT(O) AS Tordini
FROM dipendente AS D, ordine AS O
WHERE O.coddipendente = D.coddipendente
GROUP BY D.nome,D.cognome
ORDER BY Tordini DESC
LIMIT 1

-- Vista per la ricerca di dipendenti in base agli introiti generati --

CREATE VIEW introiti AS
SELECT DISTINCT D.nome, D.cognome, SUM(O.prezzototale) AS Sordine
FROM dipendente AS D, ordine AS O
WHERE O.coddipendente = D.coddipendente
GROUP BY D.nome,D.cognome
ORDER BY Sordine DESC
LIMIT 1

-- Query per la ricerca di dipendenti in base al numero di ordini in un determinato periodo --

SELECT DISTINCT D.nome, D.cognome, COUNT(O) AS Tordini
FROM dipendente AS D, ordine AS O
WHERE O.coddipendente = D.coddipendente AND O.dataacquisto >= 'valore_di' AND O.dataacquisto <= 'valore_df'
GROUP BY D.nome, D.cognome
ORDER BY Tordini DESC
LIMIT 1;

-- Vista per la ricerca di dipendenti in base agli introiti generati in un determinato periodo--

SELECT DISTINCT D.nome, D.cognome, SUM(O.prezzototale) AS Sordine
FROM dipendente AS D, ordine AS O 
WHERE O.coddipendente = D.coddipendente AND O.dataacquisto >= 'valore_di' AND O.dataacquisto <= 'valore_df'
GROUP BY D.nome, D.cognome
ORDER BY Sordine DESC
LIMIT 1;

-- Query per la ricerca dei clienti

SELECT C.codcliente, C.nome, C.cognome, AO.categoria, SUM(AO.numeropunti) AS PuntiTotali
FROM cliente AS C JOIN articoliordine AS AO ON C.codcliente = AO.codcliente
GROUP BY C.codcliente, C.nome, C.cognome, AO.categoria
ORDER BY PuntiTotali DESC;

-- Query per la ricerca dei clienti in base alla categoria di prodotti acquistati

SELECT C.Nome, C.Cognome, SUM(AO.NumeroArticoli) AS NumeroArticoliAcquistati
FROM (CLIENTE AS C NATURAL JOIN ARTICOLIORDINE AS AO) INNER JOIN PRODOTTO AS P ON AO.CodProdotto = P.CodProdotto
WHERE P.Categoria = '<categoriaInteressata>'
GROUP BY C.Nome, C.Cognome
ORDER BY NumeroArticoliAcquistati DESC;

-- Query per la ricerca dei clienti in base ai punti acquisiti per ogni categoria

SELECT C.Nome, C.Cognome, SUBSTRING(CAST(((SUM(AO.Prezzo) * 10) / 100) AS VARCHAR), 1, 4) AS PuntiTot
FROM (CLIENTE AS C NATURAL JOIN ARTICOLIORDINE AS AO) INNER JOIN PRODOTTO AS P ON AO.CodProdotto = P.CodProdotto
GROUP BY C.Nome, C.Cognome
ORDER BY PuntiTot DESC;
