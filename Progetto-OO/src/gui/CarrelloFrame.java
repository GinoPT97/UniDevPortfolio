package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import controller.Controller;

public class CarrelloFrame extends JFrame {
    private static final String ERROR_TITLE = "Errore";
    private static final String CATEGORIA_TUTTI = "Tutti";
    private static final String FONT_TAHOMA = "Tahoma";
    // Categorie allineate a NuovoProdottoFrame
    private static final String[] CATEGORIE = {CATEGORIA_TUTTI, "Frutta", "Verdura", "Farinacei", "Latticini", "Uova", "Confezionati"};
    private static final String[] PROD_COLUMNS = {"Id", "Nome", "Prezzo", "Categoria", "Scorta"};
    private static final String[] ORDINE_COLUMNS = {"Id", "Nome", "Prezzo", "Categoria", "Quantita"};

    private final DefaultTableModel ordModel = new DefaultTableModel();
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
        initComponents();
        setupActions(c);
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 500);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);

        contentPane.add(createHeader(), BorderLayout.NORTH);
        contentPane.add(createFooter(), BorderLayout.SOUTH);
        contentPane.add(createProductTable(), BorderLayout.WEST);
        contentPane.add(createOrderTable(), BorderLayout.EAST);
        contentPane.add(createControls(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 139));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel label = new JLabel("Carrello - Nuovo Ordine");
        label.setFont(new Font(FONT_TAHOMA, Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        panel.add(label);
        return panel;
    }

    private JPanel createFooter() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Data: " + dataod));
        panel.add(new JLabel("Cliente: "));

        clienteComboBox = new JComboBox<>();
        clienteComboBox.setPreferredSize(new Dimension(200, 25));
        panel.add(clienteComboBox);

        ordinebutton = createButton("Inserisci Ordine", new Color(34, 139, 34));
        panel.add(ordinebutton);
        backbutton = createButton("Indietro", new Color(178, 34, 34));
        panel.add(backbutton);

        return panel;
    }

    private JPanel createProductTable() {
        prodottotable = new JTable(new DefaultTableModel(PROD_COLUMNS, 0));
        prodottotable.setAutoCreateRowSorter(true);
        prodottotable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) c.setBackground(row % 2 == 0 ? java.awt.Color.WHITE : new java.awt.Color(240,240,240));
                return c;
            }
        });
        prodottotable.getTableHeader().setToolTipText("Clicca per ordinare la colonna");
        return createTablePanel("Prodotti Disponibili", prodottotable);
    }

    private JPanel createOrderTable() {
        ordinetable = new JTable(ordModel);
        ordinetable.setAutoCreateRowSorter(true);
        ordinetable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) c.setBackground(row % 2 == 0 ? java.awt.Color.WHITE : new java.awt.Color(240,240,240));
                return c;
            }
        });
        ordinetable.getTableHeader().setToolTipText("Clicca per ordinare la colonna");
        ordModel.setColumnIdentifiers(ORDINE_COLUMNS);
        return createTablePanel("Ordine Corrente", ordinetable);
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

    private JPanel createControls() {
        JPanel center = new JPanel();
        center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoriacb = new JComboBox<>(CATEGORIE);
        top.add(categoriacb);
        selectbutton = createButton("Filtra", new Color(70, 130, 180));
        top.add(selectbutton);

        JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        middle.add(new JLabel("Quantità:"));
        quantitatf = new JTextField(5);
        middle.add(quantitatf);
        totalelab = new JLabel("Totale: 0.00");
        totalelab.setFont(new Font(FONT_TAHOMA, Font.BOLD, 14));
        middle.add(totalelab);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        insertbutton = createButton("Inserisci", new Color(0, 153, 255));
        bottom.add(insertbutton);
        removebutton = createButton("Rimuovi", new Color(178, 34, 34));
        bottom.add(removebutton);

        center.add(top);
        center.add(middle);
        center.add(bottom);
        return center;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text, IconUtils.getIconForText(text, color));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    private void setupActions(Controller c) {
        popolazioni(c);
        selectbutton.addActionListener(e -> filtraProdotti(c));
        insertbutton.addActionListener(e -> inserisciProdotto());
        removebutton.addActionListener(e -> rimuoviProdotto());
        ordinebutton.addActionListener(e -> creaOrdine(c));
        backbutton.addActionListener(e -> c.visAndElem(1, 2));
    }

    public void clean() {
        totalelab.setText("Totale: 0.00");
        quantitatf.setText("");
        ordModel.setRowCount(0);
        clienteComboBox.setSelectedIndex(-1);
        categoriacb.setSelectedIndex(0);
    }

    public void inizializzaFrame(Controller c) {
        clean();
        popolazioni(c);
    }

    private void rimuoviProdotto() {
        int selectedRow = ordinetable.getSelectedRow();
        if (selectedRow != -1) {
            ordModel.removeRow(selectedRow);
            calcolaTotale();
        }
    }

    public double calcolaTotale() {
        double totale = 0.0;
        for (int i = 0; i < ordModel.getRowCount(); i++) {
            double prezzo = Double.parseDouble(ordModel.getValueAt(i, 2).toString());
            int quantita = Integer.parseInt(ordModel.getValueAt(i, 4).toString());
            totale += prezzo * quantita;
        }
        totalelab.setText("Totale: " + String.format("%.2f", totale));
        return totale;
    }

    public void popolazioni(Controller c) {
        clienteComboBox.removeAllItems();
        ((DefaultTableModel) prodottotable.getModel()).setRowCount(0);

        for (int i = 0; i < c.clienteModel.getRowCount(); i++)
			clienteComboBox.addItem(String.format("%s - %s %s",
                c.clienteModel.getValueAt(i, 0), c.clienteModel.getValueAt(i, 1), c.clienteModel.getValueAt(i, 2)));

        DefaultTableModel prodModel = (DefaultTableModel) prodottotable.getModel();
        for (int i = 0; i < c.prodModel.getRowCount(); i++)
			prodModel.addRow(new Object[]{
                c.prodModel.getValueAt(i, 0), c.prodModel.getValueAt(i, 1),
                c.prodModel.getValueAt(i, 3), c.prodModel.getValueAt(i, 10), c.prodModel.getValueAt(i, 11)
            });
    }

    private void filtraProdotti(Controller c) {
        String categoriaSelezionata = (String) categoriacb.getSelectedItem();
        DefaultTableModel filteredModel = new DefaultTableModel();
        filteredModel.setColumnIdentifiers(PROD_COLUMNS);

        for (int i = 0; i < c.prodModel.getRowCount(); i++) {
            String categoriaProdotto = c.prodModel.getValueAt(i, 10).toString();
            if (categoriaMatch(categoriaSelezionata, categoriaProdotto))
				filteredModel.addRow(new Object[]{
                    c.prodModel.getValueAt(i, 0), c.prodModel.getValueAt(i, 1),
                    c.prodModel.getValueAt(i, 3), categoriaProdotto, c.prodModel.getValueAt(i, 11)
                });
        }
        prodottotable.setModel(filteredModel);
    }

    // Estrae la logica di confronto categoria per ridurre la complessità cognitiva
    private boolean categoriaMatch(String categoriaSelezionata, String categoriaProdotto) {
        String catProdNorm = categoriaProdotto.trim().toLowerCase();
        String catSelNorm = categoriaSelezionata.trim().toLowerCase();
        return catSelNorm.equals(CATEGORIA_TUTTI.toLowerCase())
            || catProdNorm.equals(catSelNorm)
            || (catSelNorm.equals("frutta") && catProdNorm.contains("frutta"))
            || (catSelNorm.equals("verdura") && catProdNorm.contains("verdura"))
            || (catSelNorm.equals("farinacei") && catProdNorm.contains("farinacei"))
            || (catSelNorm.equals("latticini") && catProdNorm.contains("latticini"))
            || (catSelNorm.equals("uova") && catProdNorm.contains("uova"))
            || (catSelNorm.equals("confezionati") && (catProdNorm.contains("inscatolati") || catProdNorm.contains("confezionati")));
    }

    private void inserisciProdotto() {
        int selectedRow = prodottotable.getSelectedRow();
        String quantitaText = quantitatf.getText().trim();

        if (selectedRow == -1 || quantitaText.isEmpty()) {
            showError("Seleziona un prodotto e inserisci una quantità!");
            return;
        }

        try {
            int quantita = Integer.parseInt(quantitaText);
            if (quantita <= 0) {
                showError("La quantità deve essere un numero positivo!");
                return;
            }

            String codiceProdotto = prodottotable.getValueAt(selectedRow, 0).toString();
            String nomeProdotto = prodottotable.getValueAt(selectedRow, 1).toString();
            double prezzoUnitario = Double.parseDouble(prodottotable.getValueAt(selectedRow, 2).toString());
            String categoria = prodottotable.getValueAt(selectedRow, 3).toString();
            int scorte = Integer.parseInt(prodottotable.getValueAt(selectedRow, 4).toString());

            if (scorte < quantita) {
                showError("Scorte insufficienti!");
                return;
            }

            ordModel.addRow(new Object[]{codiceProdotto, nomeProdotto, prezzoUnitario, categoria, quantita});
            quantitatf.setText("");
            calcolaTotale();
            prodottotable.setValueAt(scorte - quantita, selectedRow, 4);

        } catch (NumberFormatException ex) {
            showError("Dati non validi!");
        }
    }

    private void creaOrdine(Controller c) {
        if (ordModel.getRowCount() == 0) {
            showError("Il carrello è vuoto!");
            return;
        }

        String clienteSelezionato = (String) clienteComboBox.getSelectedItem();
        if (clienteSelezionato == null || clienteSelezionato.trim().isEmpty()) {
            showError("Seleziona un cliente!");
            return;
        }

        try {
            Integer idCliente = findClientId(c, clienteSelezionato);
            if (idCliente == null) {
                showError("Cliente non trovato!");
                return;
            }

            int idDipendente = Integer.parseInt(c.iddip);
            double totaleOrdine = calcolaTotale();
            java.sql.Date dataOrdine = java.sql.Date.valueOf(dataod.toString());

            if (!c.nuovoordine("", dataOrdine, totaleOrdine, idCliente, idDipendente)) {
                showError("Errore nella creazione dell'ordine!");
                return;
            }

            String codOrdine = c.CurrOrd();
            boolean successoArticoli = processArticoli(c, codOrdine);
            boolean puntiAssegnati = assegnaPunti(c, idCliente, totaleOrdine);

            if (successoArticoli) {
                String messaggio = puntiAssegnati ?
                    "Ordine creato!\nPunti: " + String.format("%.2f", totaleOrdine * 0.10) :
                    "Ordine creato (errore punti)";
                JOptionPane.showMessageDialog(this, messaggio);
                clean();
            }

        } catch (SQLException | NumberFormatException e) {
            showError("Errore: " + e.getMessage());
        }
    }

    private Integer findClientId(Controller c, String clienteSelezionato) {
        for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
            String display = String.format("%s - %s %s",
                c.clienteModel.getValueAt(i, 0), c.clienteModel.getValueAt(i, 1), c.clienteModel.getValueAt(i, 2));
            if (clienteSelezionato.equals(display))
				return Integer.valueOf(c.clienteModel.getValueAt(i, 0).toString());
        }
        return null;
    }

    private boolean processArticoli(Controller c, String codOrdine) {
        for (int i = 0; i < ordModel.getRowCount(); i++)
			try {
                int quantita = Integer.parseInt(ordModel.getValueAt(i, 4).toString());
                String codiceProdotto = ordModel.getValueAt(i, 0).toString();
                double prezzo = Double.parseDouble(ordModel.getValueAt(i, 2).toString());

                if (!c.upscorte(quantita, codiceProdotto) ||
                    !c.newarticoli(codOrdine, codiceProdotto, prezzo, quantita)) {
                    showError("Errore per prodotto: " + codiceProdotto);
                    return false;
                }
            } catch (SQLException ex) {
                showError("Errore database: " + ex.getMessage());
                return false;
            }
        return true;
    }

    private boolean assegnaPunti(Controller c, int idCliente, double totale) {
        try {
            return c.uppunti(String.valueOf(idCliente), totale * 0.10);
        } catch (SQLException ex) {
            return false;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }
}