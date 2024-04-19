package com.pellegrinoprincipe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JTextArea;

public class DBHelper_REV_1
{
    // tipo di RDBMS
    public static String dbtype;
    // output error field
    public static JTextArea output;
    // oggetto Connection
    private static Connection conn;
    
    private static ResultSet rs; // oggetto ResultSet
    private static Statement st; // oggetto Statement
    
    private static String drivers_name[] =
    {
        "com.mysql.jdbc.Driver", // MySQL 5.5
        "oracle.jdbc.OracleDriver", // Oracle 11g
        "com.ibm.db2.jcc.DB2Driver", // DB2 9 Universal
        "com.microsoft.sqlserver.jdbc.SQLServerDriver", // SQL Server 2008
    };
    private static String url_format[] =
    {
        "jdbc:mysql://",
        "jdbc:oracle:thin:@",
        "jdbc:db2://",
        "jdbc:sqlserver://"
    };

    public static void loadDriver()
    {
        String driver = "";
        switch (dbtype)
        {
            case "MySQL":
                driver = drivers_name[0];
                break;
            case "Oracle":
                driver = drivers_name[1];
                break;
            case "DB2":
                driver = drivers_name[2];
            case "SQL Server":
                driver = drivers_name[3];
            default:
                break;
        }

        try
        {
            Class.forName(dbtype);
        }
        catch (ClassNotFoundException ex) { output.setText(ex.getMessage()); }
    }

    public static void doConnection(String url, String port, String dbname, String u_name, String pwd)
    {
        String complete_url = "";
        switch (dbtype)
        {
            case "MySQL":
                complete_url = url_format[0] + url + ":" + port + "/" + dbname;
                break;
            case "Oracle":
                complete_url = url_format[1] + url + ":" + port + ":" + dbname;
            case "DB2":
                complete_url = url_format[2] + url + ":" + port + "/" + dbname;
            case "SQL Server":
                complete_url = url_format[3] + url + ":" + port + ";" + "databaseName=" + dbname;
            default:
                break;
        }

        try
        {
            conn = DriverManager.getConnection(complete_url, u_name, pwd);

            output.setText("Connessione effettuata con successo!!!");
        }
        catch (SQLException ex) { output.setText(ex.getMessage()); }
    }

    public static void closeConnection()
    {        
        try // chiudo esplicitamente la connessione al database
        {
            if (conn != null)
            {
                conn.close();
                output.setText("Disconnessione effettuata con successo!!!");
            }
        }
        catch (SQLException ex) { output.setText(ex.getMessage()); }
    }

    public static ResultSet doQuery(String sql)
    {
        if (conn != null)
        {
            try  // creo una statement ed un resultset
            {
                st = conn.createStatement();
                rs = st.executeQuery(sql);
            }
            catch (SQLException ex) { output.setText(ex.getMessage()); }
            
            return rs;
        }
        else
        {
            output.setText("Nessuna connessione attiva per eseguire una query!");
            return null;
        }
    }
}
