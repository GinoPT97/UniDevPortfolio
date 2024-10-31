-- Prima eliminare le righe dalle tabelle figlie

DELETE FROM Articoliordine;
DELETE FROM Ordine;
DELETE FROM Prodotto;
DELETE FROM Tessera;

-- Poi eliminare le righe dalle tabelle madri

DELETE FROM Dipendente;
DELETE FROM Cliente;

