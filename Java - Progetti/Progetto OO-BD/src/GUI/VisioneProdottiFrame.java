package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Entita.Prodotto;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.awt.Toolkit;

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

	public void elementi(Controller c) throws SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		c.allprodotti(model);
		
		addbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.visAndprod(1);
			}
		});
		
		updatebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				try {
					if(i>=0) {
						c.visAndprod(2);
						c.modprodf.viewprod(new Prodotto(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString(), table.getValueAt(i, 2).toString(), 
				                     Double.parseDouble(table.getValueAt(i, 3).toString()), table.getValueAt(i, 4).toString(), 
                                     null,null, Boolean.parseBoolean(table.getValueAt(i, 7).toString()),null, 
                                     table.getValueAt(i, 9).toString(),Integer.parseInt(table.getValueAt(i, 10).toString())));
					}
					else JOptionPane.showMessageDialog(null, "Scegli una riga da modificare");
				} catch (NumberFormatException  e1 ) {
					JOptionPane.showMessageDialog(null, "Errore!" + "\n" +  "Tipo di errore : \n" + e1);
				}
				}
		});
		
		backbutton.addActionListener(new ActionListener() {
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
