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

import Model.Cliente;

public class VisioneClienteFrame extends JFrame {
	private JPanel contentPane;
	private JTable table;
	private JButton backbutton;
	private JButton addbutton;
	private JButton updatebutton;
	private JTextField searchtf;
	private JButton searchbutton;

	public void elementi(Controller c) {
	    // Imposta le proprietà della finestra principale
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setBounds(100, 100, 850, 450);
	    setLocationRelativeTo(null);
	    setIconImage(Toolkit.getDefaultToolkit().getImage(VisioneClienteFrame.class.getResource("/Immagini/ImmIcon.png")));

	    // Crea e imposta il pannello principale con layout BorderLayout
	    contentPane = new JPanel(new BorderLayout());
	    contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
	    setContentPane(contentPane);

	    // Crea e configura la tabella con scroll pane
	    table = new JTable(c.clienteModel);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	    JScrollPane scrollPane = new JScrollPane(table);
	    contentPane.add(scrollPane, BorderLayout.CENTER);

	    // Crea e configura il pannello del titolo
	    JPanel titlepanel = new JPanel();
	    titlepanel.setBackground(new Color(0, 128, 0));
	    JLabel titlelab = new JLabel("Amministrazione Clienti");
	    titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
	    titlepanel.add(titlelab);
	    contentPane.add(titlepanel, BorderLayout.NORTH);

	    // Crea e configura il pannello dei bottoni
	    JPanel buttonpanel = new JPanel();
	    buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Aggiungi padding tra i bottoni
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
	    // Carica i dati iniziali nella tabella
	    c.allcliente();

	    // Aggiungi ActionListener al pulsante di ricerca
	    searchbutton.addActionListener(e -> {
	        String query = searchtf.getText().trim().toLowerCase();
	        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(c.clienteModel);
	        if (query.isEmpty()) {
	            // Se la query è vuota, mostra tutti i dati
	            table.setRowSorter(null); // Rimuove il filtro
	        } else {
	            try {
	                // Utilizza RowFilter.regexFilter con il flag CASE_INSENSITIVE per il filtro case insensitive
	                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
	                table.setRowSorter(sorter);
	            } catch (PatternSyntaxException ex) {
	                JOptionPane.showMessageDialog(null, "Errore nella sintassi della ricerca: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    });

	    // Aggiungi ActionListener al pulsante di aggiunta
	    addbutton.addActionListener(e -> c.visAndcl(1));

	    // Aggiungi ActionListener al pulsante di aggiornamento
	    updatebutton.addActionListener(e -> {
	        int selectedRow = table.getSelectedRow();
	        if (selectedRow >= 0) {
	            String[] clienteData = new String[7];
	            for (int i = 0; i < clienteData.length; i++) {
	                clienteData[i] = table.getValueAt(selectedRow, i).toString();
	            }

	            c.visAndcl(2);
	            c.upclf.viewct(new Cliente(clienteData[0], clienteData[1], clienteData[2], clienteData[3], clienteData[4], clienteData[5], clienteData[6], null, null));
	        } else {
	            JOptionPane.showMessageDialog(null, "Scegli una riga da modificare", "Attenzione", JOptionPane.WARNING_MESSAGE);
	        }
	    });

	    // Aggiungi ActionListener al pulsante di ritorno
	    backbutton.addActionListener(e -> c.dipAndElem(4));
	}

	public VisioneClienteFrame(String title, Controller c) throws SQLException {
		super(title);
		this.elementi(c);
		this.azioni(c);
	}
}
