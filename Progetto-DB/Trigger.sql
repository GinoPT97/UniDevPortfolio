-- Trigger conformi alla traccia accademica del negozio di alimentari

-- TRIGGER per controllare la disponibilità del prodotto prima dell'inserimento
CREATE OR REPLACE TRIGGER ControllaDisponibilita
    BEFORE INSERT ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION ControllaDisponibilitaProdotto();

-- TRIGGER per aggiornare la scorta dopo inserimento/aggiornamento/eliminazione
CREATE OR REPLACE TRIGGER AggiornaScorta
    AFTER INSERT OR UPDATE OR DELETE ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION AggiornaScortaProdotto();

-- TRIGGER per aggiornare automaticamente il prezzo totale dell'ordine
CREATE OR REPLACE TRIGGER AggiornaPrezzoTotale
    AFTER INSERT OR UPDATE OR DELETE ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION AggiornaPrezzoTotaleOrdine();

-- TRIGGER per aggiornare i punti fedeltà del cliente dopo l'inserimento di un articolo
-- Richiesta dalla traccia: "Per ogni acquisto il cliente riceve il 10% del valore della spesa in punti fedeltà"
CREATE OR REPLACE TRIGGER AggiornaPuntiInserimento
    AFTER INSERT ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION AggiornaPuntiTessera();

-- TRIGGER per rimuovere i punti fedeltà quando un articolo viene rimosso dall'ordine
CREATE OR REPLACE TRIGGER RimuoviPuntiEliminazione
    AFTER DELETE ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION RimuoviPuntiTessera();