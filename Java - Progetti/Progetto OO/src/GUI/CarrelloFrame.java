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
    
    public void elementi() throws SQLException {
        // Configurazione del frame
        setBackground(Color.WHITE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 500);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(AdminFrame.class.getResource("/Immagini/ImmIcon.png")));

        // Creazione del pannello principale
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Pannello del titolo
        titlepanel = new JPanel();
        titlepanel.setBackground(new Color(0, 0, 139));
        contentPane.add(titlepanel, BorderLayout.NORTH);

        titlelabel = new JLabel("Nuovo Ordine");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE);
        titlepanel.add(titlelabel);

        // Pannello dei bottoni in basso
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

        // Pannello dei prodotti
        prodottopanel = new JPanel(new BorderLayout());
        prodottopanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(prodottopanel, BorderLayout.WEST);

        prodottiscrollPane = new JScrollPane();
        prodottopanel.add(prodottiscrollPane, BorderLayout.CENTER);

        prodottotable = new JTable();
        prodottotable.setModel(new DefaultTableModel(new Object[]{"Id", "Nome", "Prezzo", "Categoria", "Scorta"}, 0)); // Modifica per mostrare solo le colonne desiderate
        prodottotable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        prodottiscrollPane.setViewportView(prodottotable);

        // Pannello degli ordini
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

        // Pannello centrale per controlli
        centerpanel = new JPanel();
        centerpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.Y_AXIS));
        contentPane.add(centerpanel, BorderLayout.CENTER);

        // Pannello superiore per ComboBox e bottone Seleziona
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        centerpanel.add(topPanel);

        categoriacb = new JComboBox<>(new String[]{"Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei"});
        categoriacb.setMaximumSize(categoriacb.getPreferredSize());
        topPanel.add(categoriacb);

        selectbutton = new JButton("Seleziona");
        topPanel.add(selectbutton);

        // Pannello per la quantità e il totale
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

        // Pannello inferiore per i bottoni
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

    public void clean() {
        totalelab.setText("Totale :  0.00");
        quantitatf.setText("");
        ordModel.setRowCount(0);
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
        // Popola il JComboBox con i dati del c.clienteModel
        for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
            String id = c.clienteModel.getValueAt(i, 0).toString();
            String nome = c.clienteModel.getValueAt(i, 1).toString(); // Assumendo che il nome sia nella colonna 1
            String cognome = c.clienteModel.getValueAt(i, 2).toString(); // Assumendo che il cognome sia nella colonna 2
            clienteComboBox.addItem(id + " - " + nome + " " + cognome);
        }

        // Popola il JTable con i dati filtrati dal c.prodModel
        for (int i = 0; i < c.prodModel.getRowCount(); i++) {
            Object[] rowData = new Object[5];
            rowData[0] = c.prodModel.getValueAt(i, 0); // Id
            rowData[1] = c.prodModel.getValueAt(i, 1); // Nome
            rowData[2] = c.prodModel.getValueAt(i, 3); // Prezzo
            rowData[3] = c.prodModel.getValueAt(i, 9); // Categoria
            rowData[4] = c.prodModel.getValueAt(i, 10); // Scorta
            ((DefaultTableModel) prodottotable.getModel()).addRow(rowData);
        }
    }

    private void filtraProdotti(Controller c) {
        // Ottieni la categoria selezionata dall'utente
        String categoriaSelezionata = (String) categoriacb.getSelectedItem();

        // Crea un nuovo DefaultTableModel per contenere i prodotti filtrati
        DefaultTableModel filteredModel = new DefaultTableModel();
        filteredModel.setColumnIdentifiers(prodcolonne); // Imposta le colonne desiderate

        // Filtra i dati del modello completo e aggiungi solo i prodotti della categoria selezionata al nuovo modello
        for (int i = 0; i < c.prodModel.getRowCount(); i++) {
            String categoriaProdotto = c.prodModel.getValueAt(i, 9).toString(); // Assumiamo che la colonna 9 contenga la categoria
            if (categoriaProdotto.equalsIgnoreCase(categoriaSelezionata)) {
                filteredModel.addRow(new Object[]{
                    c.prodModel.getValueAt(i, 0), // Id
                    c.prodModel.getValueAt(i, 1), // Nome
                    c.prodModel.getValueAt(i, 2), // Prezzo
                    categoriaProdotto,              // Categoria
                    c.prodModel.getValueAt(i, 10)  // Scorta
                });
            }
        }

        // Imposta il nuovo modello filtrato alla JTable
        prodottotable.setModel(filteredModel);
    }

    private void inserisciProdotto(Controller c) {
        int selectedRow = prodottotable.getSelectedRow();
        String quantitaText = quantitatf.getText().trim(); // Rimuove eventuali spazi bianchi

        // Controllo che sia selezionato un prodotto e che la quantità non sia vuota
        if (selectedRow == -1 || quantitaText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleziona un prodotto e inserisci una quantità!");
            return;
        }

        try {
            // Controlla se la quantità è un numero intero positivo
            int quantita = Integer.parseInt(quantitaText);
            if (quantita <= 0) {
                JOptionPane.showMessageDialog(null, "La quantità deve essere un numero positivo!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Ottieni le scorte e il prezzo unitario
            int scorte = Integer.parseInt(c.prodModel.getValueAt(selectedRow, 10).toString());
            double prezzoUnitario = Double.parseDouble(c.prodModel.getValueAt(selectedRow, 2).toString());

            // Verifica disponibilità
            if (scorte < quantita) {
                JOptionPane.showMessageDialog(null, "Scorte insufficienti!");
                return;
            }

            // Calcola il totale per il prodotto e aggiungi la riga al modello
            double totaleProdotto = prezzoUnitario * quantita;
            Object[] p = {
                c.prodModel.getValueAt(selectedRow, 0), // Id
                c.prodModel.getValueAt(selectedRow, 1), // Nome
                prezzoUnitario,                          // Prezzo
                c.prodModel.getValueAt(selectedRow, 3), // Categoria
                quantita,                                // Quantità
                totaleProdotto                            // Totale per il prodotto
            };

            ordModel.addRow(p); // Usa il modello locale
            quantitatf.setText(""); // Pulisci il campo della quantità
            totale(); // Aggiorna il totale generale
        } catch (NumberFormatException ex) {
            // Messaggio di errore per input non valido
            JOptionPane.showMessageDialog(null, "Quantità non valida! Assicurati di inserire un numero intero.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void creaOrdine(Controller c) {
        try {
            java.sql.Date sd = java.sql.Date.valueOf(dataod);
            String clienteSelezionato = (String) clienteComboBox.getSelectedItem();
            String idCliente = null;

            // Cerca l'ID del cliente nel clienteModel basato su nome e cognome selezionato
            for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
                String idClienteTemp = c.clienteModel.getValueAt(i, 0).toString(); // Colonna ID cliente
                String nome = c.clienteModel.getValueAt(i, 1).toString(); // Colonna nome
                String cognome = c.clienteModel.getValueAt(i, 2).toString(); // Colonna cognome
                String nomeCognome = idClienteTemp + " - " + nome + " " + cognome; // Formato ID - Nome Cognome

                if (clienteSelezionato.equals(nomeCognome)) {
                    idCliente = idClienteTemp; // Colonna ID cliente
                    break;
                }
            }

            if (idCliente != null) {
                String idDipendente = c.iddip;
                double totaleOrdine = totale();

                c.nuovoordine(new Ordine("", sd, totaleOrdine, idCliente, idDipendente)); // Crea un nuovo ordine nel database

                // Aggiorna i dettagli dell'ordine
                for (int j = 0; j < c.ordModel.getRowCount(); j++) {
                    int quantita = Integer.parseInt(c.ordModel.getValueAt(j, 4).toString()); // Quantità
                    String codiceProdotto = c.ordModel.getValueAt(j, 0).toString(); // Codice Prodotto
                    double prezzoUnitario = Double.parseDouble(c.ordModel.getValueAt(j, 2).toString()); // Prezzo

                    c.upscorte(quantita, codiceProdotto); // Aggiorna le scorte
                    Articoli articoli = new Articoli(c.CurrOrd(), idCliente, prezzoUnitario, prezzoUnitario * quantita, quantita, c.ordModel.getValueAt(j, 3).toString());
                    c.newarticoli(articoli); // Aggiunge articoli all'ordine
                }

                c.uppunti(idCliente, totaleOrdine); // Aggiorna i punti del cliente
                JOptionPane.showMessageDialog(null, "Ordine aggiunto");
                clean(); // Pulisce i campi
            } else {
                JOptionPane.showMessageDialog(null, "Cliente non trovato!");
            }
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: " + e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e2) {
            JOptionPane.showMessageDialog(null, "Data non valida!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void azioni(Controller c) throws SQLException {
        popolazioni(c);
        
        // Listener per il pulsante di selezione categoria
        selectbutton.addActionListener(e -> filtraProdotti(c));

        // Listener per il pulsante di inserimento
        insertbutton.addActionListener(e -> inserisciProdotto(c));

        // Listener per il pulsante di rimozione
        removebutton.addActionListener(e -> {
            int selectedRow = ordinetable.getSelectedRow();
            if (selectedRow != -1) {
                ordModel.removeRow(selectedRow); // Usa il modello locale
                totale();
            }
        });

        // Listener per il pulsante di ordine
        ordinebutton.addActionListener(e -> creaOrdine(c));
        
        // Pulsante di ritorno
        backbutton.addActionListener(e -> c.visAndElem(1, 2)); 
    }

	public CarrelloFrame(String title, Controller c) throws SQLException {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}






