package DBconfig;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConfig {
	 private Connection connection = null;

	    public DBConfig(Connection connection)
	    {
	        this.connection = connection;
	    }

	    private boolean connectionExists() {
	        return !(connection == null);
	    }

	    public int createSequence() throws ConnectionException,SQLException{
	    	int result = -1;
	    	if(connectionExists())
				try {
	    			Statement st = connection.createStatement();

	    			String sql = "CREATE SEQUENCE seqid INCREMENT BY 1 MINVALUE 1 MAXVALUE 99999 START WITH 1;";
	    			result = st.executeUpdate(sql);

	    		}catch(SQLException ex){
	                System.out.println("SQL Exception in Creation Sequence : "+ex);
	            }
			return result;
	    }

	    public int createTableprova() throws ConnectionException {
	        int result = -1;

	        if(connectionExists())
				try {
	                Statement st = connection.createStatement();

	                if (!tableExists("prove")) {
	                    String sql = "CREATE TABLE prove (" +
	                            "id VARCHAR(9) NOT NULL, " +
	                            " nome VARCHAR(255), " +
	                            " contatto VARCHAR(255), " +
	                            " corso VARCHAR(255) );";
	                    result = st.executeUpdate(sql);
	                    st.close();

	                } else
						System.out.println("Table prova already exists!");
	            }
	            catch(SQLException ex)
	            {
	                System.out.println("SQL Exception in creation table prova : "+ex);
	            }
			else
				throw new ConnectionException("A connection must exist!");

	        return result;
	    }

	    private boolean tableExists(String tbl_name) throws SQLException
	    {
	        DatabaseMetaData metadata = connection.getMetaData();
	        ResultSet tables = metadata.getTables(null, null, tbl_name, null);
	        if (tables.next())
				return true;
	        return false;

	    }
}