package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import Entita.Cliente;

public class VisioneClienteFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JButton backbutton;
	private JButton addbutton;
	private JButton updatebutton;
	private JLabel titlelab;
	private JTextField searchtf;
	private JButton searchbutton;

	public void elementi() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setIconImage(Toolkit.getDefaultToolkit().getImage(VisioneClienteFrame.class.getResource("/Immagini/ImmIcon.png")));
		setLocationRelativeTo(null);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);

		table = new JTable();
		model = new DefaultTableModel();
		Object[] colonne = {"Id Cliente","Nome","Cognome","Codice fiscale","Email","Indirizzo","Telefono","Id Tessera", "Punti"};
		final Object[] rows = new Object[8];
		model.setColumnIdentifiers(colonne);
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);

		JPanel titlepanel = new JPanel();
		titlepanel.setBackground(new Color(0, 128, 0));
		titlepanel.setForeground(new Color(0, 128, 0));
		contentPane.add(titlepanel, BorderLayout.NORTH);

		titlelab = new JLabel("Amministrazione Clienti");
		titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titlelab);

		JPanel buttonpanel = new JPanel();
		contentPane.add(buttonpanel, BorderLayout.SOUTH);

		searchtf = new JTextField();
		buttonpanel.add(searchtf);
		searchtf.setColumns(10);

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
		c.allcliente(model);

		searchbutton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String query = searchtf.getText().trim().toLowerCase();
		        if (query.isEmpty()) {
		            // Se la query è vuota, mostra tutti i dati
		            table.setRowSorter(null); // Rimuove il filtro
		        } else {
		            // Applica il filtro sulla tabella
		            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
		            try {
		                // Utilizza RowFilter.regexFilter con il flag CASE_INSENSITIVE per il filtro case insensitive
		                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
		            } catch (PatternSyntaxException ex) {
		                System.out.println("Errore nella sintassi dell'espressione regolare: " + ex.getMessage());
		                return; // Esci se c'è un errore di sintassi
		            }
		            table.setRowSorter(sorter);
		        }
		    }
		});

		addbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.visAndcl(1);
			}
		});

		updatebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				if(i>=0) {
					c.visAndcl(2);
					c.upclf.viewct(new Cliente(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString(),
		                           table.getValueAt(i, 2).toString(), table.getValueAt(i, 3).toString(),
		                           table.getValueAt(i, 4).toString(), table.getValueAt(i, 5).toString(),
		                           table.getValueAt(i, 6).toString(),null,null));
				} else {
					JOptionPane.showMessageDialog(null, "Scegli una riga da modificare");
				}
			}
		});

		backbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.dipAndElem(4);
			}
		});
	}

	public VisioneClienteFrame(String title, Controller c) throws SQLException {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
