package gui;

import controller.Controller;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class RicercaFrame extends JFrame {
    private static final String TUTTI = "Tutti";
    
    // Array delle categorie e punti per evitare ridondanza
    private static final String[] CATEGORIE = {"FRUTTA", "VERDURA", "FARINACEI", "LATTICINI", "UOVA", "CONFEZIONATI", TUTTI};
    private static final String[] OPZIONI_PUNTI = {"0-500", "501-1000", "1001-5000", ">5000", TUTTI};
    
    private JTable searchtable;
    private JComboBox<String> categoriacb;
    private final DefaultTableModel searchmodel = new DefaultTableModel();
    private static final Object[] searchcolonne = {"Cod Cliente", "Nome", "Cognome", "Categoria", "Punti Categoria", "Spesa Totale", "Ordini Categoria"};
    private JButton backbutton;
    private JButton searchbutton;
    private JComboBox<String> punticb;

    public RicercaFrame(String title, Controller c) throws SQLException {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    private void elementi() {
        // Imposta l'icona del frame

        // Configura le impostazioni di base del frame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 904, 433);
        setLocationRelativeTo(null);

        // Inizializza e configura il pannello principale
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // Pannello del titolo
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(100, 149, 237));
        contentPane.add(titlePanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("Ricerca Clienti");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlePanel.add(titleLabel);

        // Pannello della tabella
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.X_AXIS));
        contentPane.add(tablePanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane();
        tablePanel.add(scrollPane);

        searchtable = new JTable();
        searchtable.setModel(searchmodel);
        searchtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchtable.setAutoCreateRowSorter(true);
        searchtable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) c.setBackground(row % 2 == 0 ? java.awt.Color.WHITE : new java.awt.Color(240,240,240));
                return c;
            }
        });
        searchtable.getTableHeader().setToolTipText("Clicca per ordinare la colonna");
        scrollPane.setViewportView(searchtable);

        // Pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Pannello per i combo box di filtro
        JPanel filterPanel = new JPanel();
        buttonPanel.add(filterPanel);

        categoriacb = new JComboBox<>(CATEGORIE);
        filterPanel.add(categoriacb);

        punticb = new JComboBox<>(OPZIONI_PUNTI);
        filterPanel.add(punticb);

        searchbutton = new JButton("Ricerca", gui.IconUtils.getIconForText("Ricerca", new Color(52, 152, 219)));
        searchbutton.setBackground(new Color(30, 144, 255));
        buttonPanel.add(searchbutton);

        backbutton = new JButton("Indietro", gui.IconUtils.getIconForText("Indietro", Color.RED));
        backbutton.setBackground(Color.RED);
        buttonPanel.add(backbutton);

        // Configura le colonne della tabella
        searchmodel.setColumnIdentifiers(searchcolonne);
    }

    private void azioni(Controller c) throws SQLException {
        // Popola la tabella inizialmente con i dati dei clienti
        c.ClientSearch(searchmodel);

        // Gestione del click sul pulsante di ricerca
        searchbutton.addActionListener(e -> applicaFiltri());

        // Gestione del click sul pulsante "Indietro"
        backbutton.addActionListener(e -> c.returnToLastFrame());
    }
    
    // Applica i filtri alla tabella in base alle selezioni dei ComboBox
    private void applicaFiltri() {
        String categoriaSelezionata = (String) categoriacb.getSelectedItem();
        String intervalloPunti = (String) punticb.getSelectedItem();

        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        // Filtro categoria (colonna 3)
        if (!TUTTI.equals(categoriaSelezionata)) {
            filters.add(RowFilter.regexFilter(categoriaSelezionata, 3));
        }

        // Filtro punti basato sull'intervallo selezionato (colonna 4 - Punti Categoria)
        if (!TUTTI.equals(intervalloPunti)) {
            RowFilter<Object, Object> puntiFilter = creaPuntiFilter(intervalloPunti);
            if (puntiFilter != null) {
                filters.add(puntiFilter);
            }
        }

        // Applica i filtri alla tabella
        try {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(searchmodel);
            if (!filters.isEmpty()) {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            } else {
                sorter.setRowFilter(null);
            }
            searchtable.setRowSorter(sorter);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Errore nell'applicazione dei filtri: " + ex.getMessage(), 
                "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Crea il filtro per i punti in base all'intervallo selezionato
    private RowFilter<Object, Object> creaPuntiFilter(String intervalloPunti) {
        return switch (intervalloPunti) {
            case "0-500" -> RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 501, 4);
            case "501-1000" -> RowFilter.andFilter(List.of(
                    RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 500, 4),
                    RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 1001, 4)
            ));
            case "1001-5000" -> RowFilter.andFilter(List.of(
                    RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 1000, 4),
                    RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 5001, 4)
            ));
            case ">5000" -> RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 5000, 4);
            default -> null;
        };
    }
}
