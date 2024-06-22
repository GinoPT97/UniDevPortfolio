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

    //Verifica l'istanza di connection
    private boolean connectionExists() {
        return !(connection == null);
    }

    //Verifica l'esistenza delle tabelle
    private boolean tableExists(String table_name) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet tables = metadata.getTables(null, null, table_name, null);
        if (tables.next()) {
			return true;
		} else {
			return false;
		}
    }

    //Verifica l'esistenza delle sequenze
    private boolean sequenceExists(String sequence_name) throws SQLException {
        Statement st = connection.createStatement();
        String sql = "SELECT c.relname FROM pg_class c WHERE c.relkind = 'S'";  //Query che ritorna il nome delle sequenze create dall'utente; fonte: https://stackoverflow.com/questions/1493262/list-all-sequences-in-a-postgres-db-8-1-with-sql
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            if (sequence_name.equals(rs.getString(1))) {

			}
                return true;
        }
        return false;
    }

    //Crea le sequenze per autogenerare le chiavi primarie di tutte le relazioni
    public int createSequences() throws ConnectionException,SQLException{
    	int result = -1;
    	if(connectionExists()) {
    		try {
    			Statement st = connection.createStatement();

    			String sql = "CREATE SEQUENCE SCodCliente INCREMENT BY 1 MINVALUE 1 MAXVALUE 99999 START WITH 1;";
    			result = st.executeUpdate(sql);

    			sql = "CREATE SEQUENCE SCodDipendente INCREMENT BY 1 MINVALUE 1 MAXVALUE 99999 START WITH 1;";
    			result = result + st.executeUpdate(sql);

    			sql = "CREATE SEQUENCE SCodProdotto INCREMENT BY 1 MINVALUE 1 MAXVALUE 99999 START WITH 1;";
    			result = result + st.executeUpdate(sql);

    			sql = "CREATE SEQUENCE SCodOrdine INCREMENT BY 1 MINVALUE 1 MAXVALUE 99999 START WITH 1;";
    			result = result + st.executeUpdate(sql);

    			sql = "CREATE SEQUENCE SCodTessera INCREMENT BY 1 MINVALUE 1 MAXVALUE 99999 START WITH 1;";
    			result = result + st.executeUpdate(sql);
    		}catch(SQLException ex){
                System.out.println("SQL Exception in Creation Sequence : "+ex);
            }
    	}
		return result;
    }

	    public int createTableCliente() throws ConnectionException {
	        int result = -1;

	        if(connectionExists())
	        {
	            try {
	                Statement st = connection.createStatement();

	                if (!tableExists("cliente")) {
	                    String sql = "CREATE TABLE cliente(\n" +
	                            " codcliente VARCHAR(5) PRIMARY KEY, CHECK(codcliente ~* '^[0-9]+$'),\n" +
	                            " nome VARCHAR(255) NOT NULL CHECK(nome ~* '^[A-Za-z ]+$'),\n" +
	                            " cognome VARCHAR(255) NOT NULL CHECK(cognome ~* '^[A-Za-z ]+$'),\n" +
	                            " codicefiscale CHAR(16) NOT NULL CHECK(codicefiscale ~* '^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$'),\n " +
	                            " indirizzo VARCHAR(255) NOT NULL,\n" +
	                            " telefono VARCHAR(20) CHECK(telefono ~* '^[0-9+ ]+$'),\n" +
	                            " email VARCHAR(255) NOT NULL CHECK(email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$')\n " +
	                            " );";

	                    result = st.executeUpdate(sql);
	                    st.close();

	                } else {
						System.out.println("Table Cliente already exists!");
					}
	            }
	            catch(SQLException ex)
	            {
	                System.out.println("SQL Exception in creation table Cliente : "+ex);
	            }
	        } else {
				throw new ConnectionException("A connection must exist!");
			}

	        return result;
	    }

	    public int createTableDipendente() throws ConnectionException {
	        int result = -1;

	        if(connectionExists())
	        {
	            try {
	                Statement st = connection.createStatement();

	                if (!tableExists("dipendente")) {
	                    String sql = "CREATE TABLE dipendente (\n" +
	                            "coddipendente VARCHAR(5) PRIMARY KEY, CHECK(coddipendente ~* '^[0-9]+$'),\n" +
	                            " nome VARCHAR(255) NOT NULL CHECK(nome ~* '^[A-Za-z ]+$'),\n" +
	                            " cognome VARCHAR(255) NOT NULL CHECK(cognome ~* '^[A-Za-z ]+$'),\n" +
	                            " codicefiscale CHAR(16) NOT NULL CHECK(codicefiscale ~* '^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$'),\n " +
	                            " indirizzo VARCHAR(255) NOT NULL,\n" +
	                            " telefono VARCHAR(20) CHECK(telefono ~* '^[0-9+ ]+$'),\n" +
	                            " email VARCHAR(255) NOT NULL CHECK(email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$')\n " +
	                            " );";

	                    result = st.executeUpdate(sql);
	                    st.close();

	                } else {
						System.out.println("Table Dipendente already exists!");
					}
	            }
	            catch(SQLException ex)
	            {
	                System.out.println("SQL Exception in creation table Dipendente : "+ex);
	            }
	        } else {
				throw new ConnectionException("A connection must exist!");
			}

	        return result;
	    }

	    public int createTableTessera() throws ConnectionException {
	        int result = -1;

	        if(connectionExists())
	        {
	            try {
	                Statement st = connection.createStatement();

	                if (!tableExists("tessera")) {
	                    String sql = "CREATE TABLE tessera(\n" +
	                            " codtessera VARCHAR(5) PRIMARY KEY, CHECK(codtessera ~* '^[0-9]+$'),\n" +
	                            " numeropunti real NOT NULL DEFAULT 0.00,\n" +
	                            " codcliente VARCHAR(5) NOT NULL UNIQUE CHECK(CodCliente ~* '^[0-9]+$'),\n" +
	                            " CONSTRAINT TesseraFK FOREIGN KEY(CodCliente) \n " +
	                            " REFERENCES CLIENTE(codcliente) \n" +
	                            " ON UPDATE CASCADE \n" +
	                            " ON DELETE CASCADE \n " +
	                            " );";

	                    result = st.executeUpdate(sql);
	                    st.close();

	                } else {
						System.out.println("Table Tessera already exists!");
					}
	            }
	            catch(SQLException ex)
	            {
	                System.out.println("SQL Exception in creation table Tessera : "+ex);
	            }
	        } else {
				throw new ConnectionException("A connection must exist!");
			}

	        return result;
	    }

	    public int createTableProdotto() throws ConnectionException {
	        int result = -1;

	        if(connectionExists())
	        {
	            try {
	                Statement st = connection.createStatement();

	                if (!tableExists("prodotto")) {
	                    String sql = "CREATE TABLE prodotto(\n" +
	                            " codprodotto VARCHAR(5) PRIMARY KEY, CHECK(CodProdotto ~* '^[0-9]+$'),\n" +
	                            " nome VARCHAR(255) NOT NULL, CHECK(Nome ~* '^[A-Za-z ]+$'),\n" +
	                            " descrizione VARCHAR(500), \n" +
	                            " prezzo NUMERIC DEFAULT 0.00, \n " +
	                            " luogoprovenienza VARCHAR(255), \n" +
	                            " dataraccolta DATE,\n " +
	                            " datamungitura DATE, \n " +
	                            " glutine BOOLEAN, \n " +
	                            " datascadenza DATE, \n " +
	                            " categoria TIPOLOGIA,\n" +
	                            " scorta INT , CHECK (scorta>=0),\n" +
	                            " CONSTRAINT categoria CHECK (((categoria ='Ortofrutticoli') AND (dataraccolta IS NOT NULL AND datamungitura IS NULL AND datascadenza IS NULL AND glutine IS NULL)) OR" +
	                            " ((categoria ='Latticini') AND (dataraccolta IS NULL AND datamungitura IS NOT NULL AND datascadenza IS NULL AND glutine IS NULL)) OR" +
	                            " ((categoria ='Inscatolati') AND (dataraccolta IS NULL AND datamungitura IS NULL AND datascadenza IS NOT NULL AND glutine IS NULL)) OR"+
	                            " ((categoria ='Farinacei') AND (dataraccolta IS NULL AND datamungitura IS NULL AND datascadenza IS NULL AND glutine IS NOT NULL)))"+
	                            " );";
	                    result = st.executeUpdate(sql);
	                    st.close();

	                } else {
						System.out.println("Table Prodotto already exists!");
					}
	            }
	            catch(SQLException ex)
	            {
	                System.out.println("SQL Exception in creation table Prodotto : "+ex);
	            }
	        } else {
				throw new ConnectionException("A connection must exist!");
			}

	        return result;
	    }

	    public int createTableOrdine() throws ConnectionException {
	        int result = -1;

	        if(connectionExists())
	        {
	            try {
	                Statement st = connection.createStatement();

	                if (!tableExists("ordine")) {
	                    String sql = "CREATE TABLE ordine(\n" +
	                    		" codordine VARCHAR(5) NOT NULL, \n" +
	                    	    " prezzototale real NOT NULL DEFAULT 0.00 CHECK (prezzototale >= 0), \n" +
	                    	    " dataacquisto date NOT NULL, \n" +
	                    	    " codcliente VARCHAR(5) NOT NULL CHECK (codcliente ~* '^[0-9]+$'), \n" +
	                    	    " coddipendente VARCHAR(5) NOT NULL CHECK (coddipendente ~* '^[0-9]+$'), \n" +
	                    	    " CONSTRAINT ordinepk PRIMARY KEY (codordine), \n" +
	                    	    " CONSTRAINT ordineclientefk FOREIGN KEY (codcliente) REFERENCES cliente (codcliente) ON UPDATE CASCADE ON DELETE NO ACTION, \n " +
	                    	    " CONSTRAINT ordinedipendentefk FOREIGN KEY (coddipendente) REFERENCES dipendente (coddipendente) ON UPDATE CASCADE ON DELETE NO ACTION\n " +
	                            " );";

	                    result = st.executeUpdate(sql);
	                    st.close();

	                } else {
						System.out.println("Table Ordine already exists!");
					}
	            }
	            catch(SQLException ex)
	            {
	                System.out.println("SQL Exception in creation table Ordine : "+ex);
	            }
	        } else {
				throw new ConnectionException("A connection must exist!");
			}

	        return result;
	    }

		public int createTableArticoliOrdine() throws ConnectionException
		{
			int result = -1;

			if (connectionExists()) {
				try {
					Statement st = connection.createStatement();
					if (!tableExists("ARTICOLIORDINE")) {
						String sql = "CREATE TABLE ARTICOLIORDINE (\n"+
								"CodOrdine VARCHAR(5) NOT NULL CHECK(CodOrdine ~* '^[0-9]+$'),\n"+
								"CodProdotto VARCHAR(5) NOT NULL CHECK(CodProdotto ~* '^[0-9]+$'),\n"+
								"CodCliente VARCHAR(5) PRIMARY KEY, CHECK(codcliente ~* '^[0-9]+$'),\n" +
								"Prezzo NUMERIC NOT NULL DEFAULT 0.00, CHECK(Prezzo >= 0.00),\n"+
								"NumeroPunti NUMERIC NOT NULL DEFAULT 0.00, CHECK(Prezzo >= 0.00), \n"+
								"NumeroArticoli INT NOT NULL,\n"+
								"Categoria TIPOLOGIA,\n"+
								"CONSTRAINT ArticoliordineClienteFK FOREIGN KEY(CodCliente) REFERENCES CLIENTE(CodCliente),\n"+
								"CONSTRAINT ArtocoliordineProdottoFK FOREIGN KEY(CodProdotto) REFERENCES PRODOTTO(CodProdotto),\n"+
								"CONSTRAINT ArtocoliordineOrdineFK FOREIGN KEY(CodOrdine) REFERENCES ORDINE(CodOrdine)\n"+
								" );";
						result = st.executeUpdate(sql);
						st.close();
					} else {
						System.out.println("Table 'ARTICOLIORDINE' already exists!");
					}
				}
				catch (SQLException ex) {
					System.out.println("SQL Exception found in creation table 'ARTICOLIORDINE': "+ex);
				}
			} else {
				throw new ConnectionException("A connection must exists!");
			}
			return result;
		}

	    public int createTipologie() throws ConnectionException {
	        int result = -1;

	        if(connectionExists())
	        {
	            try {
	                Statement st = connection.createStatement();
	                    String sql = "CREATE TYPE TIPOLOGIA AS ENUM('Ortofrutticoli','Latticini','Inscatolati','Farinacei')";
	                    result = st.executeUpdate(sql);
	            }
	            catch(SQLException ex)
	            {
	                System.out.println("SQL Exception in creation type tipologia: "+ex);
	            }
	        } else {
				throw new ConnectionException("A connection must exist!");
			}

	        return result;
	    }

	    public int populateDatabase() throws ConnectionException {
	        int result = -1;

	        if (connectionExists()) {
	            try {
	                Statement st = connection.createStatement();

	                // Popola la tabella Cliente
	                String sql = "INSERT INTO Cliente VALUES ('11111','aldo','marzante','BBBBBB11B11B111B', 'Via Don Matteo','1234567890','aldo@arte.it');" +
	                             "INSERT INTO Cliente VALUES ('22222','luca','benson','AAAAAA22A22A222A', 'Via Don Corleone','1234567890','luca@arte.it');" +
	                             "INSERT INTO Cliente VALUES ('33333','mario','sarni','CCCCCC33C33C333C','Via San giovanni','1234567890','mario@arte.it');" +
	                             "INSERT INTO Cliente VALUES ('77777','alessio','sassi','DDDDDD44D44D444D','Via cremoni','1234567890','alessio@arte.it');" +
	                             "INSERT INTO Cliente VALUES ('44455','giorgio','rossi','WWWWWW55W55W555W', 'Via Don Carlo','1234567890','giorgio@arte.it');" +
	                             "INSERT INTO Cliente VALUES ('55566','paolo','verdi','QQQQQQ66Q66Q666Q', 'Via Don Alberto','1234567890','paolo@arte.it');" +
	                             "INSERT INTO Cliente VALUES ('66677','simone','bianchi','UUUUUU77U77U777U','Via Don Giuseppe','1234567890','simone@arte.it');" +
	                             "INSERT INTO Cliente VALUES ('88899','enrico','gialli','VVVVVV99V99V999V','Via Don Mario','1234567890','enrico@arte.it');";
	                result = st.executeUpdate(sql);

	                // Popola la tabella Dipendente
	                sql = "INSERT INTO Dipendente VALUES ('89899','dario','forte','FFFFFF11F11F111F','via andromeda','1234567890','dario@arte.it');" +
	                      "INSERT INTO Dipendente VALUES ('79799','sandro','romano','LLLLLL22L22L222L','via omega','1234567890','sandro@arte.it');" +
	                      "INSERT INTO Dipendente VALUES ('34345','giulio','cesare','PPPPPP88P88P888P','via roma','1234567890','giulio@arte.it');" +
	                      "INSERT INTO Dipendente VALUES ('11111','mario','rossi','YYYYYY11Y11Y111Y','via parma','1234567890','mario@arte.it');" +
	                      "INSERT INTO Dipendente VALUES ('22222','andrea','verdi','HHHHHH22H22H222H','via milano','1234567890','andrea@arte.it');" +
	                      "INSERT INTO Dipendente VALUES ('33333','giuseppe','bianchi','KKKKKK88K88K888K','via torino','1234567890','giuseppe@arte.it');" +
	                      "INSERT INTO Dipendente VALUES ('44444','marco','gialli','GGGLLN80A01H501P','via napoli','1234567890','marco@arte.it');";
	                result += st.executeUpdate(sql);

	                // Popola la tabella Tessera
	                sql = "INSERT INTO Tessera VALUES ('55555','20','11111');" +
	                      "INSERT INTO Tessera VALUES ('44444','30','22222');" +
	                      "INSERT INTO Tessera VALUES ('66666','100','33333');" +
	                      "INSERT INTO Tessera VALUES ('88888','0','77777');" +
	                      "INSERT INTO Tessera VALUES ('77777','50','44455');" +
	                      "INSERT INTO Tessera VALUES ('66667','80','55566');" +
	                      "INSERT INTO Tessera VALUES ('55551','10','66677');" +
	                      "INSERT INTO Tessera VALUES ('33333','150','88899');";
	                result += st.executeUpdate(sql);

	                // Popola la tabella Ordine
	                sql = "INSERT INTO Ordine VALUES('12122','57','12/02/2001','11111','89899');" +
	                      "INSERT INTO Ordine VALUES('11112','45','22/03/2010','11111','89899');" +
	                      "INSERT INTO Ordine VALUES('11132','47','02/07/2011','11111','89899');" +
	                      "INSERT INTO Ordine VALUES('13112','105','22/11/2017','11111','79799');" +
	                      "INSERT INTO Ordine VALUES('13312','185','02/10/2007','11111','79799');" +
	                      "INSERT INTO Ordine VALUES('13512','15','09/12/2004','11111','34345');" +
	                      "INSERT INTO Ordine VALUES('14142','20','08/11/2009','55566','11111');" +
	                      "INSERT INTO Ordine VALUES('12131','55','12/03/2015','66677','22222');" +
	                      "INSERT INTO Ordine VALUES('12132','120','17/06/2013','88899','33333');";
	                result += st.executeUpdate(sql);

	                // Popola la tabella Prodotto
	                sql = "INSERT INTO Prodotto VALUES ('11111', 'Mela Rossa', 'Mela italiana rossa', 1.50, 'Italia', '2022-07-15', NULL, NULL, NULL, 'Ortofrutticoli', 100);" +
	                      "INSERT INTO Prodotto VALUES ('22222', 'Formaggio Parmigiano', 'Formaggio Parmigiano Reggiano', 15.00, 'Italia', NULL, '2023-06-01', NULL, NULL, 'Latticini', 50);" +
	                      "INSERT INTO Prodotto VALUES ('33333', 'Pomodori in scatola', 'Pomodori pelati in scatola', 2.00, 'Italia', NULL, NULL, NULL, '2024-12-31', 'Inscatolati', 200);" +
	                      "INSERT INTO Prodotto VALUES ('44444', 'Spaghetti', 'Spaghetti di grano duro', 1.20, 'Italia', NULL, NULL, TRUE, NULL, 'Farinacei', 300);" +
	                      "INSERT INTO Prodotto VALUES ('55555', 'Arance', 'Arance siciliane', 1.20, 'Italia', '2022-07-15', NULL, NULL, NULL, 'Ortofrutticoli', 150);" +
	                      "INSERT INTO Prodotto VALUES ('66666', 'Parmigiano', 'Parmigiano Reggiano DOP', 18.00, 'Italia', NULL, '2023-06-01', NULL, NULL, 'Latticini', 70);" +
	                      "INSERT INTO Prodotto VALUES ('77777', 'Tonno in scatola', 'Tonno al naturale in scatola', 3.50, 'Italia', NULL, NULL, NULL, '2024-12-31', 'Inscatolati', 100);" +
	                      "INSERT INTO Prodotto VALUES ('88888', 'Farina', 'Farina di grano tenero tipo \"00\"', 0.80, 'Italia', NULL, NULL, FALSE, NULL, 'Farinacei', 200);";
	                result += st.executeUpdate(sql);

	                // Popola la tabella Articoliordine
	                sql = "INSERT INTO ArticoliOrdine VALUES('12122', '44444', '77777', '1.20', '3.00', '2', 'Farinacei');" +
	                      "INSERT INTO ArticoliOrdine VALUES('11112', '33333', '77777', '2.00', '6.00', '3', 'Inscatolati');" +
	                      "INSERT INTO ArticoliOrdine VALUES('11132', '77777', '77777', '1.50', '3.50', '2', 'Ortofrutticoli');" +
	                      "INSERT INTO ArticoliOrdine VALUES('13112', '22222', '55566', '15.00', '30.00', '2', 'Latticini');" +
	                      "INSERT INTO ArticoliOrdine VALUES('13312', '33333', '44455', '2.00', '4.50', '3', 'Inscatolati');" +
	                      "INSERT INTO ArticoliOrdine VALUES('13512', '22222', '66677', '15.00', '30.00', '2', 'Latticini');" +
	                      "INSERT INTO ArticoliOrdine VALUES('12131', '33333', '22222', '2.00', '4.00', '2', 'Inscatolati');" +
	                      "INSERT INTO ArticoliOrdine VALUES('12132', '44444', '11111', '1.20', '6.00', '5', 'Farinacei');";
	                result += st.executeUpdate(sql);

	                st.close();
	            } catch(SQLException ex)
	            {
	                System.out.println("SQL Exception in creation type tipologia: "+ex);
	            }
	        } else {
				throw new ConnectionException("A connection must exist!");
			}

	        return result;
	    }
	}



