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
import javax.swing.table.DefaultTableModel;

import Model.Articoli;
import Model.Ordine;

public class CarrelloFrame extends JFrame {
	private Controller c;
	private JPanel contentPane;
	private DefaultTableModel prodmodel = new DefaultTableModel();
	private DefaultTableModel ordmodel = new DefaultTableModel();
	private Object[] prodcolonne = { "Id", "Nome", "Prezzo", "Categoria", "Scorta" };
	private Object[] ordinecolonne = { "Id", "Nome", "Prezzo", "Categoria", "Quantita" };
	private LocalDate dataod = LocalDate.now();
	private JPanel bottonpanel;
	private JPanel prodottopanel;
	private JPanel ordinepanel;
	private JPanel centerpanel;
	private JLabel datalab;
	private JLabel codfisclab;
	private JTextField codfisctf;
	private JButton backbutton;
	private JButton ordinebutton;
	private JComboBox<String> categoriacb;
	private JButton selectbutton;
	private JLabel quantitalab;
	private JTextField quantitatf;
	private JLabel totalelab;
	private JButton removebutton;
	private JButton insertbutton;
	private JTable prodottotable;
	private JScrollPane prodottiscrollPane;
	private JTable ordinetable;
	private JScrollPane ordinescrollPane;
	private JPanel titlepanel;
	private JLabel titlelabel;

	public void elementi() {
	    // Imposta le proprietà della finestra
	    setBackground(Color.WHITE);
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setBounds(100, 100, 1100, 500);
	    setLocationRelativeTo(null);
	    setIconImage(Toolkit.getDefaultToolkit().getImage(AdminFrame.class.getResource("/Immagini/ImmIcon.png")));

	    // Inizializza e configura il pannello principale
	    contentPane = new JPanel();
	    contentPane.setBackground(Color.WHITE);
	    contentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	    setContentPane(contentPane);
	    contentPane.setLayout(new BorderLayout(0, 0));

	    // Pannello per i bottoni
	    bottonpanel = new JPanel();
	    bottonpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	    contentPane.add(bottonpanel, BorderLayout.SOUTH);

	    // Etichetta e campo di testo per la data
	    datalab = new JLabel("Data Odierna : " + dataod);
	    bottonpanel.add(datalab);

	    // Etichetta e campo di testo per il codice fiscale
	    codfisclab = new JLabel("Codice Fiscale Cliente : ");
	    bottonpanel.add(codfisclab);

	    codfisctf = new JTextField();
	    codfisctf.setColumns(15);
	    bottonpanel.add(codfisctf);

	    // Pulsante per inserire l'ordine
	    ordinebutton = new JButton("Inserisci Ordine");
	    ordinebutton.setBackground(Color.GREEN);
	    bottonpanel.add(ordinebutton);

	    // Pulsante per tornare indietro
	    backbutton = new JButton("Indietro");
	    backbutton.setBackground(Color.RED);
	    bottonpanel.add(backbutton);

	    // Pannello per la tabella dei prodotti
	    prodottopanel = new JPanel();
	    prodottopanel.setLayout(new BorderLayout());
	    contentPane.add(prodottopanel, BorderLayout.WEST);

	    // Configura la tabella dei prodotti
	    prodmodel.setColumnIdentifiers(prodcolonne);
	    prodottotable = new JTable(prodmodel);
	    prodottotable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    prodottiscrollPane = new JScrollPane(prodottotable);
	    prodottopanel.add(prodottiscrollPane, BorderLayout.CENTER);

	    // Pannello per la tabella degli ordini
	    ordinepanel = new JPanel();
	    ordinepanel.setLayout(new BorderLayout());
	    contentPane.add(ordinepanel, BorderLayout.EAST);

	    // Configura la tabella degli ordini
	    ordmodel.setColumnIdentifiers(ordinecolonne);
	    ordinetable = new JTable(ordmodel);
	    ordinetable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    ordinescrollPane = new JScrollPane(ordinetable);
	    ordinepanel.add(ordinescrollPane, BorderLayout.CENTER);

	    // Pannello centrale per i controlli
	    centerpanel = new JPanel();
	    centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.Y_AXIS));
	    contentPane.add(centerpanel, BorderLayout.CENTER);

	    // Combobox per le categorie
	    categoriacb = new JComboBox<>(new String[]{"Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei"});
	    centerpanel.add(categoriacb);

	    // Pulsante per selezionare la categoria
	    selectbutton = new JButton("Seleziona");
	    centerpanel.add(selectbutton);

	    // Etichetta e campo di testo per la quantità
	    quantitalab = new JLabel("Seleziona Quantita :");
	    centerpanel.add(quantitalab);

	    quantitatf = new JTextField();
	    quantitatf.setColumns(5);
	    centerpanel.add(quantitatf);

	    // Etichetta per il totale
	    totalelab = new JLabel("Totale :  0.00");
	    centerpanel.add(totalelab);

	    // Pulsante per rimuovere un prodotto dall'ordine
	    removebutton = new JButton("Rimuovi");
	    removebutton.setBackground(Color.RED);
	    centerpanel.add(removebutton);

	    // Pulsante per inserire un prodotto nell'ordine
	    insertbutton = new JButton("Inserisci ");
	    insertbutton.setBackground(new Color(0, 153, 255));
	    centerpanel.add(insertbutton);

	    // Pannello per il titolo
	    titlepanel = new JPanel();
	    titlepanel.setBackground(new Color(0, 0, 139));
	    titlepanel.setPreferredSize(new Dimension(getWidth(), 60));
	    contentPane.add(titlepanel, BorderLayout.NORTH);

	    // Etichetta del titolo
	    titlelabel = new JLabel("Nuovo Ordine");
	    titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
	    titlelabel.setForeground(Color.WHITE);
	    titlepanel.add(titlelabel);
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
	    // Assegna l'ActionListener per il pulsante di selezione categoria
	    selectbutton.addActionListener(e -> {
	        prodmodel.setRowCount(0); // Resetta il modello della tabella dei prodotti
	        try {
	            String categoria = (String) categoriacb.getSelectedItem();
	            if (categoria != null) {
	                c.categoriaprodotti(categoria, prodmodel); // Popola il modello con i prodotti per la categoria selezionata
	            }
	        } catch (SQLException e1) {
	            JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: \n" + e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
	        }
	    });

	    // Assegna l'ActionListener per il pulsante di inserimento
	    insertbutton.addActionListener(e -> {
	        int i = prodottotable.getSelectedRow();
	        String quantitaText = quantitatf.getText();

	        // Verifica le condizioni di errore e visualizza i messaggi appropriati
	        if (i == -1) {
	            JOptionPane.showMessageDialog(null, "Seleziona un prodotto!" + (quantitaText.isEmpty() ? " e la quantita'!" : ""));
	        } else if (quantitaText.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "Seleziona la quantita'!");
	        } else {
	            try {
	                int quantita = Integer.parseInt(quantitaText);
	                int scorte = Integer.parseInt(prodmodel.getValueAt(i, 4).toString());

	                if (scorte < quantita) {
	                    JOptionPane.showMessageDialog(null, "Scorte insufficienti!");
	                } else {
	                    // Aggiunge il prodotto e la quantita' alla tabella degli ordini
	                    Object[] p = { prodmodel.getValueAt(i, 0), prodmodel.getValueAt(i, 1), prodmodel.getValueAt(i, 2),
	                                   prodmodel.getValueAt(i, 3), quantitaText };
	                    ordmodel.addRow(p);
	                    quantitatf.setText(""); // Resetta il campo di testo della quantita'
	                    totale(); // Aggiorna il totale
	                }
	            } catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(null, "Quantità non valida!", "Errore", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    });

	    // Assegna l'ActionListener per il pulsante di rimozione
	    removebutton.addActionListener(e -> {
	        int selectedRow = ordinetable.getSelectedRow();
	        if (selectedRow != -1) {
	            ordmodel.removeRow(selectedRow); // Rimuove la riga selezionata dalla tabella degli ordini
	            totale(); // Aggiorna il totale
	        } else {
	            JOptionPane.showMessageDialog(null, "Seleziona una riga da rimuovere!");
	        }
	    });

	    // Assegna l'ActionListener per il pulsante di ritorno
	    backbutton.addActionListener(e -> c.visAndCarr(2));

	    // Assegna l'ActionListener per il pulsante di ordine
	    ordinebutton.addActionListener(e -> {
	        try {
	            java.sql.Date sd = java.sql.Date.valueOf(dataod);
	            String idCliente = c.getct(codfisctf.getText());
	            String idDipendente = c.iddip;
	            double totaleOrdine = totale();
	            Ordine ordine = new Ordine("", sd, totaleOrdine, idCliente, idDipendente);

	            c.nuovoordine(ordine);

	            for (int j = 0; j < ordmodel.getRowCount(); j++) {
	                int quantita = Integer.parseInt(ordmodel.getValueAt(j, 4).toString());
	                String codiceProdotto = ordmodel.getValueAt(j, 0).toString();
	                double prezzoUnitario = Double.parseDouble(ordmodel.getValueAt(j, 2).toString());

	                c.upscorte(quantita, codiceProdotto);
	                Articoli articoli = new Articoli(c.CurrOrd(), idCliente, prezzoUnitario, prezzoUnitario * quantita, quantita, ordmodel.getValueAt(j, 3).toString());
	                c.newarticoli(articoli);
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
