package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Entita.Cliente;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Toolkit;

public class VisioneClienteFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JButton backbutton;
	private JButton addbutton;
	private JButton updatebutton;
	private JLabel titlelab;

	public void elementi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		c.allcliente(model);

		addbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.visAndcl(1);
			}
		});

		updatebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				if(i>=0) {
					c.visAndcl(2);
					c.upclf.viewct(new Cliente(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString(), 
		                           table.getValueAt(i, 2).toString(), table.getValueAt(i, 3).toString(), 
		                           table.getValueAt(i, 4).toString(), table.getValueAt(i, 5).toString(), 
		                           table.getValueAt(i, 6).toString(),null,null));
				}   
				else JOptionPane.showMessageDialog(null, "Scegli una riga da modificare");
			}
		});
		
		backbutton.addActionListener(new ActionListener() {
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
