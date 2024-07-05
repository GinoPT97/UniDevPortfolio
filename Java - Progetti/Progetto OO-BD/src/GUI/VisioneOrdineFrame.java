package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	private DefaultTableModel model;
	private JScrollPane scrollPane;
	private JPanel buttonpanel;
	private JPanel titlepanel;
	private JButton backbutton;
	private JLabel titleabel;
	private JButton ordinebutton;
	public int x;
	private JButton searchbutton;
	private JTextField searchtf;

	public void elementi() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));
		setLocationRelativeTo(null);

		scrollPane = new JScrollPane();
		contentPane.add(scrollPane);

		table = new JTable();
		model = new DefaultTableModel();
		Object[] colonne = {"Codice Ordine", "Data", "Prezzo Totale", "Cliente", "Dipendente"};
		final Object[] rows = new Object[5];
		model.setColumnIdentifiers(colonne);
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);

		buttonpanel = new JPanel();
		contentPane.add(buttonpanel, BorderLayout.SOUTH);

		searchtf = new JTextField();
		buttonpanel.add(searchtf);
		searchtf.setColumns(10);

		searchbutton = new JButton("Cerca");
		searchbutton.setBackground(new Color(60, 179, 113));
		buttonpanel.add(searchbutton);

		ordinebutton = new JButton("Nuovo Ordine");
		ordinebutton.setBackground(Color.GREEN);
		buttonpanel.add(ordinebutton);

		backbutton = new JButton("Indietro");
		backbutton.setBackground(Color.RED);
		buttonpanel.add(backbutton);

		titlepanel = new JPanel();
		titlepanel.setBackground(new Color(0, 139, 139));
		contentPane.add(titlepanel, BorderLayout.NORTH);

		titleabel = new JLabel("Visualizzazione Ordini");
		titleabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titleabel);
	}

	public void azioni(Controller c) throws SQLException {
		c.allordini(model);

		searchbutton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String query = searchtf.getText().trim().toLowerCase();
		        if (query.isEmpty()) {
		            // Se la query è vuota, ripristina la tabella mostrando tutti i dati
		            table.setRowSorter(null);
		        } else {
		            // Filtra i risultati sulla base della query
		            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
		            sorter.setRowFilter(RowFilter.regexFilter(query));
		            table.setRowSorter(sorter);
		        }
		    }
		});

		ordinebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.visAndCarr(1);
			}
		});

		backbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.returnToLastFrame();
			}
		});
	}

	public VisioneOrdineFrame(String title,Controller c) throws SQLException {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
