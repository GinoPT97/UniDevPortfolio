package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import DAOImpl.provadaoimpl;
import Entita.provaentita;

public class Prova extends JFrame{
    private Controller c;
	private JFrame frame;
	private JTextField idtf;
	private JTextField nometf;
	private JTextField contattotf;
	private JTextField corsotf;
	private JTable table;
	private DefaultTableModel model;
	private provadaoimpl pdao;
    private ArrayList<provaentita> pe;
    private JTextField txtProgettinoDiProva;

    public Prova(String s,Controller c) throws SQLException  {
		this.getContentPane().setBackground(new Color(30, 144, 255));
		this.setBounds(100, 100, 600, 450);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		c.connect();

		JLabel idlab = new JLabel("ID :");
		idlab.setBounds(61, 69, 45, 13);
		this.getContentPane().add(idlab);

		idtf = new JTextField();
		idtf.setBounds(116, 66, 96, 19);
		this.getContentPane().add(idtf);
		idtf.setColumns(10);
		idtf.setEditable(false);

		JLabel namelab = new JLabel("Nome :");
		namelab.setBounds(61, 104, 45, 13);
		this.getContentPane().add(namelab);

		nometf = new JTextField();
		nometf.setBounds(116, 101, 96, 19);
		this.getContentPane().add(nometf);
		nometf.setColumns(10);

		JLabel contattolab = new JLabel("Contatto :");
		contattolab.setBounds(49, 138, 57, 13);
		this.getContentPane().add(contattolab);

		contattotf = new JTextField();
		contattotf.setBounds(116, 135, 96, 19);
		this.getContentPane().add(contattotf);
		contattotf.setColumns(10);

		JLabel corsolab = new JLabel("Corso :");
		corsolab.setBounds(61, 173, 45, 13);
		this.getContentPane().add(corsolab);

		corsotf = new JTextField();
		corsotf.setBounds(116, 170, 96, 19);
		this.getContentPane().add(corsotf);
		corsotf.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(262, 68, 314, 335);
		this.getContentPane().add(scrollPane);

		table = new JTable();
		model = new DefaultTableModel();
		Object[] colonne = {"ID", "Nome","Contatti","Corso"};
		final Object[] rows = new Object[4];
		model.setColumnIdentifiers(colonne);
		table.setModel(model);
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			int i = table.getSelectedRow();
			idtf.setText(model.getValueAt(i, 0).toString());
			nometf.setText(model.getValueAt(i, 1).toString());
			contattotf.setText(model.getValueAt(i, 2).toString());
			corsotf.setText(model.getValueAt(i, 3).toString());
		  }
		});
		c.getAllProve(model);

		JButton addbutt = new JButton("Aggiungi");
		addbutt.setForeground(Color.GREEN);

		addbutt.setBounds(25, 280, 85, 21);
		this.getContentPane().add(addbutt);

		JButton updatebutt = new JButton("Modifica");
		updatebutt.setForeground(Color.ORANGE);
		updatebutt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				if(i>=0) {
				model.setValueAt(idtf.getText(), i, 0);
				model.setValueAt(nometf.getText(), i, 1);
				model.setValueAt(contattotf.getText(), i, 2);
				model.setValueAt(corsotf.getText(), i, 3);
				try {
					c.upprove(new provaentita(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString(), table.getValueAt(i, 2).toString(), table.getValueAt(i, 3).toString()));
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				clean();
				JOptionPane.showMessageDialog(null, "Modifica avvenuta con successo.");
			 }else {
				JOptionPane.showMessageDialog(null, "Seleziona una riga da modificare.");
			 }
			}
		});
		updatebutt.setBounds(141, 280, 85, 21);
		this.getContentPane().add(updatebutt);

		JButton delatebutt = new JButton("Elimina");
		delatebutt.setForeground(Color.RED);
		delatebutt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				try {
					c.elimina(table.getValueAt(i, 0).toString());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				if(i>=0) {
				model.removeRow(i);
				JOptionPane.showMessageDialog(null, "Eliminato con successo");
				}else {
					JOptionPane.showMessageDialog(null, "Seleziona una riga");
				}
			}
		});
		delatebutt.setBounds(25, 337, 85, 21);
		this.getContentPane().add(delatebutt);

		JButton clearbutt = new JButton("Clear");
		clearbutt.setBackground(Color.WHITE);
		clearbutt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clean();
			}
		});
		clearbutt.setBounds(141, 337, 85, 21);
		this.getContentPane().add(clearbutt);

		txtProgettinoDiProva = new JTextField();
		txtProgettinoDiProva.setEditable(false);
		txtProgettinoDiProva.setBackground(new Color(0, 128, 128));
		txtProgettinoDiProva.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 35));
		txtProgettinoDiProva.setText("Progettino di prova");
		txtProgettinoDiProva.setBounds(0, 0, 586, 59);
		getContentPane().add(txtProgettinoDiProva);
		txtProgettinoDiProva.setColumns(10);
    }

    public void clean() {
    	idtf.setText("");
		nometf.setText("");
		contattotf.setText("");
		corsotf.setText("");
    }
}