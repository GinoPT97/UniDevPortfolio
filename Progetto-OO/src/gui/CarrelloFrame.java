package gui;

import controller.Controller;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CarrelloFrame extends JFrame {
    private static final String FONT_TAHOMA = "Tahoma", ERROR_TITLE = "Errore", CATEGORIA_TUTTI = "Tutti";
    private static final String[] CATEGORIE = {CATEGORIA_TUTTI, "Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei"};
    private static final Object[] PROD_COLUMNS = {"Id", "Nome", "Prezzo", "Categoria", "Scorta"};
    private static final Object[] ORDINE_COLUMNS = {"Id", "Nome", "Prezzo", "Categoria", "Quantita"};
    
    private final DefaultTableModel ordModel = new DefaultTableModel();
    private final LocalDate dataod = LocalDate.now();
    private JTextField quantitatf;
    private JLabel totalelab;
    private JButton backbutton, ordinebutton, selectbutton, removebutton, insertbutton;
    private JComboBox<String> categoriacb, clienteComboBox;
    private JTable prodottotable, ordinetable;

    public CarrelloFrame(String title, Controller c) {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    private void elementi() {
        configurateFrame();
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);

        contentPane.add(createTitlePanel(), BorderLayout.NORTH);
        contentPane.add(createBottomPanel(), BorderLayout.SOUTH);
        contentPane.add(createTablePanel("Prodotti Disponibili", prodottotable = new JTable(new DefaultTableModel(PROD_COLUMNS, 0))), BorderLayout.WEST);
        contentPane.add(createTablePanel("Ordine Corrente", ordinetable = createOrderTable()), BorderLayout.EAST);
        contentPane.add(createCenterControls(), BorderLayout.CENTER);
    }

    private void configurateFrame() {
        setBackground(Color.WHITE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 500);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(CarrelloFrame.class.getResource("/Immagini/ImmIcon.png")));
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 139));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel label = new JLabel("Nuovo Ordine");
        label.setFont(new Font(FONT_TAHOMA, Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        panel.add(label);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Data Odierna: " + dataod.toString()));
        panel.add(new JLabel("Seleziona Cliente: "));
        clienteComboBox = new JComboBox<>();
        clienteComboBox.setPreferredSize(new Dimension(200, 25));
        panel.add(clienteComboBox);
        panel.add(ordinebutton = creaButton("Inserisci Ordine", new Color(34, 139, 34)));
        panel.add(backbutton = creaButton("Indietro", new Color(178, 34, 34)));
        return panel;
    }

    private JPanel createTablePanel(String title, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel label = new JLabel(title);
        label.setFont(new Font(FONT_TAHOMA, Font.BOLD, 14));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JTable createOrderTable() {
        JTable table = new JTable(ordModel);
        ordModel.setColumnIdentifiers(ORDINE_COLUMNS);
        return table;
    }

    private JPanel createCenterControls() {
        JPanel center = new JPanel();
        center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        categoriacb = new JComboBox<>(CATEGORIE);
        categoriacb.setMaximumSize(categoriacb.getPreferredSize());
        top.add(categoriacb);
        top.add(selectbutton = creaButton("Seleziona", new Color(70, 130, 180)));

        JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        middle.add(new JLabel("Seleziona Quantità:"));
        quantitatf = new JTextField(5);
        quantitatf.setPreferredSize(new Dimension(100, 25));
        middle.add(quantitatf);
        totalelab = new JLabel("Totale: 0.00");
        totalelab.setFont(new Font(FONT_TAHOMA, Font.BOLD, 14));
        middle.add(totalelab);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottom.add(removebutton = creaButton("Rimuovi", new Color(178, 34, 34)));
        bottom.add(insertbutton = creaButton("Inserisci", new Color(0, 153, 255)));

        center.add(top);
        center.add(middle);
        center.add(bottom);
        return center;
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
        clienteComboBox.removeAllItems();
        ((DefaultTableModel) prodottotable.getModel()).setRowCount(0);
        
        // Popola clienti e prodotti in un unico ciclo quando possibile
        for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
            clienteComboBox.addItem(String.format("%s - %s %s", 
                c.clienteModel.getValueAt(i, 0), c.clienteModel.getValueAt(i, 1), c.clienteModel.getValueAt(i, 2)));
        }

        DefaultTableModel prodModel = (DefaultTableModel) prodottotable.getModel();
        for (int i = 0; i < c.prodModel.getRowCount(); i++) {
            prodModel.addRow(new Object[]{c.prodModel.getValueAt(i, 0), c.prodModel.getValueAt(i, 1), 
                c.prodModel.getValueAt(i, 3), c.prodModel.getValueAt(i, 10), c.prodModel.getValueAt(i, 11)});
        }
    }

    private void filtraProdotti(Controller c) {
        String categoria = (String) categoriacb.getSelectedItem();
        DefaultTableModel filteredModel = new DefaultTableModel();
        filteredModel.setColumnIdentifiers(PROD_COLUMNS);

        for (int i = 0; i < c.prodModel.getRowCount(); i++) {
            String categoriaProdotto = c.prodModel.getValueAt(i, 10).toString();
            if (CATEGORIA_TUTTI.equalsIgnoreCase(categoria) || categoriaProdotto.equalsIgnoreCase(categoria)) {
                filteredModel.addRow(new Object[]{c.prodModel.getValueAt(i, 0), c.prodModel.getValueAt(i, 1),
                    c.prodModel.getValueAt(i, 3), categoriaProdotto, c.prodModel.getValueAt(i, 11)});
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

        try {
            int quantita = Integer.parseInt(quantitaText);
            if (quantita <= 0) {
                JOptionPane.showMessageDialog(null, "La quantità deve essere un numero positivo!", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                return;
            }

            String codiceProdotto = prodottotable.getValueAt(selectedRow, 0).toString();
            String nomeProdotto = prodottotable.getValueAt(selectedRow, 1).toString();
            double prezzoUnitario = Double.parseDouble(prodottotable.getValueAt(selectedRow, 2).toString());
            String categoria = prodottotable.getValueAt(selectedRow, 3).toString();
            int scorte = Integer.parseInt(prodottotable.getValueAt(selectedRow, 4).toString());

            if (scorte < quantita) {
                JOptionPane.showMessageDialog(null, "Scorte insufficienti!");
                return;
            }

            ordModel.addRow(new Object[]{codiceProdotto, nomeProdotto, prezzoUnitario, categoria, quantita, prezzoUnitario * quantita});
            quantitatf.setText("");
            totale();
            prodottotable.setValueAt(scorte - quantita, selectedRow, 4);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Dati non validi!", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void creaOrdine(Controller c) {
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
            java.sql.Date dataOrdine = java.sql.Date.valueOf(dataod.toString());
            
            if (!c.nuovoordine("", dataOrdine, totaleOrdine, idCliente, idDipendente)) {
                JOptionPane.showMessageDialog(null, "Errore nella creazione dell'ordine!", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String codOrdine = c.CurrOrd();
            boolean successoCompleto = processaArticoliOrdine(c, codOrdine);
            boolean puntiAssegnati = assegnaPunti(c, idCliente, totaleOrdine);
            
            if (successoCompleto) {
                String messaggio = puntiAssegnati ? 
                    "Ordine creato con successo!\nPunti assegnati: " + String.format("%.2f", totaleOrdine * 0.10) :
                    "Ordine creato ma errore nell'assegnazione punti!";
                JOptionPane.showMessageDialog(null, messaggio, puntiAssegnati ? "Successo" : "Attenzione", 
                    puntiAssegnati ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
                clean();
            }
            
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Errore: " + e.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Integer trovaIdCliente(Controller c, String clienteSelezionato) {
        for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
            String display = String.format("%s - %s %s", c.clienteModel.getValueAt(i, 0), 
                c.clienteModel.getValueAt(i, 1), c.clienteModel.getValueAt(i, 2));
            if (clienteSelezionato.equals(display)) {
                return Integer.valueOf(c.clienteModel.getValueAt(i, 0).toString());
            }
        }
        return null;
    }
    
    /**
     * Processa tutti gli articoli dell'ordine: aggiorna scorte e inserisce articoli nel database
     */
    private boolean processaArticoliOrdine(Controller c, String codOrdine) {
        boolean successoCompleto = true;
        
        for (int j = 0; j < ordModel.getRowCount(); j++) {
            int quantita = Integer.parseInt(ordModel.getValueAt(j, 4).toString());
            String codiceProdotto = ordModel.getValueAt(j, 0).toString();
            double prezzoUnitario = Double.parseDouble(ordModel.getValueAt(j, 2).toString());
            
            try {
                if (!c.upscorte(quantita, codiceProdotto)) {
                    JOptionPane.showMessageDialog(null, "Errore nell'aggiornamento delle scorte per il prodotto: " + codiceProdotto, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                    successoCompleto = false;
                    continue;
                }

                if (!c.newarticoli(codOrdine, codiceProdotto, prezzoUnitario, quantita)) {
                    JOptionPane.showMessageDialog(null, "Errore nell'aggiunta dell'articolo: " + codiceProdotto, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                    successoCompleto = false;
                }
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Errore database per prodotto " + codiceProdotto + ": " + ex.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                successoCompleto = false;
            }
        }
        return successoCompleto;
    }
    
    private boolean assegnaPunti(Controller c, int idCliente, double totaleOrdine) {
        try {
            return c.uppunti(String.valueOf(idCliente), totaleOrdine * 0.10);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Errore punti: " + ex.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}