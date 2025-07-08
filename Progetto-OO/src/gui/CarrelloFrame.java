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
    private static final String CATEGORIA_TUTTI = "Tutti";
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

        categoriacb = new JComboBox<>(new String[]{CATEGORIA_TUTTI, "Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei"});
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
        insertbutton.addActionListener(e -> inserisciProdotto());
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
    
    /**
     * Metodo completo per inizializzare/aggiornare il frame carrello
     */
    public void inizializzaFrame(Controller c) {
        clean();
        popolazioni(c);
        aggiornaTabellaProdotti(c);
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
        // Pulisci le tabelle prima di ripopolare
        clienteComboBox.removeAllItems();
        ((DefaultTableModel) prodottotable.getModel()).setRowCount(0);
        
        // Popola clienti nella ComboBox
        for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
            String id = c.clienteModel.getValueAt(i, 0).toString();
            String nome = c.clienteModel.getValueAt(i, 1).toString();
            String cognome = c.clienteModel.getValueAt(i, 2).toString();
            clienteComboBox.addItem(id + " - " + nome + " " + cognome);
        }

        // Popola tabella prodotti
        for (int i = 0; i < c.prodModel.getRowCount(); i++) {
            Object[] rowData = new Object[5];
            rowData[0] = c.prodModel.getValueAt(i, 0);  // ID
            rowData[1] = c.prodModel.getValueAt(i, 1);  // Nome
            rowData[2] = c.prodModel.getValueAt(i, 3);  // Prezzo
            rowData[3] = c.prodModel.getValueAt(i, 10); // Categoria
            rowData[4] = c.prodModel.getValueAt(i, 11); // Scorta
            ((DefaultTableModel) prodottotable.getModel()).addRow(rowData);
        }
    }

    private void filtraProdotti(Controller c) {
        String categoriaSelezionata = (String) categoriacb.getSelectedItem();
        DefaultTableModel filteredModel = new DefaultTableModel();
        filteredModel.setColumnIdentifiers(prodcolonne);

        for (int i = 0; i < c.prodModel.getRowCount(); i++) {
            String categoriaProdotto = c.prodModel.getValueAt(i, 10).toString();
            if (categoriaSelezionata.equalsIgnoreCase(CATEGORIA_TUTTI) || categoriaProdotto.equalsIgnoreCase(categoriaSelezionata)) {
                filteredModel.addRow(new Object[]{
                        c.prodModel.getValueAt(i, 0),
                        c.prodModel.getValueAt(i, 1),
                        c.prodModel.getValueAt(i, 3),
                        categoriaProdotto,
                        c.prodModel.getValueAt(i, 11)
                });
            }
        }

        prodottotable.setModel(filteredModel);
    }

    private void inserisciProdotto() {
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
            // Leggi i dati direttamente dalla tabella prodotti visualizzata
            String codiceProdotto = prodottotable.getValueAt(selectedRow, 0).toString();
            String nomeProdotto = prodottotable.getValueAt(selectedRow, 1).toString();
            double prezzoUnitario = Double.parseDouble(prodottotable.getValueAt(selectedRow, 2).toString());
            String categoria = prodottotable.getValueAt(selectedRow, 3).toString();
            int scorte = Integer.parseInt(prodottotable.getValueAt(selectedRow, 4).toString());

            if (scorte < quantita) {
                JOptionPane.showMessageDialog(null, "Scorte insufficienti!");
                return;
            }

            double totaleProdotto = prezzoUnitario * quantita;
            Object[] p = {
                    codiceProdotto,
                    nomeProdotto,
                    prezzoUnitario,
                    categoria,
                    quantita,
                    totaleProdotto
            };

            ordModel.addRow(p);
            quantitatf.setText("");
            totale();
            
            // Aggiorna la visualizzazione delle scorte nella tabella prodotti
            prodottotable.setValueAt(scorte - quantita, selectedRow, 4);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Dati non validi nel prodotto selezionato!", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void creaOrdine(Controller c) {
        // Validazione input
        if (ordModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Il carrello è vuoto! Aggiungi almeno un prodotto.", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String clienteSelezionato = (String) clienteComboBox.getSelectedItem();
        if (clienteSelezionato == null || clienteSelezionato.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleziona un cliente!", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            java.sql.Date sd = java.sql.Date.valueOf(dataod.toString());
            Integer idCliente = trovaIdCliente(c, clienteSelezionato);

            if (idCliente == null) {
                JOptionPane.showMessageDialog(null, "Cliente non trovato!", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idDipendente = Integer.parseInt(c.iddip);
            if (!c.verifyid(String.valueOf(idDipendente))) {
                JOptionPane.showMessageDialog(null, "Dipendente non valido!", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                return;
            }

            double totaleOrdine = totale();
            
            // Crea il nuovo ordine nel database
            boolean ordineCreato = c.nuovoordine("", sd, totaleOrdine, idCliente, idDipendente);
            
            if (!ordineCreato) {
                JOptionPane.showMessageDialog(null, "Errore nella creazione dell'ordine!", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Ottieni l'ID dell'ordine appena creato
            String codOrdineCorrente = c.CurrOrd();
            
            // Processa articoli e gestisci punti
            boolean successoCompleto = processaArticoliOrdine(c, codOrdineCorrente);
            boolean puntiAssegnati = assegnaPunti(c, idCliente, totaleOrdine);
            
            if (successoCompleto) {
                if (puntiAssegnati) {
                    // Aggiorna il modello degli ordini per mantenere la sincronizzazione
                    c.refreshOrdiniModel();
                    JOptionPane.showMessageDialog(null, "Ordine creato con successo!\nPunti assegnati: " + String.format("%.2f", totaleOrdine * 0.10));
                } else {
                    JOptionPane.showMessageDialog(null, "Ordine creato ma errore nell'assegnazione punti!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
                clean();
            }
            
        } catch (SQLException | NumberFormatException e1) {
            JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: " + e1.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Trova l'ID del cliente selezionato dalla ComboBox
     */
    private Integer trovaIdCliente(Controller c, String clienteSelezionato) {
        for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
            String idClienteTemp = c.clienteModel.getValueAt(i, 0).toString();
            String nome = c.clienteModel.getValueAt(i, 1).toString();
            String cognome = c.clienteModel.getValueAt(i, 2).toString();
            String nomeCognome = idClienteTemp + " - " + nome + " " + cognome;

            if (clienteSelezionato.equals(nomeCognome)) {
                return Integer.valueOf(idClienteTemp);
            }
        }
        return null;
    }
    
    /**
     * Processa tutti gli articoli dell'ordine: aggiorna scorte e inserisce articoli nel database
     */
    private boolean processaArticoliOrdine(Controller c, String codOrdineCorrente) {
        boolean successoCompleto = true;
        
        for (int j = 0; j < ordModel.getRowCount(); j++) {
            int quantita = Integer.parseInt(ordModel.getValueAt(j, 4).toString());
            String codiceProdotto = ordModel.getValueAt(j, 0).toString();
            double prezzoUnitario = Double.parseDouble(ordModel.getValueAt(j, 2).toString());
            
            try {
                // Prima aggiorna le scorte nel database
                boolean scorteAggiornate = c.upscorte(quantita, codiceProdotto);
                if (!scorteAggiornate) {
                    JOptionPane.showMessageDialog(null, 
                        "Errore nell'aggiornamento delle scorte per il prodotto: " + codiceProdotto, 
                        ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                    successoCompleto = false;
                    continue;
                }
                
                // Poi aggiorna il modello prodotti solo se l'aggiornamento database è riuscito
                aggiornaModelloProdotti(c, codiceProdotto, quantita);

                // Aggiungi l'articolo all'ordine nel database
                boolean articoloAggiunto = c.newarticoli(
                        codOrdineCorrente, // codOrdine
                        codiceProdotto, // codProdotto
                        prezzoUnitario, // prezzo
                        quantita // numeroArticoli
                );
                
                if (!articoloAggiunto) {
                    JOptionPane.showMessageDialog(null, 
                        "Errore nell'aggiunta dell'articolo: " + codiceProdotto, 
                        ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                    successoCompleto = false;
                }
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, 
                    "Errore database per prodotto " + codiceProdotto + ": " + ex.getMessage(), 
                    ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                successoCompleto = false;
            }
        }
        
        return successoCompleto;
    }
    
    /**
     * Aggiorna le scorte nel modello prodotti locale
     */
    private void aggiornaModelloProdotti(Controller c, String codiceProdotto, int quantita) {
        for (int k = 0; k < c.prodModel.getRowCount(); k++) {
            if (c.prodModel.getValueAt(k, 0).toString().equals(codiceProdotto)) {
                int scorteAttuali = Integer.parseInt(c.prodModel.getValueAt(k, 11).toString());
                int nuoveScorte = scorteAttuali - quantita;
                c.prodModel.setValueAt(nuoveScorte, k, 11);
                break;
            }
        }
    }
    
    /**
     * Assegna i punti fedeltà al cliente
     */
    private boolean assegnaPunti(Controller c, int idCliente, double totaleOrdine) {
        double puntiDaAssegnare = totaleOrdine * 0.10;
        
        try {
            boolean puntiAggiornati = c.uppunti(String.valueOf(idCliente), puntiDaAssegnare);
            
            if (puntiAggiornati) {
                // Aggiorna i punti nel modello clienti solo se l'aggiornamento database è riuscito
                aggiornaModelliClienti(c, idCliente, puntiDaAssegnare);
                return true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, 
                "Errore nell'aggiornamento punti: " + ex.getMessage(), 
                ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
        
        return false;
    }
    
    /**
     * Aggiorna i punti nel modello clienti locale
     */
    private void aggiornaModelliClienti(Controller c, int idCliente, double puntiDaAssegnare) {
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
    }
    
    /**
     * Aggiorna la tabella prodotti con i dati più recenti dal database
     */
    public void aggiornaTabellaProdotti(Controller c) {
        try {
            // Ricarica tutti i prodotti dal database
            c.allProdotti();
            
            // Ripopola la tabella con i dati aggiornati
            DefaultTableModel model = (DefaultTableModel) prodottotable.getModel();
            model.setRowCount(0);
            
            for (int i = 0; i < c.prodModel.getRowCount(); i++) {
                Object[] rowData = new Object[5];
                rowData[0] = c.prodModel.getValueAt(i, 0);  // ID
                rowData[1] = c.prodModel.getValueAt(i, 1);  // Nome
                rowData[2] = c.prodModel.getValueAt(i, 3);  // Prezzo
                rowData[3] = c.prodModel.getValueAt(i, 10); // Categoria
                rowData[4] = c.prodModel.getValueAt(i, 11); // Scorta
                model.addRow(rowData);
            }
            
            // Riapplica il filtro categoria se necessario
            String categoriaSelezionata = (String) categoriacb.getSelectedItem();
            if (categoriaSelezionata != null && !categoriaSelezionata.equals(CATEGORIA_TUTTI)) {
                filtraProdotti(c);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, 
                "Errore nell'aggiornamento della tabella prodotti: " + ex.getMessage(), 
                ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
}