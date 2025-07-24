-- Trigger conformi alla traccia accademica del negozio di alimentari

-- Trigger per aggiornare scorta e punti tessera dopo inserimento articolo
CREATE TRIGGER TrgAggiornaScortaPunti
AFTER INSERT ON articoliordine
FOR EACH ROW
EXECUTE FUNCTION AggiornaScortaEPunti();

