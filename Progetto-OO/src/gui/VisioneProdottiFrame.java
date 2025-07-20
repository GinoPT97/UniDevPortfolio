package gui;

import controller.Controller;
import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class VisioneProdottiFrame extends JFrame {
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

    private void elementi(Controller c) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 500);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
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
        table.setAutoCreateRowSorter(true);
        // Righe alternate
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) c.setBackground(row % 2 == 0 ? java.awt.Color.WHITE : new java.awt.Color(240,240,240));
                return c;
            }
        });
        // Tooltip intestazioni
        table.getTableHeader().setToolTipText("Clicca per ordinare la colonna");
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
        JButton button = new JButton(text, IconUtils.getIconForText(text, color));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    private void azioni(Controller c) throws SQLException {
        c.allProdotti();

        searchbutton.addActionListener(e -> filtraTabella(c));
        addbutton.addActionListener(e -> c.visAndElem(4, 1));
        updatebutton.addActionListener(e -> aggiornaProdotto(c));
        backbutton.addActionListener(e -> c.returnToLastFrame());
    }

    private void filtraTabella(Controller c) {
        String query = searchtf.getText().trim();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(c.prodModel);
        table.setRowSorter(sorter);
        if (query.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            try {
                RowFilter<DefaultTableModel, Object> filtro = creaFiltro(query);
                sorter.setRowFilter(filtro);
                if (table.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Nessun risultato trovato.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    sorter.setRowFilter(null);
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null, "Errore nella ricerca: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private RowFilter<DefaultTableModel, Object> creaFiltro(String query) {
        String[] parole = query.split("\\s+");
        return new RowFilter<DefaultTableModel, Object>() {
            @Override
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
    }

    private void aggiornaProdotto(Controller c) {
        int i = table.getSelectedRow();
        if (i >= 0) {
            try {
                String codice = table.getValueAt(i, 0).toString();
                String nome = table.getValueAt(i, 1).toString();
                String descrizione = table.getValueAt(i, 2).toString();
                double prezzo = Double.parseDouble(table.getValueAt(i, 3).toString());
                String luogoProvenienza = table.getValueAt(i, 4).toString();
                boolean glutine = "Si".equals(table.getValueAt(i, 7).toString());
                String categoria = table.getValueAt(i, 10).toString();
                int scorta = Integer.parseInt(table.getValueAt(i, 11).toString());

                c.visAndElem(4, 2);
                c.modprodf.viewprod(codice, nome, descrizione, luogoProvenienza, prezzo, scorta, glutine, categoria);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Errore nel formato dei dati: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Scegli una riga da modificare", "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
    }
}

