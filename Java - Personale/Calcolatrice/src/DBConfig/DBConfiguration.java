package DBConfig;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConfiguration {
		private Connection connection;

	    public DBConfiguration(Connection connection)
	    {
	        this.connection = connection;
	    }

	    public DBConfiguration()
	    {
	        connection = null;
	    }

	    private boolean connectionExists() {
	        return !(connection == null);
	    }

	    private boolean tableExists(String tbl_name) throws SQLException
	    {
	        DatabaseMetaData metadata = connection.getMetaData();
	        ResultSet tables = metadata.getTables(null, null, tbl_name, null);
	        if (tables.next())
	            return true;
	        return false;
	    }
	    
	    public int createTableCalcolatrice() throws ConnectionException {
	        int result = -1;

	        if(connectionExists())
	        {
	            try {
	                Statement st = connection.createStatement();

	                if (!tableExists("calcolatrice")) {
	                    String sql = "CREATE TABLE calcolatrice(\n" +
	                            " operando1 NUMERIC CHECK(operando1 >= 0),\n" +
	                            " operando2 NUMERIC CHECK(operando1 >= 0),\n" +
	                            " operatore VARCHAR(50),\n" +
	                            " risultato NUMERIC CHECK(operando1 >= 0)\n " +
	                            " );";

	                    result = st.executeUpdate(sql);
	                    st.close();

	                } else
	                    System.out.println("Table Calcolatrice already exists!");
	            }
	            catch(SQLException ex)
	            {
	                System.out.println("SQL Exception in creation table calcolatrice : "+ex);
	            }
	        }
	        else
	            throw new ConnectionException("A connection must exist!");

	        return result;
	    }
}
