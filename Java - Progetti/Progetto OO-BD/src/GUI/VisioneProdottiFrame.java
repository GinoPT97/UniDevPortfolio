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

import Entita.Prodotto;

public class VisioneProdottiFrame extends JFrame {
    private Controller c;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	public Prodotto pe;
	private JPanel titlepanel;
	private JPanel buttonpanel;
	private JButton backbutton;
	private JButton updatebutton;
	private JButton addbutton;
	private JLabel titlelabel;
	private JButton searchbutton;
	private JTextField searchtf;

	public void elementi(Controller c) throws SQLException {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);

		table = new JTable();
		model = new DefaultTableModel();
		Object[] colonne = {"Id","Nome","Descrizione","Prezzo","Provenienza","Raccolta","Mungitura","Glutine","Scadenza","Categoria","Scorta"};
		//final Object[] rows = new Object[11];
		model.setColumnIdentifiers(colonne);
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);

		titlepanel = new JPanel();
		titlepanel.setBackground(new Color(128, 0, 0));
		contentPane.add(titlepanel, BorderLayout.NORTH);

		titlelabel = new JLabel("Amminitrazione Prodotti");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titlelabel);

		buttonpanel = new JPanel();
		contentPane.add(buttonpanel, BorderLayout.SOUTH);

		searchtf = new JTextField();
		buttonpanel.add(searchtf);
		searchtf.setColumns(10);

		searchbutton = new JButton("Cerca");
		searchbutton.setBackground(new Color(46, 139, 87));
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
		c.allprodotti(model);

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
				c.visAndprod(1);
			}
		});

		updatebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				try {
					if(i>=0) {
						c.visAndprod(2);
						c.modprodf.viewprod(new Prodotto(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString(), table.getValueAt(i, 2).toString(),
				                     Double.parseDouble(table.getValueAt(i, 3).toString()), table.getValueAt(i, 4).toString(),
                                     null,null, Boolean.parseBoolean(table.getValueAt(i, 7).toString()),null,
                                     table.getValueAt(i, 9).toString(),Integer.parseInt(table.getValueAt(i, 10).toString())));
					} else {
						JOptionPane.showMessageDialog(null, "Scegli una riga da modificare");
					}
				} catch (NumberFormatException  e1 ) {
					JOptionPane.showMessageDialog(null, "Errore!" + "\n" +  "Tipo di errore : \n" + e1);
				}
				}
		});

		backbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.adminAndElem(5);
			}
		});
	}

	public VisioneProdottiFrame(String title, Controller c) throws SQLException {
		super(title);
		setIconImage(Toolkit.getDefaultToolkit().getImage(VisioneProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));
		this.elementi(c);
		this.azioni(c);
	}
}
