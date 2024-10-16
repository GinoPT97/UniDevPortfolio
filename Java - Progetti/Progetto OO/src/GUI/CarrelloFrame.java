package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private DefaultTableModel prodmodel = new DefaultTableModel();
	private DefaultTableModel ordmodel = new DefaultTableModel();
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
        prodmodel.setColumnIdentifiers(prodcolonne); // Usa le colonne corrette: Id, Nome, Prezzo, Categoria, Scorta
        prodottotable.setModel(prodmodel); // Imposta il model dal Controller
        prodottotable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        prodottiscrollPane.setViewportView(prodottotable);

        // Pannello degli ordini
        ordinepanel = new JPanel(new BorderLayout());
        ordinepanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(ordinepanel, BorderLayout.EAST);

        ordinescrollPane = new JScrollPane();
        ordinepanel.add(ordinescrollPane, BorderLayout.CENTER);

        ordinetable = new JTable();
        ordmodel.setColumnIdentifiers(ordinecolonne);
        ordinetable.setModel(ordmodel);
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
        ordmodel.setRowCount(0);
    }

    public double totale() {
        double tot = 0.00;
        for (int j = 0; j < ordmodel.getRowCount(); j++) {
            double prezzoUnitario = Double.parseDouble(ordmodel.getValueAt(j, 2).toString());
            int quantita = Integer.parseInt(ordmodel.getValueAt(j, 4).toString());
            tot += prezzoUnitario * quantita;
        }
        totalelab.setText("Totale : " + String.format("%.2f", tot));
        return tot;
    }

    // Funzione ausiliaria per popolare il JComboBox dei clienti
    private void popolaClienteComboBox(Controller c) throws SQLException {
        clienteComboBox.removeAllItems(); // Rimuove elementi esistenti
        for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
            String id = c.clienteModel.getValueAt(i, 0).toString();
            String nome = c.clienteModel.getValueAt(i, 1).toString(); // Assumendo che il nome sia nella colonna 1
            String cognome = c.clienteModel.getValueAt(i, 2).toString(); // Assumendo che il cognome sia nella colonna 2
            clienteComboBox.addItem(id + " - " + nome + " " + cognome);
        }
    }

    // Funzione ausiliaria per selezionare i prodotti in base alla categoria
    private void selezionaCategoria(Controller c) {
        prodmodel.setRowCount(0); // Resetta la tabella dei prodotti
        String categoriaSelezionata = categoriacb.getSelectedItem().toString();
        try {
            c.categoriaprodotti(categoriaSelezionata, prodmodel);
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : \n" + e1);
        }
    }

    // Funzione ausiliaria per rimuovere un prodotto dall'ordine
    private void rimuoviProdotto() {
        int selectedRow = ordinetable.getSelectedRow();
        if (selectedRow != -1) {
            ordmodel.removeRow(selectedRow); // Usa il modello locale
            totale(); // Aggiorna il totale
        }
    }

    private void inserisciProdotto(Controller c) {
        int selectedRow = prodottotable.getSelectedRow();
        System.out.println("Riga selezionata: " + selectedRow);
        String quantitaText = quantitatf.getText().trim(); // Rimuove eventuali spazi bianchi

        // Controllo che sia selezionato un prodotto e che la quantità non sia vuota
        if (selectedRow == -1 || quantitaText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleziona un prodotto e inserisci una quantità!");
            return;
        }

        // Controllo se la quantità è un numero intero positivo
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

        // Ottieni le scorte e il prezzo unitario
        try {
            // Ottieni le scorte e il prezzo unitario dal modello
            int scorte = Integer.parseInt(c.prodModel.getValueAt(selectedRow, 10).toString()); // Indice 10 per le scorte
            double prezzoUnitario = Double.parseDouble(c.prodModel.getValueAt(selectedRow, 3).toString()); // Indice 3 per il prezzo

            // Verifica disponibilità
            if (scorte < quantita) {
                JOptionPane.showMessageDialog(null, "Scorte insufficienti!");
                return;
            }

            // Aggiorna le scorte nel modello dei prodotti
            String newscorte = String.valueOf(scorte - quantita);
            c.prodModel.setValueAt(newscorte, selectedRow, 10); // Rimuovi la quantità selezionata dal modello del prodotto

            // Calcola il totale per il prodotto e aggiungi la riga al modello degli ordini
            double totaleProdotto = prezzoUnitario * quantita;
            Object[] p = {
                c.prodModel.getValueAt(selectedRow, 0), // Id
                c.prodModel.getValueAt(selectedRow, 1), // Nome
                prezzoUnitario,                          // Prezzo
                c.prodModel.getValueAt(selectedRow, 9), // Categoria (indice 9)
                quantita,                                // Quantità
                totaleProdotto                            // Totale per il prodotto
            };

            ordmodel.addRow(p); // Usa il modello locale per gli ordini
            quantitatf.setText(""); // Pulisci il campo della quantità
            totale(); // Aggiorna il totale generale
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Dati non validi nel prodotto selezionato!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void creaOrdine(Controller c) {
        try {
            // Assicurati che dataod non sia null
            if (dataod == null) {
                JOptionPane.showMessageDialog(null, "Data non valida!", "Errore", JOptionPane.ERROR_MESSAGE);
                return; // Esci dal metodo se la data non è valida
            }

            java.sql.Date sd = java.sql.Date.valueOf(dataod); // Converti LocalDate in java.sql.Date
            String clienteSelezionato = (String) clienteComboBox.getSelectedItem();
            String idCliente = trovaIdCliente(c, clienteSelezionato);

            String idDipendente = c.iddip; // Nessun controllo, usa direttamente il valore

            if (idCliente != null) {
                double totaleOrdine = totale();

                // Crea un nuovo ordine nel database
                boolean ordineCreato = c.nuovoordine(new Ordine("", sd, totaleOrdine, idCliente, idDipendente));
                if (ordineCreato) {
                    // Aggiorna gli ordini e le scorte nel database
                    for (int j = 0; j < ordmodel.getRowCount(); j++) {
                        int quantita = Integer.parseInt(ordmodel.getValueAt(j, 4).toString()); // Quantità
                        String codiceProdotto = ordmodel.getValueAt(j, 0).toString(); // Codice Prodotto

                        // Aggiorna le scorte nel database
                        boolean scorteAggiornate = c.upscorte(quantita, codiceProdotto);
                        if (!scorteAggiornate) {
                            JOptionPane.showMessageDialog(null, "Errore nell'aggiornamento delle scorte per il prodotto: " + codiceProdotto, "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    // Aggiorna i punti del cliente
                    String puntiAttualiStr = c.punti(idCliente);
                    double puntiAttuali = Double.parseDouble(puntiAttualiStr);
                    double nuoviPunti = puntiAttuali + calcolaPuntiOrdine(totaleOrdine); // Calcola i nuovi punti

                    // Aggiorna i punti nel database
                    boolean puntiAggiornati = c.uppunti(idCliente, nuoviPunti);
                    if (!puntiAggiornati) {
                        JOptionPane.showMessageDialog(null, "Errore nell'aggiornamento dei punti per il cliente: " + idCliente, "Errore", JOptionPane.ERROR_MESSAGE);
                    }

                    // Pulisci l'interfaccia utente
                    clean();
                } else {
                    JOptionPane.showMessageDialog(null, "Errore durante la creazione dell'ordine nel database!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cliente non valido selezionato!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Errore durante la creazione dell'ordine: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo per calcolare i punti basati sull'ordine
    private double calcolaPuntiOrdine(double totaleOrdine) {
        return totaleOrdine * 0.1; // Esempio: 10% del totale come punti
    }

    // Metodo ausiliario per trovare l'ID del cliente
    private String trovaIdCliente(Controller c, String clienteSelezionato) {
        for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
            String idClienteTemp = c.clienteModel.getValueAt(i, 0).toString(); // Colonna ID cliente
            String nome = c.clienteModel.getValueAt(i, 1).toString(); // Colonna nome
            String cognome = c.clienteModel.getValueAt(i, 2).toString(); // Colonna cognome
            String nomeCognome = idClienteTemp + " - " + nome + " " + cognome; // Formato ID - Nome Cognome

            if (clienteSelezionato.equals(nomeCognome)) {
                return idClienteTemp; // Colonna ID cliente
            }
        }
        return null; // Se non trovato
    }

    public void azioni(Controller c) throws SQLException {
        
        // Popola il JComboBox con i dati del c.clienteModel
        popolaClienteComboBox(c);

        // Listener per il pulsante di selezione categoria
        selectbutton.addActionListener(e -> selezionaCategoria(c));

        // Listener per il pulsante di inserimento
        insertbutton.addActionListener(e -> inserisciProdotto(c));

        // Listener per il pulsante di rimozione
        removebutton.addActionListener(e -> rimuoviProdotto());

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






