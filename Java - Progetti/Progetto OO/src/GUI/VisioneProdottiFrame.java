package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import Model.Prodotto;

public class VisioneProdottiFrame extends JFrame {
    private JPanel contentPane;
    private JTable table;
    public Prodotto pe;
    private JPanel titlepanel;
    private JPanel buttonpanel;
    private JButton backbutton;
    private JButton updatebutton;
    private JButton addbutton;
    private JLabel titlelabel;
    private JButton searchbutton;
    private JTextField searchtf;

    public void elementi(Controller c) throws SQLException {
        // Imposta le caratteristiche di base della finestra
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 500);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit()
                .getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

        // Imposta il contenitore principale e il layout
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // Crea e aggiungi la JScrollPane per la tabella
        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Crea e configura la JTable
        table = new JTable();
        table.setModel(c.prodModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(table);

        // Crea e configura il pannello del titolo
        titlepanel = new JPanel();
        titlepanel.setBackground(new Color(128, 0, 0));
        contentPane.add(titlepanel, BorderLayout.NORTH);

        titlelabel = new JLabel("Amministrazione Prodotti");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE); // Migliora la visibilità del testo
        titlepanel.add(titlelabel);

        // Crea e configura il pannello dei pulsanti
        buttonpanel = new JPanel();
        contentPane.add(buttonpanel, BorderLayout.SOUTH);

        searchtf = new JTextField(10);
        buttonpanel.add(searchtf);

        searchbutton = new JButton("Cerca");
        searchbutton.setBackground(new Color(46, 139, 87));
        searchbutton.setForeground(Color.WHITE); // Migliora la visibilità del testo
        buttonpanel.add(searchbutton);

        addbutton = new JButton("Aggiungi");
        addbutton.setBackground(Color.GREEN);
        addbutton.setForeground(Color.WHITE); // Migliora la visibilità del testo
        buttonpanel.add(addbutton);

        updatebutton = new JButton("Modifica");
        updatebutton.setBackground(new Color(70, 130, 180));
        updatebutton.setForeground(Color.WHITE); // Migliora la visibilità del testo
        buttonpanel.add(updatebutton);

        backbutton = new JButton("Indietro");
        backbutton.setBackground(Color.RED);
        backbutton.setForeground(Color.WHITE); // Migliora la visibilità del testo
        buttonpanel.add(backbutton);
    }

    public void azioni(Controller c) throws SQLException {
        // Popola la tabella con i prodotti all'avvio
        c.allProdotti();

        // Gestione della ricerca
        searchbutton.addActionListener(e -> {
            String query = searchtf.getText().trim().toLowerCase();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(c.prodModel);
            table.setRowSorter(sorter);
            if (query.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                try {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
                } catch (PatternSyntaxException ex) {
                    JOptionPane.showMessageDialog(null, "Errore nella sintassi dell'espressione regolare: " + ex.getMessage(),
                                                  "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Gestione del pulsante Aggiungi
        addbutton.addActionListener(e -> c.visAndElem(4, 1)); // Richiama il metodo per aggiungere un prodotto

        // Gestione del pulsante Modifica
        updatebutton.addActionListener(e -> {
            int i = table.getSelectedRow(); // Ottiene la riga selezionata
            if (i >= 0) {
                try {
                    // Estrai i valori dalla riga selezionata e crea un oggetto Prodotto
                    String codice = table.getValueAt(i, 0).toString();
                    String nome = table.getValueAt(i, 1).toString();
                    String descrizione = table.getValueAt(i, 2).toString();
                    double prezzo = Double.parseDouble(table.getValueAt(i, 3).toString());
                    String categoria = table.getValueAt(i, 4).toString();
                    boolean disponibile = Boolean.parseBoolean(table.getValueAt(i, 7).toString());
                    String fornitore = table.getValueAt(i, 9).toString();
                    int quantita = Integer.parseInt(table.getValueAt(i, 10).toString());

                    // Richiama la finestra di modifica con i dati del prodotto selezionato
                    c.visAndElem(4, 2);
                    c.modprodf.viewprod(new Prodotto(codice, nome, descrizione, prezzo, categoria, null, null, disponibile, null, fornitore, quantita)); // Visualizza i dettagli del prodotto da modificare
                } catch (NumberFormatException ex) {
                    // Gestione dell'errore di formato numerico
                    JOptionPane.showMessageDialog(null, "Errore nel formato dei dati: " + ex.getMessage(),
                                                  "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Messaggio di avviso se non viene selezionata nessuna riga
                JOptionPane.showMessageDialog(null, "Scegli una riga da modificare",
                                              "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Gestione del pulsante Indietro
        backbutton.addActionListener(e -> c.returnToLastFrame()); // Torna all'ultimo frame visibile
    }

    public VisioneProdottiFrame(String title, Controller c) throws SQLException {
        super(title);
        this.elementi(c);
        this.azioni(c);
    }
}

