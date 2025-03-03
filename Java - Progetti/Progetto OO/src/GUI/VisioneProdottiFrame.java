package GUI;

import java.awt.*;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import Model.Prodotto;

public class VisioneProdottiFrame extends JFrame {
    private JPanel contentPane;
    private JTable table;
    private JButton backbutton;
    private JButton addbutton;
    private JButton updatebutton;
    private JButton searchbutton;
    private JTextField searchtf;

    public VisioneProdottiFrame(String title, Controller c) throws SQLException {
        super(title);
        this.elementi(c);
        this.azioni(c);
    }

    public void elementi(Controller c) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 500);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        JPanel titlepanel = new JPanel();
        titlepanel.setBackground(new Color(128, 0, 0));
        titlepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(titlepanel, BorderLayout.NORTH);

        JLabel titlelabel = new JLabel("Amministrazione Prodotti");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE);
        titlepanel.add(titlelabel);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        table = new JTable(c.prodModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(table);

        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(buttonpanel, BorderLayout.SOUTH);

        searchtf = new JTextField(10);
        buttonpanel.add(searchtf);

        searchbutton = creaButton("Cerca", new Color(46, 139, 87));
        buttonpanel.add(searchbutton);

        addbutton = creaButton("Aggiungi", new Color(34, 139, 34));
        buttonpanel.add(addbutton);

        updatebutton = creaButton("Modifica", new Color(70, 130, 180));
        buttonpanel.add(updatebutton);

        backbutton = creaButton("Indietro", new Color(178, 34, 34));
        buttonpanel.add(backbutton);
    }

    private JButton creaButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    public void azioni(Controller c) throws SQLException {
        c.allProdotti();

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

        addbutton.addActionListener(e -> c.visAndElem(4, 1));

        updatebutton.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                try {
                    String codice = table.getValueAt(i, 0).toString();
                    String nome = table.getValueAt(i, 1).toString();
                    String descrizione = table.getValueAt(i, 2).toString();
                    double prezzo = Double.parseDouble(table.getValueAt(i, 3).toString());
                    String categoria = table.getValueAt(i, 4).toString();
                    boolean disponibile = Boolean.parseBoolean(table.getValueAt(i, 7).toString());
                    String fornitore = table.getValueAt(i, 9).toString();
                    int quantita = Integer.parseInt(table.getValueAt(i, 10).toString());

                    c.visAndElem(4, 2);
                    c.modprodf.viewprod(new Prodotto(codice, nome, descrizione, prezzo, categoria, null, null, disponibile, null, fornitore, quantita));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Errore nel formato dei dati: " + ex.getMessage(),
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Scegli una riga da modificare",
                        "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        });

        backbutton.addActionListener(e -> c.returnToLastFrame());
    }
}

