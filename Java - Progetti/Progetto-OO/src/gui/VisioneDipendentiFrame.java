package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controller.Controller;
import model.Dipendente;

public class VisioneDipendentiFrame extends JFrame {
    private JTable table;
    private JButton backbutton;
    private JButton addbutton;
    private JButton updatebutton;
    private JButton searchbutton;
    private JTextField searchtf;

    public VisioneDipendentiFrame(String title, Controller c) throws SQLException {
        super(title);
        this.elementi(c);
        this.azioni(c);
    }

    public void elementi(Controller c) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(VisioneDipendentiFrame.class.getResource("/Immagini/ImmIcon.png")));
        setBounds(100, 100, 850, 500);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        JPanel titlepanel = new JPanel();
        titlepanel.setBackground(new Color(255, 140, 0));
        titlepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(titlepanel, BorderLayout.NORTH);

        JLabel titlelab = new JLabel("Amministrazione Dipendenti");
        titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelab.setForeground(Color.WHITE);
        titlepanel.add(titlelab);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        table = new JTable(c.dipModel);
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
        c.allDipendenti();

        searchbutton.addActionListener(e -> {
            String query = searchtf.getText().trim().toLowerCase();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(c.dipModel);
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

        addbutton.addActionListener(e -> c.visAndElem(2, 1));

        updatebutton.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                c.visAndElem(2, 2);
                c.updipf.viewdip(new Dipendente(
                        table.getValueAt(i, 0).toString(),
                        table.getValueAt(i, 1).toString(),
                        table.getValueAt(i, 2).toString(),
                        table.getValueAt(i, 3).toString(),
                        table.getValueAt(i, 4).toString(),
                        table.getValueAt(i, 5).toString(),
                        table.getValueAt(i, 6).toString()
                ));
            } else {
                JOptionPane.showMessageDialog(null, "Scegli una riga da modificare");
            }
        });

        backbutton.addActionListener(e -> c.returnToLastFrame());
    }
}
