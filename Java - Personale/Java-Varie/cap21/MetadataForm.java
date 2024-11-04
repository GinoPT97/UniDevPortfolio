package com.pellegrinoprincipe;

import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.ParameterMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JComboBox;

public class MetadataForm extends javax.swing.JFrame
{
    private CallableStatement cs;
    // oggetti per i metadati
    private DatabaseMetaData dbmd;
    private ResultSetMetaData rsmd;
    private ParameterMetaData parmd;

    public MetadataForm()
    {
        initComponents();

        // effettuo la connessione al databse;
        connectionToDatabase();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always  by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cb_info_db = new javax.swing.JComboBox();
        panel_for_table = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cb_info_col = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ta_result = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cb_info_params = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MetadataForm");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Info database:"));

        jLabel1.setText("Informazione:");

        cb_info_db.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "nome e versione del database", "nome e versione del driver JDBC", "keywords SQL supportate", "livello di isolamento di  default per le transazioni", "numero massimo di caratteri utilizzabili per denominare un catalogo", "numero massimo di caratteri utilizzabili per denominare una tabella", "numero massimo di caratteri utilizzabili per denominare una colonna", "numero massimo di connessioni simultanee accettate", "supporta le outer joins ?", "supporta le stored procedures ?", "supporta le transazioni ?" }));
        cb_info_db.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_info_dbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_info_db, 0, 499, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cb_info_db, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_for_table.setBorder(javax.swing.BorderFactory.createTitledBorder("Info colonne:"));

        jLabel2.setText("Informazione:");

        cb_info_col.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "numero di colonne", "nome delle colonne", "tipi di dato delle colonne", "nome della tabella di appartenenza delle colonne" }));
        cb_info_col.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_info_colActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_for_tableLayout = new javax.swing.GroupLayout(panel_for_table);
        panel_for_table.setLayout(panel_for_tableLayout);
        panel_for_tableLayout.setHorizontalGroup(
            panel_for_tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_for_tableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_info_col, 0, 499, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_for_tableLayout.setVerticalGroup(
            panel_for_tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_for_tableLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_for_tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cb_info_col, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Output:"));

        ta_result.setColumns(20);
        ta_result.setRows(5);
        jScrollPane2.setViewportView(ta_result);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Info parametri"));

        jLabel3.setText("Informazione:");

        cb_info_params.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "numero dei parametri", "tipi di dato dei parametri", "direzionalità dei parametri" }));
        cb_info_params.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_info_paramsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_info_params, 0, 499, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cb_info_params, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_for_table, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_for_table, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void cb_info_dbActionPerformed(java.awt.event.ActionEvent evt)                                           
    {                                               
        // ottengo quale item è stato selezionato
        JComboBox cb = (JComboBox) evt.getSource();
        int ix = cb.getSelectedIndex();
        
        dbmd = DBHelper_REV_5.getDatabaseMetadata(); // DatabaseMetaData
        String info_db = "";

        try
        {            
            switch (ix) // info sul database
            {
                case 0:
                    info_db = dbmd.getDatabaseProductName() + " Ver. " + dbmd.getDatabaseProductVersion();
                    break;
                case 1:
                    info_db = dbmd.getDriverName() + " Ver. " + dbmd.getDriverVersion();
                    break;
                case 2:
                    info_db = dbmd.getSQLKeywords().replace(",", "\n");
                case 3:
                    String t_levels[] =
                    {
                        "TRANSACTION_NONE",
                        "TRANSACTION_READ_UNCOMMITTED",
                        "TRANSACTION_READ_COMMITTED",
                        null,
                        "TRANSACTION_REPEATABLE_READ",
                        null, null, null,
                        "TRANSACTION_SERIALIZABLE"
                    };

                    info_db = t_levels[dbmd.getDefaultTransactionIsolation()];
                    break;
                case 4:
                    info_db = dbmd.getMaxCatalogNameLength() + "";
                    break;
                case 5:
                    info_db = dbmd.getMaxTableNameLength() + "";
                    break;
                case 6:
                    info_db = dbmd.getMaxColumnNameLength() + "";
                    break;
                case 7:
                    info_db = dbmd.getMaxConnections() + "";
                    break;
                case 8:
                    info_db = Boolean.toString(dbmd.supportsOuterJoins());
                    break;
                case 9:
                    info_db = Boolean.toString(dbmd.supportsStoredProcedures());
                    break;
                case 10:
                    info_db = Boolean.toString(dbmd.supportsTransactions());
                    break;
                default:
                    break;
            }

            ta_result.setText(info_db);
        }
        catch (SQLException ex) { ta_result.setText(ex.getMessage()); }
    }                                          

    private void cb_info_colActionPerformed(java.awt.event.ActionEvent evt)                                            
    {                                                
        JComboBox cb = (JComboBox) evt.getSource();
        int ix = cb.getSelectedIndex();

        String query = "SELECT * FROM genere";
        ResultSet rs = DBHelper_REV_5.doQuery(query, false);

        String info_col = "";

        try
        {            
            rsmd = rs.getMetaData(); // ResultSetMetaData

            switch (ix)
            {
                case 0:
                    info_col = rsmd.getColumnCount() + "";
                    break;
                case 1:
                    for (int i = 1; i <= rsmd.getColumnCount(); i++)
                    {
                        info_col += rsmd.getColumnName(i) + "\n";
                    }
                    break;
                case 2:
                    for (int i = 1; i <= rsmd.getColumnCount(); i++)
                    {
                        info_col += rsmd.getColumnTypeName(i) + "\n";
                    }
                    break;
                case 3:
                    info_col = rsmd.getTableName(1);
                    break;
                default:
                    break;
            }

            ta_result.setText(info_col);
        }
        catch (SQLException ex) { ta_result.setText(ex.getMessage()); }
    }                                           

    private void cb_info_paramsActionPerformed(java.awt.event.ActionEvent evt)                                               
    {                                                   
        JComboBox cb = (JComboBox) evt.getSource();
        int ix = cb.getSelectedIndex();

        // ottengo un oggetto CallableStatement
        String procedure = "{CALL showConnectedUser(?, ?)}";
        cs = DBHelper_REV_5.getCallableStatement(procedure, false);

        String info_params = "";

        try
        {            
            parmd = cs.getParameterMetaData(); // ParameterMetaData

            switch (ix)
            {
                case 0:
                    info_params = parmd.getParameterCount() + "";
                    break;
                case 1:
                    for (int i = 1; i <= parmd.getParameterCount(); i++)
                    {
                        info_params += parmd.getParameterTypeName(i) + "\n";
                    }
                    break;
                case 2:
                    String p_mode[] = {"UNKNOWN", "IN" ,"INOUT", null, "OUT"};
                    
                    for (int i = 1; i <= parmd.getParameterCount(); i++)
                    {
                        info_params += p_mode[parmd.getParameterMode(i)] + "\n";
                    }
                    break;
                default:
                    break;
            }

            ta_result.setText(info_params);
        }
        catch (SQLException ex) { ta_result.setText(ex.getMessage()); }
    }                                              

    private void connectionToDatabase()
    {
        DBHelper_REV_5.dbtype = "MySQL";
        DBHelper_REV_5.output = ta_result;
        DBHelper_REV_5.loadDriver();
        DBHelper_REV_5.doConnection("localhost", "3306", "articoli", "java7", "java7");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new MetadataForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify
    private javax.swing.JComboBox cb_info_col;
    private javax.swing.JComboBox cb_info_db;
    private javax.swing.JComboBox cb_info_params;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panel_for_table;
    private javax.swing.JTextArea ta_result;
    // End of variables declaration
}
