package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Entita.Articoli;
import Entita.Ordine;

public class CarrelloFrame extends JFrame {
	private Controller c;
	private double tot = 0.00;
	private JPanel contentPane;
	private DefaultTableModel prodmodel = new DefaultTableModel();
	private DefaultTableModel ordmodel= new DefaultTableModel();
	private Object[] prodcolonne = {"Id","Nome","Prezzo","Categoria","Scorta"};
	private Object[] ordinecolonne = {"Id","Nome","Prezzo","Categoria","Quantita"};
	private LocalDate dataod = LocalDate.now();
	private JPanel bottonpanel;
	private JPanel prodottopanel;
	private JPanel ordinepanel;
	private JPanel centerpanel;
	private JLabel datalab;
	private JLabel codfisclab;
	private JTextField codfisctf;
	private JButton backbutton;
	private JButton ordinebutton;
	private JComboBox categoriacb;
	private JButton selectbutton;
	private JLabel quantitalab;
	private JTextField quantitatf;
	private JLabel totalelab;
	private JButton removebutton;
	private JButton insertbutton;
	private JTable prodottotable;
	private JScrollPane prodottiscrollPane;
	private JTable ordinetable;
	private JScrollPane ordinescrollPane;
	private JPanel titlepanel;
	private JLabel titlelabel;

	public void elementi() {
		setBackground(Color.WHITE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 500);
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(AdminFrame.class.getResource("/Immagini/ImmIcon.png")));
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		bottonpanel = new JPanel();
		contentPane.add(bottonpanel, BorderLayout.SOUTH);

		datalab = new JLabel("Data Odierna : 2022-10-15");
		bottonpanel.add(datalab);

		codfisclab = new JLabel("Codice Fiscale Cliente : ");
		bottonpanel.add(codfisclab);

		codfisctf = new JTextField();
		codfisctf.setColumns(10);
		bottonpanel.add(codfisctf);

		ordinebutton = new JButton("Inserisci Ordine");
		ordinebutton.setBackground(Color.GREEN);
		bottonpanel.add(ordinebutton);

		backbutton = new JButton("Indietro");
		backbutton.setBackground(Color.RED);
		bottonpanel.add(backbutton);

		prodottopanel = new JPanel();
		prodottopanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.add(prodottopanel, BorderLayout.WEST);
		prodottopanel.setLayout(new BoxLayout(prodottopanel, BoxLayout.X_AXIS));

		prodottiscrollPane = new JScrollPane();
		prodottopanel.add(prodottiscrollPane);

		prodottotable = new JTable();
		final Object[] rows = new Object[5];
		prodmodel.setColumnIdentifiers(prodcolonne);
		prodottotable.setModel(prodmodel);
		prodottotable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		prodottiscrollPane.setViewportView(prodottotable);

		ordinepanel = new JPanel();
		contentPane.add(ordinepanel, BorderLayout.EAST);
		ordinepanel.setLayout(new BoxLayout(ordinepanel, BoxLayout.X_AXIS));

		ordinescrollPane = new JScrollPane();
		ordinepanel.add(ordinescrollPane);

		ordinetable = new JTable();
		ordmodel.setColumnIdentifiers(ordinecolonne);
		ordinetable.setModel(ordmodel);
		ordinetable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ordinescrollPane.setViewportView(ordinetable);

		centerpanel = new JPanel();
		centerpanel.setBorder(new EmptyBorder(100, 0, 100, 0));
		contentPane.add(centerpanel);
		centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.Y_AXIS));

		categoriacb = new JComboBox(new String[]{"Ortofrutticoli","Inscatolati","Latticini","Farinacei"});
		centerpanel.add(categoriacb);

		selectbutton = new JButton("Seleziona");
		centerpanel.add(selectbutton);

		quantitalab = new JLabel("Seleziona Quantita :");
		centerpanel.add(quantitalab);

		quantitatf = new JTextField();
		quantitatf.setColumns(1);
		centerpanel.add(quantitatf);

		totalelab = new JLabel("Totale :  0.00");
		centerpanel.add(totalelab);

		removebutton = new JButton("Rimuovi");
		removebutton.setBackground(Color.RED);
		centerpanel.add(removebutton);

		insertbutton = new JButton("Inserisci ");
		insertbutton.setBackground(new Color(0, 153, 255));
		centerpanel.add(insertbutton);

		titlepanel = new JPanel();
		titlepanel.setBackground(new Color(0, 0, 139));
		contentPane.add(titlepanel, BorderLayout.NORTH);

		titlelabel = new JLabel("Nuovo Ordine");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titlelabel);
	}

	public void clean() {
		totalelab.setText("Totale :  0.00");
		quantitatf.setText("");
		codfisctf.setText("");
		prodmodel.setRowCount(0);
		ordmodel.setRowCount(0);
	}

	public double totale() {
		for(int j=0; j<ordmodel.getRowCount(); j++) {
			tot += Double.parseDouble(ordmodel.getValueAt(j, 2).toString()) * Integer.parseInt(ordmodel.getValueAt(j, 4).toString());
		}
		totalelab.setText("Totale : " + "" + String.valueOf(tot));
		double tot1 = tot;
		tot = 0.00;
		return tot1;
	}

	public void azioni(Controller c) throws SQLException {
		selectbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prodmodel.setRowCount(0);
				try {
					if(categoriacb.getSelectedItem().equals("Ortofrutticoli")) {
						c.categoriaprodotti("Ortofrutticoli", prodmodel);
					} else if (categoriacb.getSelectedItem().equals("Inscatolati")) {
						c.categoriaprodotti("Inscatolati", prodmodel);
					} else if (categoriacb.getSelectedItem().equals("Farinacei")) {
						c.categoriaprodotti("Farinacei", prodmodel);
					} else if (categoriacb.getSelectedItem().equals("Latticini")) {
						c.categoriaprodotti("Latticini", prodmodel);
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Errore!" + "\n" +  "Tipo di errore : \n" + e1);
				}
			}
		});

		insertbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = prodottotable.getSelectedRow();
				if(i==-1 && quantitatf.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Seleziona un prodotto e la quantita'!");
				} else if(i==-1) {
					JOptionPane.showMessageDialog(null, "Seleziona un prodotto!");
				} else if(quantitatf.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Seleziona la quantita'!");
				} else if(Integer.parseInt(prodmodel.getValueAt(i, 4).toString())<Integer.parseInt(quantitatf.getText())) {
					JOptionPane.showMessageDialog(null, "Scorte insufficenti!");
				} else {
					Object[] p = {prodmodel.getValueAt(i, 0),prodmodel.getValueAt(i, 1),prodmodel.getValueAt(i, 2),prodmodel.getValueAt(i, 3), quantitatf.getText()};
					ordmodel.addRow(p);
					quantitatf.setText("");
					totale();
				}
			}
		});

		removebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ordmodel.removeRow(ordinetable.getSelectedRow());
				totale();
			}
		});

		backbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.visAndCarr(2);
			}
		});

		ordinebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				  java.sql.Date sd = java.sql.Date.valueOf(dataod);
				  c.nuovoordine(new Ordine("", sd, tot, c.getct(codfisctf.getText()), c.iddip));
				  for(int j=0; j<ordmodel.getRowCount(); j++) {
					c.upscorte(Integer.parseInt(ordmodel.getValueAt(j, 4).toString()), ordmodel.getValueAt(j, 0).toString());
					c.newarticoli(new Articoli(c.CurrOrd(),c.getct(codfisctf.getText().toString()),
		                    Double.parseDouble(ordmodel.getValueAt(j, 2).toString()),
		                    Double.parseDouble(ordmodel.getValueAt(j, 2).toString())*Double.parseDouble(ordmodel.getValueAt(j, 4).toString()),
		                    Integer.parseInt(ordmodel.getValueAt(j, 4).toString()),ordmodel.getValueAt(j, 3).toString()));
				  }
				  c.uppunti(c.getct(codfisctf.getText()), totale());
				  JOptionPane.showMessageDialog(null, "Ordine aggiunto");
	              clean();
			  } catch (SQLException e1) {
				  JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
			  }
			}
		});
	}

	public CarrelloFrame(String title,Controller c) throws SQLException {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
