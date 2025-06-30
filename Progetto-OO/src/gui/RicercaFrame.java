package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RicercaFrame extends JFrame {
    private JTable searchtable;
    private JComboBox<String> categoriacb;
    private DefaultTableModel searchmodel = new DefaultTableModel();
    private Object[] searchcolonne = {"Nome", "Cognome", "Categoria", "Numero Punti"};
    private JButton backbutton;
    private JButton searchbutton;
    private JComboBox<String> punticb;

    public RicercaFrame(String title, Controller c) throws SQLException {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    public void elementi() {
        // Imposta l'icona del frame
        setIconImage(Toolkit.getDefaultToolkit().getImage(RicercaFrame.class.getResource("/Immagini/ImmIcon.png")));

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
        scrollPane.setViewportView(searchtable);

        // Pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Pannello per i combo box di filtro
        JPanel filterPanel = new JPanel();
        buttonPanel.add(filterPanel);

        categoriacb = new JComboBox<>(new String[]{"Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei", "Tutti"});
        filterPanel.add(categoriacb);

        punticb = new JComboBox<>(new String[]{"0-500", "501-1000", "1001-5000", ">5000", "Tutti"});
        filterPanel.add(punticb);

        searchbutton = new JButton("Ricerca");
        searchbutton.setBackground(new Color(30, 144, 255));
        buttonPanel.add(searchbutton);

        backbutton = new JButton("Indietro");
        backbutton.setBackground(Color.RED);
        buttonPanel.add(backbutton);

        // Configura le colonne della tabella
        searchmodel.setColumnIdentifiers(searchcolonne);
    }

    public void azioni(Controller c) throws SQLException {
        // Popola la tabella inizialmente con i dati dei clienti
        c.ClientSearch(searchmodel);

        // Gestione del click sul pulsante di ricerca
        searchbutton.addActionListener(e -> {
            String categoriaSelezionata = (String) categoriacb.getSelectedItem();
            String intervalloPunti = (String) punticb.getSelectedItem();

            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            // Filtro categoria
            if (!"Tutti".equals(categoriaSelezionata)) {
                filters.add(RowFilter.regexFilter(categoriaSelezionata, 2));
            }

            // Filtro punti basato sull'intervallo selezionato
            RowFilter<Object, Object> puntiFilter = switch (intervalloPunti) {
                case "0-500" -> RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 501, 3);
                case "501-1000" -> RowFilter.andFilter(List.of(
                        RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 500, 3),
                        RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 1001, 3)
                ));
                case "1001-5000" -> RowFilter.andFilter(List.of(
                        RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 1000, 3),
                        RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 5001, 3)
                ));
                case ">5000" -> RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 5000, 3);
                default -> null;
            };

            // Aggiunge il filtro per i punti, se applicabile
            if (puntiFilter != null) {
                filters.add(puntiFilter);
            }

            // Applica i filtri alla tabella
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(searchmodel);
            sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
            searchtable.setRowSorter(sorter);
        });

        // Gestione del click sul pulsante "Indietro"
        backbutton.addActionListener(e -> c.returnToLastFrame());
    }
}


