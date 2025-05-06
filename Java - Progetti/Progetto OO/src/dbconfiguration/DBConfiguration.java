package dbconfiguration;

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

    private boolean connectionExists() {
        return !(connection == null);
    }

    private boolean tableExists(String table_name) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        try (ResultSet tables = metadata.getTables(null, null, table_name, null)) {
            return tables.next();
        }
    }

    public int createTableCliente() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement st = connection.createStatement()) {
                if (!tableExists("cliente")) {
                    String sql = """
                            CREATE TABLE IF NOT EXISTS cliente(
                                codcliente SERIAL PRIMARY KEY,
                                nome VARCHAR(255) NOT NULL CHECK(nome ~* '^[A-Za-z ]+$'),
                                cognome VARCHAR(255) NOT NULL CHECK(cognome ~* '^[A-Za-z ]+$'),
                                codicefiscale VARCHAR(16) NOT NULL CHECK(codicefiscale ~* '^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$'),
                                indirizzo VARCHAR(255) NOT NULL,
                                telefono VARCHAR(20) CHECK(telefono ~* '^[0-9+ ]+$'),
                                email VARCHAR(255) NOT NULL CHECK(email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$')
                            );
                            """;

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
                            + " coddipendente SERIAL PRIMARY KEY,\n"
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
                            + " codtessera SERIAL PRIMARY KEY,\n"
                            + " numeropunti real NOT NULL DEFAULT 0.00,\n"
                            + " codcliente INTEGER NOT NULL UNIQUE,\n"
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
                            + " codprodotto SERIAL PRIMARY KEY,\n"
                            + " nome VARCHAR(255) NOT NULL CHECK(nome ~* '^[A-Za-z ]+$'),\n"
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
                    String sql = "CREATE TABLE IF NOT EXISTS ordine(\n" + " codordine SERIAL NOT NULL, \n"
                            + " prezzototale real NOT NULL DEFAULT 0.00 CHECK (prezzototale >= 0), \n"
                            + " dataacquisto date NOT NULL, \n"
                            + " codcliente INTEGER NOT NULL, \n"
                            + " coddipendente INTEGER NOT NULL, \n"
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
                            + "CodOrdine INTEGER NOT NULL,\n"
                            + "CodProdotto INTEGER NOT NULL,\n"
                            + "CodCliente INTEGER NOT NULL,\n"
                            + "Prezzo NUMERIC NOT NULL DEFAULT 0.00 CHECK(Prezzo >= 0.00),\n"
                            + "NumeroPunti NUMERIC NOT NULL DEFAULT 0.00 CHECK(NumeroPunti >= 0.00),\n"
                            + "NumeroArticoli INT NOT NULL,\n"
                            + "Categoria TIPOLOGIA,\n"
                            + "CONSTRAINT PK_ArticoliOrdine PRIMARY KEY (CodOrdine, CodProdotto),\n"
                            + "CONSTRAINT ArticoliordineClienteFK FOREIGN KEY(CodCliente) REFERENCES CLIENTE(CodCliente),\n"
                            + "CONSTRAINT ArticoliordineProdottoFK FOREIGN KEY(CodProdotto) REFERENCES PRODOTTO(CodProdotto),\n"
                            + "CONSTRAINT ArticoliordineOrdineFK FOREIGN KEY(CodOrdine) REFERENCES ORDINE(CodOrdine)\n"
                            + ");";
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
            String sqlDeleteArticoliordine = "DELETE FROM Articoliordine;";
            result += st.executeUpdate(sqlDeleteArticoliordine);

            String sqlDeleteOrdine = "DELETE FROM Ordine;";
            result += st.executeUpdate(sqlDeleteOrdine);

            String sqlDeleteProdotto = "DELETE FROM Prodotto;";
            result += st.executeUpdate(sqlDeleteProdotto);

            String sqlDeleteTessera = "DELETE FROM Tessera;";
            result += st.executeUpdate(sqlDeleteTessera);

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

        if (!connectionExists()) {
            throw new ConnectionException("A connection must exist!");
        }

        try (Statement st = connection.createStatement()) {
            // Inserimento dati nella tabella cliente
            String sqlCliente = "INSERT INTO cliente (codcliente, nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES "
                    + "('11111','aldo','marzante','BBBBBB11B11B111B', 'Via Don Matteo','1234567890','aldo@arte.it'),"
                    + "('22222','luca','benson','AAAAAA22A22A222A', 'Via Don Corleone','1234567890','luca@arte.it'),"
                    + "('33333','mario','sarni','CCCCCC33C33C333C','Via San giovanni','1234567890','mario@arte.it'),"
                    + "('44444','alessio','sassi','DDDDDD44D44D444D','Via cremoni','1234567890','alessio@arte.it'),"
                    + "('55555','giorgio','rossi','WWWWWW55W55W555W', 'Via Don Carlo','1234567890','giorgio@arte.it'),"
                    + "('66666','paolo','verdi','QQQQQQ66Q66Q666Q', 'Via Don Alberto','1234567890','paolo@arte.it'),"
                    + "('77777','simone','bianchi','UUUUUU77U77U777U','Via Don Giuseppe','1234567890','simone@arte.it'),"
                    + "('88888','enrico','gialli','VVVVVV99V99V999V','Via Don Mario','1234567890','enrico@arte.it') "
                    + "ON CONFLICT (codcliente) DO NOTHING;";
            result += st.executeUpdate(sqlCliente);

            // Inserimento dati nella tabella dipendente
            String sqlDipendente = "INSERT INTO dipendente (coddipendente, nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES "
                    + "('89899','dario','forte','FFFFFF11F11F111F','via andromeda','1234567890','dario@arte.it'),"
                    + "('79799','sandro','romano','LLLLLL22L22L222L','via omega','1234567890','sandro@arte.it'),"
                    + "('34345','giulio','cesare','PPPPPP88P88P888P','via roma','1234567890','giulio@arte.it'),"
                    + "('11112','mario','rossi','YYYYYY11Y11Y111Y','via parma','1234567890','mario@arte.it'),"
                    + "('22223','andrea','verdi','HHHHHH22H22H222H','via milano','1234567890','andrea@arte.it'),"
                    + "('33334','giuseppe','bianchi','KKKKKK88K88K888K','via torino','1234567890','giuseppe@arte.it'),"
                    + "('44445','marco','gialli','GGGLLN80A01H501P','via napoli','1234567890','marco@arte.it'),"
                    + "('00000','admin','administrator','ADMINA00A00A000A','Admin Address','0000000000','admin@company.com') "
                    + "ON CONFLICT (coddipendente) DO NOTHING;";
            result += st.executeUpdate(sqlDipendente);

            // Inserimento dati nella tabella tessera
            String sqlTessera = "INSERT INTO tessera (codtessera, numeropunti, codcliente) VALUES "
                    + "('55551','20','11111'),"
                    + "('44441','30','22222'),"
                    + "('66661','100','33333'),"
                    + "('88881','0','77777'),"
                    + "('77771','50','44444'),"
                    + "('66662','80','55555'),"
                    + "('55552','10','66666'),"
                    + "('33331','150','88888') "
                    + "ON CONFLICT (codtessera) DO NOTHING;";
            result += st.executeUpdate(sqlTessera);

            // Inserimento dati nella tabella prodotto
            String sqlProdotto = "INSERT INTO prodotto (codprodotto, nome, descrizione, prezzo, luogoprovenienza, dataraccolta, datamungitura, glutine, datascadenza, categoria, scorta) VALUES "
                    + "('11112', 'Mela Rossa', 'Mela italiana rossa', 1.50, 'Italia', '2022-07-01', NULL, NULL, NULL, 'Ortofrutticoli', 100),"
                    + "('22223', 'Formaggio Parmigiano', 'Formaggio Parmigiano Reggiano', 15.00, 'Italia', NULL, '2023-06-01', NULL, NULL, 'Latticini', 50),"
                    + "('33334', 'Pomodori in scatola', 'Pomodori pelati in scatola', 2.00, 'Italia', NULL, NULL, NULL, '2024-12-31', 'Inscatolati', 200),"
                    + "('44445', 'Spaghetti', 'Spaghetti di grano duro', 1.20, 'Italia', NULL, NULL, TRUE, NULL, 'Farinacei', 300),"
                    + "('55556', 'Arance', 'Arance siciliane', 1.20, 'Italia', '2022-07-01', NULL, NULL, NULL, 'Ortofrutticoli', 150),"
                    + "('66667', 'Parmigiano', 'Parmigiano Reggiano DOP', 18.00, 'Italia', NULL, '2023-06-01', NULL, NULL, 'Latticini', 70),"
                    + "('77778', 'Tonno in scatola', 'Tonno al naturale in scatola', 3.50, 'Italia', NULL, NULL, NULL, '2024-12-31', 'Inscatolati', 100),"
                    + "('88888', 'Farina', 'Farina di grano tenero tipo \"00\"', 0.80, 'Italia', NULL, NULL, FALSE, NULL, 'Farinacei', 200) "
                    + "ON CONFLICT (codprodotto) DO NOTHING;";
            result += st.executeUpdate(sqlProdotto);

            // Inserimento dati nella tabella ordine
            String sqlOrdine = "INSERT INTO ordine (codordine, prezzototale, dataacquisto, codcliente, coddipendente) VALUES "
                    + "('12123','57','2001-02-12','11111','89899'),"
                    + "('11114','45','2010-03-22','11111','89899'),"
                    + "('11134','47','2011-07-02','11111','89899'),"
                    + "('13114','105','2017-11-22','11111','79799'),"
                    + "('13314','185','2007-10-02','11111','79799'),"
                    + "('13514','15','2004-12-09','11111','34345'),"
                    + "('14144','20','2009-11-08','55555','11112'),"
                    + "('12133','55','2015-03-12','66666','22223'),"
                    + "('15153','30','2023-05-15','88888','34345'),"
                    + "('16163','80','2023-08-22','55555','79799'),"
                    + "('17173','25','2024-01-10','33333','44445'),"
                    + "('12134','120','2013-06-17','88888','33334'),"
                    + "('18183','40','2023-12-05','44444','22223'),"
                    + "('19193','65','2024-04-18','66666','33334'),"
                    + "('20204','55','2024-02-28','88888','79799') "
                    + "ON CONFLICT (codordine) DO NOTHING;";
            result += st.executeUpdate(sqlOrdine);

            // Inserimento dati nella tabella articoliordine
            String sqlArticoliOrdine = "INSERT INTO articoliordine (CodOrdine, CodProdotto, CodCliente, prezzo, numeropunti, numeroarticoli, categoria) VALUES "
                    + "('12123','88888','11111', '0.80', '2', '10', 'Farinacei'),"
                    + "('11114','88888','11111', '0.80', '2', '10', 'Farinacei'),"
                    + "('11134','77778','11111', '3.50', '2', '10', 'Inscatolati'),"
                    + "('13114','66667','11111', '18.00', '5', '4', 'Latticini'),"
                    + "('13314','66667','11111', '18.00', '5', '4', 'Latticini'),"
                    + "('13514','55556','11111', '1.20', '3', '15', 'Ortofrutticoli'),"
                    + "('14144','55556','11111', '1.20', '3', '15', 'Ortofrutticoli'),"
                    + "('12133','11112','55555', '1.50', '2', '20', 'Ortofrutticoli'),"
                    + "('15153','11112','88888', '1.50', '2', '10', 'Ortofrutticoli'),"
                    + "('16163','77778','55555', '3.50', '2', '15', 'Inscatolati'),"
                    + "('17173','77778','33333', '3.50', '2', '10', 'Inscatolati'),"
                    + "('12134','55556','88888', '1.20', '3', '25', 'Ortofrutticoli'),"
                    + "('18183','55556','44444', '1.20', '3', '20', 'Ortofrutticoli'),"
                    + "('19193','77778','66666', '3.50', '2', '30', 'Inscatolati'),"
                    + "('20204','33334','88888', '2.00', '5', '12', 'Inscatolati') "
                    + "ON CONFLICT DO NOTHING;";
            result += st.executeUpdate(sqlArticoliOrdine);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
