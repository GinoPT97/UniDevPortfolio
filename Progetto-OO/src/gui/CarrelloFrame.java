package gui;

import controller.Controller;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CarrelloFrame extends JFrame {
    private static final String FONT_TAHOMA = "Tahoma";
    private static final String ERROR_TITLE = "Errore";
    private final DefaultTableModel ordModel = new DefaultTableModel();
    private final transient Object[] prodcolonne = {"Id", "Nome", "Prezzo", "Categoria", "Scorta"};
    private final transient Object[] ordinecolonne = {"Id", "Nome", "Prezzo", "Categoria", "Quantita"};
    private final LocalDate dataod = LocalDate.now();
    private JTextField quantitatf;
    private JLabel totalelab;
    private JButton backbutton;
    private JButton ordinebutton;
    private JButton selectbutton;
    private JButton removebutton;
    private JButton insertbutton;
    private JComboBox<String> categoriacb;
    private JComboBox<String> clienteComboBox;
    private JTable prodottotable;
    private JTable ordinetable;

    public CarrelloFrame(String title, Controller c) {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    private void elementi() {
        setBackground(Color.WHITE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 500);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(CarrelloFrame.class.getResource("/Immagini/ImmIcon.png")));

        JPanel contentPane = new JPanel(new BorderLayout(0, 0));
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        JPanel titlepanel = new JPanel();
        titlepanel.setBackground(new Color(0, 0, 139));
        titlepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(titlepanel, BorderLayout.NORTH);

        JLabel titlelabel = new JLabel("Nuovo Ordine");
        titlelabel.setFont(new Font(FONT_TAHOMA, Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE);
        titlepanel.add(titlelabel);

        JPanel bottonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottonpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(bottonpanel, BorderLayout.SOUTH);

        JLabel datalab = new JLabel("Data Odierna: " + dataod.toString());
        bottonpanel.add(datalab);

        JLabel clienteLabel = new JLabel("Seleziona Cliente: ");
        bottonpanel.add(clienteLabel);

        clienteComboBox = new JComboBox<>();
        clienteComboBox.setPreferredSize(new Dimension(200, 25));
        bottonpanel.add(clienteComboBox);

        ordinebutton = creaButton("Inserisci Ordine", new Color(34, 139, 34));
        bottonpanel.add(ordinebutton);

        backbutton = creaButton("Indietro", new Color(178, 34, 34));
        bottonpanel.add(backbutton);

        JPanel prodottopanel = new JPanel(new BorderLayout());
        prodottopanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(prodottopanel, BorderLayout.WEST);

        JLabel prodottiLabel = new JLabel("Prodotti Disponibili");
        prodottiLabel.setFont(new Font(FONT_TAHOMA, Font.BOLD, 14));
        prodottiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        prodottopanel.add(prodottiLabel, BorderLayout.NORTH);

        JScrollPane prodottiscrollPane = new JScrollPane();
        prodottopanel.add(prodottiscrollPane, BorderLayout.CENTER);

        prodottotable = new JTable(new DefaultTableModel(prodcolonne, 0));
        prodottotable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        prodottiscrollPane.setViewportView(prodottotable);

        JPanel ordinepanel = new JPanel(new BorderLayout());
        ordinepanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(ordinepanel, BorderLayout.EAST);

        JLabel ordineLabel = new JLabel("Ordine Corrente");
        ordineLabel.setFont(new Font(FONT_TAHOMA, Font.BOLD, 14));
        ordineLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ordinepanel.add(ordineLabel, BorderLayout.NORTH);

        JScrollPane ordinescrollPane = new JScrollPane();
        ordinepanel.add(ordinescrollPane, BorderLayout.CENTER);

        ordinetable = new JTable(ordModel);
        ordModel.setColumnIdentifiers(ordinecolonne);
        ordinetable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordinescrollPane.setViewportView(ordinetable);

        JPanel centerpanel = new JPanel();
        centerpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.Y_AXIS));
        contentPane.add(centerpanel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        centerpanel.add(topPanel);

        categoriacb = new JComboBox<>(new String[]{"Tutti", "Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei"});
        categoriacb.setMaximumSize(categoriacb.getPreferredSize());
        topPanel.add(categoriacb);

        selectbutton = creaButton("Seleziona", new Color(70, 130, 180));
        topPanel.add(selectbutton);

        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        centerpanel.add(middlePanel);

        JLabel quantitalab = new JLabel("Seleziona Quantità:");
        middlePanel.add(quantitalab);

        quantitatf = new JTextField(5);
        quantitatf.setPreferredSize(new Dimension(100, 25));
        middlePanel.add(quantitatf);

        totalelab = new JLabel("Totale: 0.00");
        totalelab.setFont(new Font(FONT_TAHOMA, Font.BOLD, 14));
        middlePanel.add(totalelab);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        centerpanel.add(bottomPanel);

        removebutton = creaButton("Rimuovi", new Color(178, 34, 34));
        bottomPanel.add(removebutton);

        insertbutton = creaButton("Inserisci", new Color(0, 153, 255));
        bottomPanel.add(insertbutton);
    }

    private JButton creaButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    private void azioni(Controller c) {
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

    public void clean() {
        totalelab.setText("Totale :  0.00");
        quantitatf.setText("");
        ordModel.setRowCount(0);
        clienteComboBox.setSelectedIndex(-1);
        categoriacb.setSelectedIndex(0);
    }

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
            if (categoriaSelezionata.equalsIgnoreCase("Tutti") || categoriaProdotto.equalsIgnoreCase(categoriaSelezionata)) {
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
                JOptionPane.showMessageDialog(null, "La quantità deve essere un numero positivo!", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Quantità non valida! Assicurati di inserire un numero intero.", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "Dati non validi nel prodotto selezionato!", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void creaOrdine(Controller c) {
        try {
            java.sql.Date sd = java.sql.Date.valueOf(dataod.toString());
            String clienteSelezionato = (String) clienteComboBox.getSelectedItem();
            Integer idCliente = null;

            for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
                String idClienteTemp = c.clienteModel.getValueAt(i, 0).toString();
                String nome = c.clienteModel.getValueAt(i, 1).toString();
                String cognome = c.clienteModel.getValueAt(i, 2).toString();
                String nomeCognome = idClienteTemp + " - " + nome + " " + cognome;

                if (clienteSelezionato.equals(nomeCognome)) {
                    idCliente = Integer.valueOf(idClienteTemp);
                    break;
                }
            }

            if (idCliente != null) {
                int idDipendente = Integer.parseInt(c.iddip);

                if (!c.verifyid(String.valueOf(idDipendente))) {
                    JOptionPane.showMessageDialog(null, "Dipendente non valido!");
                    return;
                }

                double totaleOrdine = totale();
                c.nuovoordine("", sd, totaleOrdine, idCliente, idDipendente);

                for (int j = 0; j < ordModel.getRowCount(); j++) {
                    int quantita = Integer.parseInt(ordModel.getValueAt(j, 4).toString());
                    String codiceProdotto = ordModel.getValueAt(j, 0).toString();
                    double prezzoUnitario = Double.parseDouble(ordModel.getValueAt(j, 2).toString());
                    
                    // Trova la categoria del prodotto nel modello prodotti
                    String categoria = "";
                    for (int k = 0; k < c.prodModel.getRowCount(); k++) {
                        if (c.prodModel.getValueAt(k, 0).toString().equals(codiceProdotto)) {
                            categoria = c.prodModel.getValueAt(k, 9).toString();
                            
                            // Aggiorna le scorte nel modello prodotti
                            int scorteAttuali = Integer.parseInt(c.prodModel.getValueAt(k, 10).toString());
                            int nuoveScorte = scorteAttuali - quantita;
                            c.prodModel.setValueAt(nuoveScorte, k, 10);
                            break;
                        }
                    }

                    c.upscorte(quantita, codiceProdotto);

                    c.newarticoli(
                            c.CurrOrd(), // codOrdine
                            codiceProdotto, // codProdotto
                            prezzoUnitario, // prezzo
                            prezzoUnitario * quantita, // numPunti
                            quantita, // numeroArticoli
                            categoria, // categoria
                            idCliente // codCliente
                    );
                }

                // Calcola i punti come 10% del totale dell'ordine
                double puntiDaAssegnare = totaleOrdine * 0.10;
                c.uppunti(String.valueOf(idCliente), puntiDaAssegnare);
                
                // Aggiorna i punti nel modello clienti
                for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
                    String idClienteModel = c.clienteModel.getValueAt(i, 0).toString();
                    if (idClienteModel.equals(String.valueOf(idCliente))) {
                        Object puntiAttualiObj = c.clienteModel.getValueAt(i, 8);
                        double puntiAttuali = 0.0;
                        if (puntiAttualiObj != null && !puntiAttualiObj.toString().equals("N/A")) {
                            try {
                                puntiAttuali = Double.parseDouble(puntiAttualiObj.toString());
                            } catch (NumberFormatException ex) {
                                puntiAttuali = 0.0;
                            }
                        }
                        double nuoviPunti = puntiAttuali + puntiDaAssegnare;
                        c.clienteModel.setValueAt(String.format("%.2f", nuoviPunti), i, 8);
                        break;
                    }
                }
                
                JOptionPane.showMessageDialog(null, "Ordine aggiunto - Punti assegnati: " + String.format("%.2f", puntiDaAssegnare));
                clean();
            } else {
                JOptionPane.showMessageDialog(null, "Cliente non trovato!");
            }
        } catch (SQLException | NumberFormatException e1) {
            JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: " + e1.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
}