package DBConfiguration;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConfiguration {
    private Connection connection = null;

    public DBConfiguration(Connection connection) {
        this.connection = connection;
    }

    // Verifica l'istanza di connection
    private boolean connectionExists() {
        return !(connection == null);
    }

    // Verifica l'esistenza delle tabelle
    private boolean tableExists(String table_name) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        try (ResultSet tables = metadata.getTables(null, null, table_name, null)) {
            return tables.next();
        }
    }

    // Crea una sequenza per autogenerare le chiavi primarie di tutte le relazioni
    public int createSequences() throws ConnectionException, SQLException {
        int result = -1;
        if (connectionExists()) {
            try (Statement st = connection.createStatement()) {
                String[] sequences = {
                    "SCodCliente",
                    "SCodDipendente",
                    "SCodProdotto",
                    "SCodOrdine",
                    "SCodTessera"
                };

                for (String sequence : sequences) {
                    String sql = String.format("CREATE SEQUENCE %s INCREMENT BY 1 MINVALUE 1 MAXVALUE 99999 START WITH 1;", sequence);
                    result += st.executeUpdate(sql);
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception in Creation Sequence : " + ex);
            }
        }
        return result;
    }

    public int createTableCliente() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement st = connection.createStatement()) {
                if (!tableExists("cliente")) {
                    String sql = "CREATE TABLE IF NOT EXISTS cliente(\n"
                            + " codcliente VARCHAR(5) PRIMARY KEY, CHECK(codcliente ~* '^[0-9]+$'),\n"
                            + " nome VARCHAR(255) NOT NULL CHECK(nome ~* '^[A-Za-z ]+$'),\n"
                            + " cognome VARCHAR(255) NOT NULL CHECK(cognome ~* '^[A-Za-z ]+$'),\n"
                            + " codicefiscale CHAR(16) NOT NULL CHECK(codicefiscale ~* '^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$'),\n "
                            + " indirizzo VARCHAR(255) NOT NULL,\n"
                            + " telefono VARCHAR(20) CHECK(telefono ~* '^[0-9+ ]+$'),\n"
                            + " email VARCHAR(255) NOT NULL CHECK(email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$')\n "
                            + " );";

                    result = st.executeUpdate(sql);
                } else {
                    System.out.println("Table Cliente already exists!");
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception in creation table Cliente : " + ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int createTableDipendente() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement st = connection.createStatement()) {
                if (!tableExists("dipendente")) {
                    String sql = "CREATE TABLE IF NOT EXISTS dipendente (\n"
                            + "coddipendente VARCHAR(5) PRIMARY KEY, CHECK(coddipendente ~* '^[0-9]+$'),\n"
                            + " nome VARCHAR(255) NOT NULL CHECK(nome ~* '^[A-Za-z ]+$'),\n"
                            + " cognome VARCHAR(255) NOT NULL CHECK(cognome ~* '^[A-Za-z ]+$'),\n"
                            + " codicefiscale CHAR(16) NOT NULL CHECK(codicefiscale ~* '^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$'),\n "
                            + " indirizzo VARCHAR(255) NOT NULL,\n"
                            + " telefono VARCHAR(20) CHECK(telefono ~* '^[0-9+ ]+$'),\n"
                            + " email VARCHAR(255) NOT NULL CHECK(email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$')\n "
                            + " );";

                    result = st.executeUpdate(sql);
                } else {
                    System.out.println("Table Dipendente already exists!");
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception in creation table Dipendente : " + ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int createTableTessera() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement st = connection.createStatement()) {
                if (!tableExists("tessera")) {
                    String sql = "CREATE TABLE IF NOT EXISTS tessera(\n"
                            + " codtessera VARCHAR(5) PRIMARY KEY, CHECK(codtessera ~* '^[0-9]+$'),\n"
                            + " numeropunti real NOT NULL DEFAULT 0.00,\n"
                            + " codcliente VARCHAR(5) NOT NULL UNIQUE CHECK(CodCliente ~* '^[0-9]+$'),\n"
                            + " CONSTRAINT TesseraFK FOREIGN KEY(CodCliente) \n " + " REFERENCES CLIENTE(codcliente) \n"
                            + " ON UPDATE CASCADE \n" + " ON DELETE CASCADE \n " + " );";

                    result = st.executeUpdate(sql);
                } else {
                    System.out.println("Table Tessera already exists!");
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception in creation table Tessera : " + ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int createTableProdotto() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement st = connection.createStatement()) {
                if (!tableExists("prodotto")) {
                    String sql = "CREATE TABLE IF NOT EXISTS prodotto(\n"
                            + " codprodotto VARCHAR(5) PRIMARY KEY, CHECK(CodProdotto ~* '^[0-9]+$'),\n"
                            + " nome VARCHAR(255) NOT NULL, CHECK(Nome ~* '^[A-Za-z ]+$'),\n"
                            + " descrizione VARCHAR(500), \n" + " prezzo NUMERIC DEFAULT 0.00, \n "
                            + " luogoprovenienza VARCHAR(255), \n" + " dataraccolta DATE,\n "
                            + " datamungitura DATE, \n " + " glutine BOOLEAN, \n " + " datascadenza DATE, \n "
                            + " categoria TIPOLOGIA,\n" + " scorta INT , CHECK (scorta>=0),\n"
                            + " CONSTRAINT categoria CHECK (((categoria ='Ortofrutticoli') AND (dataraccolta IS NOT NULL AND datamungitura IS NULL AND datascadenza IS NULL AND glutine IS NULL)) OR"
                            + " ((categoria ='Latticini') AND (dataraccolta IS NULL AND datamungitura IS NOT NULL AND datascadenza IS NULL AND glutine IS NULL)) OR"
                            + " ((categoria ='Inscatolati') AND (dataraccolta IS NULL AND datamungitura IS NULL AND datascadenza IS NOT NULL AND glutine IS NULL)) OR"
                            + " ((categoria ='Farinacei') AND (dataraccolta IS NULL AND datamungitura IS NULL AND datascadenza IS NULL AND glutine IS NOT NULL)))"
                            + " );";
                    result = st.executeUpdate(sql);
                } else {
                    System.out.println("Table Prodotto already exists!");
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception in creation table Prodotto : " + ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int createTableOrdine() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement st = connection.createStatement()) {
                if (!tableExists("ordine")) {
                    String sql = "CREATE TABLE IF NOT EXISTS ordine(\n" + " codordine VARCHAR(5) NOT NULL, \n"
                            + " prezzototale real NOT NULL DEFAULT 0.00 CHECK (prezzototale >= 0), \n"
                            + " dataacquisto date NOT NULL, \n"
                            + " codcliente VARCHAR(5) NOT NULL CHECK (codcliente ~* '^[0-9]+$'), \n"
                            + " coddipendente VARCHAR(5) NOT NULL CHECK (coddipendente ~* '^[0-9]+$'), \n"
                            + " CONSTRAINT ordinepk PRIMARY KEY (codordine), \n"
                            + " CONSTRAINT ordineclientefk FOREIGN KEY (codcliente) REFERENCES cliente (codcliente) ON UPDATE CASCADE ON DELETE NO ACTION, \n "
                            + " CONSTRAINT ordinedipendentefk FOREIGN KEY (coddipendente) REFERENCES dipendente (coddipendente) ON UPDATE CASCADE ON DELETE NO ACTION\n "
                            + " );";

                    result = st.executeUpdate(sql);
                } else {
                    System.out.println("Table Ordine already exists!");
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception in creation table Ordine : " + ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int createTableArticoliOrdine() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement st = connection.createStatement()) {
                if (!tableExists("ARTICOLIORDINE")) {
                    String sql = "CREATE TABLE IF NOT EXISTS ARTICOLIORDINE (\n"
                            + "CodOrdine VARCHAR(5) NOT NULL CHECK(CodOrdine ~* '^[0-9]+$'),\n"
                            + "CodProdotto VARCHAR(5) NOT NULL CHECK(CodProdotto ~* '^[0-9]+$'),\n"
                            + "CodCliente VARCHAR(5) PRIMARY KEY, CHECK(codcliente ~* '^[0-9]+$'),\n"
                            + "Prezzo NUMERIC NOT NULL DEFAULT 0.00, CHECK(Prezzo >= 0.00),\n"
                            + "NumeroPunti NUMERIC NOT NULL DEFAULT 0.00, CHECK(Prezzo >= 0.00), \n"
                            + "NumeroArticoli INT NOT NULL,\n" + "Categoria TIPOLOGIA,\n"
                            + "CONSTRAINT ArticoliordineClienteFK FOREIGN KEY(CodCliente) REFERENCES CLIENTE(CodCliente),\n"
                            + "CONSTRAINT ArtocoliordineProdottoFK FOREIGN KEY(CodProdotto) REFERENCES PRODOTTO(CodProdotto),\n"
                            + "CONSTRAINT ArtocoliordineOrdineFK FOREIGN KEY(CodOrdine) REFERENCES ORDINE(CodOrdine)\n"
                            + " );";
                    result = st.executeUpdate(sql);
                } else {
                    System.out.println("Table 'ARTICOLIORDINE' already exists!");
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception found in creation table 'ARTICOLIORDINE': " + ex);
            }
        } else {
            throw new ConnectionException("A connection must exists!");
        }
        return result;
    }

    public int createTipologie() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement st = connection.createStatement()) {
                String sql = "DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'tipologia') THEN CREATE TYPE TIPOLOGIA AS ENUM('Ortofrutticoli','Latticini','Inscatolati','Farinacei'); END IF; END $$;";
                result = st.executeUpdate(sql);
            } catch (SQLException ex) {
                System.out.println("SQL Exception in creation type tipologia: " + ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int FromatTables() throws ConnectionException {
        int result = 0;

        if (!connectionExists()) {
            throw new ConnectionException("A connection must exist!");
        }

        try (Statement st = connection.createStatement()) {
            // Prima eliminare le righe dalle tabelle figlie
            String sqlDeleteArticoliordine = "DELETE FROM Articoliordine;";
            result += st.executeUpdate(sqlDeleteArticoliordine);

            String sqlDeleteOrdine = "DELETE FROM Ordine;";
            result += st.executeUpdate(sqlDeleteOrdine);

            String sqlDeleteProdotto = "DELETE FROM Prodotto;";
            result += st.executeUpdate(sqlDeleteProdotto);

            String sqlDeleteTessera = "DELETE FROM Tessera;";
            result += st.executeUpdate(sqlDeleteTessera);

            // Poi eliminare le righe dalle tabelle madri
            String sqlDeleteDipendente = "DELETE FROM Dipendente;";
            result += st.executeUpdate(sqlDeleteDipendente);

            String sqlDeleteCliente = "DELETE FROM Cliente;";
            result += st.executeUpdate(sqlDeleteCliente);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int populateDatabase() throws ConnectionException {
        int result = 0;

        // Verifica che la connessione esista
        if (!connectionExists()) {
            throw new ConnectionException("A connection must exist!");
        }

        try (Statement st = connection.createStatement()) {
            // Popola la tabella Cliente
            String sqlCliente = "INSERT INTO cliente (codcliente, nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES "
                    + "('11111','aldo','marzante','BBBBBB11B11B111B', 'Via Don Matteo','1234567890','aldo@arte.it'),"
                    + "('22222','luca','benson','AAAAAA22A22A222A', 'Via Don Corleone','1234567890','luca@arte.it'),"
                    + "('33333','mario','sarni','CCCCCC33C33C333C','Via San giovanni','1234567890','mario@arte.it'),"
                    + "('77777','alessio','sassi','DDDDDD44D44D444D','Via cremoni','1234567890','alessio@arte.it'),"
                    + "('44455','giorgio','rossi','WWWWWW55W55W555W', 'Via Don Carlo','1234567890','giorgio@arte.it'),"
                    + "('55566','paolo','verdi','QQQQQQ66Q66Q666Q', 'Via Don Alberto','1234567890','paolo@arte.it'),"
                    + "('66677','simone','bianchi','UUUUUU77U77U777U','Via Don Giuseppe','1234567890','simone@arte.it'),"
                    + "('88899','enrico','gialli','VVVVVV99V99V999V','Via Don Mario','1234567890','enrico@arte.it');";
            result += st.executeUpdate(sqlCliente);

            // Popola la tabella Dipendente
            String sqlDipendente = "INSERT INTO dipendente (coddipendente, nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES "
                    + "('89899','dario','forte','FFFFFF11F11F111F','via andromeda','1234567890','dario@arte.it'),"
                    + "('79799','sandro','romano','LLLLLL22L22L222L','via omega','1234567890','sandro@arte.it'),"
                    + "('34345','giulio','cesare','PPPPPP88P88P888P','via roma','1234567890','giulio@arte.it'),"
                    + "('11111','mario','rossi','YYYYYY11Y11Y111Y','via parma','1234567890','mario@arte.it'),"
                    + "('22222','andrea','verdi','HHHHHH22H22H222H','via milano','1234567890','andrea@arte.it'),"
                    + "('33333','giuseppe','bianchi','KKKKKK88K88K888K','via torino','1234567890','giuseppe@arte.it'),"
                    + "('44444','marco','gialli','GGGLLN80A01H501P','via napoli','1234567890','marco@arte.it');";
            result += st.executeUpdate(sqlDipendente);

            // Popola la tabella Tessera
            String sqlTessera = "INSERT INTO tessera (codtessera, numeropunti, codcliente) VALUES "
                    + "('55555','20','11111'),"
                    + "('44444','30','22222'),"
                    + "('66666','100','33333'),"
                    + "('88888','0','77777'),"
                    + "('77777','50','44455'),"
                    + "('66667','80','55566'),"
                    + "('55551','10','66677'),"
                    + "('33333','150','88899');";
            result += st.executeUpdate(sqlTessera);

            // Popola la tabella Prodotto
            String sqlProdotto = "INSERT INTO prodotto (codprodotto, nome, descrizione, prezzo, luogoprovenienza, dataraccolta, datamungitura, glutine, datascadenza, categoria, scorta) VALUES "
                    + "('11111', 'Mela Rossa', 'Mela italiana rossa', 1.50, 'Italia', '2022-07-01', NULL, NULL, NULL, 'Ortofrutticoli', 100),"
                    + "('22222', 'Formaggio Parmigiano', 'Formaggio Parmigiano Reggiano', 15.00, 'Italia', NULL, '2023-06-01', NULL, NULL, 'Latticini', 50),"
                    + "('33333', 'Pomodori in scatola', 'Pomodori pelati in scatola', 2.00, 'Italia', NULL, NULL, NULL, '2024-12-31', 'Inscatolati', 200),"
                    + "('44444', 'Spaghetti', 'Spaghetti di grano duro', 1.20, 'Italia', NULL, NULL, TRUE, NULL, 'Farinacei', 300),"
                    + "('55555', 'Arance', 'Arance siciliane', 1.20, 'Italia', '2022-07-01', NULL, NULL, NULL, 'Ortofrutticoli', 150),"
                    + "('66666', 'Parmigiano', 'Parmigiano Reggiano DOP', 18.00, 'Italia', NULL, '2023-06-01', NULL, NULL, 'Latticini', 70),"
                    + "('77777', 'Tonno in scatola', 'Tonno al naturale in scatola', 3.50, 'Italia', NULL, NULL, NULL, '2024-12-31', 'Inscatolati', 100),"
                    + "('88888', 'Farina', 'Farina di grano tenero tipo \"00\"', 0.80, 'Italia', NULL, NULL, FALSE, NULL, 'Farinacei', 200);";
            result += st.executeUpdate(sqlProdotto);

            // Popola la tabella Ordine
            String sqlOrdine = "INSERT INTO ordine (codordine, prezzototale, dataacquisto, codcliente, coddipendente) VALUES "
                    + "('12122','57','2001-02-12','11111','89899'),"
                    + "('11112','45','2010-03-22','11111','89899'),"
                    + "('11132','47','2011-07-02','11111','89899'),"
                    + "('13112','105','2017-11-22','11111','79799'),"
                    + "('13312','185','2007-10-02','11111','79799'),"
                    + "('13512','15','2004-12-09','11111','34345'),"
                    + "('14142','20','2009-11-08','55566','11111'),"
                    + "('12131','55','2015-03-12','66677','22222'),"
                    + "('15151','30','2023-05-15','88899','34345'),"
                    + "('16161','80','2023-08-22','55566','79799'),"
                    + "('17171','25','2024-01-10','33333','44444'),"
                    + "('12132','120','2013-06-17','88899','33333'),"
                    + "('18181','40','2023-12-05','44455','22222'),"
                    + "('19191','65','2024-04-18','66677','33333'),"
                    + "('20202','55','2024-02-28','88899','79799');";
            result += st.executeUpdate(sqlOrdine);

            // Popola la tabella ArticoliOrdine
            String sqlArticoliOrdine = "INSERT INTO articoliordine (CodOrdine, CodProdotto, CodCliente, prezzo, numeropunti, numeroarticoli, categoria) VALUES "
                    + "('12122','88888','11111', '0.80', '2', '10', 'Farinacei'),"
                    + "('11112','88888','11111', '0.80', '2', '10', 'Farinacei'),"
                    + "('11132','77777','11111', '3.50', '2', '10', 'Inscatolati'),"
                    + "('13112','66666','11111', '18.00', '5', '4', 'Latticini'),"
                    + "('13312','66666','11111', '18.00', '5', '4', 'Latticini'),"
                    + "('13512','55555','11111', '1.20', '3', '15', 'Ortofrutticoli'),"
                    + "('14142','55555','11111', '1.20', '3', '15', 'Ortofrutticoli'),"
                    + "('12131','11111','55566', '1.50', '2', '20', 'Ortofrutticoli'),"
                    + "('15151','11111','88899', '1.50', '2', '10', 'Ortofrutticoli'),"
                    + "('16161','77777','55566', '3.50', '2', '15', 'Inscatolati'),"
                    + "('17171','77777','33333', '3.50', '2', '10', 'Inscatolati'),"
                    + "('12132','55555','88899', '1.20', '3', '25', 'Ortofrutticoli'),"
                    + "('18181','55555','44455', '1.20', '3', '20', 'Ortofrutticoli'),"
                    + "('19191','77777','66677', '3.50', '2', '30', 'Inscatolati'),"
                    + "('20202','33333','88899', '2.00', '5', '12', 'Inscatolati');";
            result += st.executeUpdate(sqlArticoliOrdine);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
