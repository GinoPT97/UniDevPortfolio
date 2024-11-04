package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class VisioneOrdineFrame extends JFrame {
	private JPanel contentPane;
	private JTable table;
	private JScrollPane scrollPane;
	private JPanel buttonpanel;
	private JPanel titlepanel;
	private JButton backbutton;
	private JLabel titleabel;
	private JButton ordinebutton;
	private JButton searchbutton;
	private JTextField searchtf;

	public void elementi(Controller c) {
	    // Imposta le proprietà del frame
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setBounds(100, 100, 1000, 450);
	    setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));
	    setLocationRelativeTo(null);

	    // Imposta il contenitore principale e layout
	    contentPane = new JPanel(new BorderLayout());
	    contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
	    setContentPane(contentPane);

	    // Pannello del titolo
	    titlepanel = new JPanel();
	    titlepanel.setBackground(new Color(0, 139, 139));
	    contentPane.add(titlepanel, BorderLayout.NORTH);

	    titleabel = new JLabel("Visualizzazione Ordini");
	    titleabel.setFont(new Font("Tahoma", Font.BOLD, 30));
	    titlepanel.add(titleabel);

	    // ScrollPane e JTable
	    scrollPane = new JScrollPane();
	    contentPane.add(scrollPane, BorderLayout.CENTER);

	    table = new JTable();
	    table.setModel(c.ordModel);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    scrollPane.setViewportView(table);

	    // Pannello dei bottoni
	    buttonpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    contentPane.add(buttonpanel, BorderLayout.SOUTH);

	    searchtf = new JTextField(10);
	    buttonpanel.add(searchtf);

	    searchbutton = new JButton("Cerca");
	    searchbutton.setBackground(new Color(60, 179, 113));
	    buttonpanel.add(searchbutton);

	    ordinebutton = new JButton("Nuovo Ordine");
	    ordinebutton.setBackground(Color.GREEN);
	    buttonpanel.add(ordinebutton);

	    backbutton = new JButton("Indietro");
	    backbutton.setBackground(Color.RED);
	    buttonpanel.add(backbutton);
	}

	public void azioni(Controller c) throws SQLException {
	    // Carica tutti gli ordini nel modello della tabella
	    c.allOrdini();

	    // Configura il pulsante di ricerca
	    searchbutton.addActionListener(e -> {
	        String query = searchtf.getText().trim().toLowerCase();
	        if (query.isEmpty()) {
	            // Se la query è vuota, mostra tutti i dati
	            table.setRowSorter(null); // Rimuove il filtro
	        } else {
	            // Applica il filtro sulla tabella
	            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(c.ordModel);
	            try {
	                // Imposta il filtro della tabella con la query
	                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
	            } catch (PatternSyntaxException ex) {
	                // Mostra un messaggio di errore se la sintassi dell'espressione regolare è errata
	                JOptionPane.showMessageDialog(null, "Errore nella sintassi dell'espressione regolare: " + ex.getMessage(), "Errore di Filtro", JOptionPane.ERROR_MESSAGE);
	                return; // Esci se c'è un errore di sintassi
	            }
	            table.setRowSorter(sorter);
	        }
	    });

	    // Configura il pulsante di ordine
	    ordinebutton.addActionListener(e -> c.visAndElem(1, 1));

	    // Configura il pulsante di ritorno
	    backbutton.addActionListener(e -> c.returnToLastFrame());
	}

	public VisioneOrdineFrame(String title, Controller c) throws SQLException {
		super(title);
		this.elementi(c);
		this.azioni(c);
	}
}
