package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Model.Articoli;
import Model.Ordine;

public class CarrelloFrame extends JFrame {
    private JPanel contentPane;
    private DefaultTableModel ordModel = new DefaultTableModel();
    private Object[] prodcolonne = { "Id", "Nome", "Prezzo", "Categoria", "Scorta" };
    private Object[] ordinecolonne = { "Id", "Nome", "Prezzo", "Categoria", "Quantita" };
    private LocalDate dataod = LocalDate.now();
    private JPanel bottonpanel, prodottopanel, ordinepanel, centerpanel, titlepanel;
    private JLabel datalab, quantitalab, totalelab, titlelabel;
    private JTextField quantitatf;
    private JButton backbutton, ordinebutton, selectbutton, removebutton, insertbutton;
    private JComboBox<String> categoriacb, clienteComboBox;
    private JTable prodottotable, ordinetable;
    private JScrollPane prodottiscrollPane, ordinescrollPane;

    // Configura gli elementi dell'interfaccia
    public void elementi() throws SQLException {
        setBackground(Color.WHITE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 500);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(CarrelloFrame.class.getResource("/Immagini/ImmIcon.png")));

        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        titlepanel = new JPanel();
        titlepanel.setBackground(new Color(0, 0, 139));
        contentPane.add(titlepanel, BorderLayout.NORTH);

        titlelabel = new JLabel("Nuovo Ordine");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE);
        titlepanel.add(titlelabel);

        bottonpanel = new JPanel();
        bottonpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        contentPane.add(bottonpanel, BorderLayout.SOUTH);

        datalab = new JLabel("Data Odierna: " + dataod.toString());
        bottonpanel.add(datalab);

        JLabel clienteLabel = new JLabel("Seleziona Cliente: ");
        bottonpanel.add(clienteLabel);

        clienteComboBox = new JComboBox<>();
        clienteComboBox.setPreferredSize(new Dimension(200, 25));
        bottonpanel.add(clienteComboBox);

        ordinebutton = new JButton("Inserisci Ordine");
        ordinebutton.setBackground(Color.GREEN);
        ordinebutton.setForeground(Color.WHITE);
        bottonpanel.add(ordinebutton);

        backbutton = new JButton("Indietro");
        backbutton.setBackground(Color.RED);
        backbutton.setForeground(Color.WHITE);
        bottonpanel.add(backbutton);

        prodottopanel = new JPanel(new BorderLayout());
        prodottopanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(prodottopanel, BorderLayout.WEST);

        prodottiscrollPane = new JScrollPane();
        prodottopanel.add(prodottiscrollPane, BorderLayout.CENTER);

        prodottotable = new JTable();
        prodottotable.setModel(new DefaultTableModel(new Object[]{"Id", "Nome", "Prezzo", "Categoria", "Scorta"}, 0));
        prodottotable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        prodottiscrollPane.setViewportView(prodottotable);

        ordinepanel = new JPanel(new BorderLayout());
        ordinepanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(ordinepanel, BorderLayout.EAST);

        ordinescrollPane = new JScrollPane();
        ordinepanel.add(ordinescrollPane, BorderLayout.CENTER);

        ordinetable = new JTable();
        ordModel.setColumnIdentifiers(ordinecolonne);
        ordinetable.setModel(ordModel);
        ordinetable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordinescrollPane.setViewportView(ordinetable);

        centerpanel = new JPanel();
        centerpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.Y_AXIS));
        contentPane.add(centerpanel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        centerpanel.add(topPanel);

        categoriacb = new JComboBox<>(new String[]{"Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei"});
        categoriacb.setMaximumSize(categoriacb.getPreferredSize());
        topPanel.add(categoriacb);

        selectbutton = new JButton("Seleziona");
        topPanel.add(selectbutton);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        centerpanel.add(middlePanel);

        quantitalab = new JLabel("Seleziona Quantità:");
        middlePanel.add(quantitalab);

        quantitatf = new JTextField(5);
        quantitatf.setPreferredSize(new Dimension(100, 25));
        middlePanel.add(quantitatf);

        totalelab = new JLabel("Totale: 0.00");
        totalelab.setFont(new Font("Tahoma", Font.BOLD, 14));
        middlePanel.add(totalelab);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        centerpanel.add(bottomPanel);

        removebutton = new JButton("Rimuovi");
        removebutton.setBackground(Color.RED);
        removebutton.setForeground(Color.WHITE);
        bottomPanel.add(removebutton);

        insertbutton = new JButton("Inserisci");
        insertbutton.setBackground(new Color(0, 153, 255));
        insertbutton.setForeground(Color.WHITE);
        bottomPanel.add(insertbutton);
    }

    // Pulisce i campi
    public void clean() {
        totalelab.setText("Totale :  0.00");
        quantitatf.setText("");
        ordModel.setRowCount(0);
    }

    // Calcola il totale
    public double totale() {
        double tot = 0.00;
        for (int j = 0; j < ordModel.getRowCount(); j++) {
            double prezzoUnitario = Double.parseDouble(ordModel.getValueAt(j, 2).toString());
            int quantita = Integer.parseInt(ordModel.getValueAt(j, 4).toString());
            tot += prezzoUnitario * quantita;
        }
        totalelab.setText("Totale : " + String.format("%.2f", tot));
        return tot;
    }

    // Popola i dati
    public void popolazioni(Controller c) {
        for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
            String id = c.clienteModel.getValueAt(i, 0).toString();
            String nome = c.clienteModel.getValueAt(i, 1).toString();
            String cognome = c.clienteModel.getValueAt(i, 2).toString();
            clienteComboBox.addItem(id + " - " + nome + " " + cognome);
        }

        for (int i = 0; i < c.prodModel.getRowCount(); i++) {
            Object[] rowData = new Object[5];
            rowData[0] = c.prodModel.getValueAt(i, 0);
            rowData[1] = c.prodModel.getValueAt(i, 1);
            rowData[2] = c.prodModel.getValueAt(i, 3);
            rowData[3] = c.prodModel.getValueAt(i, 9);
            rowData[4] = c.prodModel.getValueAt(i, 10);
            ((DefaultTableModel) prodottotable.getModel()).addRow(rowData);
        }
    }

    private void filtraProdotti(Controller c) {
        String categoriaSelezionata = (String) categoriacb.getSelectedItem();
        DefaultTableModel filteredModel = new DefaultTableModel();
        filteredModel.setColumnIdentifiers(prodcolonne);

        for (int i = 0; i < c.prodModel.getRowCount(); i++) {
            String categoriaProdotto = c.prodModel.getValueAt(i, 9).toString();
            if (categoriaProdotto.equalsIgnoreCase(categoriaSelezionata)) {
                filteredModel.addRow(new Object[]{
                    c.prodModel.getValueAt(i, 0),
                    c.prodModel.getValueAt(i, 1),
                    c.prodModel.getValueAt(i, 2),
                    categoriaProdotto,
                    c.prodModel.getValueAt(i, 10)
                });
            }
        }

        prodottotable.setModel(filteredModel);
    }

    private void inserisciProdotto(Controller c) {
        int selectedRow = prodottotable.getSelectedRow();
        String quantitaText = quantitatf.getText().trim();

        if (selectedRow == -1 || quantitaText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleziona un prodotto e inserisci una quantità!");
            return;
        }

        int quantita;
        try {
            quantita = Integer.parseInt(quantitaText);
            if (quantita <= 0) {
                JOptionPane.showMessageDialog(null, "La quantità deve essere un numero positivo!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Quantità non valida! Assicurati di inserire un numero intero.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String scorteString = c.prodModel.getValueAt(selectedRow, 10).toString();
            int scorte = Integer.parseInt(scorteString);

            String prezzoString = c.prodModel.getValueAt(selectedRow, 3).toString();
            double prezzoUnitario = Double.parseDouble(prezzoString);

            if (scorte < quantita) {
                JOptionPane.showMessageDialog(null, "Scorte insufficienti!");
                return;
            }

            double totaleProdotto = prezzoUnitario * quantita;
            Object[] p = {
                c.prodModel.getValueAt(selectedRow, 0),
                c.prodModel.getValueAt(selectedRow, 1),
                prezzoUnitario,
                c.prodModel.getValueAt(selectedRow, 3),
                quantita,
                totaleProdotto
            };

            ordModel.addRow(p);
            quantitatf.setText("");
            totale();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Dati non validi nel prodotto selezionato!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void creaOrdine(Controller c) {
        try {
            java.sql.Date sd = java.sql.Date.valueOf(dataod);
            String clienteSelezionato = (String) clienteComboBox.getSelectedItem();
            String idCliente = null;

            for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
                String idClienteTemp = c.clienteModel.getValueAt(i, 0).toString();
                String nome = c.clienteModel.getValueAt(i, 1).toString();
                String cognome = c.clienteModel.getValueAt(i, 2).toString();
                String nomeCognome = idClienteTemp + " - " + nome + " " + cognome;

                if (clienteSelezionato.equals(nomeCognome)) {
                    idCliente = idClienteTemp;
                    break;
                }
            }

            if (idCliente != null) {
                String idDipendente = c.iddip;
                double totaleOrdine = totale();

                c.nuovoordine(new Ordine("", sd, totaleOrdine, idCliente, idDipendente));

                for (int j = 0; j < c.ordModel.getRowCount(); j++) {
                    int quantita = Integer.parseInt(c.ordModel.getValueAt(j, 4).toString());
                    String codiceProdotto = c.ordModel.getValueAt(j, 0).toString();
                    double prezzoUnitario = Double.parseDouble(c.ordModel.getValueAt(j, 2).toString());

                    c.upscorte(quantita, codiceProdotto);
                    Articoli articoli = new Articoli(c.CurrOrd(), idCliente, prezzoUnitario, prezzoUnitario * quantita, quantita, c.ordModel.getValueAt(j, 3).toString());
                    c.newarticoli(articoli);
                }

                c.uppunti(idCliente, totaleOrdine);
                JOptionPane.showMessageDialog(null, "Ordine aggiunto");
                clean();
            } else {
                JOptionPane.showMessageDialog(null, "Cliente non trovato!");
            }
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: " + e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e2) {
            JOptionPane.showMessageDialog(null, "Data non valida!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Configura le azioni dei componenti
    public void azioni(Controller c) throws SQLException {
        popolazioni(c);

        selectbutton.addActionListener(e -> filtraProdotti(c));
        insertbutton.addActionListener(e -> inserisciProdotto(c));
        removebutton.addActionListener(e -> {
            int selectedRow = ordinetable.getSelectedRow();
            if (selectedRow != -1) {
                ordModel.removeRow(selectedRow);
                totale();
            }
        });
        ordinebutton.addActionListener(e -> creaOrdine(c));
        backbutton.addActionListener(e -> c.visAndElem(1, 2));
    }

    public CarrelloFrame(String title, Controller c) throws SQLException {
        super(title);
        this.elementi();
        this.azioni(c);
    }
}