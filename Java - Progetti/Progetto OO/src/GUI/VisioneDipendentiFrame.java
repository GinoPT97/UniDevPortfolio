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

import Model.Dipendente;

public class VisioneDipendentiFrame extends JFrame {
	private JPanel contentPane;
	private JTable table;
	private JButton backbutton;
	private JButton addbutton;
	private JButton updatebutton;
	private JButton searchbutton;
	private JTextField searchtf;

	public void elementi(Controller c) {
	    // Imposta le proprietà della finestra principale
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setIconImage(Toolkit.getDefaultToolkit()
	            .getImage(VisioneDipendentiFrame.class.getResource("/Immagini/ImmIcon.png")));
	    setBounds(100, 100, 850, 500);
	    setLocationRelativeTo(null);

	    // Crea e configura il pannello principale con layout BorderLayout
	    contentPane = new JPanel(new BorderLayout());
	    contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
	    setContentPane(contentPane);

	    // Crea e configura la tabella con scroll pane
	    table = new JTable(c.dipModel);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	    JScrollPane scrollPane = new JScrollPane(table);
	    contentPane.add(scrollPane, BorderLayout.CENTER);

	    // Crea e configura il pannello del titolo
	    JPanel titlepanel = new JPanel();
	    titlepanel.setBackground(Color.ORANGE);
	    JLabel titlelab = new JLabel("Amministrazione Dipendenti");
	    titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
	    titlepanel.add(titlelab);
	    contentPane.add(titlepanel, BorderLayout.NORTH);

	    // Crea e configura il pannello dei bottoni
	    JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    contentPane.add(buttonpanel, BorderLayout.SOUTH);

	    searchtf = new JTextField(10);
	    buttonpanel.add(searchtf);

	    searchbutton = new JButton("Cerca");
	    searchbutton.setBackground(new Color(107, 142, 35));
	    buttonpanel.add(searchbutton);

	    addbutton = new JButton("Aggiungi");
	    addbutton.setBackground(Color.GREEN);
	    buttonpanel.add(addbutton);

	    updatebutton = new JButton("Modifica");
	    updatebutton.setBackground(new Color(70, 130, 180));
	    buttonpanel.add(updatebutton);

	    backbutton = new JButton("Indietro");
	    backbutton.setBackground(Color.RED);
	    buttonpanel.add(backbutton);
	}

	public void azioni(Controller c) throws SQLException {

		c.allDipendenti();

	    // Listener per il pulsante di ricerca
	    searchbutton.addActionListener(e -> {
	        String query = searchtf.getText().trim().toLowerCase();
	        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(c.dipModel);
	        if (query.isEmpty()) {
	            // Se la query è vuota, mostra tutti i dati
	            table.setRowSorter(null); // Rimuove il filtro
	        } else {
	            try {
	                // Applica il filtro sulla tabella con flag case insensitive
	                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
	            } catch (PatternSyntaxException ex) {
	                System.out.println("Errore nella sintassi dell'espressione regolare: " + ex.getMessage());
	                return; // Esci se c'è un errore di sintassi
	            }
	            table.setRowSorter(sorter);
	        }
	    });

	    // Listener per il pulsante di aggiunta
	    addbutton.addActionListener(e -> c.visAndDip(1));

	    // Listener per il pulsante di aggiornamento
	    updatebutton.addActionListener(e -> {
	        int i = table.getSelectedRow();
	        if (i >= 0) {
	            // Se una riga è selezionata, aggiorna il dipendente
	            c.visAndDip(2);
	            c.updipf.viewdip(new Dipendente(
	                table.getValueAt(i, 0).toString(),
	                table.getValueAt(i, 1).toString(),
	                table.getValueAt(i, 2).toString(),
	                table.getValueAt(i, 3).toString(),
	                table.getValueAt(i, 4).toString(),
	                table.getValueAt(i, 5).toString(),
	                table.getValueAt(i, 6).toString()
	            ));
	        } else {
	            JOptionPane.showMessageDialog(null, "Scegli una riga da modificare");
	        }
	    });

	    // Listener per il pulsante di ritorno alla schermata admin
	    backbutton.addActionListener(e -> c.returnToLastFrame());
	}

	public VisioneDipendentiFrame(String title, Controller c) throws SQLException {
		super(title);
		this.elementi(c);
		this.azioni(c);
	}
}
