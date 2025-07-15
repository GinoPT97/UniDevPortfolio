package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;

public class VisioneOrdineFrame extends JFrame {
    private JTable table;
    private JButton backbutton;
    private JButton ordinebutton;
    private JButton searchbutton;
    private JTextField searchtf;

    public VisioneOrdineFrame(String title, Controller c) throws SQLException {
        super(title);
        this.elementi(c);
        this.azioni(c);
    }

    public void elementi(Controller c) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 450);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        JPanel titlepanel = new JPanel();
        titlepanel.setBackground(new Color(0, 139, 139));
        titlepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(titlepanel, BorderLayout.NORTH);

        JLabel titlelabel = new JLabel("Visualizzazione Ordini");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE);
        titlepanel.add(titlelabel);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        table = new JTable(c.ordModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(table);

        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(buttonpanel, BorderLayout.SOUTH);

        searchtf = new JTextField(10);
        buttonpanel.add(searchtf);

        searchbutton = creaButton("Cerca", new Color(60, 179, 113));
        buttonpanel.add(searchbutton);

        ordinebutton = creaButton("Nuovo Ordine", new Color(34, 139, 34));
        buttonpanel.add(ordinebutton);

        backbutton = creaButton("Indietro", new Color(178, 34, 34));
        buttonpanel.add(backbutton);
    }

    private JButton creaButton(String text, Color color) {
        JButton button = new JButton(text, gui.IconUtils.getIconForText(text, color));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    public void azioni(Controller c) throws SQLException {
        c.allOrdini();

        searchbutton.addActionListener(e -> {
            String query = searchtf.getText().trim();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(c.ordModel);
            table.setRowSorter(sorter);
            if (query.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                String[] parole = query.split("\\s+");
                try {
                    RowFilter<DefaultTableModel, Object> filtro = new RowFilter<DefaultTableModel, Object>() {
                        public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                            for (String parola : parole) {
                                boolean trovata = false;
                                for (int i = 0; i < entry.getValueCount(); i++) {
                                    Object cell = entry.getValue(i);
                                    if (cell != null && cell.toString().toLowerCase().contains(parola.toLowerCase())) {
                                        trovata = true;
                                        break;
                                    }
                                }
                                if (!trovata) return false;
                            }
                            return true;
                        }
                    };
                    sorter.setRowFilter(filtro);
                    if (table.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Nessun risultato trovato.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        sorter.setRowFilter(null);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Errore nella ricerca: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        ordinebutton.addActionListener(e -> c.visAndElem(1, 1));
        backbutton.addActionListener(e -> c.returnToLastFrame());
    }
}
