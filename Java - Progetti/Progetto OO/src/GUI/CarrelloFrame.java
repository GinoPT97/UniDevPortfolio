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
    private DefaultTableModel prodmodel = new DefaultTableModel();
    private DefaultTableModel ordmodel = new DefaultTableModel();
    private Object[] prodcolonne = { "Id", "Nome", "Prezzo", "Categoria", "Scorta" };
    private Object[] ordinecolonne = { "Id", "Nome", "Prezzo", "Categoria", "Quantita" };
    private LocalDate dataod = LocalDate.now();
    private JPanel bottonpanel, prodottopanel, ordinepanel, centerpanel, titlepanel;
    private JLabel datalab, codfisclab, quantitalab, totalelab, titlelabel;
    private JTextField codfisctf, quantitatf;
    private JButton backbutton, ordinebutton, selectbutton, removebutton, insertbutton;
    private JComboBox<String> categoriacb;
    private JTable prodottotable, ordinetable;
    private JScrollPane prodottiscrollPane, ordinescrollPane;

    public void elementi() {
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
        bottonpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // FlowLayout centrato
        contentPane.add(bottonpanel, BorderLayout.SOUTH);

        // Aggiunta dei componenti al bottonpanel
        datalab = new JLabel("Data Odierna: " + dataod.toString());
        bottonpanel.add(datalab);

        codfisclab = new JLabel("Codice Fiscale Cliente: ");
        bottonpanel.add(codfisclab);

        codfisctf = new JTextField(16); // Dimensioni abbastanza grandi per il codice fiscale
        codfisctf.setPreferredSize(new Dimension(200, 25)); // Dimensione aumentata
        bottonpanel.add(codfisctf);

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
        prodmodel.setColumnIdentifiers(prodcolonne);
        prodottotable.setModel(prodmodel);
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
        centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.Y_AXIS)); // Layout verticale per allineare i componenti
        contentPane.add(centerpanel, BorderLayout.CENTER);

        // Pannello superiore per ComboBox e bottone Seleziona
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        centerpanel.add(topPanel);

        categoriacb = new JComboBox<>(new String[]{"Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei"});
        categoriacb.setMaximumSize(categoriacb.getPreferredSize()); // Si adatta al contenuto
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
	    codfisctf.setText("");
	    prodmodel.setRowCount(0);
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

	public void azioni(Controller c) throws SQLException {
	    // Pulsante di selezione categoria
	    selectbutton.addActionListener(e -> {
	        prodmodel.setRowCount(0); // Resetta il modello della tabella dei prodotti
	        try {
	            String categoria = (String) categoriacb.getSelectedItem();
	            if (categoria != null) {
	                c.categoriaprodotti(categoria, c.prodModel); // Popola il modello con i prodotti per la categoria selezionata
	            }
	        } catch (SQLException e1) {
	            JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: " + e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
	        }
	    });

	    // Pulsante di inserimento
	    insertbutton.addActionListener(e -> {
	        int selectedRow = prodottotable.getSelectedRow();
	        String quantitaText = quantitatf.getText();

	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(null, "Seleziona un prodotto!" + (quantitaText.isEmpty() ? " e la quantità!" : ""));
	            return;
	        }

	        if (quantitaText.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "Seleziona la quantità!");
	            return;
	        }

	        try {
	            int quantita = Integer.parseInt(quantitaText);
	            int scorte = Integer.parseInt(prodmodel.getValueAt(selectedRow, 4).toString());

	            if (scorte < quantita) {
	                JOptionPane.showMessageDialog(null, "Scorte insufficienti!");
	            } else {
	                // Aggiunge il prodotto e la quantità alla tabella degli ordini
	                Object[] p = {
	                    prodmodel.getValueAt(selectedRow, 0),
	                    prodmodel.getValueAt(selectedRow, 1),
	                    prodmodel.getValueAt(selectedRow, 2),
	                    prodmodel.getValueAt(selectedRow, 3),
	                    quantitaText
	                };
	                c.ordModel.addRow(p); // Aggiorna il modello ordini dal controller
	                quantitatf.setText(""); // Resetta il campo di testo della quantità
	                totale(); // Aggiorna il totale
	            }
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(null, "Quantità non valida!", "Errore", JOptionPane.ERROR_MESSAGE);
	        }
	    });

	    // Pulsante di rimozione
	    removebutton.addActionListener(e -> {
	        int selectedRow = ordinetable.getSelectedRow();
	        if (selectedRow != -1) {
	            c.ordModel.removeRow(selectedRow); // Rimuove la riga selezionata dalla tabella degli ordini
	            totale(); // Aggiorna il totale
	        } else {
	            JOptionPane.showMessageDialog(null, "Seleziona una riga da rimuovere!");
	        }
	    });

	    // Pulsante di ritorno
	    backbutton.addActionListener(e -> c.visAndElem(1, 2)); 

	    // Pulsante di ordine
	    ordinebutton.addActionListener(e -> {
	        try {
	            java.sql.Date sd = java.sql.Date.valueOf(dataod);
	            String idCliente = c.getct(codfisctf.getText());
	            String idDipendente = c.iddip;
	            double totaleOrdine = totale();

	            Ordine ordine = new Ordine("", sd, totaleOrdine, idCliente, idDipendente);
	            c.nuovoordine(ordine); // Crea un nuovo ordine nel database

	            // Aggiorna i dettagli dell'ordine
	            for (int j = 0; j < c.ordModel.getRowCount(); j++) {
	                int quantita = Integer.parseInt(c.ordModel.getValueAt(j, 4).toString());
	                String codiceProdotto = c.ordModel.getValueAt(j, 0).toString();
	                double prezzoUnitario = Double.parseDouble(c.ordModel.getValueAt(j, 2).toString());

	                c.upscorte(quantita, codiceProdotto); // Aggiorna le scorte
	                Articoli articoli = new Articoli(c.CurrOrd(), idCliente, prezzoUnitario, prezzoUnitario * quantita, quantita, c.ordModel.getValueAt(j, 3).toString());
	                c.newarticoli(articoli); // Aggiunge articoli all'ordine
	            }

	            c.uppunti(idCliente, totaleOrdine); // Aggiorna i punti del cliente
	            JOptionPane.showMessageDialog(null, "Ordine aggiunto");
	            clean(); // Pulisce i campi
	        } catch (SQLException e1) {
	            JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: " + e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
	        } catch (IllegalArgumentException e2) {
	            JOptionPane.showMessageDialog(null, "Data non valida!", "Errore", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	}


	public CarrelloFrame(String title, Controller c) throws SQLException {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
