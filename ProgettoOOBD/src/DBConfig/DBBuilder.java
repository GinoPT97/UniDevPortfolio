package DBConfig;

import Exceptions.ConnectionException;

import java.sql.*;

public class DBBuilder {

    private Connection connection = null;

    public DBBuilder(Connection connection) {
        this.connection = connection;
    }

    //Verifica l'istanza di connection
    private boolean connectionExists() {
        return !(connection == null);
    }

    //Verifica l'esistenza delle tabelle
    private boolean tableExists(String table_name) throws SQLException
    {
        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet tables = metadata.getTables(null, null, table_name, null);
        if (tables.next())
            return true;
        else
            return false;
    }

    //Verifica l'esistenza dei trigger
    private boolean triggerExists(String trigger_name) throws SQLException
    {
        Statement st = connection.createStatement();
        String sql = "SELECT trigger_name FROM information_schema.triggers";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            if (trigger_name.equals(rs.getString(1)))
                return true;
        }
        return false;
    }

    //Verifica l'esistenza delle sequenze
    private boolean sequenceExists(String sequence_name) throws SQLException
    {
        Statement st = connection.createStatement();
        String sql = "SELECT c.relname FROM pg_class c WHERE c.relkind = 'S'";  //Query che ritorna il nome delle sequenze create dall'utente; fonte: https://stackoverflow.com/questions/1493262/list-all-sequences-in-a-postgres-db-8-1-with-sql
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            if (sequence_name.equals(rs.getString(1)));
                return true;
        }
        return false;
    }

    //Crea le sequenze per autogenerare le chiavi primarie di tutte le relazioni
    public int createSequences() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                String sql = null;
                if (!sequenceExists("SqCodCliente")) {
                    sql = "CREATE SEQUENCE SqCodCliente\n" +
                            "INCREMENT BY 1\n" +
                            "MINVALUE 0\n" +
                            "START WITH 0;";
                    result = st.executeUpdate(sql);
                }
                else
                    System.out.println("Sequence SqCodCliente already exists!");
                if (!sequenceExists("SqCodDipendente")) {
                    sql = "CREATE SEQUENCE SqCodDipendente\n" +
                            "INCREMENT BY 1\n" +
                            "MINVALUE 0\n" +
                            "START WITH 0;";
                    result = result + st.executeUpdate(sql);
                }
                else
                    System.out.println("Sequence SqCodDipendente already exists!");
                if (!sequenceExists("SqCodTessera")) {
                    sql = "CREATE SEQUENCE SqCodTessera\n" +
                            "INCREMENT BY 1\n" +
                            "MINVALUE 0\n" +
                            "START WITH 0;";
                    result = result + st.executeUpdate(sql);
                    sql = "CREATE SEQUENCE SqCodOrdine\n" +
                            "INCREMENT BY 1\n" +
                            "MINVALUE 0\n" +
                            "START WITH 0;";
                    result = result + st.executeUpdate(sql);
                }
                else
                    System.out.println("Sequence SqCodOrdine already exists!");
                if (!sequenceExists("SqCodProdotto")) {
                    sql = "CREATE SEQUENCE SqCodProdotto\n" +
                            "INCREMENT BY 1\n" +
                            "MINVALUE 0\n" +
                            "START WITH 0;";
                    result = result + st.executeUpdate(sql);
                }
                else
                    System.out.println("Sequence SqCodProdotto already exists!");
            }
            catch (SQLException e) {
                System.out.println("SQL Exception: " + e);
            }
        }
        return result;
    }

    public int createTableCLIENTE() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                if (!tableExists("CLIENTE")) {
                    String sql = "CREATE TABLE CLIENTE (\n"+
                            "CodCliente VARCHAR(5) PRIMARY KEY, CHECK(CodCliente ~* '^[0-9]+$'),\n"+
                            "Nome VARCHAR(255) NOT NULL CHECK(Nome ~* '^[A-Za-z ]+$'),\n"+
                            "Cognome VARCHAR(255) NOT NULL CHECK(Cognome ~* '^[A-Za-z ]+$'),\n"+
                            "CodiceFiscale CHAR(16) NOT NULL CHECK(CodiceFiscale ~* '^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$'),\n"+
                            "Indirizzo VARCHAR(255) NOT NULL,\n"+
                            "Telefono1 VARCHAR(20) CHECK(Telefono1 ~* '^[0-9+ ]+$'),\n"+
                            "Telefono2 VARCHAR(20) CHECK(Telefono2 ~* '^[0-9+ ]+$'),\n"+
                            "Email VARCHAR(255) NOT NULL CHECK(Email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-za-z]+$')\n"+
                            ")";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Table 'CLIENTE' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation table 'CLIENTE': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTableDIPENDENTE() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                if (!tableExists("DIPENDENTE")) {
                    String sql = "CREATE TABLE DIPENDENTE (\n"+
                            "CodDipendente VARCHAR(5) PRIMARY KEY, CHECK(CodDipendente ~* '^[0-9]+$'),\n"+
                            "Nome VARCHAR(255) NOT NULL CHECK(Nome ~* '^[A-Za-z ]+$'),\n"+
                            "Cognome VARCHAR(255) NOT NULL CHECK(Cognome ~* '^[A-Za-z ]+$'),\n"+
                            "CodiceFiscale CHAR(16) NOT NULL CHECK(CodiceFiscale ~* '^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$'),\n"+
                            "Indirizzo VARCHAR(255) NOT NULL,\n"+
                            "Telefono1 VARCHAR(20) CHECK(Telefono1 ~* '^[0-9+ ]+$'),\n"+
                            "Telefono2 VARCHAR(20) CHECK(Telefono2 ~* '^[0-9+ ]+$'),\n"+
                            "Email VARCHAR(255) NOT NULL CHECK(Email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-za-z]+$')\n"+
                            ")";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Table 'DIPENDENTE' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation table 'DIPENDENTE': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTableTESSERA() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                if (!tableExists("TESSERA")) {
                    String sql = "CREATE TABLE TESSERA (\n"+
                            "CodTessera VARCHAR(5) PRIMARY KEY, CHECK(CodTessera ~* '^[0-9]+$'),\n"+
                            "NumeroPunti NUMERIC NOT NULL DEFAULT 0,\n"+
                            "CodCliente VARCHAR(5) NOT NULL UNIQUE CHECK(CodCliente ~* '^[0-9]+$'),\n"+
                            "CONSTRAINT TesseraFK FOREIGN KEY(CodCliente) REFERENCES CLIENTE(CodCliente)\n"+
                            ")";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Table 'TESSERA' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation table 'DIPENDENTE': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTableORDINE() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                if (!tableExists("ORDINE")) {
                    String sql = "CREATE TABLE ORDINE (\n"+
                            "CodOrdine VARCHAR(5) PRIMARY KEY, CHECK(CodOrdine ~* '^[0-9]+$'),\n"+
                            "PrezzoTotale NUMERIC NOT NULL DEFAULT 0.0, CHECK(PrezzoTotale >= 0.0),\n"+
                            "DataAcquisto DATE NOT NULL,\n"+
                            "CodCliente VARCHAR(5) NOT NULL,\n"+
                            "CodDipendente VARCHAR(5) NOT NULL,\n"+
                            "CONSTRAINT OrdineClienteFK FOREIGN KEY(CodCliente) REFERENCES CLIENTE(CodCliente),\n"+
                            "CONSTRAINT OrdineDipendenteFK FOREIGN KEY(CodDipendente) REFERENCES DIPENDENTE(CodDipendente)\n"+
                            ")";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Table 'ORDINE' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation table 'ORDINE': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTablePRODOTTO() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                if (!tableExists("PRODOTTO")) {
                    String sql = "CREATE TABLE PRODOTTO (\n"+
                            "CodProdotto VARCHAR(5) PRIMARY KEY, CHECK(CodProdotto ~* '^[0-9]+$'),\n"+
                            "Nome VARCHAR(255) NOT NULL, CHECK(Nome ~* '^[A-Za-z ]+$'),\n"+
                            "Descrizione VARCHAR(500),\n"+
                            "Prezzo NUMERIC NOT NULL DEFAULT 0.0,\n"+
                            "LuogoProvenienza VARCHAR(255),\n"+
                            "DataRaccolta DATE,\n"+
                            "DataMungitura DATE,\n"+
                            "Glutine BOOLEAN,\n"+
                            "DataScadenza DATE,\n"+
                            "Categoria TIPOLOGIA,\n"+
                            "Scorta INT, CHECK(Scorta >= 0),\n"+
                            "CONSTRAINT Discriminante CHECK(	((Categoria = 'Ortofrutticoli') AND (DataRaccolta IS NOT NULL AND  DataMungitura IS NULL AND DataScadenza IS NULL AND Glutine IS NULL)) OR"+
                            "((Categoria = 'Latticini') AND (DataRaccolta IS NULL AND  DataMungitura IS NOT NULL AND DataScadenza IS NULL AND Glutine IS NULL)) OR"+
                            "((Categoria = 'Inscatolati') AND (DataRaccolta IS NULL AND  DataMungitura IS NULL AND DataScadenza IS NOT NULL AND Glutine IS NULL)) OR"+
                            "((Categoria = 'Farinacei') AND (DataRaccolta IS NULL AND  DataMungitura IS NULL AND DataScadenza IS NULL AND Glutine IS NOT NULL))"+
									")\n"+
                            ")";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Table 'PRODOTTO' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation table 'PRODOTTO': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTableARTICOLIORDINE() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                if (!tableExists("ARTICOLIORDINE")) {
                    String sql = "CREATE TABLE ARTICOLIORDINE (\n"+
                            "CodOrdine VARCHAR(5) NOT NULL CHECK(CodOrdine ~* '^[0-9]+$'),\n"+
                            "CodProdotto VARCHAR(5) NOT NULL CHECK(CodProdotto ~* '^[0-9]+$'),\n"+
                            "Prezzo NUMERIC NOT NULL DEFAULT 0.00, CHECK(Prezzo >= 0.00),\n"+
                            "NumeroArticoli INT NOT NULL,\n"+
                            "CONSTRAINT ArticoliordineOrdineFK FOREIGN KEY(CodOrdine) REFERENCES ORDINE(CodOrdine),\n"+
                            "CONSTRAINT ArtocoliordineProdottoFK FOREIGN KEY(CodProdotto) REFERENCES PRODOTTO(CodProdotto)\n"+
                            ")";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Table 'ARTICOLIORDINE' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation table 'ARTICOLIORDINE': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createFunctionUpdateScorta() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                String sql = "CREATE OR REPLACE FUNCTION updateScorta()\n"+
                        "RETURNS TRIGGER\n"+
                        "LANGUAGE plpgsql\n"+
                        "AS\n"+
                        "$$\n"+
                        "BEGIN\n"+
                        "   UPDATE PRODOTTO SET Scorta = Scorta - NEW.NumeroArticoli\n"+
                        "   WHERE CodProdotto = NEW.CodProdotto;\n"+
                        "RETURN NEW;\n"+
                        "END\n"+
                        "$$";
                result = st.executeUpdate(sql);
                st.close();
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation function 'updateScorta()': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createFunctionRestoreScorta() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                String sql = "CREATE OR REPLACE FUNCTION restoreScorta()\n"+
                        "RETURNS TRIGGER\n"+
                        "LANGUAGE plpgsql\n"+
                        "AS\n"+
                        "$$\n"+
                        "BEGIN\n"+
                        "   UPDATE PRODOTTO SET Scorta = Scorta + OLD.NumeroArticoli\n"+
                        "   WHERE CodProdotto = OLD.CodProdotto;\n"+
                        "   RETURN NEW;\n"+
                        "END\n"+
                        "$$";
                result = st.executeUpdate(sql);
                st.close();
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation function 'restoreScorta()': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createFunctionSelectPrezzo() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                String sql = "CREATE OR REPLACE FUNCTION selectPrezzo()\n"+
                        "RETURNS TRIGGER\n"+
                        "LANGUAGE plpgsql\n"+
                        "AS\n"+
                        "$$\n"+
                        "BEGIN\n"+
                        "   UPDATE ARTICOLIORDINE SET Prezzo = (SELECT P.Prezzo\n"+
                        "                                       FROM PRODOTTO AS P\n"+
                        "                                       WHERE P.CodProdotto = NEW.CodProdotto) * NEW.NumeroArticoli\n"+
                        "   WHERE CodProdotto = NEW.CodProdotto;\n"+
                        "   RETURN NEW;\n"+
                        "END\n"+
                        "$$";
                result = st.executeUpdate(sql);
                st.close();
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation function 'selectPrezzo()': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createFunctionUpdatePrezzo() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                String sql = "CREATE OR REPLACE FUNCTION updatePrezzo()\n"+
                        "RETURNS TRIGGER\n"+
                        "LANGUAGE plpgsql\n"+
                        "AS\n"+
                        "$$\n"+
                        "BEGIN\n"+
                        "   UPDATE ORDINE SET PrezzoTotale = PrezzoTotale + (SELECT P.Prezzo\n"+
                        "                                                    FROM PRODOTTO AS P\n"+
                        "                                                    WHERE P.CodProdotto = NEW.CodProdotto) * NEW.NumeroArticoli\n"+
                        "   WHERE CodOrdine = NEW.CodOrdine;\n"+
                        "   RETURN NEW;\n"+
                        "END\n"+
                        "$$";
                result = st.executeUpdate(sql);
                st.close();
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation function 'updatePrezzo()': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createFunctionUpdateDeletePrezzo() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                String sql = "CREATE OR REPLACE FUNCTION updateDeletePrezzo()\n"+
                        "RETURNS TRIGGER\n"+
                        "LANGUAGE plpgsql\n"+
                        "AS\n"+
                        "$$\n"+
                        "BEGIN\n"+
                        "   UPDATE ORDINE SET PrezzoTotale = PrezzoTotale + (SELECT P.Prezzo\n"+
                        "                                                    FROM PRODOTTO AS P\n"+
                        "                                                    WHERE P.CodProdotto = OLD.CodProdotto) * OLD.NumeroArticoli\n"+
                        "   WHERE CodOrdine = OLD.CodOrdine;\n"+
                        "   RETURN NEW;\n"+
                        "END\n"+
                        "$$";
                result = st.executeUpdate(sql);
                st.close();
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation function 'updateDeletePrezzo()': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createFunctionCheckScorta() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                String sql = "CREATE OR REPLACE FUNCTION checkScorta()\n"+
                        "RETURNS TRIGGER\n"+
                        "LANGUAGE plpgsql\n"+
                        "AS\n"+
                        "$$\n"+
                        "BEGIN\n"+
                        "   IF (NEW.NumeroArticoli <=  (SELECT P.Scorta\n"+
                        "                               FROM PRODOTTO AS P\n"+
                        "                               WHERE P.CodProdotto = NEW.CodProdotto)) THEN\n"+
                        "   RETURN NEW;\n"+
                        "   ELSE;\n"+
                        "       RAISE EXCEPTION 'La scorta dell'articolo selezionato è inferiore rispetto alla quantità richiesta; il prodotto potrebbe essere disponibile in quantità ridotte!'\n"+
                        "   END IF;\n"+
                        "END\n"+
                        "$$";
                result = st.executeUpdate(sql);
                st.close();
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation function 'checkScorta()': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createFunctionUpdatePunti() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                String sql = "CREATE OR REPLACE FUNCTION updatePunti()\n"+
                        "RETURNS TRIGGER\n"+
                        "LANGUAGE plpgsql\n"+
                        "AS\n"+
                        "$$\n"+
                        "DECLARE\n"+
                        "   temp NUMERIC;\n"+
                        "BEGIN\n"+
                        "   SELECT P.Prezzo * NumeroArticoli INTO temp\n"+
                        "   FROM PRODOTTO AS P\n"+
                        "   WHERE P.CodProdotto = NEW.CodProdotto;\n"+
                        "   UPDATE TESSERA SET NumeroPunti = NumeroPunti + (temp * 10) / 100\n"+
                        "   WHERE CodCliente = (SELECT O.CodCliente\n"+
                        "                       FROM ORDINE AS O\n"+
                        "                       WHERE CodOrdine = NEW.CodOrdine);\n"+
                        "   RETURN NEW;\n"+
                        "END\n"+
                        "$$";
                result = st.executeUpdate(sql);
                st.close();
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation function 'updatePunti()': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createFunctionUpdateDeletePunti() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                String sql = "CREATE OR REPLACE FUNCTION updateDeletePunti()\n"+
                        "RETURNS TRIGGER\n"+
                        "LANGUAGE plpgsql\n"+
                        "AS\n"+
                        "$$\n"+
                        "DECLARE\n"+
                        "   temp NUMERIC;\n"+
                        "BEGIN\n"+
                        "   SELECT P.Prezzo * NumeroArticoli INTO temp\n"+
                        "   FROM PRODOTTO AS P\n"+
                        "   WHERE P.CodProdotto = OLD.CodProdotto;\n"+
                        "   UPDATE TESSERA SET NumeroPunti = NumeroPunti - (temp * 10) / 100\n"+
                        "   WHERE CodCliente = (SELECT O.CodCliente\n"+
                        "                       FROM ORDINE AS O\n"+
                        "                       WHERE CodOrdine = OLD.CodOrdine);\n"+
                        "   RETURN NEW;\n"+
                        "END\n"+
                        "$$";
                result = st.executeUpdate(sql);
                st.close();
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation function 'updateDeletePunti()': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTriggerUpdateScorta() throws ConnectionException
    {
        int result = -1;
        if (connectionExists()) {
            try {
                if (!triggerExists("updateScorta")) {
                    Statement st = connection.createStatement();
                    String sql = "CREATE TRIGGER updateScorta\n"+
                            "AFTER INSERT ON ARTICOLIORDINE\n"+
                            "FOR EACH ROW\n"+
                            "EXECUTE PROCEDURE updateScorta();";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Trigger 'updateScorta' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation trigger 'updateScorta': " + ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTriggerSelectPrezzo() throws ConnectionException {
        int result = -1;
        if (connectionExists()) {
            try {
                if (!triggerExists("selectPrezzo")) {
                    Statement st = connection.createStatement();
                    String sql = "CREATE TRIGGER selectPrezzo\n" +
                            "AFTER INSERT ON ARTICOLIORDINE\n" +
                            "FOR EACH ROW\n" +
                            "EXECUTE PROCEDURE selectPrezzo();";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Trigger 'selectPrezzo' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation trigger 'selectPrezzo: " + ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTriggerUpdatePrezzo() throws ConnectionException {
        int result = -1;
        if (connectionExists()) {
            try {
                if (!triggerExists("updatePrezzo")) {
                    Statement st = connection.createStatement();
                    String sql = "CREATE TRIGGER updatePrezzo\n" +
                            "AFTER INSERT ON ARTICOLIORDINE\n" +
                            "FOR EACH ROW\n" +
                            "EXECUTE PROCEDURE updatePrezzo();";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Trigger 'updatePrezzo' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation trigger 'updatePrezzo: " + ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTriggerUpdateDeletePrezzo() throws ConnectionException {
        int result = -1;
        if (connectionExists()) {
            try {
                if (!triggerExists("updateDeletePrezzo")) {
                    Statement st = connection.createStatement();
                    String sql = "CREATE TRIGGER updateDeletePrezzo\n" +
                            "AFTER INSERT ON ARTICOLIORDINE\n" +
                            "FOR EACH ROW\n" +
                            "EXECUTE PROCEDURE updateDeletePrezzo();";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Trigger 'updateDeletePrezzo' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation trigger 'updateDeletePrezzo: " + ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTriggerCheckScorta() throws ConnectionException {
        int result = -1;
        if (connectionExists()) {
            try {
                if (!triggerExists("checkScorta")) {
                    Statement st = connection.createStatement();
                    String sql = "CREATE TRIGGER checkScorta\n" +
                            "AFTER INSERT ON ARTICOLIORDINE\n" +
                            "FOR EACH ROW\n" +
                            "EXECUTE PROCEDURE checkScorta();";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Trigger 'checkScorta' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation trigger 'checkScorta': " + ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTriggerUpdatePunti() throws ConnectionException {
        int result = -1;
        if (connectionExists()) {
            try {
                if (!triggerExists("updatePunti")) {
                    Statement st = connection.createStatement();
                    String sql = "CREATE TRIGGER updatePunti\n" +
                            "AFTER UPDATE OF PrezzoTotale ON ORDINE\n" +
                            "FOR EACH ROW\n" +
                            "EXECUTE PROCEDURE updatePunti();";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Trigger 'updatePunti' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation trigger 'updatePunti': " + ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTriggerRestoreScorta() throws ConnectionException {
        int result = -1;
        if (connectionExists()) {
            try {
                if (!triggerExists("restoreScorta")) {
                    Statement st = connection.createStatement();
                    String sql = "CREATE TRIGGER restoreScorta\n" +
                            "AFTER DELETE ON ARTICOLIORDINE\n" +
                            "FOR EACH ROW\n" +
                            "EXECUTE PROCEDURE restoreScorta();";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Trigger 'restoreScorta' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation trigger 'restoreScorta': " + ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTriggerUpdateDeletePunti() throws ConnectionException {
        int result = -1;
        if (connectionExists()) {
            try {
                if (!triggerExists("updateDeletePunti")) {
                    Statement st = connection.createStatement();
                    String sql = "CREATE TRIGGER updateDeletePunti\n" +
                            "AFTER DELETE ON ARTICOLIORDINE\n" +
                            "FOR EACH ROW\n" +
                            "EXECUTE PROCEDURE updateDeletePunti();";
                    result = st.executeUpdate(sql);
                    st.close();
                }
                else
                    System.out.println("Trigger 'updateDeletePunti' already exists!");
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation trigger 'updateDeletePunti': " + ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }

    public int createTypeTIPOLOGIA() throws ConnectionException
    {
        int result = -1;

        if (connectionExists()) {
            try {
                Statement st = connection.createStatement();
                String sql = "CREATE TYPE TIPOLOGIA AS ENUM('Ortofrutticoli', 'Farinacei', 'Inscatolati', 'Latticini')";
                result = st.executeUpdate(sql);
            }
            catch (SQLException ex) {
                System.out.println("SQL Exception found in creation type 'TIPOLOGIA': "+ex);
            }
        }
        else
            throw new ConnectionException("A connection must exists!");
        return result;
    }
}
