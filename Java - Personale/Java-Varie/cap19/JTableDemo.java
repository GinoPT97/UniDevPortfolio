package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class JTableDemo extends JFrame
{    
    public static JTable table, table_custom; // JTable

    public JTableDemo()
    {
        super("JTable DEMO");

        String columns_headers[] = {"Nome", "Cognome", "Eta'", "Data di nascita", "Citta'"};

        Object[][] data =
        {
            {"Pellegrino", "Principe", 38, "23/06/1972", "Napoli"},
            {"Mario", "Rossi", 58, "11/07/1966", "Roma"},
            {"Biagio", "Bianchi", 27, "16/01/1980", "Milano"}
        };
        
        table = new JTable(data, columns_headers); // tabella con il table model di default        
        table_custom = new JTable(new AbstractTableModel() // tabella con un table model custom
        {
            // sorgente dati
            private String col_names[] = {"Peso in KG", "Colore", "Distanza in KM", "Dato sicuro?"};
            Object[][] source_data =
            {
                {50, "Red", 256, true},
                {88, "Green", 111, false},
                {66, "Blue", 899, true}
            };

            public int getRowCount() { return source_data.length; }
            public int getColumnCount(){ return col_names.length; }
            public String getColumnName(int column) { return col_names[column]; }
            
            public Object getValueAt(int rowIndex, int columnIndex)
            {
                return source_data[rowIndex][columnIndex];
            }

            // rendi editabile solo le celle con il colore
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return columnIndex == 1;
            }

            // rendiamo aggiornabile il data model ma solo per il colore
            public void setValueAt(Object aValue, int rowIndex, int columnIndex)
            {
                if (columnIndex == 1)
                {
                    source_data[rowIndex][columnIndex] = aValue;
                    fireTableCellUpdated(rowIndex, columnIndex);
                }
            }

            // consentiamo di chiamare il corretto cell renderer
            public Class<?> getColumnClass(int columnIndex)
            {
                return getValueAt(0, columnIndex).getClass();
            }
        });
        table_custom.setAutoCreateRowSorter(true); // permette il sorter sulle colonne  

        // creo un pannello al cui interno metto una tabella
        JPanel panel_table_top = new JPanel(new BorderLayout());
        panel_table_top.add(table.getTableHeader(), BorderLayout.PAGE_START);
        panel_table_top.add(table, BorderLayout.CENTER);

        // creo un pannello al cui interno metto una tabella
        JPanel panel_table_bottom = new JPanel(new BorderLayout());
        panel_table_bottom.add(table_custom.getTableHeader(), BorderLayout.PAGE_START);
        panel_table_bottom.add(table_custom, BorderLayout.CENTER);

        // aggiungo i pannelli ad uno splitpane
        JSplitPane split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel_table_top, panel_table_bottom);
        split_pane.setDividerLocation(150);
        
        add(split_pane, BorderLayout.CENTER); // aggiungo lo split pane al container
    }

    public static void main(String args[])
    {
        JTableDemo window = new JTableDemo();

        // imposto le dimensioni della finestra
        window.setSize(650, 300);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
