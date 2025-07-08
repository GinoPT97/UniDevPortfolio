-- Trigger conformi alla traccia accademica del negozio di alimentari

-- TRIGGER per controllare la disponibilità del prodotto prima dell'inserimento
CREATE OR REPLACE TRIGGER controlla_disponibilita
    BEFORE INSERT ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION controlla_disponibilita_prodotto();

-- TRIGGER per aggiornare la scorta dopo inserimento/aggiornamento/eliminazione
CREATE OR REPLACE TRIGGER aggiorna_scorta
    AFTER INSERT OR UPDATE OR DELETE ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION aggiorna_scorta_prodotto();

-- TRIGGER per aggiornare automaticamente il prezzo totale dell'ordine
CREATE OR REPLACE TRIGGER aggiorna_prezzo_totale
    AFTER INSERT OR UPDATE OR DELETE ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION aggiorna_prezzo_totale_ordine();

-- TRIGGER per aggiornare i punti fedeltà del cliente dopo l'inserimento di un articolo
-- Richiesta dalla traccia: "Per ogni acquisto il cliente riceve il 10% del valore della spesa in punti fedeltà"
CREATE OR REPLACE TRIGGER aggiorna_punti_inserimento
    AFTER INSERT ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION aggiorna_punti_tessera();

-- TRIGGER per rimuovere i punti fedeltà quando un articolo viene rimosso dall'ordine
CREATE OR REPLACE TRIGGER rimuovi_punti_eliminazione
    AFTER DELETE ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION rimuovi_punti_tessera();