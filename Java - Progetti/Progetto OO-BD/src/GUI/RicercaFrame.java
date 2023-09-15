package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Toolkit;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import javax.swing.JScrollBar;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class RicercaFrame extends JFrame {

	private JPanel contentPane;
	private JTable searchtable;
	private JButton selettorebutton;
	private JComboBox categoriacb;
	private DefaultTableModel searchmodel = new DefaultTableModel();
	private Object[] searchcolonne = {"Nome","Cognome","Categoria","Numero Punti"};
	private JButton backbutton;
	private JButton searchbutton;
	private JButton clearbutton;
	private JButton selectpuntibutt;
	public int x;
	
	public void elementi() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(RicercaFrame.class.getResource("/Immagini/ImmIcon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 904, 433);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));
		setLocationRelativeTo(null);

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel titlepanel = new JPanel();
		titlepanel.setBackground(new Color(100, 149, 237));
		contentPane.add(titlepanel, BorderLayout.NORTH);
		
		JLabel titlelabel = new JLabel("Ricerca Clienti");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlelabel.setBackground(new Color(100, 149, 237));
		titlepanel.add(titlelabel);
		
		JPanel searchpanel = new JPanel();
		searchpanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		contentPane.add(searchpanel, BorderLayout.WEST);
		searchpanel.setLayout(new BoxLayout(searchpanel, BoxLayout.Y_AXIS));
		
		JPanel categoriapanel = new JPanel();
		searchpanel.add(categoriapanel);
		
		categoriacb = new JComboBox(new String[]{"Ortofrutticoli","Inscatolati","Latticini","Farinacei"});
		categoriapanel.add(categoriacb);
		
		selettorebutton = new JButton("Seleziona");
		categoriapanel.add(selettorebutton);
		
		JPanel puntipanel = new JPanel();
		searchpanel.add(puntipanel);
		
		JLabel puntilab = new JLabel("Ricerca Clienti per punti");
		puntipanel.add(puntilab);
		
		JPanel panel = new JPanel();
		searchpanel.add(panel);
		
		JComboBox punticb = new JComboBox(new String[] {"0-500","501-1000","1001-5000",">5000","Tutti"});
		panel.add(punticb);
		
		selectpuntibutt = new JButton("seleziona");
		panel.add(selectpuntibutt);
		
		JPanel tablepanel = new JPanel();
		contentPane.add(tablepanel);
		tablepanel.setLayout(new BoxLayout(tablepanel, BoxLayout.X_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		tablepanel.add(scrollPane);
		
		searchtable = new JTable();
		searchtable.setModel(searchmodel);
		searchtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(searchtable);
		
		JPanel buttonpanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonpanel.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		contentPane.add(buttonpanel, BorderLayout.SOUTH);
		
		searchbutton = new JButton("Ricerca");
		searchbutton.setBackground(new Color(30, 144, 255));
		buttonpanel.add(searchbutton);
		
		clearbutton = new JButton("Pulisci");
		buttonpanel.add(clearbutton);
		
		backbutton = new JButton("Indietro");
		backbutton.setBackground(Color.RED);
		backbutton.setHorizontalAlignment(SwingConstants.TRAILING);
		buttonpanel.add(backbutton);
		final Object[] rows = new Object[5];
		searchmodel.setColumnIdentifiers(searchcolonne);
	}
	
	public void azioni(Controller c) throws SQLException{
		c.ClientSearch(searchmodel);
		
		selectpuntibutt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		backbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.searchAndElem(x);
			}
		});
		
	}
	
	public RicercaFrame(String title,Controller c) throws SQLException{
		super(title);
		this.elementi();
		this.azioni(c);
	}

}
