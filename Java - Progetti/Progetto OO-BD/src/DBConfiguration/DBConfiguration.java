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

    //Verifica l'esistenza dei trigger
    private boolean triggerExists(String trigger_name) throws SQLException {
        Statement st = connection.createStatement();
        String sql = "SELECT trigger_name FROM information_schema.triggers";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            if (trigger_name.equals(rs.getString(1))) {
				return true;
			}
        }
        return false;
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
	                            "CONSTRAINT ArtocoliordineProdottoFK FOREIGN KEY(CodProdotto) REFERENCES PRODOTTO(CodProdotto)\n"+
	                            "CONSTRAINT ArtocoliordineOrdineFK FOREIGN KEY(CodOrdine) REFERENCES PRODOTTO(CodOrdine)\\n"+
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
	}