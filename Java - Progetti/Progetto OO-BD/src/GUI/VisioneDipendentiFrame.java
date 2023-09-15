package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Entita.Cliente;
import Entita.Dipendente;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class VisioneDipendentiFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JScrollPane scrollPane;
	private JPanel titlepanel;
	private JPanel buttonpanel;
	private JButton backbutton;
	private JButton addbutton;
	private JButton updatebutton;
	private JLabel titlelab;

	public void elementi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(VisioneDipendentiFrame.class.getResource("/Immagini/ImmIcon.png")));
		setBounds(100, 100, 850, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setLocationRelativeTo(null);
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane);
		
		table = new JTable();
		model = new DefaultTableModel();
		Object[] colonne = {"Id","Nome","Cognome","Codice fiscale","Email","Indirizzo","Telefono"};
		final Object[] rows = new Object[7];
		model.setColumnIdentifiers(colonne);
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		
		titlepanel = new JPanel();
		titlepanel.setBackground(Color.ORANGE);
		contentPane.add(titlepanel, BorderLayout.NORTH);
		
		titlelab = new JLabel("Amministrazione Dipendenti");
		titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titlelab);
		
		buttonpanel = new JPanel();
		contentPane.add(buttonpanel, BorderLayout.SOUTH);
		
		addbutton = new JButton("Aggiungi");
		addbutton.setBackground(Color.GREEN);
		buttonpanel.add(addbutton);
		
		updatebutton = new JButton("Modifica");
		updatebutton.setBackground(Color.BLUE);
		buttonpanel.add(updatebutton);
		
		backbutton = new JButton("Indietro");
		backbutton.setBackground(Color.RED);
		buttonpanel.add(backbutton);
	}
	
	public void azioni(Controller c) throws SQLException {
		c.alldipendenti(model);

		addbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.visAnddip(1);
			}
		});
		
		updatebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				if(i>=0) {
					c.visAnddip(2);
					c.updipf.viewdip(new Dipendente(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString(), 
							   table.getValueAt(i, 2).toString(), table.getValueAt(i, 3).toString(), 
							   table.getValueAt(i, 4).toString(), table.getValueAt(i, 5).toString(), table.getValueAt(i, 6).toString()));
				}
				else JOptionPane.showMessageDialog(null, "Scegli una riga da modificare");
			}
		});
		
		backbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.adminAndElem(5);
			}
		});
	}
	
	public VisioneDipendentiFrame(String title,Controller c) throws SQLException {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
