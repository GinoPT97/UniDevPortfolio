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
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

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
    
	public void elementi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

		ordinebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.visAndCarr(1);
			}
		});

		backbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.visAndCarr(x);
			}
		});
	}
	
	public VisioneOrdineFrame(String title,Controller c) throws SQLException {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
