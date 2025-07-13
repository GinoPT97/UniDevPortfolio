package gui;

import controller.Controller;
import java.awt.*;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class VisioneClienteFrame extends JFrame {
    private JTable table;
    private JButton backbutton;
    private JButton addbutton;
    private JButton updatebutton;
    private JButton dettagliTesseraButton;
    private JTextField searchtf;
    private JButton searchbutton;

    public VisioneClienteFrame(String title, Controller c) throws SQLException {
        super(title);
        this.elementi(c);
        this.azioni(c);
    }

    private void elementi(Controller c) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 450);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(VisioneClienteFrame.class.getResource("/Immagini/ImmIcon.png")));

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        JPanel titlepanel = new JPanel();
        titlepanel.setBackground(new Color(0, 128, 0));
        titlepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(titlepanel, BorderLayout.NORTH);

        JLabel titlelab = new JLabel("Amministrazione Clienti");
        titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelab.setForeground(Color.WHITE);
        titlepanel.add(titlelab);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        table = new JTable(c.clienteModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(table);

        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(buttonpanel, BorderLayout.SOUTH);

        searchtf = new JTextField(10);
        buttonpanel.add(searchtf);

        searchbutton = creaButton("Cerca", new Color(107, 142, 35));
        buttonpanel.add(searchbutton);

        addbutton = creaButton("Aggiungi", new Color(34, 139, 34));
        buttonpanel.add(addbutton);

        updatebutton = creaButton("Modifica", new Color(70, 130, 180));
        buttonpanel.add(updatebutton);

        dettagliTesseraButton = creaButton("Dettagli Tessera", new Color(255, 140, 0));
        buttonpanel.add(dettagliTesseraButton);

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

    private void azioni(Controller c) throws SQLException {
        c.allCliente();

        searchbutton.addActionListener(e -> {
            String query = searchtf.getText().trim().toLowerCase();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(c.clienteModel);
            table.setRowSorter(sorter);
            if (query.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                try {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
                } catch (PatternSyntaxException ex) {
                    JOptionPane.showMessageDialog(null, "Errore nella sintassi della ricerca: " + ex.getMessage(),
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addbutton.addActionListener(e -> c.visAndElem(3, 1));

        updatebutton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String[] clienteData = new String[7];
                for (int i = 0; i < clienteData.length; i++) {
                    clienteData[i] = table.getValueAt(selectedRow, i).toString();
                }

                c.visAndElem(3, 2);
                c.upclf.viewct(
                        clienteData[0], // codCliente
                        clienteData[1], // nome
                        clienteData[2], // cognome
                        clienteData[3], // codFis
                        clienteData[5], // indirizzo
                        clienteData[4], // email
                        clienteData[6]  // telefono
                );
            } else {
                JOptionPane.showMessageDialog(null, "Scegli una riga da modificare", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        });

        backbutton.addActionListener(e -> c.returnToLastFrame());

        dettagliTesseraButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Indici colonne: 7 = Id Tessera, 8 = Punti, 9 = Stato Tessera
                Object idTessera = table.getValueAt(selectedRow, 7);
                Object punti = table.getValueAt(selectedRow, 8);
                Object stato = table.getValueAt(selectedRow, 9);
                String scadenza = "N/A";
                // Prova a recuperare la data scadenza se presente nel modello (opzionale)
                if (table.getColumnCount() > 10) {
                    Object val = table.getValueAt(selectedRow, 10);
                    if (val != null) scadenza = val.toString();
                }
                String msg = "ID Tessera: " + (idTessera != null ? idTessera : "N/A") +
                        "\nPunti: " + (punti != null ? punti : "N/A") +
                        "\nStato: " + (stato != null ? stato : "N/A") +
                        "\nScadenza: " + scadenza;
                JOptionPane.showMessageDialog(this, msg, "Dettagli Tessera", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un cliente per vedere i dettagli della tessera", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
