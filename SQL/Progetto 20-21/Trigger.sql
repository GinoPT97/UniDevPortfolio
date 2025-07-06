-- Trigger conformi alla struttura del database secondo la traccia accademica

-- Trigger per controllare la disponibilità del prodotto prima dell'inserimento
CREATE OR REPLACE TRIGGER controlla_disponibilita
    BEFORE INSERT ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION controlla_disponibilita_prodotto();

-- Trigger per aggiornare la scorta dopo inserimento/aggiornamento/eliminazione
CREATE OR REPLACE TRIGGER aggiorna_scorta
    AFTER INSERT OR UPDATE OR DELETE ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION aggiorna_scorta_prodotto();

-- Trigger per aggiornare il prezzo totale dell'ordine
CREATE OR REPLACE TRIGGER aggiorna_prezzo_totale
    AFTER INSERT OR UPDATE OR DELETE ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION aggiorna_prezzo_totale_ordine();

-- Trigger per aggiornare i punti fedeltà del cliente dopo l'inserimento di un articolo
CREATE OR REPLACE TRIGGER aggiorna_punti_inserimento
    AFTER INSERT ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION aggiorna_punti_fedelta();

-- Trigger per rimuovere i punti fedeltà quando un articolo viene rimosso dall'ordine
CREATE OR REPLACE TRIGGER rimuovi_punti_eliminazione
    AFTER DELETE ON articoliordine
    FOR EACH ROW
    EXECUTE FUNCTION rimuovi_punti_fedelta();