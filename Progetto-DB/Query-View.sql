-- ESEMPI DI QUERY SU COME USARE LE VIEW DEL PROGETTO --

-- 1. Filtrare i punti per categoria per un cliente specifico
SELECT *
FROM PuntiPerCategoria
WHERE CodCliente = 1;

-- 2. Filtrare i punti per categoria in un intervallo di date
SELECT c.*, o.dataacquisto
FROM PuntiPerCategoria c
JOIN ordine o ON c.CodCliente = o.codcliente
WHERE o.dataacquisto BETWEEN '2025-01-01' AND '2025-07-24';

-- 3. Visualizzare i punti totali di tutti i clienti con tessera attiva
SELECT *
FROM PuntiTotaliClienti
WHERE StatoTessera = 'ATTIVA';

-- 4. Statistiche dipendenti in un intervallo di tempo
SELECT *
FROM StatisticheDipendenti
WHERE PrimaVendita >= '2025-01-01' AND UltimaVendita <= '2025-07-24';

-- 5. Prodotti con scorta scarsa
SELECT *
FROM ProdottiCompleti
WHERE LivelloScorta = 'SCARSO';
