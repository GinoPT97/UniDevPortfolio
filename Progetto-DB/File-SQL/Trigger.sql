-- Trigger conformi alla traccia accademica del negozio di alimentari

-- Trigger per aggiornare scorta e punti tessera dopo inserimento articolo
CREATE TRIGGER TrgAggiornaScortaPunti
AFTER INSERT ON articoliordine
FOR EACH ROW
EXECUTE FUNCTION AggiornaScortaEPunti();

-- Trigger per aggiornare il prezzo totale dell'ordine dopo inserimento articolo
CREATE TRIGGER TrgAggiornaPrezzoOrdine
AFTER INSERT ON articoliordine
FOR EACH ROW
EXECUTE FUNCTION AggiornaPrezzoOrdine();

-- Trigger per bloccare l'inserimento se la scorta è insufficiente
CREATE TRIGGER TrgVerificaScortaProdotto
BEFORE INSERT ON articoliordine
FOR EACH ROW
EXECUTE FUNCTION VerificaScortaProdotto();

-- Trigger per aggiornare lo stato della tessera dopo inserimento o aggiornamento
CREATE TRIGGER TrgAggiornaStatoTessera
BEFORE INSERT OR UPDATE ON tessera
FOR EACH ROW
EXECUTE FUNCTION AggiornaStatoTessera();

-- Trigger per ripristinare scorta e punti tessera dopo DELETE da articoliordine
CREATE TRIGGER TrgRipristinaScortaPunti
AFTER DELETE ON articoliordine
FOR EACH ROW
EXECUTE FUNCTION RipristinaScortaProdotto();

-- Trigger per aggiornare il prezzo totale dell'ordine dopo DELETE da articoliordine
CREATE TRIGGER TrgAggiornaPrezzoOrdineDelete
AFTER DELETE ON articoliordine
FOR EACH ROW
EXECUTE FUNCTION AggiornaPrezzoOrdineDelete();