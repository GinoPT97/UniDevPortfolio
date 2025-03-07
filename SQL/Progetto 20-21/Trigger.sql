-- Trigger per aggiornare la scorta dopo l'inserimento di un articolo
CREATE TRIGGER updateScorta
AFTER INSERT ON ARTICOLIORDINE
FOR EACH ROW
EXECUTE FUNCTION updateScorta();

-- Trigger per calcolare il prezzo dell'articolo
CREATE TRIGGER selectPrezzo
AFTER INSERT ON ARTICOLIORDINE
FOR EACH ROW
EXECUTE FUNCTION selectPrezzo();

-- Trigger per aggiornare il prezzo totale dell'ordine
CREATE TRIGGER updatePrezzo
AFTER INSERT OR UPDATE ON ARTICOLIORDINE
FOR EACH ROW
EXECUTE FUNCTION updatePrezzo();

-- Trigger per aggiornare il prezzo totale dopo la rimozione di un articolo
CREATE TRIGGER updateDeletePrezzo
AFTER DELETE ON ARTICOLIORDINE
FOR EACH ROW
EXECUTE FUNCTION updateDeletePrezzo();

-- Trigger per controllare la scorta prima di inserire un articolo
CREATE TRIGGER checkScorta
BEFORE INSERT ON ARTICOLIORDINE
FOR EACH ROW
EXECUTE FUNCTION checkScorta();

-- Trigger per aggiornare i punti del cliente
CREATE TRIGGER updatePunti
AFTER UPDATE OF PrezzoTotale ON ORDINE
FOR EACH ROW
EXECUTE FUNCTION updatePunti();

-- Trigger per ripristinare la scorta dopo la rimozione di un articolo
CREATE TRIGGER restoreScorta
AFTER DELETE ON ARTICOLIORDINE
FOR EACH ROW
EXECUTE FUNCTION restoreScorta();

-- Trigger per eliminare i punti dopo la rimozione di un articolo
CREATE TRIGGER updateDeletePunti
AFTER DELETE ON ARTICOLIORDINE
FOR EACH ROW
EXECUTE FUNCTION updateDeletePunti();

